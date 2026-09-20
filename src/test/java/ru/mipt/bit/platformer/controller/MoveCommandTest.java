package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Tank;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MoveCommandTest {

    private final Level level = new Level(10, 8, new Tank(new GridPoint2(1, 1), Direction.RIGHT), emptyList());

    @Test
    void movesGivenObjectInGivenDirection() {
        RecordingMovable movable = new RecordingMovable();
        Command command = new MoveCommand(movable, Direction.UP, level);

        command.execute();

        assertEquals(Direction.UP, movable.lastDirection);
        assertSame(level, movable.lastLevel);
    }

    private static class RecordingMovable implements Movable {

        private Direction lastDirection;
        private Level lastLevel;

        @Override
        public void move(Direction direction, Level level) {
            this.lastDirection = direction;
            this.lastLevel = level;
        }

        @Override
        public void update(float deltaTime) {
        }

        @Override
        public boolean isMoving() {
            return false;
        }
    }
}
