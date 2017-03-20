package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.*;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.cloneObject;

/**
 * Created by vladimir-k on 20.03.17.
 */
public class PersistentRedBlackTreeSet<T extends Comparable<T> & Serializable> implements PersistentSet<T>, Serializable {

    private RedBlackTreeSet<T> treeSet;

    private PersistentRedBlackTreeSet(RedBlackTreeSet<T> treeSet) {
        this.treeSet = treeSet;
    }

    public PersistentRedBlackTreeSet() {
        this.treeSet = new RedBlackTreeSet<T>();
    }

    @Override
    public boolean contains(T element) {
        return treeSet.contains(element);
    }

    @Override
    public PersistentSet<T> insert(T element) {
        if (this.treeSet.contains(element)) {
            return this;
        } else {
            T newElement = (T) cloneObject(element);
            RedBlackTreeSet<T> newTreeSet = buildCopyExcluding(this.treeSet);
            newTreeSet.insert(newElement);
            return new PersistentRedBlackTreeSet<>(newTreeSet);
        }
    }

    @Override
    public PersistentSet<T> remove(T element) {
        if (!this.treeSet.contains(element)) {
            return this;
        } else {
            RedBlackTreeSet<T> newTreeSet = buildCopyExcluding(this.treeSet, element);
            return new PersistentRedBlackTreeSet<T>(newTreeSet);
        }
    }

    @Override
    public int size() {
        return treeSet.size();
    }

    @Override
    public Iterator<T> iterator() {
        return treeSet.iterator();
    }

    private RedBlackTreeSet<T> buildCopyExcluding(RedBlackTreeSet<T> treeSet, T... excludes) {
        RedBlackTreeSet<T> deepCopy = new RedBlackTreeSet<>();
        treeSet.forEach(element -> {
            if (!Arrays.asList(excludes).contains(element)){
                deepCopy.insert(element);
            }
        });
        return deepCopy;
    }

}
