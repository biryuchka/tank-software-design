package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.HealthHolder;
import ru.mipt.bit.platformer.model.Movable;

/** Hangs a health bar over every moving object which has health, and takes it off on demand. */
public class HealthBarDecorator implements RenderableDecorator {

    private final Texture pixel;

    public HealthBarDecorator() {
        this(whitePixel());
    }

    HealthBarDecorator(Texture pixel) {
        this.pixel = pixel;
    }

    @Override
    public Renderable decorate(GameObject object, Renderable renderable) {
        if (!hasHealthBar(object) || renderable instanceof HealthBarGraphics) {
            return renderable;
        }
        return new HealthBarGraphics(renderable, object, (HealthHolder) object, pixel);
    }

    @Override
    public Renderable undecorate(Renderable renderable) {
        if (renderable instanceof HealthBarGraphics) {
            return ((HealthBarGraphics) renderable).getOrigin();
        }
        return renderable;
    }

    @Override
    public void dispose() {
        pixel.dispose();
    }

    private boolean hasHealthBar(GameObject object) {
        return object instanceof Movable && object instanceof HealthHolder;
    }

    private static Texture whitePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        try {
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            return new Texture(pixmap);
        } finally {
            pixmap.dispose();
        }
    }
}
