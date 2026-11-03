package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level implements Field, BulletRegistry {

    private final int width;
    private final int height;
    // everything placed on the field, in the order it has to be drawn
    private final List<GameObject> objects = new ArrayList<>();
    private final List<Combatant> combatants = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<LevelListener> listeners = new ArrayList<>();
    private Combatant player;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /** A new listener is told about everything the level already holds, so it starts in step. */
    public void addListener(LevelListener listener) {
        listeners.add(listener);
        for (GameObject object : objects) {
            listener.onObjectAdded(object);
        }
    }

    public <T extends GameObject & Combatant> void addPlayer(T player) {
        this.player = player;
        addCombatant(player);
    }

    public <T extends GameObject & Combatant> void addCombatant(T combatant) {
        combatants.add(combatant);
        register(combatant);
    }

    public <T extends GameObject & Obstacle> void addObstacle(T obstacle) {
        obstacles.add(obstacle);
        register(obstacle);
    }

    @Override
    public void addBullet(Bullet bullet) {
        // a bullet fired into a wall of the level never appears
        if (!contains(bullet.getCoordinates())) {
            return;
        }
        bullets.add(bullet);
        register(bullet);
    }

    // free means inside the field, not occupied by an obstacle and not taken by another unit
    @Override
    public boolean isFree(GridPoint2 coordinates) {
        if (!contains(coordinates)) {
            return false;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(coordinates)) {
                return false;
            }
        }
        for (Combatant combatant : combatants) {
            // a moving unit holds both the tile it leaves and the tile it heads to
            if (combatant.getCoordinates().equals(coordinates)
                    || combatant.getDestinationCoordinates().equals(coordinates)) {
                return false;
            }
        }
        return true;
    }

    /** One tick of the world: everybody lives through the time passed, then the bullets are resolved. */
    public void update(float deltaTime) {
        for (Combatant combatant : new ArrayList<>(combatants)) {
            combatant.update(deltaTime);
        }
        for (Bullet bullet : new ArrayList<>(bullets)) {
            bullet.update(deltaTime);
        }
        for (Bullet bullet : new ArrayList<>(bullets)) {
            resolve(bullet);
        }
    }

    private void resolve(Bullet bullet) {
        if (bullet.isMoving()) {
            return;
        }
        GameObject target = objectAt(bullet.getCoordinates(), bullet);
        if (target != null) {
            if (target.hit(bullet.getDamage())) {
                remove(target);
            }
            remove(bullet);
        } else if (!contains(bullet.getNextCoordinates())) {
            // flew out of the field
            remove(bullet);
        } else {
            bullet.flyOn();
        }
    }

    private GameObject objectAt(GridPoint2 coordinates, GameObject except) {
        for (GameObject object : objects) {
            if (object != except && object.getCoordinates().equals(coordinates)) {
                return object;
            }
        }
        return null;
    }

    private void register(GameObject object) {
        objects.add(object);
        for (LevelListener listener : new ArrayList<>(listeners)) {
            listener.onObjectAdded(object);
        }
    }

    private void remove(GameObject object) {
        if (!objects.remove(object)) {
            return;
        }
        combatants.remove(object);
        obstacles.remove(object);
        bullets.remove(object);
        if (object == player) {
            player = null;
        }
        for (LevelListener listener : new ArrayList<>(listeners)) {
            listener.onObjectRemoved(object);
        }
    }

    public Combatant getPlayer() {
        return player;
    }

    public List<Combatant> getCombatants() {
        return Collections.unmodifiableList(combatants);
    }

    public List<GameObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean contains(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }
}
