package ru.mipt.bit.platformer.model;

/** Object which is able to move between tiles of the level. */
public interface Movable {

    void move(Direction direction, Level level);

    void update(float deltaTime);

    boolean isMoving();
}
