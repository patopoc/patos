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
import com.patos.handlers.SoundManager;
import com.patos.model.Bullet;
import com.patos.model.Cartridge;
import com.patos.model.Crosshair;
import com.patos.model.GunTrigger;
import com.patos.model.CounterDisplay;
import com.patos.model.TextFont;

import java.security.acl.Group;

import javax.swing.Action;

/**
 * Created by steve on 21/07/2015.
 */
public class Engine {

    public static boolean pause=false;
    public LevelManager levelManager;
    public SoundManager soundManager;

    private GamePlayController gamePlayController;  //this controller is global because it needs to run on update
    private MenuController menuController;
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
        soundManager= new SoundManager(this);
        soundManager.loadMusic("music.mp3");
        soundManager.playMusic(true, 0.7f);
        show("menu");

    }

    public void show(String controllerName){

        if(controllerName.equals("gamePlay")){
            gamePlayController= new GamePlayController(this, duckIntervalSec, targetPositions,shotDuration);
            gamePlayController.isActive=true;
            MainGame.stage.addActor(gamePlayController);
        }
        else if(controllerName.equals("menu")){
            menuController= new MenuController(this);
            menuController.setPosition(MainGame.worldWidth/2 - menuController.getWidth()/2,
                    -menuController.getHeight(),
                    MainGame.worldWidth/2 - menuController.getWidth()/2,
                    MainGame.worldHeight/2 - menuController.getHeight()/2);
            MainGame.stage.addActor(menuController);
        }
        else if(controllerName.equals("levels")){
            LevelsController levelsController= new LevelsController(this, "level", "level","level_locked");
            levelsController.createLevelsGrid(3, 500, 400);
            levelsController.setPosition(MainGame.worldWidth/2 - levelsController.getWidth()/2,
                                         -levelsController.getHeight(),
                                         MainGame.worldWidth/2 - levelsController.getWidth()/2,
                                         MainGame.worldHeight/2 - levelsController.getHeight()/2);

            MainGame.stage.addActor(levelsController);
        }
        else if(controllerName.equals("score")){
            ScoreController scoreController = new ScoreController(this);
            scoreController.setPosition(MainGame.worldWidth/2 - scoreController.getWidth()/2,
                                        -scoreController.getHeight(),
                                        MainGame.worldWidth/2 - scoreController.getWidth()/2,
                                        MainGame.worldHeight/2- scoreController.getHeight()/2);

            MainGame.stage.addActor(scoreController);
        }
    }

    public void update(float delta){
        if(gamePlayController != null && gamePlayController.isActive)
            gamePlayController.update(delta);
    }

}
