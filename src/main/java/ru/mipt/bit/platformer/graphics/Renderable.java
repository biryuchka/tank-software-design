package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

/** Anything the renderer is able to draw. */
public interface Renderable extends Disposable {

    void render(Batch batch, TileMovement tileMovement);
}
