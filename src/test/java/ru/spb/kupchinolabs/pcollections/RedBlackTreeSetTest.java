package ru.spb.kupchinolabs.pcollections;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by vladimir-k on 16.03.17.
 */
public class RedBlackTreeSetTest {

    @Test
    public void insertRoot() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        //TODO test size
        assertTrue(ts.insert(1));
        assertFalse(ts.insert(1));
    }
}
