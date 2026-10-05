package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Where the level comes from. Implementations are free to hardcode it, generate it
 * or read it from a file: nobody downstream knows the difference.
 */
public interface LevelSource {

    Level load();

    static LevelSource hardcoded(int width, int height) {
        return () -> {
            Level level = new Level(width, height);
            level.addPlayer(new Tank(new GridPoint2(1, 1), Direction.RIGHT));
            level.addObstacle(new Tree(new GridPoint2(1, 3)));
            level.addObstacle(new Tree(new GridPoint2(4, 5)));
            level.addObstacle(new Tree(new GridPoint2(7, 2)));
            return level;
        };
    }

    static LevelSource random(int width, int height, int treeCount, long seed) {
        int tiles = width * height;
        if (treeCount < 0 || treeCount >= tiles) {
            throw new IllegalArgumentException("Cannot place " + treeCount + " trees on " + tiles + " tiles");
        }
        return () -> {
            Random random = new Random(seed);
            Set<GridPoint2> taken = new HashSet<>();
            Level level = new Level(width, height);
            level.addPlayer(new Tank(freeTile(width, height, random, taken), Direction.RIGHT));
            for (int i = 0; i < treeCount; i++) {
                level.addObstacle(new Tree(freeTile(width, height, random, taken)));
            }
            return level;
        };
    }

    private static GridPoint2 freeTile(int width, int height, Random random, Set<GridPoint2> taken) {
        GridPoint2 coordinates;
        do {
            coordinates = new GridPoint2(random.nextInt(width), random.nextInt(height));
        } while (!taken.add(coordinates));
        return coordinates;
    }
}
