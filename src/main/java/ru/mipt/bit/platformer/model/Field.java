package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** The level as a moving object sees it: the only thing it needs to know is where it may go. */
public interface Field {

    boolean isFree(GridPoint2 coordinates);
}
