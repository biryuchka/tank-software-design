package ru.mipt.bit.platformer.model.state;

/** Nearly destroyed: the tank crawls three times as slowly and its gun is out of order. */
public class HeavyState implements TankState {

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public boolean canShoot() {
        return false;
    }

    @Override
    public float getMovementTimeFactor() {
        return 3f;
    }

    @Override
    public float getReloadTimeFactor() {
        return 1f;
    }
}
