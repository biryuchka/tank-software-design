package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank extends GameObject implements Movable {

    private static final float DEFAULT_MOVEMENT_SPEED = 0.4f;

    // which tile the tank wants to go next
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction;
    // seconds it takes to cross one tile
    private final float movementSpeed;

    public Tank(GridPoint2 coordinates, Direction direction) {
        this(coordinates, direction, DEFAULT_MOVEMENT_SPEED);
    }

    public Tank(GridPoint2 coordinates, Direction direction, float movementSpeed) {
        super(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void move(Direction direction, Level level) {
        if (isMoving()) {
            return;
        }
        GridPoint2 nextCoordinates = direction.apply(coordinates);
        if (level.isFree(nextCoordinates)) {
            destinationCoordinates = nextCoordinates;
            movementProgress = 0f;
        }
        // the tank turns even when the way is blocked
        this.direction = direction;
    }

    @Override
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (!isMoving()) {
            // record that the tank has reached its destination
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return direction.getRotation();
    }
}
