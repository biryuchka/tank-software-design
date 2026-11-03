package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** Scatters trees and AI tanks over the field and puts the player on one of the tiles left. */
public class RandomLevelSource implements LevelSource {

    private final int width;
    private final int height;
    private final int treeCount;
    private final int aiTankCount;
    private final long seed;

    public RandomLevelSource(int width, int height, int treeCount, int aiTankCount) {
        this(width, height, treeCount, aiTankCount, System.nanoTime());
    }

    public RandomLevelSource(int width, int height, int treeCount, int aiTankCount, long seed) {
        int tiles = width * height;
        // the player needs a tile of its own on top of the trees and the AI tanks
        if (treeCount < 0 || aiTankCount < 0 || treeCount + aiTankCount >= tiles) {
            throw new IllegalArgumentException("Cannot place " + treeCount + " trees and "
                    + aiTankCount + " tanks on " + tiles + " tiles");
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.aiTankCount = aiTankCount;
        this.seed = seed;
    }

    @Override
    public Level load() {
        Random random = new Random(seed);
        Set<GridPoint2> taken = new HashSet<>();
        Level level = new Level(width, height);
        for (int i = 0; i < treeCount; i++) {
            level.addObstacle(new Tree(freeTile(random, taken)));
        }
        for (int i = 0; i < aiTankCount; i++) {
            level.addMovable(new Tank(freeTile(random, taken), Direction.RIGHT));
        }
        level.addPlayer(new Tank(freeTile(random, taken), Direction.RIGHT));
        return level;
    }

    private GridPoint2 freeTile(Random random, Set<GridPoint2> taken) {
        GridPoint2 coordinates;
        do {
            coordinates = new GridPoint2(random.nextInt(width), random.nextInt(height));
        } while (!taken.add(coordinates));
        return coordinates;
    }
}
