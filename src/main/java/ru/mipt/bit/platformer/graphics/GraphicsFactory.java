package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

/** Knows how a given object of the level has to be drawn. */
public interface GraphicsFactory {

    Renderable create(GameObject object);
}
