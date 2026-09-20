package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Object which does not let anybody enter its tile. */
public interface Obstacle {

    GridPoint2 getCoordinates();
}
