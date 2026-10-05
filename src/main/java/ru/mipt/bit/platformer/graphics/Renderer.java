package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.utils.Disposable;

/** Draws a single frame of the whole scene. */
public interface Renderer extends Disposable {

    void render();
}
