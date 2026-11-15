package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Object which travels from tile to tile and lives through the time passing. */
public interface Moving {

    GridPoint2 getCoordinates();

    /** The tile the object is heading to; equal to the current one while it stands still. */
    GridPoint2 getDestinationCoordinates();

    void update(float deltaTime);

    boolean isMoving();
}
