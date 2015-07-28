package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.patos.MainGame;
import com.patos.handlers.Level;
import com.patos.handlers.LevelManager;
import com.patos.model.HUDButton;

/**
 * Created by steve on 27/07/2015.
 */
public class LevelsController extends Group{

    private Array<HUDButton> levelButtons;
    private Array<Level> levels;
    private Engine engine;
    private boolean isActive=false;
    private String iconEnabledDownRoot;
    private String iconDisabledRoot;
    private String iconEnabledUpRoot;
    private float gridCellWidth;
    private float gridCellHeight;

    public LevelsController(final Engine engine, String iconEnabledDownRoot, String iconEnabledUpRoot, String iconDisabledRoot){
        levels= engine.levelManager.getLevels();
        this.engine= engine;
        this.iconDisabledRoot= iconDisabledRoot;
        this.iconEnabledDownRoot= iconEnabledDownRoot;
        this.iconEnabledUpRoot= iconEnabledUpRoot;
    }

    public void createLevelsGrid(int cols, float width, float height){
        int currentCol=0;
        float currentRowY= height;
        setWidth(width);
        setHeight(height);

        gridCellWidth= width/cols;
        gridCellHeight=gridCellWidth;

        for(int i=0; i < levels.size; i++){
            final HUDButton levelButton;
            if(levels.get(i).levelEnabled){
                levelButton= new HUDButton(iconEnabledDownRoot, iconEnabledUpRoot, i);
                final int currentLevel=i;

                levelButton.setInputListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.log("boton","down");

                        levelButton.isPressed = true;
                        return true;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.log("boton","up");
                        if (isActive) {
                            isActive = false;
                            levelButton.isPressed = false;
                            outAction(new Runnable() {
                                @Override
                                public void run() {
                                    engine.levelManager.setCurrentLevel(currentLevel+1);
                                    engine.show("gamePlay");
                                    remove();
                                }
                            });
                        }
                    }
                });
            }
            else{
                levelButton= new HUDButton(iconDisabledRoot, iconDisabledRoot);

            }

            addActor(levelButton);

            levelButton.setPosition(currentCol * gridCellWidth, currentRowY);
            currentCol++;
            if(currentCol >= cols) {
                currentCol = 0;
                currentRowY -= gridCellHeight;
            }
        }
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
        addAction(Actions.sequence(Actions.moveTo(x, y, 1f), Actions.run(action)));
    }
}
