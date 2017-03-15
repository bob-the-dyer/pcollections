package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;

/**
 * Created by vladimir-k on 12.03.17.
 */
public interface SimpleSet<T> extends Iterable<T>, Serializable {

    boolean contains(T element);

    boolean insert(T element);

    boolean remove(T element);

    int size();

}
