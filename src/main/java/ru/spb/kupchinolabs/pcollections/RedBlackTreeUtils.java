package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;

import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.BLACK;

/**
 * Created by vladimir-k on 16.03.17.
 */
class RedBlackTreeUtils {

    static <T extends Comparable<T> & Serializable> boolean treeIsEmpty(RedBlackTreeSet<T> ts) {
        return ts.rootNode == null;
    }

    static <T extends Comparable<T> & Serializable> void insertRoot(RedBlackTreeSet<T> ts, T element) {
        if (!treeIsEmpty(ts)) {
            throw new IllegalStateException("inserting root for not empty tree");
        }

        RedBlackTreeNode<T> root = buildNodeWithLeafs(element, null);
        root.setColor(BLACK);
        ts.rootNode = root;
    }

    static <T extends Comparable<T> & Serializable> RedBlackTreeNode<T> buildNodeWithLeafs(T element, RedBlackTreeNode<T> parent) {
        RedBlackTreeNode<T> node = new RedBlackTreeNode<>();
        node.setValue(element);
        node.setParent(parent);

        RedBlackTreeNode<T> left = new RedBlackTreeNode<>();
        left.setColor(BLACK);
        node.setLeft(left);

        RedBlackTreeNode<T> right = new RedBlackTreeNode<>();
        right.setColor(BLACK);
        node.setRight(right);

        return node;
    }

    static <T extends Comparable<T> & Serializable> void repaintAndRebalance(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> currentNode) {

    }
}
