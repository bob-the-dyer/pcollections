package ru.spb.kupchinolabs.pcollections;

import java.io.Serializable;

/**
 * Created by vladimir-k on 13.03.17.
 */
public interface PersistentSet<T> extends Iterable<T> {

    boolean contains(T element);

    PersistentSet<T> insert(T element);

    PersistentSet<T> remove(T element);

    int size();

    //TODO consider adding convenience methods like 'empty', 'singleton' and 'from(final Collection<? extends T> list)'
    //TODO consider rename 'insert' to 'plus' and 'remove' to 'minus'
    //TODO consider adding convenience methods like 'plusAll' and 'minusAll'
    //TODO consider adding convenience methods like 'union', 'difference' and 'intersection'

}
