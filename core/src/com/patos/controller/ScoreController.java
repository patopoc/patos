package com.patos.controller;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.patos.MainGame;
import com.patos.model.HUDButton;
import com.patos.model.TextFont;

/**
 * Created by steve on 27/07/2015.
 */
public class ScoreController extends Table{

    private Engine engine;
    private boolean isActive=false;

    public ScoreController(final Engine engine){
        this.engine= engine;
        if(MainGame.debug)
            setDebug(true);
        setSize(300, 300);
        Image scoreTitle= new Image(MainGame.hudAtlas.findRegion("text_score"));
        add(scoreTitle);
        row();
        TextFont score= new TextFont(TextFont.FontSize.Small);
        score.setText("" + Engine.score + "/" + engine.levelManager.getLevels().
                get(engine.levelManager.getCurrentLevel()).maxPoints);
        add(score);
        row();

        final HUDButton playButton= new HUDButton("crosshair_outline_large", "crosshair_red_large");
        playButton.setInputListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playButton.isPressed = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isActive) {
                    isActive = false;
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
        add(playButton);
        playButton.setPosition(0,0);
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
