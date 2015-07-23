package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.patos.MainGame;

/**
 * Created by steve on 21/07/2015.
 */
public class Crosshair extends Actor{
    public boolean isShooting=false;
    private float x=0;
    private float y=0;
    private Vector3 touchPos;
    private float shotDuration;
    private float time=0;
    private Rectangle bounds;
    ShapeRenderer renderer;

    public Crosshair(float shotDuration) {
        super();
        this.shotDuration= shotDuration;
        renderer= new ShapeRenderer();
        touchPos = new Vector3();
        setWidth(MainGame.hudAtlas.findRegion("crosshair_outline_large").getRegionWidth());
        setHeight(MainGame.hudAtlas.findRegion("crosshair_outline_large").getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
        bounds=new Rectangle(getX(), getY(), getWidth(), getHeight());
        //setTouchable(Touchable.enabled);
        //addListener(createInputListener());
    }

    public InputListener createInputListener(){
        InputListener listener= new InputListener(){

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            public void touchDragged(InputEvent event, float touchX, float touchY, int pointer){
                touchPos.x= touchX;
                touchPos.y= touchY;
                touchPos.z=0;
                //MainGame.mainCam.unproject(touchPos);
                x= touchPos.x - getWidth()/2;
                y=touchPos.y - getHeight()/2;
            }
        };
        return listener;
    }

    public void setPosition(float x, float y){
        this.x=x;
        this.y=y;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void shoot(){
        isShooting=true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        time += Gdx.graphics.getDeltaTime();
        bounds.setPosition(x, y);
        if(isShooting && time <= shotDuration){
            batch.draw(MainGame.hudAtlas.findRegion("crosshair_red_small"), x, y);
        }
        else{
            time=0;
            isShooting=false;
            batch.draw(MainGame.hudAtlas.findRegion("crosshair_outline_small"), x, y);
        }
        batch.end();
        renderer.setProjectionMatrix(MainGame.stage.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(1, 0, 0, 1);
        renderer.rect(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight());
        renderer.end();
        batch.begin();
    }

    //@Override
    //public Actor hit(float arg0, float arg1, boolean flag){
    //    return this;
    //}

}
