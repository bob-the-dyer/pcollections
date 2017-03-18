package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.BLACK;

/**
 * Created by vladimir-k on 16.03.17.
 */
class BinaryTreeUtils {

    static <T extends Comparable<T> & Serializable> T cloneElement(T element) {
        //TODO consider cloning element this way (via serialization/deserialization) or another
        //disabling for now as of significant performance degradation
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

    private static <T extends Comparable<T> & Serializable> T maximum(BinaryTreeNode<T> node) {
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

    static <T extends Comparable<T> & Serializable> boolean searchAndRemove(Consumer<BinaryTreeNode<T>> rootNodeSetter, T element,
                                                                            BinaryTreeNode<T> currentNode, BinaryTreeNode<T> parentNode,
                                                                            boolean lastStepLeft, Consumer<BinaryTreeNode<T>> parentNodeModifier) {
        while (currentNode != null) {
            T curValue = currentNode.getValue();
            int compareTo = curValue.compareTo(element);
            if (compareTo == 0)
                return remove(rootNodeSetter, currentNode, parentNode, lastStepLeft, parentNodeModifier);
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

    private static <T extends Comparable<T> & Serializable> boolean remove(Consumer<BinaryTreeNode<T>> rootNodeSetter,
                                                                           BinaryTreeNode<T> currentNode, BinaryTreeNode<T> parentNode,
                                                                           boolean lastStepLeft, Consumer<BinaryTreeNode<T>> parentNodeModifier) {
        if (!currentNode.getLeft().isPresent() && !currentNode.getRight().isPresent()) { //leaf with no children
            if (parentNode == currentNode) {
                rootNodeSetter.accept(null);
            } else if (lastStepLeft) {
                parentNode.setLeft(null);
            } else {
                parentNode.setRight(null);
            }
            parentNodeModifier.accept(parentNode);
            return true;
        }
        if (currentNode.getLeft().isPresent() ^ currentNode.getRight().isPresent()) { //leaf with single child
            BinaryTreeNode<T> child = currentNode.getLeft().isPresent() ? currentNode.getLeft().get() : currentNode.getRight().get();
            if (parentNode == currentNode) {
                rootNodeSetter.accept(child);
            } else if (lastStepLeft) {
                parentNode.setLeft(child);
            } else {
                parentNode.setRight(child);
            }
            parentNodeModifier.accept(parentNode);
            return true;
        }
        if (currentNode.getLeft().isPresent() && currentNode.getRight().isPresent()) { //leaf with both children
            BinaryTreeNode<T> leftChild = currentNode.getLeft().get();
            T maxFromLeft = maximum(leftChild);
            currentNode.setValue(maxFromLeft);
            return searchAndRemove(rootNodeSetter, maxFromLeft, leftChild, currentNode, true, parentNodeModifier);
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

    static <T extends Comparable<T> & Serializable> void repaintAndRebalanceOnInsert(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> currentNode) {
        //TODO later
    }

    static <T extends Comparable<T> & Serializable> void repaintAndRebalanceOnRemove(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> parentOfRemoved) {
        //TODO later
    }

    static <T extends Comparable<T> & Serializable> void validateTree(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> changedNode) {
        if (ts.rootNode == null) return;
        validateRootIsBlack(ts);
        validateEachRedNodeHasBlackChildrenRecursively(ts.rootNode);
        validateBlackDepth(ts.rootNode);
    }

    private static <T extends Comparable<T> & Serializable> void validateRootIsBlack(RedBlackTreeSet<T> ts) {
        if (ts.rootNode.getColor() != BLACK)
            throw new IllegalStateException("violation of RBTree property on node " + ts.rootNode + " : root should be black");
    }

    private static <T extends Comparable<T> & Serializable> void validateEachRedNodeHasBlackChildrenRecursively(RedBlackTreeNode<T> node) {
        if (node.getColor() != RedBlackTreeNodeColor.RED) return;
        if (node.getLeft().isPresent()) {
            RedBlackTreeNode<T> leftChild = (RedBlackTreeNode<T>) node.getLeft().get();
            if (leftChild.getColor() != BLACK) {
                throw new IllegalStateException("violation of RBTree property " + node + ": each red node should have black children");
            }
            validateEachRedNodeHasBlackChildrenRecursively(leftChild);
        }
        if (node.getRight().isPresent()) {
            RedBlackTreeNode<T> rightChild = (RedBlackTreeNode<T>) node.getRight().get();
            if (rightChild.getColor() != BLACK) {
                throw new IllegalStateException("violation of RBTree property on node " + node + " : each red node should have black children");
            }
            validateEachRedNodeHasBlackChildrenRecursively(rightChild);
        }
    }

    private static <T extends Comparable<T> & Serializable> void validateBlackDepth(RedBlackTreeNode<T> node) {
        //TODO
    }


}
