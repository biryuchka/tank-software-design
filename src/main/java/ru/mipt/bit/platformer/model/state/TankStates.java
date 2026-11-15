package ru.mipt.bit.platformer.model.state;

/** Decides which state a tank is in; a booster picked up on the field would be another one. */
public interface TankStates {

    TankState stateOf(float healthRatio);
}
