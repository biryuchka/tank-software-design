package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelGraphicsTest {

    private final FakeDecorator decorator = new FakeDecorator();
    private final LevelGraphics graphics = new LevelGraphics(new FakeGraphicsFactory(), decorator);

    private final Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
    private final Tree tree = new Tree(new GridPoint2(2, 2));

    @Test
    void drawsEveryObjectOfTheLevel() {
        graphics.onObjectAdded(tank);
        graphics.onObjectAdded(tree);

        assertEquals(2, graphics.all().size());
    }

    @Test
    void forgetsAndDisposesTheGraphicsOfAnObjectWhichLeft() {
        graphics.onObjectAdded(tank);
        FakeRenderable renderable = (FakeRenderable) graphics.all().iterator().next();

        graphics.onObjectRemoved(tank);

        assertTrue(graphics.all().isEmpty());
        assertTrue(renderable.disposed);
    }

    @Test
    void showsNoHealthBarsUntilTheyAreSwitchedOn() {
        graphics.onObjectAdded(tank);

        assertFalse(graphics.areHealthBarsVisible());
        assertFalse(graphics.all().iterator().next() instanceof FakeBar);
    }

    @Test
    void putsABarOnEveryObjectWhichHasHealth() {
        graphics.onObjectAdded(tank);
        graphics.onObjectAdded(tree);

        graphics.toggleHealthBars();

        assertTrue(graphics.areHealthBarsVisible());
        List<Renderable> all = new ArrayList<>(graphics.all());
        assertTrue(all.get(0) instanceof FakeBar, "the tank carries a bar");
        assertFalse(all.get(1) instanceof FakeBar, "the tree has no health to show");
    }

    @Test
    void takesTheBarsOffOnTheSecondSwitch() {
        graphics.onObjectAdded(tank);
        Renderable original = graphics.all().iterator().next();

        graphics.toggleHealthBars();
        graphics.toggleHealthBars();

        assertFalse(graphics.areHealthBarsVisible());
        assertSame(original, graphics.all().iterator().next());
    }

    @Test
    void anObjectJoiningWhileTheBarsAreOnGetsOneAtOnce() {
        graphics.toggleHealthBars();

        graphics.onObjectAdded(tank);

        assertTrue(graphics.all().iterator().next() instanceof FakeBar);
    }

    @Test
    void disposesEverythingItHoldsTogetherWithTheDecorator() {
        graphics.onObjectAdded(tank);
        FakeRenderable renderable = (FakeRenderable) graphics.all().iterator().next();

        graphics.dispose();

        assertTrue(renderable.disposed);
        assertTrue(decorator.disposed);
        assertTrue(graphics.all().isEmpty());
    }

    private static class FakeGraphicsFactory implements GraphicsFactory {

        @Override
        public Renderable create(GameObject object) {
            return new FakeRenderable();
        }
    }

    private static class FakeDecorator implements RenderableDecorator {

        private boolean disposed;

        @Override
        public Renderable decorate(GameObject object, Renderable renderable) {
            if (!(object instanceof Tank) || renderable instanceof FakeBar) {
                return renderable;
            }
            return new FakeBar(renderable);
        }

        @Override
        public Renderable undecorate(Renderable renderable) {
            return renderable instanceof FakeBar ? ((FakeBar) renderable).origin : renderable;
        }

        @Override
        public void dispose() {
            disposed = true;
        }
    }

    private static class FakeRenderable implements Renderable {

        private boolean disposed;

        @Override
        public void render(Batch batch, TileMovement tileMovement) {
        }

        @Override
        public void dispose() {
            disposed = true;
        }
    }

    private static class FakeBar implements Renderable {

        private final Renderable origin;

        private FakeBar(Renderable origin) {
            this.origin = origin;
        }

        @Override
        public void render(Batch batch, TileMovement tileMovement) {
            origin.render(batch, tileMovement);
        }

        @Override
        public void dispose() {
            origin.dispose();
        }
    }
}
