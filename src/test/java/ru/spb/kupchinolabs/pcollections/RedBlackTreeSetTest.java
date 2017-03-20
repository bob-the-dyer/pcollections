package ru.spb.kupchinolabs.pcollections;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vladimir-k on 16.03.17.
 */
public class RedBlackTreeSetTest extends BaseSimpleSetTest {

    @Before
    public void init() {
        treeSet = new RedBlackTreeSet<>();
    }

    @Test
    public void prettyPrint() {
        List<Integer> initialElements = Arrays.asList(3, 1, 5, -1, 2, 4, 6);
        initialElements.forEach((element) -> {
            System.out.println(">>>>>>>>> inserting " + element);
            treeSet.insert(element);
            System.out.println("now tree looks like:");
            System.out.println(treeSet);
            System.out.println("<<<<<<<<<");
        });
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void orderingStochastic() {
        super.orderingStochastic();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeNull() {
        super.removeNull();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void insertContainsDeleteStochastic() {
        super.insertContainsDeleteStochastic();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void stochasticRemovalOfSingleElement() {
        super.stochasticRemovalOfSingleElement();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void empty() {
        super.empty();
    }
}
