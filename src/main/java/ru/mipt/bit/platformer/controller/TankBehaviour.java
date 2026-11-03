package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.BulletRegistry;
import ru.mipt.bit.platformer.model.Combatant;
import ru.mipt.bit.platformer.model.Field;

/** Strategy which decides what a unit controlled by AI does on this frame: drive or fire. */
public interface TankBehaviour {

    Command decide(Combatant tank, Field field, BulletRegistry registry);
}
