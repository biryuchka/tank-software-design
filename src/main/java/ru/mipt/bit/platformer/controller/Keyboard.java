package ru.mipt.bit.platformer.controller;

/** Source of key states, hides the way they are actually read. */
public interface Keyboard {

    boolean isKeyPressed(int key);
}
