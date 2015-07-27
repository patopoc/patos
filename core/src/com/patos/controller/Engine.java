package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.patos.MainGame;
import com.patos.handlers.LevelManager;
import com.patos.model.Bullet;
import com.patos.model.Cartridge;
import com.patos.model.Crosshair;
import com.patos.model.GunTrigger;
import com.patos.model.CounterDisplay;
import com.patos.model.TextFont;

import javax.swing.Action;

/**
 * Created by steve on 21/07/2015.
 */
public class Engine {

    private LevelManager levelManager;
    private GamePlayController gamePlayController;
    private MenuController menuController;

    public static int score=0;


    public Engine(int ducksNum, float duckIntervalSec, int targetsNum, int targetPositions, float shotDuration){
        levelManager= new LevelManager();
        gamePlayController= new GamePlayController(ducksNum, duckIntervalSec,targetsNum,targetPositions,shotDuration);
        menuController= new MenuController(this);
        menuController.setPosition(MainGame.worldWidth/2 - menuController.getWidth()/2,
                                    -menuController.getHeight(),
                                    MainGame.worldWidth/2 - menuController.getWidth()/2,
                                    MainGame.worldHeight/2 - menuController.getHeight()/2);
        MainGame.stage.addActor(menuController);
    }

    public void show(String controllerName){
        if(controllerName.equals("gamePlay")){
            gamePlayController.isActive=true;
            MainGame.stage.addActor(gamePlayController);
        }
    }

    public void update(float delta){
        if(gamePlayController.isActive)
            gamePlayController.update(delta);
    }

}
