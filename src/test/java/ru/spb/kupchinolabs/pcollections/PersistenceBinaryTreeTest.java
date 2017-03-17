package ru.spb.kupchinolabs.pcollections;

import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by vladimir-k on 15.03.17.
 */
public class PersistenceBinaryTreeTest {

    @Test
    public void ordering() {

        final PersistentSet<Integer>[] treeSetArr = new PersistentSet[]{new PersistentBinaryTreeSet()};
        List<Integer> initialElements = new ArrayList<>();
        new Random().ints(1000, 0, 1000).forEach(i -> {
            if (!initialElements.contains(i)) initialElements.add(i);
            treeSetArr[0] = treeSetArr[0].insert(i);
        });
        PersistentSet<Integer> treeSet = treeSetArr[0];

        assertEquals(initialElements.size(), treeSet.size());

        Collections.sort(initialElements);

        List<Integer> traversedElements = new ArrayList<>();
        treeSet.forEach(traversedElements::add);

        assertEquals(initialElements, traversedElements);
    }

    @Test(expected = NullPointerException.class)
    public void insertNull() {
        PersistentSet<Integer> treeSet = new PersistentBinaryTreeSet<>();
        treeSet.insert(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNull() {
        PersistentSet<Integer> treeSet = new PersistentBinaryTreeSet<>();
        treeSet.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeViaIterator() {
        PersistentSet<Integer> treeSet = new PersistentBinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        treeSet.iterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNextOnEmptyTree() {
        PersistentSet<Integer> treeSet = new PersistentBinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        Iterator<Integer> iterator = treeSet.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void insertContainsDeleteStochastic() {
        for (int i = 0; i < 1000; i++) {
            List<Integer> initialElements = Arrays.asList(1, 2, 3, 5);
            Collections.shuffle(initialElements);

            final PersistentSet<Integer>[] treeSetArr = new PersistentSet[]{new PersistentBinaryTreeSet()};
            initialElements.forEach(e -> {
                treeSetArr[0] = treeSetArr[0].insert(e);
            });
            PersistentSet<Integer> treeSet = treeSetArr[0];

            assertFalse(treeSet.contains(8));

            int sizeBefore = treeSet.size();
            treeSet = treeSet.insert(1);
            assertEquals(sizeBefore, treeSet.size());

            assertTrue(treeSet.contains(5));
            treeSet = treeSet.remove(5);
            assertFalse(treeSet.contains(5));

            sizeBefore = treeSet.size();
            treeSet = treeSet.remove(5);
            assertEquals(sizeBefore, treeSet.size());
        }
    }

    @Test
    public void serialization() throws IOException, ClassNotFoundException {
        PersistentSet<Integer> treeSet = new PersistentBinaryTreeSet<>();
        treeSet = treeSet.insert(3).insert(1).insert(5);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(baos);
        outputStream.writeObject(treeSet);
        outputStream.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream inputStream = new ObjectInputStream(bais);
        PersistentSet<Integer> deserializedSet = (PersistentSet<Integer>) inputStream.readObject();

        assertEquals(treeSet.size(), deserializedSet.size());

        List<Integer> originalElements = new ArrayList<>();
        treeSet.forEach(originalElements::add);

        List<Integer> deserializedElements = new ArrayList<>();
        deserializedSet.forEach(deserializedElements::add);

        assertEquals(originalElements, deserializedElements);
    }

    @Test
    public void stochasticRemoval() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        for (int i = 0; i < 100000; i++) {
            Collections.shuffle(elements);
            final PersistentSet<Integer>[] treeSetArr = new PersistentSet[]{new PersistentBinaryTreeSet()};
            elements.forEach(e -> {
                treeSetArr[0] = treeSetArr[0].insert(e);
            });
            PersistentSet<Integer> treeSet = treeSetArr[0];
            treeSet = treeSet.remove(5);
            List<Integer> traversedElements = new ArrayList<>();
            treeSet.forEach(traversedElements::add);
            assertEquals(Arrays.asList(1, 2, 3, 4, 6, 7, 8, 9, 10), traversedElements);
        }
    }

    @Test
    public void empty() {
        PersistentSet<Integer> treeSet = new PersistentBinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        treeSet = treeSet.remove(5);
        assertFalse(treeSet.contains(5));
        assertFalse(treeSet.iterator().hasNext());
    }

    @Test
    public void persistence() {
        PersistentSet<Integer> treeSet = new PersistentBinaryTreeSet<>();
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

}
