package com.patos.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.patos.controller.Engine;

import java.util.HashMap;

/**
 * Created by steve on 30/07/2015.
 */
public class SoundManager {
    private Music mainMusic;
    private HashMap<String, Sound> sfx;
    Engine engine;

    public SoundManager(Engine engine){
        this.engine= engine;
        sfx= new HashMap<String, Sound>();
    }

    public void loadMusic(String musicFile){
        mainMusic= Gdx.audio.newMusic(Gdx.files.internal("sounds/"+musicFile));
    }

    public void playMusic(boolean loop, float vol){
        if(mainMusic != null){
            mainMusic.setLooping(loop);
            mainMusic.setVolume(vol);
            mainMusic.play();
        }
    }

    public void playSound(String soundFile, boolean loop, float vol){
        if(soundFile != null && !soundFile.equals("")) {
            if (!sfx.containsKey(soundFile)) {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundFile));
                sfx.put(soundFile, sound);
            }
            long id = sfx.get(soundFile).play();
            sfx.get(soundFile).setLooping(id, loop);
            sfx.get(soundFile).setVolume(id, vol);
        }
    }

    public void stopSound(String soundFile){
        if(soundFile != null && !soundFile.equals("")) {
            if (sfx.containsKey(soundFile)) {
                sfx.get(soundFile).stop();
                sfx.get(soundFile).dispose();
                sfx.remove(soundFile);
            }
        }
    }
}
