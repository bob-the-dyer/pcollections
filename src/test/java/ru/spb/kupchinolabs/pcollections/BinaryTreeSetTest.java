package ru.spb.kupchinolabs.pcollections;

import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.*;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeSetTest {

    @Test
    public void testBinaryTreeSetOrdering() {

        IBinaryTreeSet<Integer> treeSet = new BinaryTreeSet<>();
        assertEquals(0, treeSet.size());

        List<Integer> initialElements = new ArrayList<>();
        new Random().ints(1000, 0, 1000).forEach(i -> {
            System.out.println(i);
            if (!initialElements.contains(i)) initialElements.add(i);
            treeSet.insert(i);
        });

        assertEquals(initialElements.size(), treeSet.size());

        Collections.sort(initialElements);

        List<Integer> traversedElements = new ArrayList<>();
        treeSet.forEach(traversedElements::add);

        assertEquals(initialElements, traversedElements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBinaryTreeSetNullOnInsert() {
        IBinaryTreeSet<Integer> treeSet = new BinaryTreeSet<>();
        treeSet.insert(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBinaryTreeSetRemoveViaIterator() {
        IBinaryTreeSet<Integer> treeSet = new BinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        treeSet.iterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testBinaryTreeSetIteratorNextOnEmptyTree() {
        IBinaryTreeSet<Integer> treeSet = new BinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        Iterator<Integer> iterator = treeSet.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testBinaryTreeSetInsertContainsDelete() {

        IBinaryTreeSet<Integer> treeSet = new BinaryTreeSet<>();
        assertTrue(treeSet.insert(1));
        assertTrue(treeSet.insert(2));
        assertTrue(treeSet.insert(3));
        assertTrue(treeSet.insert(5));

        assertFalse(treeSet.contains(8));

        assertFalse(treeSet.insert(1));

        assertTrue(treeSet.contains(5));
        assertTrue(treeSet.remove(5));
        assertFalse(treeSet.contains(5));
        assertFalse(treeSet.remove(5));
    }

    @Test
    public void testBinaryTreeSetSize() {
        IBinaryTreeSet<Integer> treeSet = new BinaryTreeSet<>();
        treeSet.insert(16);
        treeSet.insert(10);
        treeSet.insert(20);
        assertEquals(3, treeSet.size());

    }

}
