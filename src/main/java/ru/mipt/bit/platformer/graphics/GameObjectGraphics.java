package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

/** Texture of a single game object and its place on the screen. */
public class GameObjectGraphics implements Disposable {

    private final GameObject object;
    // Texture decodes an image file and loads it into GPU memory, it represents a native resource
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public GameObjectGraphics(GameObject object, String texturePath) {
        this.object = object;
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
    }

    public void render(Batch batch, TileMovement tileMovement) {
        // for a still object both tiles are the same, so it stays in its tile center
        tileMovement.moveRectangleBetweenTileCenters(rectangle, object.getCoordinates(),
                object.getDestinationCoordinates(), object.getMovementProgress());
        drawTextureRegionUnscaled(batch, graphics, rectangle, object.getRotation());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
