package com.patos.model;

/**
 * Created by steve on 23/07/2015.
 */

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Curtain extends Group {

    public Curtain(float x, float y, TextureAtlas atlas){
        Image curtain= new Image(atlas.findRegion("curtain"));
        Image rope= new Image(atlas.findRegion("curtain_rope"));
        rope.setPosition(-(rope.getWidth() / 2) + 15, curtain.getHeight() / 2 - rope.getHeight() / 2);
        addActor(curtain);
        addActor(rope);
        setWidth(curtain.getWidth());
        setHeight(curtain.getHeight());
        setPosition(x, y);
    }

}