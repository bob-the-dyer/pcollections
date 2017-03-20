package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.cloneElement;

/**
 * Created by vladimir-k on 15.03.17.
 */
public class PersistentBinaryTreeSet<T extends Comparable<T> & Serializable> implements PersistentSet<T>, Serializable {

    private SimpleSet<T> treeSet;

    private PersistentBinaryTreeSet(SimpleSet<T> treeSet) {
        this.treeSet = treeSet;
    }

    public PersistentBinaryTreeSet() {
        this.treeSet = new BinaryTreeSet<T>();
    }

    @Override
    public boolean contains(T element) {
        return treeSet.contains(element);
    }

    @Override
    public PersistentSet<T> insert(T element) {
        T newElement = cloneElement(element);
        SimpleSet<T> newTreeSet = deepCopy(this.treeSet);
        newTreeSet.insert(newElement);
        return new PersistentBinaryTreeSet<>(newTreeSet);
    }

    @Override
    public PersistentSet<T> remove(T element) {
        SimpleSet<T> newTreeSet = deepCopy(this.treeSet);
        newTreeSet.remove(element);
        return new PersistentBinaryTreeSet<>(newTreeSet);
    }

    @Override
    public int size() {
        return treeSet.size();
    }

    @Override
    public Iterator<T> iterator() {
        return treeSet.iterator();
    }

    private SimpleSet<T> deepCopy(SimpleSet<T> treeSet) {
        //TODO consider other ways to get deepCopy: ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.cloneElement() or fair traverse
        // for now just iterating over elements of original tree and building new tree from scratch
        BinaryTreeSet<T> deepCopy = new BinaryTreeSet<>();
        List<T> elements = new ArrayList<T>();
        treeSet.forEach(elements::add);
        Collections.shuffle(elements);
        elements.forEach(deepCopy::insert);
        return deepCopy;
    }
}
