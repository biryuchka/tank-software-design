package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level implements Field {

    private final int width;
    private final int height;
    // everything placed on the field, in the order it has to be drawn
    private final List<GameObject> objects = new ArrayList<>();
    private final List<Movable> movables = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private Movable player;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public <T extends GameObject & Movable> void addPlayer(T player) {
        this.player = player;
        addMovable(player);
    }

    public <T extends GameObject & Movable> void addMovable(T movable) {
        objects.add(movable);
        movables.add(movable);
    }

    public <T extends GameObject & Obstacle> void addObstacle(T obstacle) {
        objects.add(obstacle);
        obstacles.add(obstacle);
    }

    // free means inside the field and not occupied by an obstacle
    @Override
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
        for (Movable movable : movables) {
            movable.update(deltaTime);
        }
    }

    public Movable getPlayer() {
        return player;
    }

    public List<GameObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean contains(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }
}
