package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.*;
import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.BLACK;

/**
 * Created by vladimir-k on 16.03.17.
 */
public class RedBlackTreeSet<T extends Comparable<T> & Serializable> implements SimpleSet<T>, Serializable {

    RedBlackTreeNode<T> rootNode;
    private int size = 0;

    @Override
    public boolean contains(T element) {
        return BinaryTreeUtils.contains(element, rootNode);
    }

    @Override
    public boolean insert(final T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        T newElement = (T) cloneObject(element);
        if (rootNode == null) {
            RedBlackTreeNode<T> node = new RedBlackTreeNode<>();
            node.setValue(newElement);
            node.setParent(null);
            node.setColor(BLACK);
            rootNode = node;
            size++;
            repaintAndRebalanceOnInsert(this, rootNode);
            validateTree(this); //TODO later move out to test code
            return true;
        } else {
            RedBlackTreeNode<T> newNode = new RedBlackTreeNode<T>();
            boolean inserted = BinaryTreeUtils.insertElement(rootNode, newElement, () -> newNode, parentNode -> {
                newNode.setParent((RedBlackTreeNode<T>) parentNode);
                repaintAndRebalanceOnInsert(this, newNode);
                validateTree(this); //TODO later move out to test code
            });
            if (inserted) {
                size++;
            }
            return inserted;
        }
    }

    @Override
    public boolean remove(T element) {
        throw new UnsupportedOperationException("remove on RBTree is not supported yet, use PersistentRedBlackTreeSet instead");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderTreeSetIterator<T>(rootNode);
    }

    @Override
    public String toString() {
        Queue<RedBlackTreeNode<T>> stack = new LinkedBlockingQueue<>();
        if (rootNode != null) {
            stack.offer(rootNode);
        }
        return "RBTS{\n" +
                buildWidthTraverseStringPyramith(stack) +
                "}";
    }

}
