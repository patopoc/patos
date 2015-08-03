package com.patos.controller;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.patos.MainGame;
import com.patos.model.HUDButton;
import com.patos.model.TextFont;

/**
 * Created by steve on 27/07/2015.
 */
public class ScoreController extends Group{

    private Engine engine;
    private boolean isActive=false;
    private Table container;

    public ScoreController(final Engine engine){
        this.engine= engine;
        container=new Table();
        setSize(MainGame.worldWidth, MainGame.worldHeight);
        addActor(container);

        if(MainGame.debug)
            container.setDebug(true);

        container.setSize(300, 300);
        container.setPosition(getWidth() / 2 - container.getWidth() / 2,
                getHeight() / 2 - container.getHeight() / 2);
        Image scoreTitle= new Image(MainGame.hudAtlas.findRegion("text_score"));
        container.add(scoreTitle).padBottom(10);
        container.row();
        TextFont score= new TextFont(TextFont.FontSize.Small);
        score.setText("" + Engine.score + "/" + engine.levelManager.getLevels().
                get(engine.levelManager.getCurrentLevel()).maxPoints);
        container.add(score).padBottom(10);
        container.row();

        final HUDButton levelsButton= new HUDButton("btn_small_clicked","btn_small_normal","btn_small_selected", "button.mp3");
        levelsButton.setIcon("icon_levels");
        levelsButton.setInputListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                levelsButton.setState(HUDButton.State.Clicked);
                levelsButton.isPressed = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isActive) {
                    isActive = false;
                    levelsButton.setState(HUDButton.State.Normal);
                    levelsButton.isPressed = false;
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

        final HUDButton replayButton= new HUDButton("btn_small_clicked","btn_small_normal","btn_small_selected", "button.mp3");
        replayButton.setIcon("icon_replay");
        replayButton.setInputListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                replayButton.setState(HUDButton.State.Clicked);
                replayButton.isPressed = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isActive) {
                    isActive = false;
                    replayButton.setState(HUDButton.State.Normal);
                    replayButton.isPressed = false;
                    outAction(new Runnable() {
                        @Override
                        public void run() {
                            engine.show("gamePlay");
                            remove();
                        }
                    });
                }
            }
        });
        Table starContainer= new Table();
        createStars(starContainer,
                engine.levelManager.getLevels().get(engine.levelManager.getCurrentLevel()).stars);
        container.add(starContainer).row();

        Table buttonContainer= new Table();
        buttonContainer.add(levelsButton).pad(10);
        buttonContainer.add(replayButton).pad(10);
        container.add(buttonContainer).row();

        container.setBackground(new TiledDrawable(MainGame.hudAtlas.findRegion("black_box")));
        container.setPosition(getWidth() / 2 - container.getWidth() / 2,
                getHeight() / 2 - container.getHeight() / 2);
    }

    public void createStars(Table levelCell, int stars){
        Table starsTable= new Table();
        for(int i=1; i<= 3; i++){
            Image star= new Image(new TextureRegionDrawable(MainGame.hudAtlas.findRegion("star_large")));
            if(i > stars){
                star.setColor(0,0,0,0.7f);
            }
            starsTable.add(star).padLeft(5).padRight(5);
        }
        levelCell.add(starsTable);

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
        int stars= engine.levelManager.getLevel(engine.levelManager.getCurrentLevel()).stars;
        if(stars == 0 )
            engine.soundManager.playSound("laughter_man.mp3",false, 1);
        else if(stars > 0 && stars < 3)
            engine.soundManager.playSound("score_applause.mp3",false, 1);
        else if(stars == 3){
            engine.soundManager.playSound("cheering.mp3",false, 1);
        }
        addAction(Actions.sequence(Actions.moveTo(x,y,1f),Actions.run(action)));
    }
}
