package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.BulletRegistry;
import ru.mipt.bit.platformer.model.Combatant;
import ru.mipt.bit.platformer.model.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Asks the behaviour what every unit under AI control should do this frame. */
public class AiCommandProvider implements CommandProvider {

    private final List<Combatant> tanks;
    private final TankBehaviour behaviour;
    private final Field field;
    private final BulletRegistry registry;

    public AiCommandProvider(Collection<? extends Combatant> tanks, TankBehaviour behaviour,
                             Field field, BulletRegistry registry) {
        this.tanks = new ArrayList<>(tanks);
        this.behaviour = behaviour;
        this.field = field;
        this.registry = registry;
    }

    @Override
    public Collection<Command> commands() {
        Collection<Command> commands = new ArrayList<>();
        for (Combatant tank : tanks) {
            commands.add(behaviour.decide(tank, field, registry));
        }
        return commands;
    }
}
