package com.patos.controller;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by steve on 21/07/2015.
 */
public class Engine {

    private DuckController duckController;
    private float intervalSeconds;


    public Engine(int ducksNum, float duckIntervalSec){
        duckController= new DuckController(ducksNum);
        intervalSeconds= duckIntervalSec;
    }

    public void update(float delta){
        duckController.spawnDuck(delta, intervalSeconds);
    }
}
