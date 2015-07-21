package com.patos.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by steve on 14/07/2015.
 */
public class Content {
    private HashMap<String, Texture> textures;
    private HashMap<String, Music> musics;
    private HashMap<String, Sound> sounds;

    public Content(){
        textures= new HashMap<String, Texture>();
        musics= new HashMap<String, Music>();
        sounds= new HashMap<String, Sound>();
    }

    /************************/
    /* Textures        ******/
    /************************/

    public void loadTexture(String path, String key){
        Texture texture= new Texture(Gdx.files.internal(path));
        textures.put(key, texture);
    }

    public Texture getTexture(String key){
        return textures.get(key);
    }

    public void removeTexture(String key){
        Texture texture= textures.get(key);
        if(texture != null){
            textures.remove(key);
            texture.dispose();
        }
    }
}
