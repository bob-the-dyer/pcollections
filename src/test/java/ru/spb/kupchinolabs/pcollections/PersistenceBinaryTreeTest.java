package ru.spb.kupchinolabs.pcollections;

import org.junit.Before;

/**
 * Created by vladimir-k on 15.03.17.
 */
public class PersistenceBinaryTreeTest extends BasePersistentSetTest {

    @Before
    public void init() {
        treeSet = new PersistentBinaryTreeSet<>();
    }

}