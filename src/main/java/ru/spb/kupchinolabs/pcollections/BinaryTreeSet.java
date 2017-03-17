package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.cloneElement;
import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.searchAndRemove;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeSet<T extends Comparable<T> & Serializable> implements SimpleSet<T>, Serializable {

    BinaryTreeNode<T> rootNode;

    public boolean contains(T element) {
        return BinaryTreeUtils.contains(element, rootNode);
    }

    public boolean insert(T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        T newElement = cloneElement(element);
        if (BinaryTreeUtils.treeIsEmpty(this)) {
            BinaryTreeUtils.insertRoot(this, newElement);
            return true;
        } else {
            BinaryTreeNode<T> newNode = new BinaryTreeNode<T>();
            return BinaryTreeUtils.insertElement(rootNode, newElement, () -> newNode, parentNode -> {
            });
        }
    }

    @Override
    public boolean remove(T element) {
        if (element == null) throw new NullPointerException("null elements are not supported");
        return searchAndRemove(newRoot -> {
            this.rootNode = newRoot;
        }, element, rootNode, rootNode, false, parentOfRemoved -> {
        });
    }

    public int size() {
        return BinaryTreeUtils.size(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderTreeSetIterator<T>(rootNode);
    }


    //TODO consider adding constructor with Comparator to support elements which do not implement Comparable
    //TODO consider adding equals and hashCode to support collection friendliness
}
