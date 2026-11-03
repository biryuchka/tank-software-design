package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelListenerTest {

    @Test
    void newListenerIsToldAboutObjectsAlreadyOnTheLevel() {
        Level level = new Level(10, 8);
        Tank player = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        Tree tree = new Tree(new GridPoint2(3, 3));
        level.addPlayer(player);
        level.addObstacle(tree);

        RecordingListener listener = new RecordingListener();
        level.addListener(listener);

        // the initial state arrives through the very same events as every later change
        assertEquals(2, listener.added.size());
        assertSame(player, listener.added.get(0));
        assertSame(tree, listener.added.get(1));
    }

    @Test
    void listenerHearsAboutObjectsAddedLater() {
        Level level = new Level(10, 8);
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);

        level.addObstacle(new Tree(new GridPoint2(3, 3)));

        assertEquals(1, listener.added.size());
    }

    @Test
    void listenerHearsAboutAFiredBullet() {
        Level level = new Level(10, 8);
        Tank player = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        level.addPlayer(player);
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);

        player.shoot(level);

        // the player arrived on subscription, the bullet right after it
        assertEquals(2, listener.added.size());
        assertTrue(listener.added.get(1) instanceof Bullet);
    }

    @Test
    void listenerHearsAboutObjectsLeavingTheLevel() {
        Level level = new Level(10, 8);
        Tank player = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        Tank target = new Tank(new GridPoint2(2, 1), Direction.LEFT, 0.4f, 0, 0f);
        level.addPlayer(player);
        level.addCombatant(target);
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);

        player.shoot(level);
        level.update(0.01f);

        // the destroyed tank and the spent bullet both leave
        assertEquals(2, listener.removed.size());
        assertTrue(listener.removed.contains(target));
    }

    @Test
    void everyListenerIsNotified() {
        Level level = new Level(10, 8);
        RecordingListener first = new RecordingListener();
        RecordingListener second = new RecordingListener();
        level.addListener(first);
        level.addListener(second);

        level.addObstacle(new Tree(new GridPoint2(3, 3)));

        assertEquals(1, first.added.size());
        assertEquals(1, second.added.size());
    }

    private static class RecordingListener implements LevelListener {

        private final List<GameObject> added = new ArrayList<>();
        private final List<GameObject> removed = new ArrayList<>();

        @Override
        public void onObjectAdded(GameObject object) {
            added.add(object);
        }

        @Override
        public void onObjectRemoved(GameObject object) {
            removed.add(object);
        }
    }
}
