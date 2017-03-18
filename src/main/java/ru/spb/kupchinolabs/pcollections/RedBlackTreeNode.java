package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by vladimir-k on 16.03.17.
 */
class RedBlackTreeNode<T extends Comparable<T> & Serializable> extends BinaryTreeNode<T> implements Serializable {

    private RedBlackTreeNode<T> parent;
    private RedBlackTreeNodeColor color;

    RedBlackTreeNode() {
        this.color = RedBlackTreeNodeColor.RED;
    }

    Optional<RedBlackTreeNode<T>> getParent() {
        return Optional.ofNullable(parent);
    }

    void setParent(RedBlackTreeNode<T> parent) {
        this.parent = parent;
    }

    RedBlackTreeNodeColor getColor() {
        return color;
    }

    void setColor(RedBlackTreeNodeColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "RBTNode{" +
                "value=" + getValue() +
                ", parent=" + (parent != null ? parent.getValue() : null) +
                ", left=" + (getLeft().isPresent() ? getLeft().get().getValue() : null) +
                ", right=" + (getRight().isPresent() ? getRight().get().getValue() : null) +
                ", color=" + color +
                '}';
    }
}
