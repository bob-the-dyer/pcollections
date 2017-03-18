package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by vladimir-k on 12.03.17.
 */
class BinaryTreeNode<T extends Comparable<T> & Serializable> implements Serializable {

    private BinaryTreeNode<T> left;
    private BinaryTreeNode<T> right;
    private T value;

    Optional<? extends BinaryTreeNode<T>> getLeft() {
        return Optional.ofNullable(left);
    }

    void setLeft(BinaryTreeNode<T> left) {
        this.left = left;
    }

    Optional<? extends BinaryTreeNode<T>> getRight() {
        return Optional.ofNullable(right);
    }

    void setRight(BinaryTreeNode<T> right) {
        this.right = right;
    }

    T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
    }
}
