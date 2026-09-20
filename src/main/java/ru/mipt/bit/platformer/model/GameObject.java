package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Object placed on a tile of the level. */
public abstract class GameObject {

    protected final GridPoint2 coordinates;

    protected GameObject(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    // a copy, so that nobody can move the object bypassing it
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    // by default an object stays in its tile and is not rotated
    public GridPoint2 getDestinationCoordinates() {
        return getCoordinates();
    }

    public float getMovementProgress() {
        return 1f;
    }

    public float getRotation() {
        return 0f;
    }
}
