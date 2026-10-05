package ru.mipt.bit.platformer.model;

/** Object which is able to move between tiles of the field. */
public interface Movable {

    void move(Direction direction, Field field);

    void update(float deltaTime);

    boolean isMoving();
}
