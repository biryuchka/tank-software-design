package ru.mipt.bit.platformer.model;

/**
 * Learns about the objects of the level: the ones already there when it subscribes
 * and the ones appearing and disappearing afterwards.
 */
public interface LevelListener {

    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
