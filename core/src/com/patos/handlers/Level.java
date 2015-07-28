package com.patos.handlers;

import com.badlogic.gdx.utils.Array;

/**
 * Created by steve on 27/07/2015.
 */
public class Level {
    public boolean levelPassed;
    public boolean levelEnabled;
    public int maxPoints;
    public int currentPoints;
    public int stars;
    public int time;
    public Array<LevelTarget> targets;
}

