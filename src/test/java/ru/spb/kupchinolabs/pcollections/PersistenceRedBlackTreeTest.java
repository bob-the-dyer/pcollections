package ru.spb.kupchinolabs.pcollections;

import org.junit.Before;

/**
 * Created by vladimir-k on 20.03.17.
 */
public class PersistenceRedBlackTreeTest extends BasePersistentSetTest {

    @Before
    public void init() {
        treeSet = new PersistentRedBlackTreeSet<>();
    }

}