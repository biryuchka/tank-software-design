package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The graphics of the objects of the level: it follows the objects appearing and disappearing
 * there and switches the health bars on and off.
 */
public class LevelGraphics implements LevelListener, Disposable {

    private final GraphicsFactory graphicsFactory;
    private final RenderableDecorator healthBars;
    // insertion order is the drawing order
    private final Map<GameObject, Renderable> renderables = new LinkedHashMap<>();
    private boolean healthBarsVisible;

    public LevelGraphics(GraphicsFactory graphicsFactory, RenderableDecorator healthBars) {
        this.graphicsFactory = graphicsFactory;
        this.healthBars = healthBars;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        Renderable renderable = graphicsFactory.create(object);
        renderables.put(object, healthBarsVisible ? healthBars.decorate(object, renderable) : renderable);
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        Renderable renderable = renderables.remove(object);
        if (renderable != null) {
            renderable.dispose();
        }
    }

    public void toggleHealthBars() {
        healthBarsVisible = !healthBarsVisible;
        for (Map.Entry<GameObject, Renderable> entry : renderables.entrySet()) {
            Renderable renderable = entry.getValue();
            entry.setValue(healthBarsVisible
                    ? healthBars.decorate(entry.getKey(), renderable)
                    : healthBars.undecorate(renderable));
        }
    }

    public boolean areHealthBarsVisible() {
        return healthBarsVisible;
    }

    public Collection<Renderable> all() {
        return new ArrayList<>(renderables.values());
    }

    @Override
    public void dispose() {
        for (Renderable renderable : renderables.values()) {
            renderable.dispose();
        }
        renderables.clear();
        healthBars.dispose();
    }
}
