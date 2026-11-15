package ru.mipt.bit.platformer.model.state;

/** The state follows the health left: above 70% light, from 15% medium, below 15% heavy. */
public class HealthStates implements TankStates {

    public static final float MEDIUM_THRESHOLD = 0.7f;
    public static final float HEAVY_THRESHOLD = 0.15f;

    private final TankState light = new LightState();
    private final TankState medium = new MediumState();
    private final TankState heavy = new HeavyState();
    private final TankState destroyed = new DestroyedState();

    @Override
    public TankState stateOf(float healthRatio) {
        if (healthRatio <= 0f) {
            return destroyed;
        }
        if (healthRatio < HEAVY_THRESHOLD) {
            return heavy;
        }
        if (healthRatio <= MEDIUM_THRESHOLD) {
            return medium;
        }
        return light;
    }
}
