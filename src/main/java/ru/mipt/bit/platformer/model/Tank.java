package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.state.HealthStates;
import ru.mipt.bit.platformer.model.state.TankState;
import ru.mipt.bit.platformer.model.state.TankStates;

public class Tank implements GameObject, Combatant, HealthHolder {

    public static final float DEFAULT_MOVEMENT_SPEED = 0.4f;
    public static final int DEFAULT_HEALTH = 100;
    public static final float DEFAULT_RELOAD_TIME = 0.7f;

    private static final TankStates DEFAULT_STATES = new HealthStates();

    private final Movement movement;
    private final Health health;
    private final TankStates states;
    private final float reloadTime;
    private TankState state;
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
        this(coordinates, direction, movementSpeed, health, reloadTime, DEFAULT_STATES);
    }

    public Tank(GridPoint2 coordinates, Direction direction, float movementSpeed, int health, float reloadTime,
                TankStates states) {
        this.movement = new Movement(coordinates, direction, movementSpeed);
        this.health = new Health(health);
        this.states = states;
        this.state = states.stateOf(this.health.getRatio());
        this.reloadTime = reloadTime;
        this.timeSinceLastShot = reloadTime;
    }

    @Override
    public void move(Direction direction, Field field) {
        if (!state.canMove() || isMoving()) {
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
        return state.canShoot() && timeSinceLastShot >= reloadTime * state.getReloadTimeFactor();
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
        movement.update(deltaTime / state.getMovementTimeFactor());
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
        state = states.stateOf(health.getRatio());
        // a tank without health left leaves the level
        return !health.isAlive();
    }

    public TankState getState() {
        return state;
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
