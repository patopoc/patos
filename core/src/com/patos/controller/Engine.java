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

    public LevelManager levelManager;
    private GamePlayController gamePlayController;
    private MenuController menuController;
    private LevelsController levelsController;
    private int ducksNum;
    private float duckIntervalSec;
    private int targetsNum;
    private int targetPositions;
    private float shotDuration;

    public static int score=0;


    public Engine(int ducksNum, float duckIntervalSec, int targetsNum, int targetPositions, float shotDuration){
        levelManager= new LevelManager();
        this.ducksNum= ducksNum;
        this.duckIntervalSec= duckIntervalSec;
        this.targetsNum= targetsNum;
        this.targetPositions= targetPositions;
        this.shotDuration= shotDuration;
        menuController= new MenuController(this);
        menuController.setPosition(MainGame.worldWidth/2 - menuController.getWidth()/2,
                                    -menuController.getHeight(),
                                    MainGame.worldWidth/2 - menuController.getWidth()/2,
                                    MainGame.worldHeight/2 - menuController.getHeight()/2);
        MainGame.stage.addActor(menuController);

    }

    public void show(String controllerName){
        if(controllerName.equals("gamePlay")){
            Gdx.app.log("current level",""+levelManager.getCurrentLevel());
            gamePlayController= new GamePlayController(ducksNum, duckIntervalSec,targetsNum,targetPositions,shotDuration);
            gamePlayController.isActive=true;
            MainGame.stage.addActor(gamePlayController);
        }
        else if(controllerName.equals("levels")){
            levelsController= new LevelsController(this, "text", "text","text_cross");
            levelsController.createLevelsGrid(3, 400, 400);
            levelsController.setPosition(MainGame.worldWidth/2 - levelsController.getWidth()/2,
                                         -levelsController.getHeight(),
                                         MainGame.worldWidth/2 - levelsController.getWidth()/2,
                                         MainGame.worldHeight/2 - levelsController.getHeight()/2);

            MainGame.stage.addActor(levelsController);
        }
    }

    public void update(float delta){
        if(gamePlayController != null && gamePlayController.isActive)
            gamePlayController.update(delta);
    }

}
