package ru.mipt.bit.platformer.model;

/** Object which is able to fire bullets into the level. */
public interface Shooter {

    boolean canShoot();

    void shoot(BulletRegistry registry);
}
