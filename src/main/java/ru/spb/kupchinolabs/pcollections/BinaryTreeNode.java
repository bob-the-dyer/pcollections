package ru.spb.kupchinolabs.pcollections;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeNode<T extends Comparable<T>> {

    private BinaryTreeNode left;
    private BinaryTreeNode right;
    private T value;

    public BinaryTreeNode<T> getLeft() {
        return left;
    }

    public BinaryTreeNode<T> setLeft(BinaryTreeNode<T> left) {
        this.left = left;
        return this;
    }

    public BinaryTreeNode<T> getRight() {
        return right;
    }

    public BinaryTreeNode<T> setRight(BinaryTreeNode<T> right) {
        this.right = right;
        return this;
    }

    public T getValue() {
        return value;
    }

    public BinaryTreeNode<T> setValue(T value) {
        this.value = value;
        return this;
    }
}
