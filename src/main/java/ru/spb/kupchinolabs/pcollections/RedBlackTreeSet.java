package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;

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
        T newElement = cloneElement(element);
        if (rootNode == null) {
            RedBlackTreeNode<T> node = new RedBlackTreeNode<>();
            node.setValue(newElement);
            node.setParent(null);
            node.setColor(BLACK);
            rootNode = node;
            size++;
            return true;
        } else {
            RedBlackTreeNode<T> newNode = new RedBlackTreeNode<T>();
            boolean inserted = BinaryTreeUtils.insertElement(rootNode, newElement, () -> newNode, parentNode -> {
                newNode.setParent(parentNode);
                repaintAndRebalanceOnInsert(this, newNode);
                validateTree(this, newNode); //TODO later move out to test code
            });
            if (inserted) {
                size++;
            }
            return inserted;
        }
    }

    @Override
    public boolean remove(T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        boolean removed = searchAndRemove(newRoot -> {
            this.rootNode = (RedBlackTreeNode<T>) newRoot;
            this.rootNode.setParent(null);
        }, element, rootNode, rootNode, false, parentOfRemoved -> {
            repaintAndRebalanceOnRemove(this, (RedBlackTreeNode<T>) parentOfRemoved);
            validateTree(this, (RedBlackTreeNode<T>) parentOfRemoved); //TODO later move out to test code
        });
        if (removed) {
            size--;
        }
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderTreeSetIterator<T>(rootNode);
    }
}
