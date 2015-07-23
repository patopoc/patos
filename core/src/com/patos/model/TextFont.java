package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.patos.MainGame;

/**
 * Created by steve on 23/07/2015.
 */
public class TextFont extends Group {

    public enum FontSize{
        Normal, Small
    }

    private FontSize fontSize;
    public TextFont(FontSize fontSize){
        this.fontSize=fontSize;
    }

    public void setText(String text){
        clearChildren();
        for(int i= 0; i< text.length(); i++){
            Image character=null;
            if(text.charAt(i) == ':'){
                if(fontSize == FontSize.Normal)
                    character= new Image(MainGame.hudAtlas.findRegion("text_dots"));
                else if(fontSize == FontSize.Small)
                    character= new Image(MainGame.hudAtlas.findRegion("text_dots_small"));
            }
            else if(text.charAt(i) == 'x'){
                if(fontSize == FontSize.Normal)
                    character= new Image(MainGame.hudAtlas.findRegion("text_cross"));
                else if(fontSize == FontSize.Small)
                    character= new Image(MainGame.hudAtlas.findRegion("text_cross_small"));
            }
            else if(text.charAt(i) == '+'){
                if(fontSize == FontSize.Normal)
                    character= new Image(MainGame.hudAtlas.findRegion("text_plus"));
                else if(fontSize == FontSize.Small)
                    character= new Image(MainGame.hudAtlas.findRegion("text_plus_small"));
            }
            else{
                if(fontSize == FontSize.Normal)
                    character= new Image(MainGame.hudAtlas.findRegion("text_"+text.charAt(i)));
                else if(fontSize == FontSize.Small)
                    character= new Image(MainGame.hudAtlas.findRegion("text_"+ text.charAt(i) +"_small"));
            }
            if(character != null) {
                addActor(character);
                setSize(character.getWidth() * i+1, character.getHeight());
                character.setPosition(character.getWidth() * i, 0);
            }
        }
    }


}
