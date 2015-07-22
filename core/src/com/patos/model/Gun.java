package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.patos.MainGame;

/**
 * Created by steve on 21/07/2015.
 */
public class Gun extends Actor{
    private boolean isShooting=false;
    private float x=0;
    private float y=0;
    private Vector3 touchPos;

    public Gun() {
        super();
        touchPos = new Vector3();
        setWidth(MainGame.objectAtlas.findRegion("rifle").getRegionWidth());
        setHeight(MainGame.objectAtlas.findRegion("rifle").getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
        setTouchable(Touchable.enabled);
        addListener(createInputListener());
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
                x= touchPos.x;
                y=touchPos.y;
            }
        };
        return listener;
    }

    public void setPosition(float x, float y){
        this.x=x;
        this.y=y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(isShooting){
            batch.draw(MainGame.objectAtlas.findRegion("rifle_red"), x, y);
        }
        else{
            batch.draw(MainGame.objectAtlas.findRegion("rifle"), x, y);
        }
    }

    @Override
    public Actor hit(float arg0, float arg1, boolean flag){
        return this;
    }

}
