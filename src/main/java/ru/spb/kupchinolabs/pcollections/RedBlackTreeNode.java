package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by vladimir-k on 16.03.17.
 */
class RedBlackTreeNode<T extends Comparable<T> & Serializable> implements Serializable {

    private RedBlackTreeNode<T> parent;
    private RedBlackTreeNode<T> left;
    private RedBlackTreeNode<T> right;
    private RedBlackTreeNodeColor color;
    private T value;

    RedBlackTreeNode() {
        this.color = RedBlackTreeNodeColor.RED;
    }

    Optional<RedBlackTreeNode<T>> getParent() {
        return Optional.ofNullable(parent);
    }

    void setParent(RedBlackTreeNode<T> parent) {
        this.parent = parent;
    }

    Optional<RedBlackTreeNode<T>> getLeft() {
        return Optional.ofNullable(left);
    }

    void setLeft(RedBlackTreeNode<T> left) {
        this.left = left;
    }

    Optional<RedBlackTreeNode<T>> getRight() {
        return Optional.ofNullable(right);
    }

    void setRight(RedBlackTreeNode<T> right) {
        this.right = right;
    }

    RedBlackTreeNodeColor getColor() {
        return color;
    }

    void setColor(RedBlackTreeNodeColor color) {
        this.color = color;
    }

    Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    RedBlackTreeNode<T> setValue(T value) {
        this.value = value;
        return this;
    }
}
