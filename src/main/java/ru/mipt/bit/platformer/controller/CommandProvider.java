package ru.mipt.bit.platformer.controller;

import java.util.Collection;

/** Produces the commands to be executed on the current frame: from the keyboard, from AI, ... */
public interface CommandProvider {

    Collection<Command> commands();
}
