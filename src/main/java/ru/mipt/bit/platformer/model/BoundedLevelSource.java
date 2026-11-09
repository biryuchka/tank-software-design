package ru.mipt.bit.platformer.model;

/**
 * Lets a level through only when it fits the map it is drawn on: the model takes its size
 * from the layout, while the background is loaded separately and stays fixed.
 */
public class BoundedLevelSource implements LevelSource {

    private final LevelSource origin;
    private final int maxWidth;
    private final int maxHeight;

    public BoundedLevelSource(LevelSource origin, int maxWidth, int maxHeight) {
        this.origin = origin;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public Level load() {
        Level level = origin.load();
        if (level.getWidth() > maxWidth || level.getHeight() > maxHeight) {
            throw new IllegalArgumentException("Level of " + level.getWidth() + "x" + level.getHeight()
                    + " tiles does not fit the map of " + maxWidth + "x" + maxHeight + " tiles");
        }
        return level;
    }
}
