package ru.spb.kupchinolabs.pcollections;

/**
 * Created by vladimir-k on 12.03.17.
 */
public interface IBinaryTreeSet<T> extends Iterable<T> {

    boolean contains(T element);

    boolean insert(T element);

    boolean remove(T element);

    int size();

}
