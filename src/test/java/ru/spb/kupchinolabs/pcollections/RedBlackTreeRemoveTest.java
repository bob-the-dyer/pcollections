package ru.spb.kupchinolabs.pcollections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static ru.spb.kupchinolabs.pcollections.BinaryTreeUtils.validateTree;
import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.BLACK;
import static ru.spb.kupchinolabs.pcollections.RedBlackTreeNodeColor.RED;

/**
 * Created by vladimir-k on 21.03.17.
 */
public class RedBlackTreeRemoveTest {
    @Test
    public void trivialRebalanceAndRepaintOnRemovingRed() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        //System.out.println(ts);
        validateTree(ts);

        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(3);

        //System.out.println(ts);
        validateTree(ts);

        RedBlackTreeNode<Integer> _1B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(1);

        ts.rootNode.setLeft(_1B);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setRight(_5B);

        //System.out.println(ts);
        validateTree(ts);

        RedBlackTreeNode<Integer> _0R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(_1B)
                .setValue(0);

        _1B.setLeft(_0R);

        //System.out.println(ts);
        validateTree(ts);

        RedBlackTreeNode<Integer> _2R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(_1B)
                .setValue(2);

        _1B.setRight(_2R);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 5;

        ts.remove(0);
        //System.out.println(ts);

        ts.remove(2);
        //System.out.println(ts);
    }

    @Test
    public void removingBlackNodeWithRedParent() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        //System.out.println(ts);
        validateTree(ts);

        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(3);

        //System.out.println(ts);
        validateTree(ts);

        RedBlackTreeNode<Integer> _1R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(ts.rootNode)
                .setValue(1);

        ts.rootNode.setLeft(_1R);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setRight(_5B);

        RedBlackTreeNode<Integer> _0B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(_1R)
                .setColor(BLACK)
                .setValue(0);

        _1R.setLeft(_0B);

        RedBlackTreeNode<Integer> _2B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(_1R)
                .setColor(BLACK)
                .setValue(2);

        _1R.setRight(_2B);

        ts.size = 5;

        //System.out.println(ts);
        validateTree(ts);

        ts.remove(2);
        //System.out.println(ts);
    }

    @Test
    public void removeRoot() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        //System.out.println(ts);
        validateTree(ts);

        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(3);

        ts.size = 1;

        //System.out.println(ts);
        validateTree(ts);

        ts.remove(3);
        //System.out.println(ts);
    }

    @Test
    public void removeRedChildFromLeft() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        //System.out.println(ts);
        validateTree(ts);

        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(3);

        RedBlackTreeNode<Integer> _1R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(ts.rootNode)
                .setValue(1);

        ts.rootNode.setLeft(_1R);

        ts.size = 2;

        //System.out.println(ts);
        validateTree(ts);

        ts.remove(1);
        //System.out.println(ts);
    }

    @Test
    public void removeRedChildFromRight() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        //System.out.println(ts);
        validateTree(ts);

        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(3);

        RedBlackTreeNode<Integer> _1R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(ts.rootNode)
                .setValue(1);

        RedBlackTreeNode<Integer> _5R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setLeft(_1R);
        ts.rootNode.setRight(_5R);

        ts.size = 3;

        //System.out.println(ts);
        validateTree(ts);

        ts.remove(5);
        //System.out.println(ts);
    }

    @Test
    public void siblingIsRed() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(10);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setLeft(_5B);

        RedBlackTreeNode<Integer> _15R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(ts.rootNode)
                .setValue(15);

        ts.rootNode.setRight(_15R);

        RedBlackTreeNode<Integer> _12B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_15R)
                .setValue(12);

        RedBlackTreeNode<Integer> _20B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_15R)
                .setValue(20);

        _15R.setLeft(_12B);
        _15R.setRight(_20B);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 5;

        ts.remove(5);
        //System.out.println(ts);
    }

    @Test
    public void siblingIsRedMirrored() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(25);

        RedBlackTreeNode<Integer> _30B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(30);

        ts.rootNode.setRight(_30B);

        RedBlackTreeNode<Integer> _15R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(ts.rootNode)
                .setValue(15);

        ts.rootNode.setLeft(_15R);

        RedBlackTreeNode<Integer> _12B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_15R)
                .setValue(12);

        RedBlackTreeNode<Integer> _20B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_15R)
                .setValue(20);

        _15R.setLeft(_12B);
        _15R.setRight(_20B);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 5;

        ts.remove(30);
        //System.out.println(ts);
    }

    @Test
    public void parentSiblingAndChildrenAreBlack() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(10);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setLeft(_5B);

        RedBlackTreeNode<Integer> _15R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(15);

        ts.rootNode.setRight(_15R);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 3;

        ts.remove(5);
        //System.out.println(ts);
    }

    @Test
    public void parentSiblingAndChildrenAreBlackMirrored() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(10);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setLeft(_5B);

        RedBlackTreeNode<Integer> _15B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(15);

        ts.rootNode.setRight(_15B);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 3;

        ts.remove(15);
        //System.out.println(ts);
    }

    @Test
    public void parentSiblingAndRightSiblingChildAreBlackAndLeftSiblingChildIsRed() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(10);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setLeft(_5B);

        RedBlackTreeNode<Integer> _20B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(20);

        ts.rootNode.setRight(_20B);

        RedBlackTreeNode<Integer> _15R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_20B)
                .setValue(15);

        _20B.setLeft(_15R);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 4;

        ts.remove(5);
        //System.out.println(ts);
    }

    @Test
    public void parentSiblingAndRightSiblingChildAreBlackAndLeftSiblingChildIsRedMirrored() {
        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(10);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(5);

        ts.rootNode.setLeft(_5B);

        RedBlackTreeNode<Integer> _20B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(20);

        ts.rootNode.setRight(_20B);

        RedBlackTreeNode<Integer> _7R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_5B)
                .setValue(7);

        _5B.setRight(_7R);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 4;

        ts.remove(20);
        //System.out.println(ts);
    }

    @Test
    public void debugUnexpectedCase() {
        //            6B
        //       4R        8R
        //    2B   5B    7B  10B
        // 1R   3R          9R 11R

        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(6);

        RedBlackTreeNode<Integer> _4R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(ts.rootNode)
                .setValue(4);

        RedBlackTreeNode<Integer> _8R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(ts.rootNode)
                .setValue(8);

        ts.rootNode.setLeft(_4R);
        ts.rootNode.setRight(_8R);

        RedBlackTreeNode<Integer> _2B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_4R)
                .setValue(2);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_4R)
                .setValue(5);

        _4R.setLeft(_2B);
        _4R.setRight(_5B);

        RedBlackTreeNode<Integer> _1R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_2B)
                .setValue(1);

        RedBlackTreeNode<Integer> _3R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_2B)
                .setValue(3);

        _2B.setLeft(_1R);
        _2B.setRight(_3R);

        RedBlackTreeNode<Integer> _7B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_8R)
                .setValue(7);

        RedBlackTreeNode<Integer> _10B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_8R)
                .setValue(10);

        _8R.setLeft(_7B);
        _8R.setRight(_10B);

        RedBlackTreeNode<Integer> _9R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_10B)
                .setValue(9);

        RedBlackTreeNode<Integer> _11R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_10B)
                .setValue(11);

        _10B.setLeft(_9R);
        _10B.setRight(_11R);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 11;

        ts.remove(5);
        assertEquals(10, ts.size());
        //System.out.println(ts);

        ts.remove(7);
        //System.out.println(ts);
        assertEquals(9, ts.size());
    }

    @Test
    public void debugUnexpectedCase2() {
        //           5B
        //     2B          9R
        // 1B      4B  6B     10B
        //       3R      8R

        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(5);

        RedBlackTreeNode<Integer> _2R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(ts.rootNode)
                .setValue(2);

        RedBlackTreeNode<Integer> _9R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(ts.rootNode)
                .setValue(9);

        ts.rootNode.setLeft(_2R);
        ts.rootNode.setRight(_9R);

        RedBlackTreeNode<Integer> _6B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_9R)
                .setValue(6);

        RedBlackTreeNode<Integer> _10B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_9R)
                .setValue(10);

        _9R.setLeft(_6B);
        _9R.setRight(_10B);

        RedBlackTreeNode<Integer> _8R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_6B)
                .setValue(8);

        _6B.setRight(_8R);

        RedBlackTreeNode<Integer> _1B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_2R)
                .setValue(1);

        RedBlackTreeNode<Integer> _4B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_2R)
                .setValue(4);

        _2R.setLeft(_1B);
        _2R.setRight(_4B);

        RedBlackTreeNode<Integer> _3R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_4B)
                .setValue(3);

        _4B.setRight(_3R);


        ts.size = 9;

        //System.out.println(ts);
        validateTree(ts);

        ts.remove(10);
        //System.out.println(ts);

        ts.remove(1);
        //System.out.println(ts);

    }

    @Test
    public void debugUnexpectedCase3() {
        //            6B
        //       4B        8B
        //    2B   5B    7B  10R
        // 1R   3R          9B 11B

        RedBlackTreeSet<Integer> ts = new RedBlackTreeSet<>();
        ts.rootNode = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setValue(6);

        RedBlackTreeNode<Integer> _4R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(4);

        RedBlackTreeNode<Integer> _8R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(ts.rootNode)
                .setValue(8);

        ts.rootNode.setLeft(_4R);
        ts.rootNode.setRight(_8R);

        RedBlackTreeNode<Integer> _2B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_4R)
                .setValue(2);

        RedBlackTreeNode<Integer> _5B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_4R)
                .setValue(5);

        _4R.setLeft(_2B);
        _4R.setRight(_5B);

        RedBlackTreeNode<Integer> _1R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_2B)
                .setValue(1);

        RedBlackTreeNode<Integer> _3R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_2B)
                .setValue(3);

        _2B.setLeft(_1R);
        _2B.setRight(_3R);

        RedBlackTreeNode<Integer> _7B = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_8R)
                .setValue(7);

        RedBlackTreeNode<Integer> _10R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(RED)
                .setParent(_8R)
                .setValue(10);

        _8R.setLeft(_7B);
        _8R.setRight(_10R);

        RedBlackTreeNode<Integer> _9R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_10R)
                .setValue(9);

        RedBlackTreeNode<Integer> _11R = (RedBlackTreeNode<Integer>) new RedBlackTreeNode<Integer>()
                .setColor(BLACK)
                .setParent(_10R)
                .setValue(11);

        _10R.setLeft(_9R);
        _10R.setRight(_11R);

        //System.out.println(ts);
        validateTree(ts);

        ts.size = 11;

        ts.remove(8);
        assertEquals(10, ts.size());
        //System.out.println(ts);

    }


}