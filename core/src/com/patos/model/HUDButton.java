package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    private String clickSound;
    private int imageIndex=-1;
    private float x=0;
    private float y=0;

    private ShapeRenderer renderer;

    public HUDButton(String onDownImage, String onUpImage, String onClickSound){
        this(onDownImage,onUpImage);
        clickSound= onClickSound;
    }

    public HUDButton(String onDownImage, String onUpImage, int imageIndex, String onClickSound){
        this(onDownImage,onUpImage, imageIndex);
        clickSound= onClickSound;
    }

    public HUDButton(String onDownImage, String onUpImage) {
        super();
        renderer= new ShapeRenderer();
        this.onDownImage= onDownImage;
        this.onUpImage= onUpImage;
        setWidth(MainGame.hudAtlas.findRegion(onUpImage).getRegionWidth());
        setHeight(MainGame.hudAtlas.findRegion(onUpImage).getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
        setTouchable(Touchable.enabled);
    }
    public HUDButton(String onDownImage, String onUpImage, int index) {
        super();
        renderer= new ShapeRenderer();
        imageIndex=index;
        this.onDownImage= onDownImage;
        this.onUpImage= onUpImage;
        setWidth(MainGame.hudAtlas.findRegion(onUpImage,index).getRegionWidth());
        setHeight(MainGame.hudAtlas.findRegion(onUpImage,index).getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
        setTouchable(Touchable.enabled);
    }

    public String getClickSound(){
        return clickSound;
    }

    public void setInputListener(InputListener listener){
        addListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(isPressed){
            batch.draw(MainGame.hudAtlas.findRegion(onDownImage), getX(), getY());
        }
        else{
            if(imageIndex != -1)
                batch.draw(MainGame.hudAtlas.findRegion(onUpImage,imageIndex), getX(), getY());
            else
                batch.draw(MainGame.hudAtlas.findRegion(onUpImage), getX(), getY());
        }

        if(MainGame.debug) {
            batch.end();
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(1, 0, 0, 1);
            Vector2 coord= new Vector2();
            localToStageCoordinates(coord);
            renderer.rect(coord.x, coord.y, getWidth(), getHeight());
            renderer.end();
            batch.begin();
        }
    }

}
