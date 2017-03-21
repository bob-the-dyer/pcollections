package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.Iterator;

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
            RedBlackTreeSet<T> newTreeSet = deepCopy(this.treeSet);
            newTreeSet.insert(newElement);
            return new PersistentRedBlackTreeSet<>(newTreeSet);
        }
    }

    @Override
    public PersistentSet<T> remove(T element) {
        if (!this.treeSet.contains(element)) {
            return this;
        } else {
            RedBlackTreeSet<T> newTreeSet = deepCopy(this.treeSet);
            newTreeSet.remove(element);
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

    private RedBlackTreeSet<T> deepCopy(RedBlackTreeSet<T> treeSet) {
        RedBlackTreeSet<T> deepCopy = new RedBlackTreeSet<>();
        treeSet.forEach(deepCopy::insert);
        return deepCopy;
    }

}
