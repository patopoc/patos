package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.patos.MainGame;
import com.patos.controller.DuckController;
import com.patos.handlers.MoveToSin;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import sun.applet.Main;

/**
 * Created by steve on 21/07/2015.
 */
public class Duck extends Group{
    public boolean badDuck=false;
    public DuckType type;
    public boolean shot=false;
    private ShapeRenderer renderer;
    private  Image duckImage;
    private Image stick;
    private Vector2 duckStageCoord;

    private Rectangle bounds;

    public enum DuckType{
        Brown(6, 10), Yellow(6, 10), Colored(6, 10);

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
        renderer= new ShapeRenderer();
        this.badDuck= isBad;
        this.type= type;
        duckStageCoord= new Vector2();
        stick= new Image(MainGame.objectAtlas.findRegion("stick_wood_outline"));

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

        setSize(duckImage.getWidth(), duckImage.getHeight() + stick.getHeight());
        addActor(stick);
        addActor(duckImage);

        duckImage.setPosition(getWidth() / 2 - duckImage.getWidth() / 2, stick.getHeight() - 5);
        stick.setPosition(getWidth() / 2 - stick.getWidth() / 2, 0);
        bounds= new Rectangle(duckImage.getX(), duckImage.getY(), duckImage.getWidth(), duckImage.getHeight());

        MoveToSin moveToSin= new MoveToSin();
        moveToSin.setPosition(MainGame.worldWidth+duckImage.getWidth(), 0);
        moveToSin.setDuration(type.getSpeed());
        moveToSin.setPeriod(1f);
        moveToSin.setRange(50f);
        moveToSin.setStartPosition(0, getY());
        addAction(Actions.sequence(moveToSin, Actions.run(new Runnable() {
            @Override
            public void run() {
                //removeDuck();
            }
        })));

    }

    private void removeDuck(){
        this.remove();
    }

    public void killDuck(Bullet.BulletType bulletType, float shotX, float shotY){
        Image shot=null;
        switch (type){
            case Brown:
                if(bulletType == Bullet.BulletType.Small)
                    shot= new Image(MainGame.objectAtlas.findRegion("shot_brown_small"));
                else if(bulletType == Bullet.BulletType.Large)
                    shot= new Image(MainGame.objectAtlas.findRegion("shot_brown_large"));
                break;
            case Yellow:
                if(bulletType == Bullet.BulletType.Small)
                    shot= new Image(MainGame.objectAtlas.findRegion("shot_yellow_small"));
                else if(bulletType == Bullet.BulletType.Large)
                    shot= new Image(MainGame.objectAtlas.findRegion("shot_yellow_large"));
                break;
            case Colored:
                if(bulletType == Bullet.BulletType.Small)
                    shot= new Image(MainGame.objectAtlas.findRegion("shot_grey_small"));
                else if(bulletType == Bullet.BulletType.Large)
                    shot= new Image(MainGame.objectAtlas.findRegion("shot_grey_large"));
                break;
        }
        //add shot to specified position
        addActor(shot);
        Vector2 coord= new Vector2(shotX, shotY);
        stageToLocalCoordinates(coord);
        shot.setPosition(coord.x, coord.y);

        // play killed sound and add points

    }

    public Rectangle getBounds(){
        return bounds;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);

        duckImage.localToStageCoordinates(duckStageCoord);
        bounds.setPosition(duckStageCoord.x, duckStageCoord.y);
        duckStageCoord.set(0,0);

        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 1, 0, 1);
        renderer.rect(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight());
        renderer.end();
        batch.begin();
    }

}
