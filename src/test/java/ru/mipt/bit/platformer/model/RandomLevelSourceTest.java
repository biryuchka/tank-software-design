package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelSourceTest {

    @Test
    void placesRequestedNumberOfTreesAndThePlayer() {
        Level level = new RandomLevelSource(10, 8, 5, 0, 42L).load();

        assertEquals(6, level.getObjects().size());
        assertNotNull(level.getPlayer());
    }

    @Test
    void neverPutsTwoObjectsOnOneTile() {
        Level level = new RandomLevelSource(10, 8, 20, 3, 1L).load();

        Set<GridPoint2> occupied = new HashSet<>();
        for (GameObject object : level.getObjects()) {
            assertTrue(occupied.add(object.getCoordinates()), "two objects share a tile");
        }
    }

    @Test
    void keepsEverythingInsideTheField() {
        Level level = new RandomLevelSource(10, 8, 20, 3, 7L).load();

        for (GameObject object : level.getObjects()) {
            GridPoint2 coordinates = object.getCoordinates();
            assertTrue(coordinates.x >= 0 && coordinates.x < 10);
            assertTrue(coordinates.y >= 0 && coordinates.y < 8);
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        Level first = new RandomLevelSource(10, 8, 5, 2, 123L).load();
        Level second = new RandomLevelSource(10, 8, 5, 2, 123L).load();

        for (int i = 0; i < first.getObjects().size(); i++) {
            assertEquals(first.getObjects().get(i).getCoordinates(),
                    second.getObjects().get(i).getCoordinates());
        }
    }

    @Test
    void repeatedLoadOfOneSourceGivesSameLevel() {
        RandomLevelSource source = new RandomLevelSource(10, 8, 5, 2, 99L);

        Level first = source.load();
        Level second = source.load();

        for (int i = 0; i < first.getObjects().size(); i++) {
            assertEquals(first.getObjects().get(i).getCoordinates(),
                    second.getObjects().get(i).getCoordinates());
        }
    }

    @Test
    void placesRequestedNumberOfAiTanks() {
        Level level = new RandomLevelSource(10, 8, 5, 4, 5L).load();

        // four tanks driven by AI plus the player
        assertEquals(5, level.getMovables().size());
        assertTrue(level.getMovables().contains(level.getPlayer()));
    }

    @Test
    void refusesToPlaceMoreTreesThanTilesAvailable() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(2, 2, 4, 0, 0L));
    }
}
