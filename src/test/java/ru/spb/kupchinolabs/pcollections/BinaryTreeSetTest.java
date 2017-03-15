package ru.spb.kupchinolabs.pcollections;

import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by vladimir-k on 12.03.17.
 */
public class BinaryTreeSetTest {

    @Test
    public void testOrdering() {

        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
        assertEquals(0, treeSet.size());

        List<Integer> initialElements = new ArrayList<>();
        new Random().ints(1000, 0, 1000).forEach(i -> {
            if (!initialElements.contains(i)) initialElements.add(i);
            treeSet.insert(i);
        });

        assertEquals(initialElements.size(), treeSet.size());

        Collections.sort(initialElements);

        List<Integer> traversedElements = new ArrayList<>();
        treeSet.forEach(traversedElements::add);

        assertEquals(initialElements, traversedElements);
    }

    @Test(expected = NullPointerException.class)
    public void testInsertNull() {
        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
        treeSet.insert(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNull() {
        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
        treeSet.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveViaIterator() {
        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        treeSet.iterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNextOnEmptyTree() {
        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        Iterator<Integer> iterator = treeSet.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testInsertContainsDelete() {
        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
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
    public void testSerialization() throws IOException, ClassNotFoundException {
        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
        treeSet.insert(3);
        treeSet.insert(1);
        treeSet.insert(5);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(baos);
        outputStream.writeObject(treeSet);
        outputStream.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream inputStream = new ObjectInputStream(bais);
        SimpleSet<Integer> deserializedSet = (SimpleSet<Integer>) inputStream.readObject();

        assertEquals(treeSet.size(), deserializedSet.size());

        List<Integer> originalElements = new ArrayList<>();
        treeSet.forEach(originalElements::add);

        List<Integer> deserializedElements = new ArrayList<>();
        deserializedSet.forEach(deserializedElements::add);

        assertEquals(originalElements, deserializedElements);
    }

    @Test
    public void testStochasticRemoval(){
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(elements);
            SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
            elements.forEach(treeSet::insert);
            assertTrue(treeSet.remove(5));
            List<Integer> traversedElements = new ArrayList<>();
            treeSet.forEach(traversedElements::add);
            assertEquals(Arrays.asList(1, 2, 3, 4, 6, 7, 8, 9, 10), traversedElements);
        }
    }

    @Test
    public void testEmpty(){
        SimpleSet<Integer> treeSet = new BinaryTreeSet<>();
        assertEquals(0, treeSet.size());
        assertFalse(treeSet.remove(5));
        assertFalse(treeSet.contains(5));
        assertFalse(treeSet.iterator().hasNext());
    }

}
