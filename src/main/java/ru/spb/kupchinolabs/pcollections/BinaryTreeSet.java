package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.cloneElement;
import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.maximum;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeSet<T extends Comparable<T> & Serializable> implements SimpleSet<T>, Serializable {

    BinaryTreeNode<T> rootNode;

    public boolean contains(T element) {
        BinaryTreeNode<T> node = rootNode;
        while (node != null) {
            T curValue = node.getValue();
            int compareTo = curValue.compareTo(element);
            if (compareTo == 0) return true;
            if (compareTo < 0) {
                node = node.getRight().orElse(null);
            } else {
                node = node.getLeft().orElse(null);
            }
        }
        return false;
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
        BinaryTreeNode<T> parentNode = rootNode;
        BinaryTreeNode<T> currentNode = rootNode;
        boolean lastStepLeft = false;
        return searchRemovable(element, currentNode, parentNode, lastStepLeft);
    }

    private boolean searchRemovable(T element, BinaryTreeNode<T> currentNode, BinaryTreeNode<T> parentNode, boolean lastStepLeft) {
        while (currentNode != null) {
            T curValue = currentNode.getValue();
            int compareTo = curValue.compareTo(element);
            if (compareTo == 0) return remove(currentNode, parentNode, lastStepLeft);
            parentNode = currentNode;
            if (compareTo < 0) {
                currentNode = currentNode.getRight().orElse(null);
                lastStepLeft = false;
            } else {
                currentNode = currentNode.getLeft().orElse(null);
                lastStepLeft = true;
            }
        }
        return false;
    }

    private boolean remove(BinaryTreeNode<T> currentNode, BinaryTreeNode<T> parentNode, boolean lastStepLeft) {
        if (!currentNode.getLeft().isPresent() && !currentNode.getRight().isPresent()) { //leaf with no children
            if (parentNode == currentNode) {
                rootNode = null;
            } else if (lastStepLeft) {
                parentNode.setLeft(null);
            } else {
                parentNode.setRight(null);
            }
            return true;
        }
        if (currentNode.getLeft().isPresent() ^ currentNode.getRight().isPresent()) { //leaf with single child
            BinaryTreeNode<T> child = currentNode.getLeft().isPresent() ? currentNode.getLeft().get() : currentNode.getRight().get();
            if (parentNode == currentNode) {
                rootNode = child;
            } else if (lastStepLeft) {
                parentNode.setLeft(child);
            } else {
                parentNode.setRight(child);
            }
            return true;
        }
        if (currentNode.getLeft().isPresent() && currentNode.getRight().isPresent()) { //leaf with both children
            BinaryTreeNode<T> leftChild = currentNode.getLeft().get();
            T maxFromLeft = maximum(leftChild);
            currentNode.setValue(maxFromLeft);
            return searchRemovable(maxFromLeft, leftChild, currentNode, true);
        }
        throw new IllegalStateException("we shouldn't get here ever");
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
