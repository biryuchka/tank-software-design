package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Walking from tile to tile: where the object is, where it goes and how far it got.
 * Tanks and bullets delegate their movement here instead of inheriting it.
 */
public class Movement {

    private final GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float progress = 1f;
    private Direction direction;
    // seconds it takes to cross one tile
    private final float speed;

    public Movement(GridPoint2 coordinates, Direction direction, float speed) {
        this.coordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.speed = speed;
    }

    public void goTo(GridPoint2 destination) {
        this.destinationCoordinates = new GridPoint2(destination);
        this.progress = 0f;
    }

    public void turn(Direction direction) {
        this.direction = direction;
    }

    public void update(float deltaTime) {
        progress = continueProgress(progress, deltaTime, speed);
        if (!isMoving()) {
            // record that the object has reached its destination
            coordinates.set(destinationCoordinates);
        }
    }

    public boolean isMoving() {
        return !isEqual(progress, 1f);
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public GridPoint2 getNextCoordinates() {
        return direction.apply(coordinates);
    }

    public float getProgress() {
        return progress;
    }

    public Direction getDirection() {
        return direction;
    }
}
