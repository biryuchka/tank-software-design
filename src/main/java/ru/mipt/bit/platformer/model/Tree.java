package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tree implements GameObject, Obstacle {

    private final GridPoint2 coordinates;

    public Tree(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        // a copy, so that nobody can move the tree bypassing it
        return new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return getCoordinates();
    }

    @Override
    public float getMovementProgress() {
        return 1f;
    }

    @Override
    public float getRotation() {
        return 0f;
    }

    @Override
    public boolean hit(int damage) {
        // a bullet breaks against the tree, the tree stays
        return false;
    }
}
