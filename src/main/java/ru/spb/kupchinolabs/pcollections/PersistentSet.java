package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;

/**
 * Created by vladimir-k on 13.03.17.
 */
public interface PersistentSet<T> extends Iterable<T>, Serializable {

    boolean contains(T element);

    PersistentSet<T> insert(T element);

    PersistentSet<T> remove(T element);

    int size();
}
