package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
    private boolean handlingExternalAction=false;

    public MenuController(final Engine engine){
        playButton= new HUDButton("btn_play_clicked", "btn_play_normal", "btn_play_selected", "button.mp3");
        this.engine= engine;
        playButton.setInputListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                engine.soundManager.playSound(playButton.getClickSound(),false,1);
                playButton.isPressed=true;
                playButton.setState(HUDButton.State.Clicked);
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                if(isActive) {
                    isActive=false;
                    playButton.isPressed = false;
                    playButton.setState(HUDButton.State.Normal);
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
        setSize(playButton.getWidth(), playButton.getHeight());
        playButton.setTouchable(Touchable.disabled);
        addActor(playButton);
    }

    public void setPosition(float startX, float startY, float endX, float endY){
        setPosition(startX, startY);
        inAction(endX, endY, new Runnable() {
            @Override
            public void run() {
                isActive = true;
                playButton.setTouchable(Touchable.enabled);
            }
        });

    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(MainGame.selectAction && !handlingExternalAction){
            MainGame.selectAction=false;
            handlingExternalAction=true;
            outAction(new Runnable() {
                @Override
                public void run() {
                    engine.show("levels");
                    handlingExternalAction=false;
                    remove();
                }
            });
        }
    }
    private void outAction(Runnable action){
        addAction(Actions.sequence(Actions.moveTo(getX(), MainGame.worldHeight + getHeight(), 1f),
                Actions.run(action)));
    }

    private void inAction(float x, float y, Runnable action){
        addAction(Actions.sequence(Actions.delay(1f),Actions.moveTo(x,y,1f),Actions.run(action)));
    }
}
