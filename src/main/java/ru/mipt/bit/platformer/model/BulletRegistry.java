package ru.mipt.bit.platformer.model;

/** Whatever a fired bullet has to be handed over to, so that it starts living in the level. */
public interface BulletRegistry {

    void addBullet(Bullet bullet);
}
