package ru.spb.kupchinolabs.pcollections;

import java.io.*;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.BLACK;
import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.RED;

/**
 * Created by vladimir-k on 16.03.17.
 */
class BinaryTreeUtils {

    static Object cloneObject(Object object) {
        //TODO consider cloning object another way as of significant performance degradation or restricting elements to be Clonable
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(baos);
            outputStream.writeObject(object);
            outputStream.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream inputStream = new ObjectInputStream(bais);
            return inputStream.readObject();
        } catch (Exception e) {
            throw new IllegalStateException("fail in cloning object " + object + " via serialization/deserialization", e);
        }
    }

    private static <T extends Comparable<T> & Serializable> T maximum(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> maxNode = node;
        while (maxNode.getRight().isPresent()) maxNode = maxNode.getRight().get();
        return maxNode.getValue();
    }

    private static <T extends Comparable<T> & Serializable> T minimum(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> minNode = node;
        while (minNode.getLeft().isPresent()) minNode = minNode.getLeft().get();
        return minNode.getValue();
    }

    static <T extends Comparable<T> & Serializable> boolean insertElement(BinaryTreeNode<T> rootNode, T newElement,
                                                                          Supplier<BinaryTreeNode<T>> newNodeSupplier,
                                                                          Consumer<BinaryTreeNode<T>> onNodeInsertCallback) {
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
        onNodeInsertCallback.accept(parentNode);
        return true;
    }

    static <T extends Comparable<T> & Serializable> boolean searchAndRemove(Consumer<BinaryTreeNode<T>> rootNodeSetter, T element,
                                                                            BinaryTreeNode<T> currentNode, BinaryTreeNode<T> parentNode,
                                                                            boolean lastStepLeft, Consumer<OnNodeRemoveCallbackArgument<T>> onNodeRemoveCallback) {
        while (currentNode != null) {
            T curValue = currentNode.getValue();
            int compareTo = curValue.compareTo(element);
            if (compareTo == 0)
                return remove(rootNodeSetter, currentNode, parentNode, lastStepLeft, onNodeRemoveCallback);
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
                                                                           boolean lastStepLeft, Consumer<OnNodeRemoveCallbackArgument<T>> onNodeRemoveCallback) {
        if (!currentNode.getLeft().isPresent() && !currentNode.getRight().isPresent()) { //leaf with no children
            if (parentNode == currentNode) {
                rootNodeSetter.accept(null);
                parentNode = null;
            } else if (lastStepLeft) {
                parentNode.setLeft(null);
            } else {
                parentNode.setRight(null);
            }
            OnNodeRemoveCallbackArgument<T> callbackArgument = new OnNodeRemoveCallbackArgument<>(currentNode, parentNode);
            onNodeRemoveCallback.accept(callbackArgument);
            return true;
        }
        if (currentNode.getLeft().isPresent() || currentNode.getRight().isPresent()) { //leaf with at least one child
            if (currentNode.getLeft().isPresent()) {
                BinaryTreeNode<T> leftChild = currentNode.getLeft().get();
                T maxFromLeft = maximum(leftChild);
                currentNode.setValue(maxFromLeft);
                return searchAndRemove(rootNodeSetter, maxFromLeft, leftChild, currentNode, true, onNodeRemoveCallback);
            } else {
                BinaryTreeNode<T> rightChild = currentNode.getRight().get();
                T minFromRight = minimum(rightChild);
                currentNode.setValue(minFromRight);
                return searchAndRemove(rootNodeSetter, minFromRight, rightChild, currentNode, false, onNodeRemoveCallback);
            }
        }
        throw new IllegalStateException("we shouldn't get here ever");
    }

    static <T extends Comparable<T> & Serializable> boolean contains(T element, BinaryTreeNode<T> rootNode) {
        if (element == null) throw new NullPointerException("null elements are not supported");
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
        if (!currentNode.getParent().isPresent()) { // currentNode is root
            currentNode.setColor(BLACK);
        } else if (currentNode.getParent().get().getColor() == BLACK) {
            return;
        } else { // currentNode's parent is RED, which means grandparent exists
            RedBlackTreeNode<T> parent = currentNode.getParent().get();
            RedBlackTreeNode<T> grandparent = grandparent(currentNode).get();
            Optional<RedBlackTreeNode<T>> uncle = uncle(currentNode);
            if (uncle.isPresent() && uncle.get().getColor() == RED) {
                uncle.get().setColor(BLACK);
                parent.setColor(BLACK);
                grandparent.setColor(RED);
                repaintAndRebalanceOnInsert(ts, grandparent);
            } else { //uncle is black or doesn't exist
                if (parent.getRight().isPresent() && parent.getRight().get() == currentNode &&
                        grandparent.getLeft().isPresent() && grandparent.getLeft().get() == parent) {
                    rotateLeft(true, currentNode, parent, grandparent, ts);
                    lastRepaintAndRebalance((RedBlackTreeNode<T>) currentNode.getLeft().get(), ts);
                } else if (parent.getLeft().isPresent() && parent.getLeft().get() == currentNode &&
                        grandparent.getRight().isPresent() && grandparent.getRight().get() == parent) {
                    rotateRight(true, currentNode, parent, grandparent, ts);
                    lastRepaintAndRebalance((RedBlackTreeNode<T>) currentNode.getRight().get(), ts);
                } else {
                    lastRepaintAndRebalance(currentNode, ts);
                }
            }
        }
    }

    private static <T extends Comparable<T> & Serializable> void lastRepaintAndRebalance(RedBlackTreeNode<T> node, RedBlackTreeSet<T> ts) {
        RedBlackTreeNode<T> parent = node.getParent().get();
        RedBlackTreeNode<T> grandparent = grandparent(node).get();
        parent.setColor(BLACK);
        grandparent.setColor(RED);
        Optional<RedBlackTreeNode<T>> grandgrandparent = grandparent.getParent();
        if (parent.getLeft().isPresent() && parent.getLeft().get() == node) {
            boolean fromCenterToOutside = grandgrandparent.isPresent()
                    && grandgrandparent.get().getRight().isPresent()
                    && grandgrandparent.get().getRight().get() == grandparent;
            rotateRight(fromCenterToOutside, parent, grandparent, grandgrandparent.orElse(null), ts);
        } else {
            boolean fromCenterToOutside = grandgrandparent.isPresent()
                    && grandgrandparent.get().getLeft().isPresent()
                    && grandgrandparent.get().getLeft().get() == grandparent;
            rotateLeft(fromCenterToOutside, parent, grandparent, grandgrandparent.orElse(null), ts);
        }
    }

    private static <T extends Comparable<T> & Serializable> void rotateLeft(boolean fromCenterToOutside, RedBlackTreeNode<T> rightChild, RedBlackTreeNode<T> node, RedBlackTreeNode<T> parent, RedBlackTreeSet<T> ts) {
        rightChild.setParent(parent);
        if (parent != null) {
            if (fromCenterToOutside) {
                parent.setLeft(rightChild);
            } else {
                parent.setRight(rightChild);
            }
        } else {
            ts.rootNode = rightChild;
        }
        node.setParent(rightChild);
        if (rightChild.getLeft().isPresent()) {
            ((RedBlackTreeNode<T>) rightChild.getLeft().get()).setParent(node);
        }
        node.setRight(rightChild.getLeft().orElse(null));
        rightChild.setLeft(node);
    }

    private static <T extends Comparable<T> & Serializable> void rotateRight(boolean fromCenterToOutside, RedBlackTreeNode<T> leftChild, RedBlackTreeNode<T> node, RedBlackTreeNode<T> parent, RedBlackTreeSet<T> ts) {
        leftChild.setParent(parent);
        if (parent != null) {
            if (fromCenterToOutside) {
                parent.setRight(leftChild);
            } else {
                parent.setLeft(leftChild);
            }
        } else {
            ts.rootNode = leftChild;
        }
        node.setParent(leftChild);
        if (leftChild.getRight().isPresent()) {
            ((RedBlackTreeNode<T>) leftChild.getRight().get()).setParent(node);
        }
        node.setLeft(leftChild.getRight().orElse(null));
        leftChild.setRight(node);
    }

    static <T extends Comparable<T> & Serializable> void repaintAndRebalanceOnRemove(RedBlackTreeSet<T> ts,
                                                                                     RedBlackTreeNode<T> removedNode, RedBlackTreeNode<T> currentNode) {
        if (currentNode == null) { // empty tree
            return;
        }
        if (removedNode.getColor() == RED) {
            return;
        }

        // removedNode is black and currentNode can be black or red, tree has more than single root node

        //adding phantom node
        RedBlackTreeNode<T> phantomNode = new RedBlackTreeNode<T>()
                .setParent(currentNode)
                .setColor(BLACK);
        if (currentNode.getLeft().isPresent()) {
            currentNode.setRight(phantomNode);
        } else {
            currentNode.setLeft(phantomNode);
        }

        repaintAndRebalanceOnRemoveRecursively(ts, phantomNode);

        //cleaning up phantom node
        RedBlackTreeNode<T> phantomChild = null;
        if (phantomNode.getLeft().isPresent()) { //TODO FIXME phantomNode should remain leaf, meaning no child
            phantomChild = (RedBlackTreeNode<T>) phantomNode.getLeft().get();
        } else if (phantomNode.getRight().isPresent()) {
            phantomChild = (RedBlackTreeNode<T>) phantomNode.getRight().get();
        }
        RedBlackTreeNode<T> phantomParent = phantomNode.getParent().get();
        if (phantomChild != null) {
            phantomChild.setParent(phantomParent);
        }
        if (phantomParent.getLeft().isPresent() && phantomParent.getLeft().get() == phantomNode) {
            phantomParent.setLeft(phantomChild != null ? phantomChild : null);
        } else if (phantomParent.getRight().isPresent() && phantomParent.getRight().get() == phantomNode) {
            phantomParent.setRight(phantomChild != null ? phantomChild : null);
        } else {
            throw new IllegalStateException("we shouldn't get here ever");
        }
        phantomNode.setParent(null);
    }

    private static <T extends Comparable<T> & Serializable> void repaintAndRebalanceOnRemoveRecursively(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> currentNode) {
        if (!currentNode.getParent().isPresent()) { // we've reached root node
            return;
        } else {
            RedBlackTreeNode<T> sibling = sibling(currentNode).get();
            RedBlackTreeNode<T> parent = currentNode.getParent().get();
            if (sibling.getColor() == RED) { // case sibling is red
                assert parent.getColor() == BLACK;
                parent.setColor(RED);
                sibling.setColor(BLACK);
                Optional<RedBlackTreeNode<T>> grandparent = parent.getParent();
                if (parent.getLeft().isPresent() && parent.getLeft().get() == currentNode) {
                    boolean b = grandparent.isPresent()
                            && grandparent.get().getRight().isPresent()
                            && grandparent.get().getRight().get() == parent;
                    rotateLeft(!b, sibling, parent, grandparent.orElse(null), ts);
                } else {
                    boolean b = grandparent.isPresent()
                            && grandparent.get().getLeft().isPresent()
                            && grandparent.get().getLeft().get() == parent;
                    rotateRight(!b, sibling, parent, grandparent.orElse(null), ts);
                }
            }
            sibling = sibling(currentNode).get(); // sibling for currentNode has been changed (?)
            parent = currentNode.getParent().get(); // parent for currentNode has been changed (?)
            if (sibling.getColor() == BLACK && parent.getColor() == BLACK
                    && (!sibling.getLeft().isPresent() || ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == BLACK)
                    && (!sibling.getRight().isPresent() || ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == BLACK)) { // sibling and its children are black, parent is black
                sibling.setColor(RED);
                repaintAndRebalanceOnRemoveRecursively(ts, parent);
            } else if (sibling.getColor() == BLACK && parent.getColor() == RED
                    && (!sibling.getLeft().isPresent() || ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == BLACK)
                    && (!sibling.getRight().isPresent() || ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == BLACK)) { // sibling and its children are black, parent is red
                sibling.setColor(RED);
                parent.setColor(BLACK);
            } else {
                if (sibling.getColor() == BLACK && parent.getLeft().get() == currentNode
                        && (sibling.getLeft().isPresent() && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == RED)
                        && (!sibling.getRight().isPresent() || ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == BLACK)) {
                    sibling.setColor(RED);
                    ((RedBlackTreeNode<T>) sibling.getLeft().get()).setColor(BLACK);
                    rotateRight(true, (RedBlackTreeNode<T>) sibling.getLeft().get(), sibling, parent, ts);
                } else if (sibling.getColor() == BLACK && parent.getRight().get() == currentNode
                        && (sibling.getRight().isPresent() && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == RED)
                        && (!sibling.getLeft().isPresent() || ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == BLACK)) {
                    sibling.setColor(RED);
                    ((RedBlackTreeNode<T>) sibling.getRight().get()).setColor(BLACK);
                    rotateLeft(true, (RedBlackTreeNode<T>) sibling.getRight().get(), sibling, parent, ts);
                }
                sibling = sibling(currentNode).get(); // sibling for currentNode has been changed (?)
                parent = currentNode.getParent().get(); // parent for currentNode has been changed (?)
                if (sibling.getColor() == BLACK && parent.getLeft().get() == currentNode
                        && (sibling.getRight().isPresent() && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == RED)) {
                    sibling.setColor(parent.getColor());
                    parent.setColor(BLACK);
                    ((RedBlackTreeNode<T>) sibling.getRight().get()).setColor(BLACK);
                    Optional<RedBlackTreeNode<T>> grandparent = grandparent(currentNode);
                    boolean b = grandparent.isPresent()
                            && grandparent.get().getRight().isPresent()
                            && grandparent.get().getRight().get() == parent;
                    rotateLeft(!b, sibling, parent, grandparent.orElse(null), ts);
                } else if (sibling.getColor() == BLACK && parent.getRight().get() == currentNode
                        && (sibling.getLeft().isPresent() && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == RED)) {
                    sibling.setColor(parent.getColor());
                    parent.setColor(BLACK);
                    ((RedBlackTreeNode<T>) sibling.getLeft().get()).setColor(BLACK);
                    Optional<RedBlackTreeNode<T>> grandparent = grandparent(currentNode);
                    boolean b = grandparent.isPresent()
                            && grandparent.get().getLeft().isPresent()
                            && grandparent.get().getLeft().get() == parent;
                    rotateRight(!b, sibling, parent, grandparent.orElse(null), ts);
                }
            }
        }
    }

    static <T extends Comparable<T> & Serializable> void validateTree(RedBlackTreeSet<T> ts) {
        if (ts.rootNode == null) return;
        validateRootIsBlack(ts);
        validateEachRedNodeHasBlackChildrenRecursively(ts.rootNode, ts);
        validateSameBlackDepthRecursively(ts.rootNode, ts);
    }

    private static <T extends Comparable<T> & Serializable> void validateRootIsBlack(RedBlackTreeSet<T> ts) {
        if (ts.rootNode.getColor() != BLACK) {
            throw new IllegalStateException("violation of RBTree property on node " + ts.rootNode + " : root should be black, " + ts);
        }
    }

    private static <T extends Comparable<T> & Serializable> void validateEachRedNodeHasBlackChildrenRecursively(RedBlackTreeNode<T> node, RedBlackTreeSet<T> ts) {
        if (node.getLeft().isPresent()) {
            RedBlackTreeNode<T> leftChild = (RedBlackTreeNode<T>) node.getLeft().get();
            if (node.getColor() == RED && leftChild.getColor() != BLACK) {
                throw new IllegalStateException("violation of RBTree property " + node + ": each red node should have black children, " + ts);
            }
            validateEachRedNodeHasBlackChildrenRecursively(leftChild, ts);
        }
        if (node.getRight().isPresent()) {
            RedBlackTreeNode<T> rightChild = (RedBlackTreeNode<T>) node.getRight().get();
            if (node.getColor() == RED && rightChild.getColor() != BLACK) {
                throw new IllegalStateException("violation of RBTree property on node " + node + " : each red node should have black children, " + ts);
            }
            validateEachRedNodeHasBlackChildrenRecursively(rightChild, ts);
        }
    }

    private static <T extends Comparable<T> & Serializable> int validateSameBlackDepthRecursively(RedBlackTreeNode<T> node, RedBlackTreeSet<T> ts) {
        if (null == node) {
            return 0;
        } else {
            int blackDepthLeft = validateSameBlackDepthRecursively((RedBlackTreeNode<T>) node.getLeft().orElse(null), ts);
            int blackDepthRight = validateSameBlackDepthRecursively((RedBlackTreeNode<T>) node.getRight().orElse(null), ts);
            if (blackDepthLeft != blackDepthRight) {
                throw new IllegalStateException("violation of RBTree property on node " + node + " : black depth differs " + blackDepthLeft + "(left) vs. " + blackDepthRight + ", " + ts);
            } else {
                return (node.getColor() == BLACK ? 1 : 0) + blackDepthLeft;
            }
        }
    }

    static <T extends Comparable<T> & Serializable> String buildWidthTraverseStringPyramidRB(Queue<RedBlackTreeNode<T>> queue) {
        StringBuilder sb = new StringBuilder();
        Queue<RedBlackTreeNode<T>> newQueue = new LinkedBlockingDeque<>();
        while (!queue.isEmpty()) {
            RedBlackTreeNode<T> node = queue.remove();
            sb.append(node.getValue()).append("_").append(node.getColor().getShortName()).append(" ");
            if (node.getLeft().isPresent()) {
                newQueue.offer((RedBlackTreeNode<T>) node.getLeft().get());
            }
            if (node.getRight().isPresent()) {
                newQueue.offer((RedBlackTreeNode<T>) node.getRight().get());
            }
        }
        sb.append("\n");
        if (!newQueue.isEmpty()) {
            sb.append(buildWidthTraverseStringPyramidRB(newQueue));
        }
        return sb.toString();
    }

    static <T extends Comparable<T> & Serializable> String buildWidthTraverseStringPyramid(Queue<BinaryTreeNode<T>> queue) {
        StringBuilder sb = new StringBuilder();
        Queue<BinaryTreeNode<T>> newQueue = new LinkedBlockingDeque<>();
        while (!queue.isEmpty()) {
            BinaryTreeNode<T> node = queue.remove();
            sb.append(node.getValue()).append(" ");
            if (node.getLeft().isPresent()) {
                newQueue.offer(node.getLeft().get());
            }
            if (node.getRight().isPresent()) {
                newQueue.offer(node.getRight().get());
            }
        }
        sb.append("\n");
        if (!newQueue.isEmpty()) {
            sb.append(buildWidthTraverseStringPyramid(newQueue));
        }
        return sb.toString();
    }

    private static <T extends Comparable<T> & Serializable> Optional<RedBlackTreeNode<T>> grandparent(RedBlackTreeNode<T> node) {
        if (node != null && node.getParent().isPresent()) {
            return node.getParent().get().getParent();
        } else {
            return Optional.empty();
        }
    }

    static <T extends Comparable<T> & Serializable> Optional<RedBlackTreeNode<T>> sibling(RedBlackTreeNode<T> node) {
        if (node != null && node.getParent().isPresent()) {
            RedBlackTreeNode<T> parent = node.getParent().get();
            if (parent.getLeft().isPresent() && parent.getLeft().get() == node) {
                return (Optional<RedBlackTreeNode<T>>) parent.getRight();
            } else {
                return (Optional<RedBlackTreeNode<T>>) parent.getLeft();
            }
        } else {
            return Optional.empty();
        }
    }

    private static <T extends Comparable<T> & Serializable> Optional<RedBlackTreeNode<T>> uncle(RedBlackTreeNode<T> node) {
        Optional<RedBlackTreeNode<T>> grandparent = grandparent(node);
        if (grandparent.isPresent()) {
            if (node.getParent().get() == grandparent.get().getLeft().orElse(null)) {
                return (Optional<RedBlackTreeNode<T>>) grandparent.get().getRight();
            } else {
                return (Optional<RedBlackTreeNode<T>>) grandparent.get().getLeft();
            }
        } else {
            return Optional.empty();
        }
    }

    static <T extends Comparable<T> & Serializable> int size(SimpleSet<T> ts) {
        final int[] size = {0};
        ts.forEach(i -> {
            size[0]++;
        });
        return size[0];
    }

}
