package ru.mipt.bit.platformer.model;

/** Health of a unit: how much it started with and how much is left. */
public class Health {

    private final int maximum;
    private int current;

    public Health(int maximum) {
        if (maximum <= 0) {
            throw new IllegalArgumentException("Health has to be positive, got " + maximum);
        }
        this.maximum = maximum;
        this.current = maximum;
    }

    public void damage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage cannot be negative, got " + amount);
        }
        current = Math.max(0, current - amount);
    }

    public boolean isAlive() {
        return current > 0;
    }

    public int getCurrent() {
        return current;
    }

    public int getMaximum() {
        return maximum;
    }

    /** 0..1: the share of the health which is left. */
    public float getRatio() {
        return (float) current / maximum;
    }
}
