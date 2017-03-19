package ru.spb.kupchinolabs.pcollections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.*;

/**
 * Created by vladimir-k on 16.03.17.
 */
public class RedBlackTreeSetTest extends BaseBinaryTreeSetTest {

    @Before
    public void init() {
        treeSet = new RedBlackTreeSet<>();
    }

    @Test
    public void insertRoot() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        assertEquals(0, ts.size());
        assertTrue(ts.insert(1));
        assertTrue(ts.insert(3));
        assertTrue(ts.insert(2));
        assertFalse(ts.insert(1));
    }

    @Test
    public void debug() {
        List<Integer> initialElements = Arrays.asList(7, 9, 5, 10, 2, 3);
        List<Integer> expectedElements = Arrays.asList(2, 3, 7, 9, 10);

        initialElements.forEach((element) -> {
            System.out.println(">>>>>>>>> inserting " + element);
            treeSet.insert(element);
            System.out.println("now tree looks like:");
            System.out.println(treeSet);
            System.out.println("<<<<<<<<<");
        });
        System.out.println(">>>>>>>>> removing " + 5);
        Assert.assertTrue(treeSet.remove(5));
        System.out.println("now tree looks like:");
        System.out.println(treeSet);
        System.out.println("<<<<<<<<<");

        List<Integer> traversedElements = new ArrayList<>();
        treeSet.forEach(traversedElements::add);
        Assert.assertEquals(expectedElements, traversedElements);
        expectedElements.forEach(element -> {
            System.out.println(">>>>>>>>> removing " + element);
            Assert.assertTrue(treeSet.remove(element));
            System.out.println("now tree looks like:");
            System.out.println(treeSet);
            System.out.println("<<<<<<<<<");
        });
    }

    @Test
    public void prettyPrint() {
        List<Integer> initialElements = Arrays.asList(1,10,7,8,9);

        initialElements.forEach((element) -> {
            System.out.println(">>>>>>>>> inserting " + element);
            treeSet.insert(element);
            System.out.println("now tree looks like:");
            System.out.println(treeSet);
            System.out.println("<<<<<<<<<");
        });
    }

}
