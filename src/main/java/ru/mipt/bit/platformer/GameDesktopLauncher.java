package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.controller.GdxKeyboard;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.controller.MoveCommand;
import ru.mipt.bit.platformer.graphics.GameObjectGraphics;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.Renderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.FileLevelSource;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.RandomLevelSource;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_MAP_PATH = "level.tmx";
    private static final String LEVEL_LAYOUT_PATH = "level.txt";
    private static final String RANDOM_LEVEL_ARGUMENT = "random";
    // the tiled map the level is drawn on is 10x8 tiles
    private static final int LEVEL_WIDTH = 10;
    private static final int LEVEL_HEIGHT = 8;
    private static final int RANDOM_TREE_COUNT = 12;
    private static final Color BACKGROUND_COLOR = new Color(0f, 0f, 0.2f, 1f);
    private static final Interpolation MOVEMENT_INTERPOLATION = Interpolation.smooth;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private static final Map<Class<? extends GameObject>, String> TEXTURE_PATHS = new HashMap<>();

    static {
        TEXTURE_PATHS.put(Tank.class, "images/tank_blue.png");
        TEXTURE_PATHS.put(Tree.class, "images/greenTree.png");
    }

    private final LevelSource levelSource;

    private Renderer renderer;
    private InputController inputController;
    private Level level;

    public GameDesktopLauncher(LevelSource levelSource) {
        this.levelSource = levelSource;
    }

    @Override
    public void create() {
        level = levelSource.load();

        TiledMap map = new TmxMapLoader().load(LEVEL_MAP_PATH);
        TiledMapTileLayer groundLayer = getSingleLayer(map);
        TileMovement tileMovement = new TileMovement(groundLayer, MOVEMENT_INTERPOLATION);

        LevelRenderer levelRenderer = new LevelRenderer(new SpriteBatch(), map, tileMovement, BACKGROUND_COLOR);
        for (GameObject object : level.getObjects()) {
            levelRenderer.add(new GameObjectGraphics(object, TEXTURE_PATHS.get(object.getClass())));
        }
        renderer = levelRenderer;

        inputController = createInputController(level.getPlayer());
    }

    private InputController createInputController(Movable player) {
        InputController controller = new InputController(new GdxKeyboard());
        registerMovement(controller, player, Direction.UP, UP, W);
        registerMovement(controller, player, Direction.LEFT, LEFT, A);
        registerMovement(controller, player, Direction.DOWN, DOWN, S);
        registerMovement(controller, player, Direction.RIGHT, RIGHT, D);
        return controller;
    }

    private void registerMovement(InputController controller, Movable player, Direction direction, int... keys) {
        MoveCommand command = new MoveCommand(player, direction, level);
        for (int key : keys) {
            controller.registerCommand(key, command);
        }
    }

    @Override
    public void render() {
        inputController.processInput();
        level.update(Gdx.graphics.getDeltaTime());
        renderer.render();
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
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        new Lwjgl3Application(new GameDesktopLauncher(chooseLevelSource(args)), config);
    }

    // the only place which knows how the level is filled; swapping the strategy changes nothing else
    private static LevelSource chooseLevelSource(String[] args) {
        if (args.length > 0 && RANDOM_LEVEL_ARGUMENT.equals(args[0])) {
            return new RandomLevelSource(LEVEL_WIDTH, LEVEL_HEIGHT, RANDOM_TREE_COUNT);
        }
        return new FileLevelSource(LEVEL_LAYOUT_PATH);
    }
}
