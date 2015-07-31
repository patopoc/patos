package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

    private float fontWidth=-1;
    private float fontHeight=-1;
    private FontSize fontSize;
    private String color="";
    public TextFont(FontSize fontSize){
        this.fontSize=fontSize;
    }

    public void setText(String text, String color){
        this.color=color;
        setText(text);
    }
    public void setText(String text){
        clearChildren();
        //workaround for different bitmap size, take the size of the 0 wich is the fatty one
        if(fontSize == FontSize.Small){
            fontWidth= MainGame.hudAtlas.findRegion("text_0_small").getRegionWidth();
            fontHeight= MainGame.hudAtlas.findRegion("text_0_small").getRegionHeight();
        }
        else if(fontSize == FontSize.Normal){
            fontWidth= MainGame.hudAtlas.findRegion("text",0).getRegionWidth();
            fontHeight= MainGame.hudAtlas.findRegion("text",0).getRegionHeight();
        }

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
            else if(text.charAt(i) == '/'){
                if(fontSize == FontSize.Normal)
                    character= new Image(MainGame.hudAtlas.findRegion("text_slash"));
                else if(fontSize == FontSize.Small)
                    character= new Image(MainGame.hudAtlas.findRegion("text_slash_small"));
            }
            else if(text.charAt(i) == ' '){
                character= new Image();
                character.setSize(fontWidth, fontHeight);
            }
            else{
                if(fontSize == FontSize.Normal)
                    character= new Image(MainGame.hudAtlas.findRegion("text_"+text.charAt(i)));
                else if(fontSize == FontSize.Small)
                    character= new Image(MainGame.hudAtlas.findRegion("text_"+ text.charAt(i) +"_small"));
            }

            if(character != null) {
                if(color != "")
                    character.setColor(Color.valueOf(color));

                addActor(character);
                setSize(character.getWidth() * i+1, character.getHeight());
                character.setPosition(fontWidth * i, 0);
            }
        }
    }


}
