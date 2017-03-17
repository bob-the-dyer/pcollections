package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.BLACK;

/**
 * Created by vladimir-k on 16.03.17.
 */
class BinaryTreeUtils {

    static <T extends Comparable<T> & Serializable> boolean treeIsEmpty(RedBlackTreeSet<T> ts) {
        return ts.rootNode == null;
    }

    static <T extends Comparable<T> & Serializable> void insertRoot(BinaryTreeSet<T> ts, T element) {
        if (!treeIsEmpty(ts)) {
            throw new IllegalStateException("inserting root for not empty tree");
        }
        ts.rootNode = new BinaryTreeNode<T>();
        ts.rootNode.setValue(element);
    }

    static <T extends Comparable<T> & Serializable> void insertRoot(RedBlackTreeSet<T> ts, T element) {
        if (!treeIsEmpty(ts)) {
            throw new IllegalStateException("inserting root for not empty tree");
        }
        RedBlackTreeNode<T> node = new RedBlackTreeNode<>();
        node.setValue(element);
        node.setParent(null);
        node.setColor(BLACK);
        ts.rootNode = node;
    }

    static <T extends Comparable<T> & Serializable> void repaintAndRebalance(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> currentNode) {

    }

    static <T extends Comparable<T> & Serializable> T cloneElement(T element) {
        //TODO consider cloning element this way or another
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream outputStream = new ObjectOutputStream(baos);
//            outputStream.writeObject(element);
//            outputStream.close();
//
//            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//            ObjectInputStream inputStream = new ObjectInputStream(bais);
//            newElement = (T) inputStream.readObject();
//        } catch (Exception e) {
//            throw new IllegalStateException("fail in cloning element " + element + " via serialization/deserialization", e);
//        }
        return element;
    }

    static <T extends Comparable<T> & Serializable> boolean treeIsEmpty(BinaryTreeSet<T> ts) {
        return ts.rootNode == null;
    }

    static <T extends Comparable<T> & Serializable> T maximum(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> maxNode = node;
        while (maxNode.getRight().isPresent()) maxNode = maxNode.getRight().get();
        return maxNode.getValue().get();
    }

    static <T extends Comparable<T> & Serializable> boolean insertElement(BinaryTreeNode<T> rootNode, T newElement,
                                                                          Supplier<BinaryTreeNode<T>> newNodeSupplier,
                                                                          Consumer<BinaryTreeNode<T>> parentConsumer) {
        BinaryTreeNode<T> parentNode = null;
        BinaryTreeNode<T> currentNode = rootNode;
        boolean lastStepLeft = false;
        while (currentNode != null) {
            T curValue = currentNode.getValue().get();
            int compareTo = curValue.compareTo(newElement);
            if (compareTo == 0) return false;
            parentNode = currentNode;
            if (compareTo < 0) {
                currentNode = currentNode.getRight().orElse(null);
                lastStepLeft = false;
            } else {
                currentNode = currentNode.getLeft().orElse(null);
                lastStepLeft = true;
            }
        }

        BinaryTreeNode<T> newNode = newNodeSupplier.get();
        newNode.setValue(newElement);

        if (lastStepLeft) {
            parentNode.setLeft(newNode);
        } else {
            parentNode.setRight(newNode);
        }

        parentConsumer.accept(parentNode);

        return true;
    }

    static <T extends Comparable<T> & Serializable> int size(SimpleSet<T> ts) {
        final int[] size = {0};
        ts.forEach(i -> {
            size[0]++;
        });
        return size[0];
    }


}
