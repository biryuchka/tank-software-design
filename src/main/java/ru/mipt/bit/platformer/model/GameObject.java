package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Object placed on a tile of the level: it has a place, a look and a reaction to being hit. */
public interface GameObject {

    GridPoint2 getCoordinates();

    /** The tile the object is heading to; equal to the current one while it stands still. */
    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();

    /** @return true when the object does not survive the hit and has to leave the level */
    boolean hit(int damage);
}
