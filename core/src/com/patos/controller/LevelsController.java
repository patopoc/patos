package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.patos.MainGame;
import com.patos.handlers.Level;
import com.patos.handlers.LevelManager;
import com.patos.model.HUDButton;
import com.patos.model.TextFont;

/**
 * Created by steve on 27/07/2015.
 */
public class LevelsController extends Group {

    private Array<HUDButton> levelButtons;
    private Array<Level> levels;
    private Engine engine;
    private boolean isActive=false;
    private String iconEnabledDownRoot;
    private String iconDisabledRoot;
    private String iconEnabledUpRoot;
    private float gridCellWidth;
    private float gridCellHeight;
    private Table container;
    private HUDButton backButton;

    public LevelsController(final Engine engine, String iconEnabledDownRoot, String iconEnabledUpRoot, String iconDisabledRoot){

        backButton= new HUDButton("btn_small_clicked", "btn_small_normal","btn_small_selected","button.mp3");
        backButton.setIcon("icon_replay");
        backButton.setPosition(MainGame.worldWidth - (backButton.getWidth() +20),
                               MainGame.worldHeight - (backButton.getHeight()+20));
        backButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                engine.soundManager.playSound(backButton.getClickSound(),false,1);
                backButton.isPressed=true;
                backButton.setState(HUDButton.State.Clicked);
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                if(isActive) {
                    isActive=false;
                    backButton.isPressed = false;
                    backButton.setState(HUDButton.State.Normal);
                    outAction(new Runnable() {
                        @Override
                        public void run() {
                            engine.show("menu");
                            remove();
                        }
                    });
                }
            }
        });

        addActor(backButton);
        container= new Table();
        if(MainGame.debug)
            container.setDebug(true);

        levels= engine.levelManager.getLevels();
        this.engine= engine;
        this.iconDisabledRoot= iconDisabledRoot;
        this.iconEnabledDownRoot= iconEnabledDownRoot;
        this.iconEnabledUpRoot= iconEnabledUpRoot;
        addActor(container);
        setWidth(MainGame.worldWidth);
        setHeight(MainGame.worldHeight);
    }

    public void createLevelsGrid(int cols, float width, float height){
        int currentCol=0;
        container.setWidth(width);
        container.setHeight(height);
        container.top();

        gridCellWidth= width/cols;
        gridCellHeight=gridCellWidth;

        for(int i=0; i < levels.size; i++){
            final HUDButton levelButton;
            Table levelCellGroup= new Table();
            if(MainGame.debug)
                levelCellGroup.setDebug(true);
            if(levels.get(i).levelEnabled){
                levelButton= new HUDButton(iconEnabledDownRoot, iconEnabledUpRoot,"", i,"button.mp3");
                final int currentLevel=i;

                levelButton.setInputListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.log("boton","down");
                        engine.soundManager.playSound(levelButton.getClickSound(), false, 1);
                        levelButton.setState(HUDButton.State.Clicked);
                        levelButton.isPressed = true;
                        return true;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.log("boton","up");
                        if (isActive) {
                            isActive = false;
                            levelButton.setState(HUDButton.State.Normal);
                            levelButton.isPressed = false;
                            outAction(new Runnable() {
                                @Override
                                public void run() {
                                    engine.levelManager.setCurrentLevel(currentLevel);
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

            levelCellGroup.add(levelButton).pad(5).row();
            createStars(levelCellGroup, levels.get(i).stars);
            TextFont points= new TextFont(TextFont.FontSize.Small);
            points.setText(levels.get(i).currentPoints+"/"+levels.get(i).maxPoints, 0.5f);
            levelCellGroup.add(points).pad(5);

            container.padTop(10);
            container.add(levelCellGroup).expandX();

            currentCol++;
            if(currentCol >= cols) {
                currentCol = 0;
                container.row();
            }
        }

        // set Table pattern background
        container.setBackground(new TiledDrawable(MainGame.hudAtlas.findRegion("black_box")));
        container.setPosition(getWidth()/2 - container.getWidth()/2,
                              getHeight()/2- container.getHeight() /2);
    }

    public void createStars(Table levelCell, int stars){
        Table starsTable= new Table();
        for(int i=1; i<= 3; i++){
            Image star= new Image(new TextureRegionDrawable(MainGame.hudAtlas.findRegion("star_small")));
            if(i > stars){
                star.setColor(0,0,0,0.7f);
            }
            starsTable.add(star);
        }
        levelCell.add(starsTable).pad(5).row();

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
