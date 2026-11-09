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
import ru.mipt.bit.platformer.controller.AiCommandProvider;
import ru.mipt.bit.platformer.controller.Command;
import ru.mipt.bit.platformer.controller.CommandProvider;
import ru.mipt.bit.platformer.controller.GdxKeyboard;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.controller.MoveCommand;
import ru.mipt.bit.platformer.controller.RandomTankBehaviour;
import ru.mipt.bit.platformer.controller.ShootCommand;
import ru.mipt.bit.platformer.controller.ToggleCommand;
import ru.mipt.bit.platformer.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.TextureGraphicsFactory;
import ru.mipt.bit.platformer.graphics.Renderer;
import ru.mipt.bit.platformer.model.BoundedLevelSource;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.FileLevelSource;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.RandomLevelSource;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Combatant;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private static final int RANDOM_AI_TANK_COUNT = 4;
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";
    private static final String BULLET_TEXTURE_PATH = "images/brick.png";
    private static final Color BACKGROUND_COLOR = new Color(0f, 0f, 0.2f, 1f);
    private static final Interpolation MOVEMENT_INTERPOLATION = Interpolation.smooth;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private final LevelSource levelSource;

    private final List<CommandProvider> commandProviders = new ArrayList<>();

    private Renderer renderer;
    private Level level;

    public GameDesktopLauncher(LevelSource levelSource) {
        this.levelSource = levelSource;
    }

    @Override
    public void create() {
        TiledMap map = new TmxMapLoader().load(LEVEL_MAP_PATH);
        TiledMapTileLayer groundLayer = getSingleLayer(map);
        TileMovement tileMovement = new TileMovement(groundLayer, MOVEMENT_INTERPOLATION);

        TextureGraphicsFactory graphicsFactory = new TextureGraphicsFactory()
                .register(Tank.class, TANK_TEXTURE_PATH)
                .register(Tree.class, TREE_TEXTURE_PATH)
                .register(Bullet.class, BULLET_TEXTURE_PATH);
        LevelGraphics levelGraphics = new LevelGraphics(graphicsFactory, new HealthBarDecorator());
        renderer = new LevelRenderer(new SpriteBatch(), map, tileMovement, BACKGROUND_COLOR, levelGraphics);

        level = new BoundedLevelSource(levelSource, groundLayer.getWidth(), groundLayer.getHeight()).load();
        // the graphics learn about the objects already placed and about every later change
        level.addListener(levelGraphics);

        commandProviders.add(createInputController(level.getPlayer(), levelGraphics));
        commandProviders.add(createAi());
    }

    private InputController createInputController(Combatant player, LevelGraphics levelGraphics) {
        InputController controller = new InputController(new GdxKeyboard());
        registerMovement(controller, player, Direction.UP, UP, W);
        registerMovement(controller, player, Direction.LEFT, LEFT, A);
        registerMovement(controller, player, Direction.DOWN, DOWN, S);
        registerMovement(controller, player, Direction.RIGHT, RIGHT, D);
        controller.registerCommand(SPACE, new ShootCommand(player, level));
        controller.registerOnPress(L, new ToggleCommand(levelGraphics::toggleHealthBars));
        return controller;
    }

    private AiCommandProvider createAi() {
        List<Combatant> aiTanks = new ArrayList<>(level.getCombatants());
        aiTanks.remove(level.getPlayer());
        return new AiCommandProvider(aiTanks, new RandomTankBehaviour(), level, level);
    }

    private void registerMovement(InputController controller, Combatant player, Direction direction, int... keys) {
        MoveCommand command = new MoveCommand(player, direction, level);
        for (int key : keys) {
            controller.registerCommand(key, command);
        }
    }

    @Override
    public void render() {
        // 1. collect the commands of this frame: from the keyboard and from AI
        Collection<Command> commands = new ArrayList<>();
        for (CommandProvider provider : commandProviders) {
            commands.addAll(provider.commands());
        }

        // 2. execute them
        for (Command command : commands) {
            command.execute();
        }

        // 3. let every object of the level live through the time passed
        level.update(Gdx.graphics.getDeltaTime());

        // 4. draw the level
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
            return new RandomLevelSource(LEVEL_WIDTH, LEVEL_HEIGHT, RANDOM_TREE_COUNT, RANDOM_AI_TANK_COUNT);
        }
        return new FileLevelSource(LEVEL_LAYOUT_PATH);
    }
}
