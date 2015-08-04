package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.patos.MainGame;

/**
 * Created by steve on 21/07/2015.
 */
public class HUDButton extends Group {
    public boolean isPressed=false;
    private String onDownImage;
    private String onUpImage;
    private String onSelectedImage;
    private String clickSound;
    private int imageIndex=-1;
    private float x=0;
    private float y=0;

    private ShapeRenderer renderer;

    private Image button;
    private Image icon;

    public enum State{
        Normal, Clicked, Selected
    }

    public HUDButton(String onDownImage, String onUpImage, String onSelectedImage, String onClickSound){
        this(onDownImage,onUpImage);
        clickSound= onClickSound;
        this.onSelectedImage=onSelectedImage;
    }

    public HUDButton(String onDownImage, String onUpImage, String onSelectedImage, int imageIndex, String onClickSound){
        this(onDownImage, onUpImage, imageIndex);
        clickSound= onClickSound;
        this.onSelectedImage=onSelectedImage;
    }

    public HUDButton(String onDownImage, String onUpImage) {
        super();
        renderer= new ShapeRenderer();
        this.onDownImage= onDownImage;
        this.onUpImage= onUpImage;
        setWidth(MainGame.hudAtlas.findRegion(onUpImage).getRegionWidth());
        setHeight(MainGame.hudAtlas.findRegion(onUpImage).getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
        button= new Image();
        button.setSize(getWidth(), getHeight());
        setState(State.Normal);
        addActor(button);
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
        button= new Image();
        button.setSize(getWidth(), getHeight());
        setState(State.Normal);
        addActor(button);
    }

    public void setIcon(String iconName){
        if(icon == null) {
            icon = new Image();
            addActor(icon);
        }
        TextureRegion region= new TextureRegion(MainGame.hudAtlas.findRegion(iconName));
        icon.setSize(region.getRegionWidth(), region.getRegionHeight());
        icon.setDrawable(new TextureRegionDrawable(region));

        icon.setPosition(getWidth() / 2 - icon.getWidth() / 2,
                getHeight() / 2 - icon.getHeight() / 2);

    }

    public String getClickSound(){
        return clickSound;
    }

    public void setInputListener(InputListener listener){
        addListener(listener);
    }

    public void setState(State state){
        switch (state){
            case Normal:
                if(imageIndex != -1) {
                    //batch.draw(MainGame.hudAtlas.findRegion(onUpImage, imageIndex), getX(), getY());
                    button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onUpImage, imageIndex)));
                }
                else {
                    //batch.draw(MainGame.hudAtlas.findRegion(onUpImage), getX(), getY());
                    button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onUpImage)));
                }
                break;
            case Clicked:
                if(imageIndex != -1){
                    if(onDownImage.equals(onUpImage)) {
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onUpImage, imageIndex)));
                        button.setColor(Color.valueOf("e86a17bb"));
                    }
                    else
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onDownImage, imageIndex)));
                }
                else{
                    if(onDownImage.equals(onUpImage)) {
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onUpImage)));
                        button.setColor(Color.valueOf("e86a17bb"));
                    }
                    else{
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onDownImage)));
                    }
                }
                break;
            case Selected:
                if(imageIndex != -1){
                    if(onSelectedImage.equals("")) {
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onUpImage, imageIndex)));
                        button.setColor(Color.valueOf("51b4eebb"));
                    }
                    else
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onSelectedImage, imageIndex)));
                }
                else{
                    if(onSelectedImage.equals("")) {
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onUpImage)));
                        button.setColor(Color.valueOf("51b4eebb"));
                    }
                    else{
                        button.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion(onSelectedImage)));
                    }
                }
                break;
        }
    }

    @Override
    public void setSize(float width, float height){
        super.setSize(width, height);
        button.setSize(width,height);
        if(icon != null){
            icon.setPosition(getWidth() / 2 - icon.getWidth() / 2,
                    getHeight() / 2 - icon.getHeight() / 2);
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);

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
