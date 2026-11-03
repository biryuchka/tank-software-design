package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Movable;

/** Strategy which decides what a tank controlled by AI does on this frame. */
public interface TankBehaviour {

    Command decide(Movable tank, Field field);
}
