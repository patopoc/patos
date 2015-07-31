package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.patos.MainGame;
import com.patos.controller.Engine;
import com.patos.handlers.MoveToSin;

/**
 * Created by steve on 21/07/2015.
 */
public class Target extends Group{
    //public boolean badTarget=false;
    public TargetType type;
    public boolean shot=false;
    private ShapeRenderer renderer;
    private  Image targetImage;
    private Image stick;
    private Vector2 targetStageCoord;

    private Rectangle bounds;

    //public Target(TargetType type, boolean isBad){
    public Target(TargetType type, String groupLayer){
        renderer= new ShapeRenderer();
        //this.badTarget= isBad;
        this.type= type;
        targetStageCoord= new Vector2();
        targetImage = new Image(MainGame.objectAtlas.findRegion(type.getImageName()));

        if(groupLayer.equals("duck")) {
            stick = new Image(MainGame.objectAtlas.findRegion("stick_wood_outline"));

            MoveToSin moveToSin= new MoveToSin();
            moveToSin.setPosition(MainGame.worldWidth+targetImage.getWidth(), 0);
            moveToSin.setDuration(type.getSpeed());
            moveToSin.setPeriod(1f);
            moveToSin.setRange(50f);
            moveToSin.setStartPosition(0, 0);
            addAction(Actions.sequence(moveToSin, Actions.run(new Runnable() {
                @Override
                public void run() {
                    //removeTarget();
                }
            })));
        }
        else if(groupLayer.equals("board")) {
            stick = new Image(MainGame.objectAtlas.findRegion("stick_metal_outline"));
            targetImage.setSize(targetImage.getWidth() / 2, targetImage.getHeight() / 2);

            addAction(Actions.sequence(Actions.moveBy(0,150,1f),Actions.delay(1), Actions.moveBy(0, -150, 1f),Actions.run(new Runnable() {
                @Override
                public void run() {
                    remove();
                }
            })));
        }

        //Gdx.app.log("Target setSize",""+targetImage+" || "+stick);
        setSize(targetImage.getWidth(), targetImage.getHeight() + stick.getHeight());
        addActor(stick);
        addActor(targetImage);

        targetImage.setPosition(getWidth() / 2 - targetImage.getWidth() / 2, stick.getHeight() - 5);
        stick.setPosition(getWidth() / 2 - stick.getWidth() / 2, 0);
        bounds= new Rectangle(targetImage.getX(), targetImage.getY(), targetImage.getWidth(), targetImage.getHeight());

    }

    private void removeTarget(){
        this.remove();
    }

    public void killTarget(Bullet.BulletType bulletType, float shotX, float shotY){
        Image shot=null;
            if(bulletType == Bullet.BulletType.Small)
                shot= new Image(MainGame.objectAtlas.findRegion("shot_grey_small"));
            else if(bulletType == Bullet.BulletType.Large)
                shot= new Image(MainGame.objectAtlas.findRegion("shot_grey_large"));

        //add shot to specified position
        addActor(shot);
        Vector2 coord= new Vector2(shotX, shotY);
        stageToLocalCoordinates(coord);
        shot.setPosition(coord.x, coord.y);

        final TextFont pointsTag = new TextFont(TextFont.FontSize.Small);
        pointsTag.setPosition(coord.x, getHeight() - 20);

        if(type.isBad()) {
            pointsTag.setText("+" + type.getPoints());
        }
        else{
            pointsTag.setText("-" + type.getPoints(),"FF0000FF");
        }

        pointsTag.setRotation(10f);
        pointsTag.addAction(
                Actions.sequence(Actions.parallel(Actions.moveBy(0, 50, 1f)
                        , Actions.fadeOut(1f))
                        , Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        pointsTag.remove();
                    }
                })));
        addActor(pointsTag);

        // play killed sound and add points

    }

    public Rectangle getBounds(){
        return bounds;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);

        targetImage.localToStageCoordinates(targetStageCoord);
        bounds.setPosition(targetStageCoord.x, targetStageCoord.y);
        targetStageCoord.set(0, 0);

        if(MainGame.debug) {
            batch.end();
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(0, 1, 0, 1);
            renderer.rect(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight());
            renderer.end();
            batch.begin();
        }
    }

    @Override
    public void act(float delta){
        if(!Engine.pause)
            super.act(delta);
    }

}
