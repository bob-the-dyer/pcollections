package ru.spb.kupchinolabs.pcollections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeSet<T extends Comparable<T>> implements IBinaryTreeSet<T> {

    private BinaryTreeNode<T> rootNode;

    public boolean contains(T element) {
        return contains(element, rootNode);
    }

    private boolean contains(T element, BinaryTreeNode<T> node) {
        while (node != null) {
            T curValue = node.getValue();
            int compareTo = curValue.compareTo(element);
            if (compareTo == 0) return true;
            if (compareTo < 0) {
                node = node.getRight();
            } else {
                node = node.getLeft();
            }
        }
        return false;
    }

    public boolean insert(T element) {
        if (element == null) throw new IllegalArgumentException("null elements are not supported");
        if (rootNode == null) {
            rootNode = new BinaryTreeNode<T>().setValue(element);
            return true;
        } else {
            BinaryTreeNode<T> parentNode = null;
            BinaryTreeNode<T> currentNode = rootNode;
            boolean lastStepLeft = false;
            while (currentNode != null) {
                T curValue = currentNode.getValue();
                int compareTo = curValue.compareTo(element);
                if (compareTo == 0) return false;
                parentNode = currentNode;
                if (compareTo < 0) {
                    currentNode = currentNode.getRight();
                    lastStepLeft = false;
                } else {
                    currentNode = currentNode.getLeft();
                    lastStepLeft = true;
                }
            }
            BinaryTreeNode<T> newNode = new BinaryTreeNode<T>().setValue(element);
            if (lastStepLeft) {
                parentNode.setLeft(newNode);
            } else {
                parentNode.setRight(newNode);
            }
            return true;
        }
    }

    @Override
    public boolean remove(T element) {
        //TODO
        return false;
    }

    public int size() {
        final int[] size = {0};
        forEach(i -> {
            size[0]++;
        });
        return size[0];
    }

    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeSetIterator();
    }

    private class BinaryTreeSetIterator implements Iterator<T> {

        Stack<BinaryTreeNodeStep> stack = new Stack<>();

        BinaryTreeSetIterator() {
            if (rootNode != null && rootNode.getValue() != null) {
                if (rootNode.getRight() != null) {
                    stack.push(new BinaryTreeNodeStep(Step.RIGHT, rootNode));
                }
                stack.push(new BinaryTreeNodeStep(Step.VISIT, rootNode));
                if (rootNode.getLeft() != null) {
                    stack.push(new BinaryTreeNodeStep(Step.LEFT, rootNode));
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (stack.isEmpty()) throw new NoSuchElementException("no more elements to iterate");
            BinaryTreeNodeStep curStep = stack.pop();
            while (curStep.step != Step.VISIT) {
                BinaryTreeNode<T> childNode;
                if (curStep.step == Step.LEFT) {
                    childNode = curStep.node.getLeft();
                } else {
                    childNode = curStep.node.getRight();
                }
                if (childNode != null) {
                    if (childNode.getRight() != null) {
                        stack.push(new BinaryTreeNodeStep(Step.RIGHT, childNode));
                    }
                    stack.push(new BinaryTreeNodeStep(Step.VISIT, childNode));
                    if (childNode.getLeft() != null) {
                        stack.push(new BinaryTreeNodeStep(Step.LEFT, childNode));
                    }
                }
                curStep = stack.pop();
            }
            return curStep.node.getValue();
        }

        class BinaryTreeNodeStep {
            final Step step;
            final BinaryTreeNode<T> node;

            BinaryTreeNodeStep(Step step, BinaryTreeNode<T> node) {
                this.step = step;
                this.node = node;
            }

        }

    }

    private enum Step {
        LEFT, RIGHT, VISIT
    }


    //TODO add equals and hashCode to support collection friendliness
}
