package ru.spb.kupchinolabs.pcollections;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by vladimir-k on 20.03.17.
 */
abstract public class BasePersistentSetTest {

    PersistentSet<Integer> treeSet;

    @Test
    public void orderingStochastic() {
        final PersistentSet<Integer>[] treeSetArr = new PersistentSet[]{treeSet};

        assertEquals(0, treeSetArr[0].size());
        List<Integer> initialElements = new ArrayList<>();
        new Random().ints(1000, 0, 1000).forEach(e -> {
            if (!initialElements.contains(e)) initialElements.add(e);
            treeSetArr[0] = treeSetArr[0].insert(e);
        });

        assertEquals(initialElements.size(), treeSetArr[0].size());

        Collections.sort(initialElements);

        List<Integer> traversedElements = new ArrayList<>();
        treeSetArr[0].forEach(traversedElements::add);

        assertEquals(initialElements, traversedElements);
        initialElements.forEach(e -> {
            treeSetArr[0] = treeSetArr[0].remove(e);
        });
        assertEquals(0, treeSetArr[0].size());
    }

    @Test(expected = NullPointerException.class)
    public void insertNull() {
        treeSet.insert(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNull() {
        treeSet.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeViaIterator() {
        assertEquals(0, treeSet.size());
        treeSet.iterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNextOnEmptyTree() {
        assertEquals(0, treeSet.size());
        Iterator<Integer> iterator = treeSet.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void insertContainsDeleteStochastic() {
        final PersistentSet<Integer>[] treeSetArr = new PersistentSet[]{treeSet};
        List<Integer> initialElements = Arrays.asList(1, 2, 3, 5);
        List<Integer> targetElements = Arrays.asList(1, 2, 3);

        for (int i = 0; i < 1000; i++) {
            assertEquals(0, treeSetArr[0].size());
            Collections.shuffle(initialElements);

            initialElements.forEach(e -> {
                treeSetArr[0] = treeSetArr[0].insert(e);
            });

            assertFalse(treeSetArr[0].contains(8));

            int sizeBefore = treeSetArr[0].size();
            treeSetArr[0] = treeSetArr[0].insert(1);
            assertEquals(sizeBefore, treeSetArr[0].size());

            assertTrue(treeSetArr[0].contains(5));
            treeSetArr[0] = treeSetArr[0].remove(5);
            assertFalse(treeSetArr[0].contains(5));

            sizeBefore = treeSetArr[0].size();
            treeSetArr[0] = treeSetArr[0].remove(5);
            assertEquals(sizeBefore, treeSetArr[0].size());

            targetElements.forEach(e -> {
                treeSetArr[0] = treeSetArr[0].remove(e);
            });
        }
    }

    @Test
    public void serialization() throws IOException, ClassNotFoundException {
        treeSet = treeSet.insert(3).insert(1).insert(5);

        PersistentSet<Integer> deserializedSet = (PersistentSet<Integer>) BinaryTreeUtils.cloneObject(treeSet);

        assertEquals(treeSet.size(), deserializedSet.size());

        List<Integer> originalElements = new ArrayList<>();
        treeSet.forEach(originalElements::add);

        List<Integer> deserializedElements = new ArrayList<>();
        deserializedSet.forEach(deserializedElements::add);

        assertEquals(originalElements, deserializedElements);
    }

    @Test
    public void stochasticRemoval() {
        List<Integer> originalElements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> targetElements = Arrays.asList(1, 2, 3, 4, 6, 7, 8, 9, 10);

        final PersistentSet<Integer>[] treeSetArr = new PersistentSet[]{treeSet};

        for (int i = 0; i < 10000; i++) {
            assertEquals(0, treeSetArr[0].size());

            Collections.shuffle(originalElements);

            originalElements.forEach(e -> {
                treeSetArr[0] = treeSetArr[0].insert(e);
            });

            treeSetArr[0] = treeSetArr[0].remove(5);

            List<Integer> traversedElements = new ArrayList<>();
            treeSetArr[0].forEach(traversedElements::add);

            assertEquals(targetElements, traversedElements);

            targetElements.forEach(e -> {
                treeSetArr[0] = treeSetArr[0].remove(e);
            });
        }
    }

    @Test
    public void empty() {
        assertEquals(0, treeSet.size());
        treeSet = treeSet.remove(5);
        assertFalse(treeSet.contains(5));
        assertFalse(treeSet.iterator().hasNext());
    }

    @Test
    public void persistence() {
        PersistentSet<Integer> treeSet1 = treeSet.insert(1);

        assertEquals(0, treeSet.size());
        assertEquals(1, treeSet1.size());
        assertFalse(treeSet.contains(1));
        assertTrue(treeSet1.contains(1));

        PersistentSet<Integer> treeSet0 = treeSet1.remove(1);

        assertEquals(0, treeSet0.size());
        assertEquals(1, treeSet1.size());
        assertFalse(treeSet0.contains(1));
        assertTrue(treeSet1.contains(1));
    }

    @Test
    public void insertingOrDeletingWithNoEffect() {
        PersistentSet<Integer> newTreeSet = treeSet.insert(5);
        PersistentSet<Integer> sameTreeSet = newTreeSet.insert(5);
        assertTrue(newTreeSet == sameTreeSet);
        sameTreeSet = newTreeSet.remove(8);
        assertTrue(newTreeSet == sameTreeSet);
    }

    @Test
    public void bestPracticesUsage() {
        PersistentSet<Integer> treeSet1 = treeSet;
        PersistentSet<Integer> treeSet2 = treeSet1.insert(42);
        assert treeSet1.size() == 0;
        assert !treeSet1.contains(42);
        assert treeSet2.size() == 1;
        assert treeSet2.contains(42);
        PersistentSet<Integer> treeSet3 = treeSet2.remove(42);
        assert treeSet2.contains(42);
        assert treeSet2.size() == 1;
        assert !treeSet3.contains(42);
        assert treeSet3.size() == 0;
    }

}