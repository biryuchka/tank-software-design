package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.HealthHolder;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.clamp;

/** Decorator: draws the graphics it wraps and a health bar above them. */
public class HealthBarGraphics implements Renderable {

    private static final float WIDTH = 72f;
    private static final float HEIGHT = 10f;
    private static final float VERTICAL_OFFSET = 50f;
    private static final Color EMPTY_PART_COLOR = new Color(0.15f, 0.15f, 0.15f, 0.8f);

    private final Renderable origin;
    private final GameObject object;
    private final HealthHolder health;
    private final Texture pixel;
    private final Rectangle rectangle = new Rectangle(0f, 0f, WIDTH, HEIGHT);

    public HealthBarGraphics(Renderable origin, GameObject object, HealthHolder health, Texture pixel) {
        this.origin = origin;
        this.object = object;
        this.health = health;
        this.pixel = pixel;
    }

    public Renderable getOrigin() {
        return origin;
    }

    @Override
    public void render(Batch batch, TileMovement tileMovement) {
        origin.render(batch, tileMovement);

        tileMovement.moveRectangleBetweenTileCenters(rectangle, object.getCoordinates(),
                object.getDestinationCoordinates(), object.getMovementProgress());
        float x = rectangle.x;
        float y = rectangle.y + VERTICAL_OFFSET;
        float ratio = clamp(health.getHealthRatio(), 0f, 1f);

        Color previousColor = new Color(batch.getColor());
        batch.setColor(EMPTY_PART_COLOR);
        batch.draw(pixel, x, y, WIDTH, HEIGHT);
        // green while the unit is healthy, through yellow to red as the health goes down
        batch.setColor(1f - ratio, ratio, 0f, 1f);
        batch.draw(pixel, x, y, WIDTH * ratio, HEIGHT);
        batch.setColor(previousColor);
    }

    @Override
    public void dispose() {
        // the pixel is shared by every bar and belongs to the decorator which created it
        origin.dispose();
    }
}
