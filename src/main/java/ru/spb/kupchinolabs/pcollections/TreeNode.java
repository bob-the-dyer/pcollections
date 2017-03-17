package ru.spb.kupchinolabs.pcollections;

import java.util.Optional;

/**
 * Created by vladimir-k on 17.03.17.
 */
abstract class TreeNode<T> {

    abstract Optional<? extends TreeNode> getLeft();

    abstract Optional<? extends TreeNode> getRight();

    abstract Optional<T> getValue();

}
