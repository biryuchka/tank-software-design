package ru.mipt.bit.platformer.model;

/**
 * Strategy of filling the level: generated randomly, read from a file, built by hand.
 * Nobody downstream knows which one is in use.
 */
public interface LevelSource {

    Level load();
}
