package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
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
            } else if (lastStepLeft) {
                parentNode.setLeft(null);
            } else {
                parentNode.setRight(null);
            }
            OnNodeRemoveCallbackArgument<T> callbackArgument = new OnNodeRemoveCallbackArgument<>(currentNode, parentNode);
            onNodeRemoveCallback.accept(callbackArgument);
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
            OnNodeRemoveCallbackArgument<T> callbackArgument = new OnNodeRemoveCallbackArgument<>(currentNode, child);
            onNodeRemoveCallback.accept(callbackArgument);
            return true;
        }
        if (currentNode.getLeft().isPresent() && currentNode.getRight().isPresent()) { //leaf with both children
            BinaryTreeNode<T> leftChild = currentNode.getLeft().get();
            T maxFromLeft = maximum(leftChild);
            currentNode.setValue(maxFromLeft);
            return searchAndRemove(rootNodeSetter, maxFromLeft, leftChild, currentNode, true, onNodeRemoveCallback);
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
            boolean internal = grandgrandparent.isPresent()
                    && grandgrandparent.get().getRight().isPresent()
                    && grandgrandparent.get().getRight().get() == grandparent;
            rotateRight(internal, parent, grandparent, grandgrandparent.orElse(null), ts);
        } else {
            boolean internal = grandgrandparent.isPresent()
                    && grandgrandparent.get().getLeft().isPresent()
                    && grandgrandparent.get().getLeft().get() == grandparent;
            rotateLeft(internal, parent, grandparent, grandgrandparent.orElse(null), ts);
        }
    }

    private static <T extends Comparable<T> & Serializable> void rotateLeft(boolean internal, RedBlackTreeNode<T> rightChild, RedBlackTreeNode<T> node, RedBlackTreeNode<T> parent, RedBlackTreeSet<T> ts) {
        rightChild.setParent(parent);
        if (parent != null) {
            if (internal) {
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

    private static <T extends Comparable<T> & Serializable> void rotateRight(boolean internal, RedBlackTreeNode<T> leftChild, RedBlackTreeNode<T> node, RedBlackTreeNode<T> parent, RedBlackTreeSet<T> ts) {
        leftChild.setParent(parent);
        if (parent != null) {
            if (internal) {
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
        if (removedNode.getColor() == RED) {
            return;
        }
        if (removedNode.getColor() == BLACK && currentNode != null && currentNode.getColor() == RED) {
            currentNode.setColor(BLACK);
            return;
        }
        // removedNode, parent and currentNode are black
        repaintAndRebalanceOnRemoveRecursively(ts, currentNode, true);
    }

    private static <T extends Comparable<T> & Serializable> void repaintAndRebalanceOnRemoveRecursively(RedBlackTreeSet<T> ts, RedBlackTreeNode<T> currentNode, boolean firstRun) {
        if (!currentNode.getParent().isPresent()) { // we've reached root node
            if (!currentNode.getLeft().isPresent() && !currentNode.getRight().isPresent()) { // single node
                return;
            } else {
                if (firstRun) {
                    currentNode.setColor(RED);
                    if (currentNode.getLeft().isPresent()) {
                        rotateRight(false, (RedBlackTreeNode<T>) currentNode.getLeft().get(), currentNode, null, ts);
                    } else {
                        rotateLeft(false, (RedBlackTreeNode<T>) currentNode.getRight().get(), currentNode, null, ts);
                    }
                }
            }
        } else {
            RedBlackTreeNode<T> sibling = sibling(currentNode).get();
            RedBlackTreeNode<T> parent = currentNode.getParent().get();
            if (sibling.getColor() == RED) {
                parent.setColor(RED);
                sibling.setColor(BLACK);
                Optional<RedBlackTreeNode<T>> grandparent = parent.getParent();
                if (parent.getLeft().isPresent() && parent.getLeft().get() == currentNode) {
                    boolean internal = grandparent.isPresent()
                            && grandparent.get().getRight().isPresent()
                            && grandparent.get().getRight().get() == parent;
                    rotateRight(internal, currentNode, parent, grandparent.orElse(null), ts);
                } else {
                    boolean internal = grandparent.isPresent()
                            && grandparent.get().getLeft().isPresent()
                            && grandparent.get().getLeft().get() == parent;
                    rotateLeft(internal, currentNode, parent, grandparent.orElse(null), ts);
                }
            }
            sibling = sibling(currentNode).get(); // sibling for currentNode has been changed (?)
            parent = currentNode.getParent().get(); // parent for currentNode has been changed (?)
            if (sibling.getColor() == BLACK && parent.getColor() == BLACK
                    && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == BLACK
                    && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == BLACK) {
                sibling.setColor(RED);
                repaintAndRebalanceOnRemoveRecursively(ts, parent, false);
            } else if (sibling.getColor() == BLACK && parent.getColor() == RED
                    && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == BLACK
                    && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == BLACK) {
                sibling.setColor(RED);
                parent.setColor(BLACK);
            } else {
                if (sibling.getColor() == BLACK && parent.getLeft().get() == currentNode
                        && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == RED
                        && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == BLACK) {
                    sibling.setColor(RED);
                    ((RedBlackTreeNode<T>) sibling.getLeft().get()).setColor(BLACK);
                    rotateRight(true, (RedBlackTreeNode<T>) sibling.getLeft().get(), sibling, parent, ts);
                } else if (sibling.getColor() == BLACK && parent.getRight().get() == currentNode
                        && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == RED
                        && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == BLACK) {
                    sibling.setColor(RED);
                    ((RedBlackTreeNode<T>) sibling.getRight().get()).setColor(BLACK);
                    rotateLeft(true, (RedBlackTreeNode<T>) sibling.getRight().get(), sibling, parent, ts);
                } else {
                    throw new IllegalStateException("we shouldn't get here ever");
                }
                sibling = sibling(currentNode).get(); // sibling for currentNode has been changed (?)
                parent = currentNode.getParent().get(); // parent for currentNode has been changed (?)
                if (sibling.getColor() == BLACK && parent.getLeft().get() == currentNode
                        && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == RED
                        && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == BLACK) {
                    //just checking
                } else if (sibling.getColor() == BLACK && parent.getRight().get() == currentNode
                        && ((RedBlackTreeNode<T>) sibling.getLeft().get()).getColor() == RED
                        && ((RedBlackTreeNode<T>) sibling.getRight().get()).getColor() == BLACK) {
                    //just checking
                } else {
                    throw new IllegalStateException("we shouldn't get here ever");
                }
                sibling.setColor(parent.getColor());
                parent.setColor(BLACK);
                if (parent.getLeft().get() == currentNode) {
                    ((RedBlackTreeNode<T>) sibling.getRight().get()).setColor(BLACK);
                    rotateLeft(false, sibling, parent, grandparent(currentNode).orElse(null), ts);
                } else {
                    ((RedBlackTreeNode<T>) sibling.getLeft().get()).setColor(BLACK);
                    rotateRight(false, sibling, parent, grandparent(currentNode).orElse(null), ts);
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

    static <T extends Comparable<T> & Serializable> String buildWidthTraverseStringFixed(Queue<RedBlackTreeNode<T>> queue) {
        StringBuilder sb = new StringBuilder();
        Queue<RedBlackTreeNode<T>> newQueue = new LinkedBlockingDeque<>();
        while (!queue.isEmpty()) {
            RedBlackTreeNode<T> node = queue.remove();
            sb.append(node.getValue()).append(node.getColor() == RED ? "R" : "B").append(" ");
            if (node.getLeft().isPresent()) {
                newQueue.offer((RedBlackTreeNode<T>) node.getLeft().get());
            }
            if (node.getRight().isPresent()) {
                newQueue.offer((RedBlackTreeNode<T>) node.getRight().get());
            }
        }
        sb.append("\n");
        if (!newQueue.isEmpty()) {
            sb.append(buildWidthTraverseStringFixed(newQueue));
        }
        return sb.toString();
    }

    static <T extends Comparable<T> & Serializable> Optional<RedBlackTreeNode<T>> grandparent(RedBlackTreeNode<T> node) {
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

    static <T extends Comparable<T> & Serializable> Optional<RedBlackTreeNode<T>> uncle(RedBlackTreeNode<T> node) {
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
}
