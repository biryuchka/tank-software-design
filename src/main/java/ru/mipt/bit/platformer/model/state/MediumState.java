package ru.mipt.bit.platformer.model.state;

/** Damaged: the tank still fights, but drives twice as slowly. */
public class MediumState implements TankState {

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
        return 2f;
    }

    @Override
    public float getReloadTimeFactor() {
        return 1f;
    }
}
