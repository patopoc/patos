package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.patos.MainGame;
import com.patos.model.HUDButton;

/**
 * Created by steve on 27/07/2015.
 */
public class MenuController extends Group{

    private HUDButton playButton;
    private Engine engine;
    private boolean isActive=false;

    public MenuController(final Engine engine){
        playButton= new HUDButton("crosshair_outline_large", "crosshair_red_large");
        this.engine= engine;
        playButton.setInputListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                playButton.isPressed=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                if(isActive) {
                    isActive=false;
                    playButton.isPressed = false;
                    outAction(new Runnable() {
                        @Override
                        public void run() {
                            engine.show("levels");
                            remove();
                        }
                    });
                }
            }
        });
        addActor(playButton);
    }

    public void setPosition(float startX, float startY, float endX, float endY){
        setPosition(startX,startY);
        inAction(endX, endY, new Runnable() {
            @Override
            public void run() {
                isActive=true;
            }
        });

    }

    private void outAction(Runnable action){
        addAction(Actions.sequence(Actions.moveTo(getX(), MainGame.worldHeight + getHeight(), 1f),
                Actions.run(action)));
    }

    private void inAction(float x, float y, Runnable action){
        addAction(Actions.sequence(Actions.moveTo(x,y,1f),Actions.run(action)));
    }
}
