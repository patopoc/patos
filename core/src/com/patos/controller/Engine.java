package com.patos.controller;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by steve on 21/07/2015.
 */
public class Engine {

    private DuckController duckController;
    private TargetController targetController;
    private float intervalSeconds;


    public Engine(int ducksNum, float duckIntervalSec, int targetsNum, int targetPositions){
        duckController= new DuckController(ducksNum);
        targetController= new TargetController(targetsNum, targetPositions);
        intervalSeconds= duckIntervalSec;
    }

    public void update(float delta){
        duckController.spawnDuck(delta, intervalSeconds);
        targetController.spawnTarget(delta, intervalSeconds);
    }
}
