package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelRenderer implements Disposable {

    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final List<GameObjectGraphics> objectsGraphics = new ArrayList<>();

    public LevelRenderer(String levelPath) {
        batch = new SpriteBatch();
        level = new TmxMapLoader().load(levelPath);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public void addObject(GameObject object, String texturePath) {
        objectsGraphics.add(new GameObjectGraphics(object, texturePath));
    }

    public int getLevelWidth() {
        return groundLayer.getWidth();
    }

    public int getLevelHeight() {
        return groundLayer.getHeight();
    }

    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();
        for (GameObjectGraphics graphics : objectsGraphics) {
            graphics.render(batch, tileMovement);
        }
        // submit all drawing requests
        batch.end();
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        for (GameObjectGraphics graphics : objectsGraphics) {
            graphics.dispose();
        }
        level.dispose();
        batch.dispose();
    }
}
