package ru.mipt.bit.platformer.model.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthStatesTest {

    private final TankStates states = new HealthStates();

    @Test
    void aboveSeventyPercentTheTankIsUntouched() {
        TankState state = states.stateOf(0.71f);

        assertTrue(state instanceof LightState);
        assertEquals(1f, state.getMovementTimeFactor());
        assertTrue(state.canShoot());
    }

    @Test
    void fromFifteenToSeventyPercentItDrivesTwiceAsSlowly() {
        assertTrue(states.stateOf(0.7f) instanceof MediumState);
        assertTrue(states.stateOf(0.15f) instanceof MediumState);
        assertEquals(2f, states.stateOf(0.5f).getMovementTimeFactor());
        assertTrue(states.stateOf(0.5f).canShoot());
    }

    @Test
    void belowFifteenPercentItCrawlsAndCannotShoot() {
        TankState state = states.stateOf(0.14f);

        assertTrue(state instanceof HeavyState);
        assertEquals(3f, state.getMovementTimeFactor());
        assertFalse(state.canShoot());
        assertTrue(state.canMove());
    }

    @Test
    void withoutHealthTheTankDoesNothingAtAll() {
        TankState state = states.stateOf(0f);

        assertTrue(state instanceof DestroyedState);
        assertFalse(state.canMove());
        assertFalse(state.canShoot());
    }

    @Test
    void theSameStateIsHandedOutEveryTime() {
        assertEquals(states.stateOf(0.9f), states.stateOf(0.8f));
    }
}
