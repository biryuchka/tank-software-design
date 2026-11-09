package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;

/** Puts an extra layer of graphics on top of a Renderable and is able to take it off again. */
public interface RenderableDecorator extends Disposable {

    Renderable decorate(GameObject object, Renderable renderable);

    Renderable undecorate(Renderable renderable);
}
