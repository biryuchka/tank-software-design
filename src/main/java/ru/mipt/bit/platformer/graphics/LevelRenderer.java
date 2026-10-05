package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelRenderer implements Renderer {

    private final Batch batch;
    private final TiledMap map;
    private final MapRenderer mapRenderer;
    private final TileMovement tileMovement;
    private final Color backgroundColor;
    private final List<Renderable> renderables = new ArrayList<>();

    public LevelRenderer(Batch batch, TiledMap map, TileMovement tileMovement, Color backgroundColor) {
        this.batch = batch;
        this.map = map;
        this.mapRenderer = createSingleLayerMapRenderer(map, batch);
        this.tileMovement = tileMovement;
        this.backgroundColor = backgroundColor;
    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
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
        for (Renderable renderable : renderables) {
            renderable.render(batch, tileMovement);
        }
        // submit all drawing requests
        batch.end();
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        for (Renderable renderable : renderables) {
            renderable.dispose();
        }
        map.dispose();
        batch.dispose();
    }
}
