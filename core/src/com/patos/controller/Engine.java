package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.patos.MainGame;
import com.patos.model.Gun;

import sun.applet.Main;

/**
 * Created by steve on 21/07/2015.
 */
public class Engine {

    private DuckController duckController;
    private TargetController targetController;
    private float intervalSeconds;
    private Gun gun;


    public Engine(int ducksNum, float duckIntervalSec, int targetsNum, int targetPositions){
        duckController= new DuckController(ducksNum);
        targetController= new TargetController(targetsNum, targetPositions);
        intervalSeconds= duckIntervalSec;
        gun= new Gun();
        Gdx.app.log("bounds", gun.getWidth() + " , " + gun.getHeight());
        gun.setPosition(MainGame.worldWidth / 2, 0);
        MainGame.stage.addActor(gun);
    }

    public void update(float delta){
        duckController.spawnDuck(delta, intervalSeconds);
        targetController.spawnTarget(delta, intervalSeconds);
    }
}
