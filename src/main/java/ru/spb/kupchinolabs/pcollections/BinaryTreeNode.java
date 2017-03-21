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

    BinaryTreeNode<T> setLeft(BinaryTreeNode<T> left) {
        this.left = left;
        return this;
    }

    Optional<? extends BinaryTreeNode<T>> getRight() {
        return Optional.ofNullable(right);
    }

    BinaryTreeNode<T> setRight(BinaryTreeNode<T> right) {
        this.right = right;
        return this;
    }

    T getValue() {
        return value;
    }

    BinaryTreeNode<T> setValue(T value) {
        this.value = value;
        return this;
    }
}
