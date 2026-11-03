package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Movable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Asks the behaviour what every tank under AI control should do. */
public class AiCommandProvider implements CommandProvider {

    private final List<Movable> tanks;
    private final TankBehaviour behaviour;
    private final Field field;

    public AiCommandProvider(Collection<? extends Movable> tanks, TankBehaviour behaviour, Field field) {
        this.tanks = new ArrayList<>(tanks);
        this.behaviour = behaviour;
        this.field = field;
    }

    @Override
    public Collection<Command> commands() {
        Collection<Command> commands = new ArrayList<>();
        for (Movable tank : tanks) {
            commands.add(behaviour.decide(tank, field));
        }
        return commands;
    }
}
