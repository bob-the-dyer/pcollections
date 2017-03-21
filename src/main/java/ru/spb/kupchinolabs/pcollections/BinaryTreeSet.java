package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.*;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeSet<T extends Comparable<T> & Serializable> implements SimpleSet<T>, Serializable {

    private BinaryTreeNode<T> rootNode;
    private int size = 0;

    public boolean contains(T element) {
        return BinaryTreeUtils.contains(element, rootNode);
    }

    public boolean insert(T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        T newElement = (T) cloneObject(element);
        if (rootNode == null) {
            rootNode = new BinaryTreeNode<T>();
            rootNode.setValue(newElement);
            size++;
            assert size == BinaryTreeUtils.size(this); //TODO remove later
            return true;
        } else {
            BinaryTreeNode<T> newNode = new BinaryTreeNode<T>();
            boolean inserted = BinaryTreeUtils.insertElement(rootNode, newElement, () -> newNode, parentNode -> {
            });
            if (inserted) {
                size++;
            }
            assert size == BinaryTreeUtils.size(this); //TODO remove later
            return inserted;
        }
    }

    @Override
    public boolean remove(T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        boolean removed = searchAndRemove(newRoot -> this.rootNode = newRoot, element, rootNode, rootNode, false, arg -> {
        });
        if (removed) {
            size--;
        }
        assert size == BinaryTreeUtils.size(this); //TODO remove later
        return removed;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderTreeSetIterator<T>(rootNode);
    }

    @Override
    public String toString() {
        Queue<BinaryTreeNode<T>> queue = new LinkedBlockingQueue<>();
        if (rootNode != null) {
            queue.offer(rootNode);
        }
        return "BTS{\n" +
                buildWidthTraverseStringPyramid(queue) +
                "}";
    }

    //TODO consider adding constructor with Comparator to support elements which do not implement Comparable
    //TODO consider adding equals and hashCode to support collection friendliness
}
