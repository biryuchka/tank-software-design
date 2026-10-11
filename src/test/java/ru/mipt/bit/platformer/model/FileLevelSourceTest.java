package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileLevelSourceTest {

    @Test
    void readsLevelFromRealFile() {
        // test-level.txt is three rows of four tiles with a tree at the top and the player on the right
        Level level = new FileLevelSource("test-level.txt").load();

        assertEquals(4, level.getWidth());
        assertEquals(3, level.getHeight());
        assertEquals(new GridPoint2(3, 1), level.getPlayer().getCoordinates());
    }

    @Test
    void takesFieldSizeFromTheLayout() {
        Level level = FileLevelSource.parse(asList("___X__", "______"));

        assertEquals(6, level.getWidth());
        assertEquals(2, level.getHeight());
    }

    @Test
    void firstLineOfFileIsTopRowOfField() {
        Level level = FileLevelSource.parse(asList(
                "T__",
                "_X_",
                "__T"));

        // y grows upwards, so the tree of the first line is at the highest row
        assertFalse(level.isFree(new GridPoint2(0, 2)));
        assertFalse(level.isFree(new GridPoint2(2, 0)));
        assertEquals(new GridPoint2(1, 1), level.getPlayer().getCoordinates());
    }

    @Test
    void treesBlockTheirTilesAndEmptyTilesStayFree() {
        Level level = FileLevelSource.parse(asList(
                "TT_",
                "_X_"));

        assertFalse(level.isFree(new GridPoint2(0, 1)));
        assertFalse(level.isFree(new GridPoint2(1, 1)));
        assertTrue(level.isFree(new GridPoint2(2, 1)));
        assertTrue(level.isFree(new GridPoint2(0, 0)));
    }

    @Test
    void everyObjectOfTheLayoutIsReadyToBeDrawn() {
        Level level = FileLevelSource.parse(asList(
                "TT_",
                "_X_"));

        // two trees plus the player
        assertEquals(3, level.getObjects().size());
    }

    @Test
    void shorterLinesArePaddedWithEmptyTiles() {
        Level level = FileLevelSource.parse(asList("X", "____"));

        assertEquals(4, level.getWidth());
        assertTrue(level.isFree(new GridPoint2(3, 1)));
    }

    @Test
    void refusesLayoutWithoutPlayer() {
        List<String> lines = singletonList("T__");

        assertThrows(IllegalArgumentException.class, () -> FileLevelSource.parse(lines));
    }

    @Test
    void refusesLayoutWithTwoPlayers() {
        List<String> lines = asList("X__", "__X");

        assertThrows(IllegalArgumentException.class, () -> FileLevelSource.parse(lines));
    }

    @Test
    void refusesUnknownSymbol() {
        List<String> lines = asList("X_?", "___");

        assertThrows(IllegalArgumentException.class, () -> FileLevelSource.parse(lines));
    }

    @Test
    void refusesEmptyLayout() {
        List<String> lines = emptyList();

        assertThrows(IllegalArgumentException.class, () -> FileLevelSource.parse(lines));
    }

    @Test
    void refusesMissingFile() {
        LevelSource source = new FileLevelSource("there-is-no-such-level.txt");

        assertThrows(IllegalArgumentException.class, source::load);
    }
}
