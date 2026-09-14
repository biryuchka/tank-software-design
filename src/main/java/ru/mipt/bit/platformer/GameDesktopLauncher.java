package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";

    private static final Map<Integer, Direction> KEY_TO_DIRECTION = new LinkedHashMap<>();

    static {
        KEY_TO_DIRECTION.put(UP, Direction.UP);
        KEY_TO_DIRECTION.put(W, Direction.UP);
        KEY_TO_DIRECTION.put(LEFT, Direction.LEFT);
        KEY_TO_DIRECTION.put(A, Direction.LEFT);
        KEY_TO_DIRECTION.put(DOWN, Direction.DOWN);
        KEY_TO_DIRECTION.put(S, Direction.DOWN);
        KEY_TO_DIRECTION.put(RIGHT, Direction.RIGHT);
        KEY_TO_DIRECTION.put(D, Direction.RIGHT);
    }

    private LevelRenderer levelRenderer;
    private Level level;

    @Override
    public void create() {
        levelRenderer = new LevelRenderer(LEVEL_PATH);

        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        List<Tree> trees = Arrays.asList(
                new Tree(new GridPoint2(1, 3)),
                new Tree(new GridPoint2(4, 5)),
                new Tree(new GridPoint2(7, 2))
        );
        level = new Level(levelRenderer.getLevelWidth(), levelRenderer.getLevelHeight(), tank, trees);

        levelRenderer.addObject(tank, TANK_TEXTURE_PATH);
        for (Tree tree : trees) {
            levelRenderer.addObject(tree, TREE_TEXTURE_PATH);
        }
    }

    @Override
    public void render() {
        for (Map.Entry<Integer, Direction> keyDirection : KEY_TO_DIRECTION.entrySet()) {
            if (Gdx.input.isKeyPressed(keyDirection.getKey())) {
                level.getTank().move(keyDirection.getValue(), level);
                break;
            }
        }

        level.update(Gdx.graphics.getDeltaTime());

        levelRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        levelRenderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
