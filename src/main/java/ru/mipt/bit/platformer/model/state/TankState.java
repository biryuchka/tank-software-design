package ru.mipt.bit.platformer.model.state;

/** What a tank is able to do right now and how fast it does it. */
public interface TankState {

    boolean canMove();

    boolean canShoot();

    float getMovementTimeFactor();

    float getReloadTimeFactor();
}
