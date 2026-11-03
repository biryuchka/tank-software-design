package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelListener;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

/**
 * Draws the level. It owns no list of its own: it listens to the level and keeps the
 * graphics in step with the objects appearing and disappearing there.
 */
public class LevelRenderer implements Renderer, LevelListener {

    private final Batch batch;
    private final TiledMap map;
    private final MapRenderer mapRenderer;
    private final TileMovement tileMovement;
    private final Color backgroundColor;
    private final GraphicsFactory graphicsFactory;
    // insertion order is the drawing order
    private final Map<GameObject, Renderable> renderables = new LinkedHashMap<>();

    public LevelRenderer(Batch batch, TiledMap map, TileMovement tileMovement,
                         Color backgroundColor, GraphicsFactory graphicsFactory) {
        this.batch = batch;
        this.map = map;
        this.mapRenderer = createSingleLayerMapRenderer(map, batch);
        this.tileMovement = tileMovement;
        this.backgroundColor = backgroundColor;
        this.graphicsFactory = graphicsFactory;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        renderables.put(object, graphicsFactory.create(object));
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        Renderable renderable = renderables.remove(object);
        if (renderable != null) {
            renderable.dispose();
        }
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // render each tile of the level
        mapRenderer.render();

        // start recording all drawing commands
        batch.begin();
        for (Renderable renderable : new ArrayList<>(renderables.values())) {
            renderable.render(batch, tileMovement);
        }
        // submit all drawing requests
        batch.end();
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        for (Renderable renderable : renderables.values()) {
            renderable.dispose();
        }
        renderables.clear();
        map.dispose();
        batch.dispose();
    }
}
