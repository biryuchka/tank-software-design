package ru.mipt.bit.platformer.controller;

/** Anything which is turned on and off by the same action. */
@FunctionalInterface
public interface Switch {

    void toggle();
}
