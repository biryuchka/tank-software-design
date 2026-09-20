package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Gdx;

public class GdxKeyboard implements Keyboard {

    @Override
    public boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }
}
