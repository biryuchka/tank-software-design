package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Flies tile by tile in one direction until it hits something or leaves the field. */
public class Bullet implements GameObject, Movable {

    public static final float DEFAULT_SPEED = 0.12f;
    public static final int DEFAULT_DAMAGE = 1;

    private final Movement movement;
    private final Direction direction;
    private final int damage;

    public Bullet(GridPoint2 coordinates, Direction direction) {
        this(coordinates, direction, DEFAULT_DAMAGE, DEFAULT_SPEED);
    }

    public Bullet(GridPoint2 coordinates, Direction direction, int damage, float speed) {
        this.movement = new Movement(coordinates, direction, speed);
        this.direction = direction;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public Direction getDirection() {
        return direction;
    }

    /** Sends the bullet to the next tile; the level calls it once the current tile turns out to be clear. */
    public void flyOn() {
        movement.goTo(movement.getNextCoordinates());
    }

    public GridPoint2 getNextCoordinates() {
        return movement.getNextCoordinates();
    }

    @Override
    public void move(Direction direction, Field field) {
        // a bullet keeps its own course and takes no orders
    }

    @Override
    public void update(float deltaTime) {
        movement.update(deltaTime);
    }

    @Override
    public boolean isMoving() {
        return movement.isMoving();
    }

    @Override
    public GridPoint2 getCoordinates() {
        return movement.getCoordinates();
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return movement.getDestinationCoordinates();
    }

    @Override
    public float getMovementProgress() {
        return movement.getProgress();
    }

    @Override
    public float getRotation() {
        return direction.getRotation();
    }

    @Override
    public boolean hit(int damage) {
        // bullets destroy each other
        return true;
    }
}
