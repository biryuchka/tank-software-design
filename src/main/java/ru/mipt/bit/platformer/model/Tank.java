package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tank implements GameObject, Combatant, HealthHolder {

    public static final float DEFAULT_MOVEMENT_SPEED = 0.4f;
    public static final int DEFAULT_HEALTH = 100;
    public static final float DEFAULT_RELOAD_TIME = 0.7f;

    private final Movement movement;
    private final Health health;
    private final float reloadTime;
    private float timeSinceLastShot;

    public Tank(GridPoint2 coordinates, Direction direction) {
        this(coordinates, direction, DEFAULT_MOVEMENT_SPEED, DEFAULT_HEALTH, DEFAULT_RELOAD_TIME);
    }

    public Tank(GridPoint2 coordinates, Direction direction, float movementSpeed) {
        this(coordinates, direction, movementSpeed, DEFAULT_HEALTH, DEFAULT_RELOAD_TIME);
    }

    public Tank(GridPoint2 coordinates, Direction direction, int health) {
        this(coordinates, direction, DEFAULT_MOVEMENT_SPEED, health, DEFAULT_RELOAD_TIME);
    }

    public Tank(GridPoint2 coordinates, Direction direction, float movementSpeed, int health, float reloadTime) {
        this.movement = new Movement(coordinates, direction, movementSpeed);
        this.health = new Health(health);
        this.reloadTime = reloadTime;
        this.timeSinceLastShot = reloadTime;
    }

    @Override
    public void move(Direction direction, Field field) {
        if (isMoving()) {
            return;
        }
        GridPoint2 nextCoordinates = direction.apply(movement.getCoordinates());
        if (field.isFree(nextCoordinates)) {
            movement.goTo(nextCoordinates);
        }
        // the tank turns even when the way is blocked
        movement.turn(direction);
    }

    @Override
    public boolean canShoot() {
        return timeSinceLastShot >= reloadTime;
    }

    @Override
    public void shoot(BulletRegistry registry) {
        if (!canShoot()) {
            return;
        }
        timeSinceLastShot = 0f;
        // the bullet starts on the tile the tank looks at
        registry.addBullet(new Bullet(movement.getNextCoordinates(), movement.getDirection()));
    }

    @Override
    public void update(float deltaTime) {
        movement.update(deltaTime);
        timeSinceLastShot += deltaTime;
    }

    @Override
    public boolean isMoving() {
        return movement.isMoving();
    }

    @Override
    public int getHealth() {
        return health.getCurrent();
    }

    @Override
    public int getMaxHealth() {
        return health.getMaximum();
    }

    @Override
    public float getHealthRatio() {
        return health.getRatio();
    }

    @Override
    public boolean hit(int damage) {
        health.damage(damage);
        // a tank without health left leaves the level
        return !health.isAlive();
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
        return movement.getDirection().getRotation();
    }
}
