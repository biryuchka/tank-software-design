package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

import java.util.HashMap;
import java.util.Map;

/** Picks the texture of an object by its type. */
public class TextureGraphicsFactory implements GraphicsFactory {

    private final Map<Class<?>, String> texturePaths = new HashMap<>();

    public TextureGraphicsFactory register(Class<?> type, String texturePath) {
        texturePaths.put(type, texturePath);
        return this;
    }

    @Override
    public Renderable create(GameObject object) {
        String texturePath = texturePaths.get(object.getClass());
        if (texturePath == null) {
            throw new IllegalArgumentException("No texture registered for " + object.getClass().getName());
        }
        return new GameObjectGraphics(object, texturePath);
    }
}
