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

    static <T extends Comparable<T> & Serializable> void repaintAndRebalanceOnInsert(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> currentNode) {
        //TODO later
    }

    static <T extends Comparable<T> & Serializable> void repaintAndRebalanceOnRemove(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> parentOfRemoved) {
        //TODO later
    }

    static <T extends Comparable<T> & Serializable> void validateTree(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> changedNode) {
        //TODO later
    }

    static <T extends Comparable<T> & Serializable> T cloneElement(T element) {
//        try { //TODO consider cloning element this way or another
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
        return maxNode.getValue();
    }

    static <T extends Comparable<T> & Serializable> boolean insertElement(BinaryTreeNode<T> rootNode, T newElement,
                                                                          Supplier<BinaryTreeNode<T>> newNodeSupplier,
                                                                          Consumer<BinaryTreeNode<T>> parentConsumer) {
        BinaryTreeNode<T> parentNode = null;
        BinaryTreeNode<T> currentNode = rootNode;
        boolean lastStepLeft = false;
        while (currentNode != null) {
            T curValue = currentNode.getValue();
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

    static <T extends Comparable<T> & Serializable> boolean searchAndRemove(Consumer<BinaryTreeNode<T>> rootNodeConsumer, T element,
                                                                            BinaryTreeNode<T> currentNode, BinaryTreeNode<T> parentNode,
                                                                            boolean lastStepLeft, Consumer<BinaryTreeNode<T>> parentConsumer) {
        while (currentNode != null) {
            T curValue = currentNode.getValue();
            int compareTo = curValue.compareTo(element);
            if (compareTo == 0) return remove(rootNodeConsumer, currentNode, parentNode, lastStepLeft, parentConsumer);
            parentNode = currentNode;
            if (compareTo < 0) {
                currentNode = currentNode.getRight().orElse(null);
                lastStepLeft = false;
            } else {
                currentNode = currentNode.getLeft().orElse(null);
                lastStepLeft = true;
            }
        }
        return false;
    }

    private static <T extends Comparable<T> & Serializable> boolean remove(Consumer<BinaryTreeNode<T>> rootNodeConsumer,
                                                                           BinaryTreeNode<T> currentNode, BinaryTreeNode<T> parentNode,
                                                                           boolean lastStepLeft, Consumer<BinaryTreeNode<T>> parentConsumer) {
        if (!currentNode.getLeft().isPresent() && !currentNode.getRight().isPresent()) { //leaf with no children
            if (parentNode == currentNode) {
                rootNodeConsumer.accept(null);
            } else if (lastStepLeft) {
                parentNode.setLeft(null);
            } else {
                parentNode.setRight(null);
            }
            parentConsumer.accept(parentNode);
            return true;
        }
        if (currentNode.getLeft().isPresent() ^ currentNode.getRight().isPresent()) { //leaf with single child
            BinaryTreeNode<T> child = currentNode.getLeft().isPresent() ? currentNode.getLeft().get() : currentNode.getRight().get();
            if (parentNode == currentNode) {
                rootNodeConsumer.accept(child);
            } else if (lastStepLeft) {
                parentNode.setLeft(child);
            } else {
                parentNode.setRight(child);
            }
            parentConsumer.accept(parentNode);
            return true;
        }
        if (currentNode.getLeft().isPresent() && currentNode.getRight().isPresent()) { //leaf with both children
            BinaryTreeNode<T> leftChild = currentNode.getLeft().get();
            T maxFromLeft = maximum(leftChild);
            currentNode.setValue(maxFromLeft);
            return searchAndRemove(rootNodeConsumer, maxFromLeft, leftChild, currentNode, true, parentConsumer);
        }
        throw new IllegalStateException("we shouldn't get here ever");
    }

    static <T extends Comparable<T> & Serializable> boolean contains(T element, BinaryTreeNode<T> rootNode) {
        BinaryTreeNode<T> node = rootNode;
        while (node != null) {
            T curValue = node.getValue();
            int compareTo = curValue.compareTo(element);
            if (compareTo == 0) return true;
            if (compareTo < 0) {
                node = node.getRight().orElse(null);
            } else {
                node = node.getLeft().orElse(null);
            }
        }
        return false;
    }


}
