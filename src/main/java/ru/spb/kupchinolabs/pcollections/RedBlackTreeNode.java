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

    RedBlackTreeNode<T> setParent(RedBlackTreeNode<T> parent) {
        this.parent = parent;
        return this;
    }

    RedBlackTreeNodeColor getColor() {
        return color;
    }

    RedBlackTreeNode<T> setColor(RedBlackTreeNodeColor color) {
        this.color = color;
        return this;
    }

    @Override
    public String toString() {
        return "RBTNode{" +
                "value=" + getValue() +
                ", parent=" + (parent != null ? parent.getValue() + "_" + parent.getColor().getShortName() : null) +
                ", left=" + (getLeft().isPresent() ? getLeft().get().getValue() + "_" + ((RedBlackTreeNode<T>) getLeft().get()).getColor().getShortName() : null) +
                ", right=" + (getRight().isPresent() ? getRight().get().getValue() + "_" + ((RedBlackTreeNode<T>) getRight().get()).getColor().getShortName() : null) +
                ", color=" + color +
                '}';
    }
}
