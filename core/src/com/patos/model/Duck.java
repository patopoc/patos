package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.patos.MainGame;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import sun.applet.Main;

/**
 * Created by steve on 21/07/2015.
 */
public class Duck extends Group{
    public boolean badDuck=false;
    public DuckType type;
    public boolean shot=false;

    public enum DuckType{
        Brown(6, 10), Yellow(4, 20), Colored(3, 30);

        float speed;
        int points;

        DuckType(float speed, int points){
            this.speed= speed;
            this.points= points;
        }

        float getSpeed(){
            return speed;
        }

        float getPoints(){
            return points;
        }
    }

    public Duck(DuckType type, boolean isBad){
        this.badDuck= isBad;
        this.type= type;
        Image stick= new Image(MainGame.objectAtlas.findRegion("stick_wood_outline"));
        Image duckImage=null;
        switch (type){
            case Brown:
                if(isBad){
                    duckImage = new Image(MainGame.objectAtlas.findRegion("duck_outline_target_brown"));
                }
                else {
                    duckImage = new Image(MainGame.objectAtlas.findRegion("duck_outline_brown"));
                }
                break;
            case Yellow:
                if(isBad){
                    duckImage = new Image(MainGame.objectAtlas.findRegion("duck_outline_target_yellow"));
                }
                else {
                    duckImage = new Image(MainGame.objectAtlas.findRegion("duck_outline_yellow"));
                }
                break;
            case Colored:
                if(isBad){
                    duckImage = new Image(MainGame.objectAtlas.findRegion("duck_outline_target_white"));
                }
                else {
                    duckImage = new Image(MainGame.objectAtlas.findRegion("duck_outline_white"));
                }
                break;
        }
        addActor(stick);
        addActor(duckImage);

        duckImage.setPosition(getWidth() / 2 - duckImage.getWidth() / 2, stick.getHeight() - 5);
        stick.setPosition(getWidth() / 2 - stick.getWidth() / 2, 0);

        addAction(Actions.parallel(
                Actions.moveTo(MainGame.worldWidth, 0, type.getSpeed()),
                Actions.forever(Actions.sequence(Actions.moveBy(0, 100, 1f),
                        Actions.moveBy(0, -100, 1f)))));
        MoveToAction ac= new MoveToAction();

    }
}
