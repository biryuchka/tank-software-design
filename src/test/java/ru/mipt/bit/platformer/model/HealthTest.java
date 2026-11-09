package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthTest {

    @Test
    void startsFull() {
        Health health = new Health(100);

        assertEquals(100, health.getCurrent());
        assertEquals(100, health.getMaximum());
        assertEquals(1f, health.getRatio());
        assertTrue(health.isAlive());
    }

    @Test
    void losesTheDamageTaken() {
        Health health = new Health(100);

        health.damage(20);

        assertEquals(80, health.getCurrent());
        assertEquals(0.8f, health.getRatio());
        assertTrue(health.isAlive());
    }

    @Test
    void diesWhenNothingIsLeft() {
        Health health = new Health(40);

        health.damage(20);
        health.damage(20);

        assertEquals(0, health.getCurrent());
        assertFalse(health.isAlive());
    }

    @Test
    void neverGoesBelowZero() {
        Health health = new Health(10);

        health.damage(100);

        assertEquals(0, health.getCurrent());
        assertEquals(0f, health.getRatio());
        assertFalse(health.isAlive());
    }

    @Test
    void refusesMeaninglessValues() {
        assertThrows(IllegalArgumentException.class, () -> new Health(0));
        assertThrows(IllegalArgumentException.class, () -> new Health(-1));
        assertThrows(IllegalArgumentException.class, () -> new Health(10).damage(-1));
    }
}
