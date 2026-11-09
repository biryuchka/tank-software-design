package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundedLevelSourceTest {

    private static LevelSource sourceOf(int width, int height) {
        return () -> {
            Level level = new Level(width, height);
            level.addPlayer(new Tank(new GridPoint2(0, 0), Direction.RIGHT));
            return level;
        };
    }

    @Test
    void letsThroughALevelOfTheSameSizeAsTheMap() {
        LevelSource source = sourceOf(10, 8);

        Level level = new BoundedLevelSource(source, 10, 8).load();

        assertEquals(10, level.getWidth());
        assertEquals(8, level.getHeight());
    }

    @Test
    void letsThroughASmallerLevel() {
        Level level = new BoundedLevelSource(sourceOf(5, 4), 10, 8).load();

        assertEquals(5, level.getWidth());
    }

    @Test
    void refusesALevelWiderThanTheMap() {
        LevelSource source = new BoundedLevelSource(sourceOf(11, 8), 10, 8);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, source::load);

        assertTrue(thrown.getMessage().contains("11x8"));
        assertTrue(thrown.getMessage().contains("10x8"));
    }

    @Test
    void refusesALevelTallerThanTheMap() {
        LevelSource source = new BoundedLevelSource(sourceOf(10, 9), 10, 8);

        assertThrows(IllegalArgumentException.class, source::load);
    }

    @Test
    void returnsTheVeryLevelTheSourceBuilt() {
        Level built = new Level(4, 4);
        LevelSource source = () -> built;

        assertSame(built, new BoundedLevelSource(source, 10, 8).load());
    }
}
