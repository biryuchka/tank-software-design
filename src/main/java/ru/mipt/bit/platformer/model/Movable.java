package ru.mipt.bit.platformer.model;

/** Moving object which takes orders where to go; a bullet keeps its own course and is not one. */
public interface Movable extends Moving {

    void move(Direction direction, Field field);
}
