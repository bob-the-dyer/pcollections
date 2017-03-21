package ru.spb.kupchinolabs.pcollections;

/**
 * Created by vladimir-k on 16.03.17.
 */
public enum RedBlackTreeNodeColor {
    RED("R"),
    BLACK("B");

    private final String shortName;

    RedBlackTreeNodeColor(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
