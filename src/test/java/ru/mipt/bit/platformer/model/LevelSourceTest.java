package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelSourceTest {

    @Test
    void hardcodedSourceBuildsSameLevelEveryTime() {
        LevelSource source = LevelSource.hardcoded(10, 8);

        Level first = source.load();
        Level second = source.load();

        assertEquals(4, first.getObjects().size());
        assertEquals(first.getObjects().size(), second.getObjects().size());
        assertFalse(first.isFree(new GridPoint2(1, 3)));
        assertNotNull(first.getPlayer());
    }

    @Test
    void randomSourcePlacesRequestedNumberOfTrees() {
        Level level = LevelSource.random(10, 8, 5, 42L).load();

        // one player plus five trees
        assertEquals(6, level.getObjects().size());
        assertNotNull(level.getPlayer());
    }

    @Test
    void randomSourceNeverPutsTwoObjectsOnOneTile() {
        Level level = LevelSource.random(10, 8, 20, 1L).load();

        Set<GridPoint2> occupied = new HashSet<>();
        for (GameObject object : level.getObjects()) {
            assertTrue(occupied.add(object.getCoordinates()), "two objects share a tile");
        }
    }

    @Test
    void randomSourceKeepsEverythingInsideTheField() {
        Level level = LevelSource.random(10, 8, 20, 7L).load();

        for (GameObject object : level.getObjects()) {
            GridPoint2 coordinates = object.getCoordinates();
            assertTrue(coordinates.x >= 0 && coordinates.x < 10);
            assertTrue(coordinates.y >= 0 && coordinates.y < 8);
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        Level first = LevelSource.random(10, 8, 5, 123L).load();
        Level second = LevelSource.random(10, 8, 5, 123L).load();

        for (int i = 0; i < first.getObjects().size(); i++) {
            assertEquals(first.getObjects().get(i).getCoordinates(),
                    second.getObjects().get(i).getCoordinates());
        }
    }

    @Test
    void refusesToPlaceMoreTreesThanTilesAvailable() {
        assertThrows(IllegalArgumentException.class, () -> LevelSource.random(2, 2, 4, 0L));
    }
}
