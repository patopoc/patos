package com.patos.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.patos.MainGame;
import com.patos.controller.TargetController;

/**
 * Created by steve on 21/07/2015.
 */
public class Target extends Group{

    public TargetType type;
    public boolean shot=false;
    private ShapeRenderer renderer;
    private Vector2 targetStageCoord;
    Image targetImage=null;

    private Rectangle bounds;

    public enum TargetType{
        Red(6, 40), Colored(3, 30);

        float speed;
        int points;

        TargetType(float speed, int points){
            this.speed= speed;
            this.points= points;
        }

        public float getSpeed(){
            return speed;
        }

        public int getPoints(){
            return points;
        }
    }

    public Target(TargetType type){
        renderer= new ShapeRenderer();
        targetStageCoord= new Vector2();
        this.type= type;
        Image stick= new Image(MainGame.objectAtlas.findRegion("stick_metal_outline"));
        switch (type){
            case Red:
                targetImage = new Image(MainGame.objectAtlas.findRegion("target_red1_outline"));
                break;
            case Colored:
                targetImage = new Image(MainGame.objectAtlas.findRegion("target_colored_outline"));
                break;
        }
        addActor(stick);
        addActor(targetImage);
        setSize(targetImage.getWidth(), targetImage.getHeight() + stick.getHeight());

        targetImage.setSize(targetImage.getWidth() / 2, targetImage.getHeight() / 2);
        targetImage.setPosition(getWidth() / 2 - targetImage.getWidth() / 2, stick.getHeight() - 5);
        stick.setPosition(getWidth() / 2 - stick.getWidth() / 2, 0);
        bounds= new Rectangle(targetImage.getX(), targetImage.getY(), targetImage.getWidth(), targetImage.getHeight());



        addAction(Actions.sequence(Actions.moveBy(0,150,1f),Actions.delay(1), Actions.moveBy(0, -150, 1f),Actions.run(new Runnable() {
            @Override
            public void run() {
                removeActor();
            }
        })));

    }

    private void removeActor(){
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
        pointsTag.setPosition(coord.x, coord.y);
        pointsTag.setText("+" + type.getPoints());
        pointsTag.setRotation(10f);
        pointsTag.addAction(
                Actions.sequence(Actions.parallel(Actions.moveBy(0, 70, 1f)
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
}
