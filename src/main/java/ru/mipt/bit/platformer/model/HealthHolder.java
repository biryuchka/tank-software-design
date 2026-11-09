package ru.mipt.bit.platformer.model;

/** Object which carries health and lets it be looked at, a health bar for instance. */
public interface HealthHolder {

    int getHealth();

    int getMaxHealth();

    float getHealthRatio();
}
