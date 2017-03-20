package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.cloneElement;
import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.searchAndRemove;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeSet<T extends Comparable<T> & Serializable> implements SimpleSet<T>, Serializable {

    private BinaryTreeNode<T> rootNode;
    private int size = 0;

    public boolean contains(T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        return BinaryTreeUtils.contains(element, rootNode);
    }

    public boolean insert(T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        T newElement = cloneElement(element);
        if (rootNode == null) {
            rootNode = new BinaryTreeNode<T>();
            rootNode.setValue(newElement);
            size++;
            return true;
        } else {
            BinaryTreeNode<T> newNode = new BinaryTreeNode<T>();
            boolean inserted = BinaryTreeUtils.insertElement(rootNode, newElement, () -> newNode, parentNode -> {
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
            this.rootNode = newRoot;
        }, element, rootNode, rootNode, false, (onNodeRemoveCallbackArgument) -> {
        });
        if (removed) {
            size--;
        }
        return removed;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderTreeSetIterator<T>(rootNode);
    }


    //TODO consider adding constructor with Comparator to support elements which do not implement Comparable
    //TODO consider adding equals and hashCode to support collection friendliness
}
