package com.patos.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by steve on 23/07/2015.
 */
public class CounterDisplay extends Group {

    private TextFont text;
    public CounterDisplay(TextureRegion icon, float iconSeparation, TextFont.FontSize fontSize,  float x, float y){
        Image scoreIcon= new Image(icon);
        scoreIcon.setPosition(x,y);
        addActor(scoreIcon);
        text= new TextFont(fontSize);
        text.setPosition(scoreIcon.getWidth() + iconSeparation, scoreIcon.getY());
        addActor(text);
    }

    public void setValue(int score){
        text.setText(String.valueOf(score));
    }
    public void setValue(String score){
        text.setText(score);
    }

}
