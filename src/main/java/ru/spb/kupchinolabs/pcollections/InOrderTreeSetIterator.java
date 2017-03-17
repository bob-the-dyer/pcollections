package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by vladimir-k on 17.03.17.
 */
class InOrderTreeSetIterator<T extends Comparable<T> & Serializable> implements Iterator<T> {

    private Stack<TreeNodeStep> stack = new Stack<>();

    InOrderTreeSetIterator(BinaryTreeNode<T> rootNode) {
        if (rootNode != null && rootNode.getValue().isPresent()) {
            if (rootNode.getRight().isPresent()) {
                stack.push(new TreeNodeStep(Step.RIGHT, rootNode));
            }
            stack.push(new TreeNodeStep(Step.VISIT, rootNode));
            if (rootNode.getLeft().isPresent()) {
                stack.push(new TreeNodeStep(Step.LEFT, rootNode));
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
        TreeNodeStep curStep = stack.pop();
        while (curStep.step != Step.VISIT) {
            BinaryTreeNode<T> childNode;
            if (curStep.step == Step.LEFT) {
                childNode = curStep.node.getLeft().orElse(null);
            } else {
                childNode = curStep.node.getRight().orElse(null);
            }
            if (childNode != null) {
                if (childNode.getRight().isPresent()) {
                    stack.push(new TreeNodeStep(Step.RIGHT, childNode));
                }
                stack.push(new TreeNodeStep(Step.VISIT, childNode));
                if (childNode.getLeft().isPresent()) {
                    stack.push(new TreeNodeStep(Step.LEFT, childNode));
                }
            }
            curStep = stack.pop();
        }
        return curStep.node.getValue().get();
    }

    class TreeNodeStep {

        final Step step;

        final BinaryTreeNode<T> node;

        TreeNodeStep(Step step, BinaryTreeNode<T> node) {
            this.step = step;
            this.node = node;
        }

    }

    enum Step {
        LEFT,
        VISIT,
        RIGHT;
    }

}
