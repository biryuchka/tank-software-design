package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

/** Draws a frame: the tiles of the map and then every graphics the level has. */
public class LevelRenderer implements Renderer {

    private final Batch batch;
    private final TiledMap map;
    private final MapRenderer mapRenderer;
    private final TileMovement tileMovement;
    private final Color backgroundColor;
    private final LevelGraphics levelGraphics;

    public LevelRenderer(Batch batch, TiledMap map, TileMovement tileMovement,
                         Color backgroundColor, LevelGraphics levelGraphics) {
        this.batch = batch;
        this.map = map;
        this.mapRenderer = createSingleLayerMapRenderer(map, batch);
        this.tileMovement = tileMovement;
        this.backgroundColor = backgroundColor;
        this.levelGraphics = levelGraphics;
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
        for (Renderable renderable : levelGraphics.all()) {
            renderable.render(batch, tileMovement);
        }
        // submit all drawing requests
        batch.end();
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        levelGraphics.dispose();
        map.dispose();
        batch.dispose();
    }
}
