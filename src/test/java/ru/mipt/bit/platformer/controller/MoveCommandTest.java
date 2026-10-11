package ru.mipt.bit.platformer.controller;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Movable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MoveCommandTest {

    private final Field field = coordinates -> true;

    @Test
    void movesGivenObjectInGivenDirection() {
        RecordingMovable movable = new RecordingMovable();
        Command command = new MoveCommand(movable, Direction.UP, field);

        command.execute();

        assertEquals(Direction.UP, movable.lastDirection);
        assertSame(field, movable.lastField);
    }

    private static class RecordingMovable implements Movable {

        private Direction lastDirection;
        private Field lastField;

        @Override
        public GridPoint2 getCoordinates() {
            return new GridPoint2();
        }

        @Override
        public void move(Direction direction, Field field) {
            this.lastDirection = direction;
            this.lastField = field;
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
