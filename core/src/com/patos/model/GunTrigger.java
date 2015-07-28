package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.patos.MainGame;

/**
 * Created by steve on 21/07/2015.
 */
public class GunTrigger extends Actor{
    public boolean isShooting=false;
    private float x=0;
    private float y=0;

    public GunTrigger() {
        super();
        setWidth(MainGame.hudAtlas.findRegion("crosshair_outline_large").getRegionWidth());
        setHeight(MainGame.hudAtlas.findRegion("crosshair_outline_large").getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
        setTouchable(Touchable.enabled);
        addListener(createInputListener());
    }

    public InputListener createInputListener(){
        InputListener listener= new InputListener(){

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                isShooting=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                isShooting=false;
            }

        };
        return listener;
    }

    public void setPosition(float x, float y){
        super.setPosition(x,y);
        this.x=x;
        this.y=y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(isShooting){
            batch.draw(MainGame.hudAtlas.findRegion("crosshair_red_small"), x, y);
        }
        else{
            batch.draw(MainGame.hudAtlas.findRegion("crosshair_outline_small"), x, y);
        }
    }

    //@Override
    //public Actor hit(float arg0, float arg1, boolean flag){
    //    return this;
    //}

}
