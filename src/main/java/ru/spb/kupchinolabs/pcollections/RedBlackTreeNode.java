package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by vladimir-k on 16.03.17.
 */
class RedBlackTreeNode<T extends Comparable<T> & Serializable> extends BinaryTreeNode<T> implements Serializable {

    private BinaryTreeNode<T> parent;
    private RedBlackTreeNodeColor color;

    RedBlackTreeNode() {
        this.color = RedBlackTreeNodeColor.RED;
    }

    Optional<BinaryTreeNode<T>> getParent() {
        return Optional.ofNullable(parent);
    }

    void setParent(BinaryTreeNode<T> parent) {
        this.parent = parent;
    }

    RedBlackTreeNodeColor getColor() {
        return color;
    }

    void setColor(RedBlackTreeNodeColor color) {
        this.color = color;
    }

}
