package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.cloneElement;
import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.repaintAndRebalance;

/**
 * Created by vladimir-k on 16.03.17.
 */
public class RedBlackTreeSet<T extends Comparable<T> & Serializable> implements SimpleSet<T>, Serializable {

    RedBlackTreeNode<T> rootNode;

    @Override
    public boolean contains(T element) {
        return false;
    }

    @Override
    public boolean insert(final T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        T newElement = cloneElement(element);
        if (BinaryTreeUtils.treeIsEmpty(this)) {
            BinaryTreeUtils.insertRoot(this, newElement);
            return true;
        } else {
            RedBlackTreeNode<T> newNode = new RedBlackTreeNode<T>();
            return BinaryTreeUtils.insertElement(rootNode, newElement, () -> newNode, parentNode -> {
                newNode.setParent(parentNode);
                repaintAndRebalance(this, newNode);
            });
        }
    }

    @Override
    public boolean remove(T element) {
        return false;
    }

    @Override
    public int size() {
        return BinaryTreeUtils.size(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderTreeSetIterator<T>(rootNode);
    }
}
