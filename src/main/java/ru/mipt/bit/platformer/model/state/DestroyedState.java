package ru.mipt.bit.platformer.model.state;

/** Burnt out: whoever still holds a reference to the tank gets nothing out of it. */
public class DestroyedState implements TankState {

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canShoot() {
        return false;
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
