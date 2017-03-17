package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;

import static ru.spb.kupchinolabs.pcollections.RedBlackTreeUtils.buildNodeWithLeafs;
import static ru.spb.kupchinolabs.pcollections.RedBlackTreeUtils.treeIsEmpty;

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
        //TODO consider cloning element
        if (treeIsEmpty(this)) {
            RedBlackTreeUtils.insertRoot(this, element);
            return true;
        } else {
            RedBlackTreeNode<T> currentNode = rootNode;
            boolean lastStepLeft = false;
            while (currentNode.getValue().isPresent()) {
                T curValue = currentNode.getValue().get();
                int compareTo = curValue.compareTo(element);
                if (compareTo == 0) return false;
                if (compareTo < 0) {
                    currentNode = currentNode.getRight().get();
                    lastStepLeft = false;
                } else {
                    currentNode = currentNode.getLeft().get();
                    lastStepLeft = true;
                }
            }
            RedBlackTreeNode<T> curParent = currentNode.getParent().get();
            RedBlackTreeNode<T> newNode = buildNodeWithLeafs(element, curParent);
            if (lastStepLeft) {
                curParent.setLeft(newNode);
            } else {
                curParent.setRight(newNode);
            }
            RedBlackTreeUtils.repaintAndRebalance(this, currentNode);
            return true;
        }
    }

    @Override
    public boolean remove(T element) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
