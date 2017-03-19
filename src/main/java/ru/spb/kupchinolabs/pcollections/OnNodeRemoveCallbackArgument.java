package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;

/**
 * Created by vladimir-k on 19.03.17.
 */
public class OnNodeRemoveCallbackArgument<T extends Comparable<T> & Serializable> {
    BinaryTreeNode<T> removedNode;
    BinaryTreeNode<T> baseNode;

    OnNodeRemoveCallbackArgument(BinaryTreeNode<T> removedNode, BinaryTreeNode<T> baseNode) {
        this.removedNode = removedNode;
        this.baseNode = baseNode;
    }
}
