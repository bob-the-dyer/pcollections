package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;

/**
 * Created by vladimir-k on 19.03.17.
 */
public class OnNodeRemoveCallbackArgument<T extends Comparable<T> & Serializable> {
    BinaryTreeNode<T> removedNode;
    BinaryTreeNode<T> childNode;
    BinaryTreeNode<T> parentNode;

    OnNodeRemoveCallbackArgument(BinaryTreeNode<T> removedNode, BinaryTreeNode<T> childNode, BinaryTreeNode<T> parentNode) {
        this.removedNode = removedNode;
        this.childNode = childNode;
        this.parentNode = parentNode;
    }
}
