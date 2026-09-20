package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void appliesItsVectorToCoordinates() {
        GridPoint2 start = new GridPoint2(3, 4);

        assertEquals(new GridPoint2(3, 5), Direction.UP.apply(start));
        assertEquals(new GridPoint2(3, 3), Direction.DOWN.apply(start));
        assertEquals(new GridPoint2(2, 4), Direction.LEFT.apply(start));
        assertEquals(new GridPoint2(4, 4), Direction.RIGHT.apply(start));
    }

    @Test
    void doesNotChangeGivenCoordinates() {
        GridPoint2 start = new GridPoint2(3, 4);

        Direction.UP.apply(start);

        assertEquals(new GridPoint2(3, 4), start);
    }
}
