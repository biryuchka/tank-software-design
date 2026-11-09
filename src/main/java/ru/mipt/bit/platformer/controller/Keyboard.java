package ru.mipt.bit.platformer.controller;

/** Source of key states, hides the way they are actually read. */
public interface Keyboard {

    boolean isKeyPressed(int key);

    /** True only on the frame the key went down, so that one press means one action. */
    boolean isKeyJustPressed(int key);
}
