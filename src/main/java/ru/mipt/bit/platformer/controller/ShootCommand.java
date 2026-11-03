package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.BulletRegistry;
import ru.mipt.bit.platformer.model.Shooter;

public class ShootCommand implements Command {

    private final Shooter shooter;
    private final BulletRegistry registry;

    public ShootCommand(Shooter shooter, BulletRegistry registry) {
        this.shooter = shooter;
        this.registry = registry;
    }

    @Override
    public void execute() {
        shooter.shoot(registry);
    }
}
