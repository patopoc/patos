package com.patos.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.patos.MainGame;

/**
 * Created by steve on 21/07/2015.
 */
public class HUDButton extends Actor{
    public boolean isPressed=false;
    private String onDownImage;
    private String onUpImage;
    private float x=0;
    private float y=0;

    public HUDButton(String onDownImage, String onUpImage) {
        super();
        this.onDownImage= onDownImage;
        this.onUpImage= onUpImage;
        setWidth(MainGame.hudAtlas.findRegion(onUpImage).getRegionWidth());
        setHeight(MainGame.hudAtlas.findRegion(onUpImage).getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
        setTouchable(Touchable.enabled);
    }

    public void setInputListener(InputListener listener){
        addListener(listener);
    }

    public void setPosition(float x, float y){
        this.x=x;
        this.y=y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(isPressed){
            batch.draw(MainGame.hudAtlas.findRegion(onDownImage), x, y);
        }
        else{
            batch.draw(MainGame.hudAtlas.findRegion(onUpImage), x, y);
        }
    }

    @Override
    public Actor hit(float arg0, float arg1, boolean flag){
        return this;
    }

}
