package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Object which is able to move between tiles of the field. */
public interface Movable {

    GridPoint2 getCoordinates();

    void move(Direction direction, Field field);

    void update(float deltaTime);

    boolean isMoving();
}
