package ru.mipt.bit.platformer.model.state;

/** Barely scratched: the tank behaves as if it had taken no damage at all. */
public class LightState implements TankState {

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public boolean canShoot() {
        return true;
    }

    @Override
    public float getMovementTimeFactor() {
        return 1f;
    }

    @Override
    public float getReloadTimeFactor() {
        return 1f;
    }
}
