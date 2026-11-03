package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.BulletRegistry;
import ru.mipt.bit.platformer.model.Combatant;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiCommandProviderTest {

    private static final Field EVERYTHING_IS_FREE = coordinates -> true;
    private static final BulletRegistry NOWHERE = bullet -> { };

    @Test
    void asksBehaviourAboutEveryTank() {
        Tank first = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        Tank second = new Tank(new GridPoint2(5, 5), Direction.RIGHT);
        RecordingBehaviour behaviour = new RecordingBehaviour();

        Collection<Command> commands =
                new AiCommandProvider(asList(first, second), behaviour, EVERYTHING_IS_FREE, NOWHERE).commands();

        assertEquals(2, commands.size());
        assertEquals(asList(first, second), behaviour.asked);
    }

    @Test
    void producesNothingWhenThereAreNoAiTanks() {
        Collection<Command> commands =
                new AiCommandProvider(emptyList(), new RecordingBehaviour(), EVERYTHING_IS_FREE, NOWHERE).commands();

        assertTrue(commands.isEmpty());
    }

    @Test
    void passesTheFieldToTheBehaviour() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        RecordingBehaviour behaviour = new RecordingBehaviour();

        new AiCommandProvider(asList(tank), behaviour, EVERYTHING_IS_FREE, NOWHERE).commands();

        assertEquals(EVERYTHING_IS_FREE, behaviour.lastField);
    }

    private static class RecordingBehaviour implements TankBehaviour {

        private final List<Combatant> asked = new ArrayList<>();
        private Field lastField;

        @Override
        public Command decide(Combatant tank, Field field, BulletRegistry registry) {
            asked.add(tank);
            lastField = field;
            return new MoveCommand(tank, Direction.UP, field);
        }
    }
}
