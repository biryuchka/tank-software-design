package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the whole level from a text file: 'T' is a tree, 'X' is the player start
 * position, '_' is an empty tile. The first line of the file is the top row of the field.
 */
public class FileLevelSource implements LevelSource {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final String resourcePath;

    public FileLevelSource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public Level load() {
        return parse(readLines());
    }

    private List<String> readLines() {
        InputStream stream = FileLevelSource.class.getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("Level file is not found: " + resourcePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            List<String> lines = new ArrayList<>();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
            return lines;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read level file: " + resourcePath, e);
        }
    }

    // package-private: lets the tests check the format without touching the file system
    static Level parse(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Level file has no rows");
        }
        int height = lines.size();
        int width = 0;
        for (String line : lines) {
            width = Math.max(width, line.length());
        }

        Level level = new Level(width, height);
        GridPoint2 playerCoordinates = null;
        for (int row = 0; row < height; row++) {
            String line = lines.get(row);
            // the first line of the file is the top row of the field
            int y = height - 1 - row;
            for (int x = 0; x < line.length(); x++) {
                char symbol = line.charAt(x);
                if (symbol == TREE) {
                    level.addObstacle(new Tree(new GridPoint2(x, y)));
                } else if (symbol == PLAYER) {
                    if (playerCoordinates != null) {
                        throw new IllegalArgumentException("Level file has more than one player start position");
                    }
                    playerCoordinates = new GridPoint2(x, y);
                } else if (symbol != EMPTY) {
                    throw new IllegalArgumentException("Unexpected symbol '" + symbol
                            + "' at row " + (row + 1) + ", column " + (x + 1));
                }
            }
        }
        if (playerCoordinates == null) {
            throw new IllegalArgumentException("Level file has no player start position");
        }
        level.addPlayer(new Tank(playerCoordinates, Direction.RIGHT));
        return level;
    }
}
