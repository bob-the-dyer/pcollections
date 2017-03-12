package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;

/**
 * Created by vladimir-k on 12.03.17.
 */
class BinaryTreeNode<T extends Comparable<T> & Serializable> implements Serializable {

    private BinaryTreeNode<T> left;
    private BinaryTreeNode<T> right;
    private T value;

    BinaryTreeNode<T> getLeft() {
        return left;
    }

    BinaryTreeNode<T> setLeft(BinaryTreeNode<T> left) {
        this.left = left;
        return this;
    }

    BinaryTreeNode<T> getRight() {
        return right;
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
