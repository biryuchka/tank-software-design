package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void staysInItsTile() {
        Tree tree = new Tree(new GridPoint2(2, 5));

        assertEquals(new GridPoint2(2, 5), tree.getCoordinates());
        assertEquals(tree.getCoordinates(), tree.getDestinationCoordinates());
        assertEquals(1f, tree.getMovementProgress());
        assertEquals(0f, tree.getRotation());
    }

    @Test
    void copiesCoordinatesGivenToConstructor() {
        GridPoint2 coordinates = new GridPoint2(2, 5);
        Tree tree = new Tree(coordinates);

        coordinates.set(9, 9);

        assertEquals(new GridPoint2(2, 5), tree.getCoordinates());
    }

    @Test
    void cannotBeMovedThroughReturnedCoordinates() {
        Tree tree = new Tree(new GridPoint2(2, 5));

        tree.getCoordinates().set(9, 9);

        assertEquals(new GridPoint2(2, 5), tree.getCoordinates());
    }
}
