package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Level {

    private final int width;
    private final int height;
    private final Tank tank;
    private final List<Obstacle> obstacles;

    public Level(int width, int height, Tank tank, Collection<? extends Obstacle> obstacles) {
        this.width = width;
        this.height = height;
        this.tank = tank;
        this.obstacles = new ArrayList<>(obstacles);
    }

    // free means inside the level and not occupied by an obstacle
    public boolean isFree(GridPoint2 coordinates) {
        if (!contains(coordinates)) {
            return false;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(coordinates)) {
                return false;
            }
        }
        return true;
    }

    public void update(float deltaTime) {
        tank.update(deltaTime);
    }

    private boolean contains(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }
}
