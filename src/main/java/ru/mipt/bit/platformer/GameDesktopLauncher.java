package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.controller.GdxKeyboard;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.controller.MoveCommand;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";

    private LevelRenderer levelRenderer;
    private InputController inputController;
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

        inputController = createInputController(tank);
    }

    private InputController createInputController(Movable tank) {
        InputController controller = new InputController(new GdxKeyboard());
        registerMovement(controller, tank, Direction.UP, UP, W);
        registerMovement(controller, tank, Direction.LEFT, LEFT, A);
        registerMovement(controller, tank, Direction.DOWN, DOWN, S);
        registerMovement(controller, tank, Direction.RIGHT, RIGHT, D);
        return controller;
    }

    private void registerMovement(InputController controller, Movable tank, Direction direction, int... keys) {
        MoveCommand command = new MoveCommand(tank, direction, level);
        for (int key : keys) {
            controller.registerCommand(key, command);
        }
    }

    @Override
    public void render() {
        inputController.processInput();
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
