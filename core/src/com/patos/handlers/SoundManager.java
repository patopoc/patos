package com.patos.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.patos.controller.Engine;

import java.util.HashMap;

/**
 * Created by steve on 30/07/2015.
 */
public class SoundManager {
    private Music mainMusic;
    private HashMap<String, Sound> sfx;
    AssetManager assetManager;
    Engine engine;

    public SoundManager(Engine engine){
        this.engine= engine;
        assetManager= new AssetManager();
        sfx= new HashMap<String, Sound>();
    }

    public void loadMusic(String musicFile){
        mainMusic= Gdx.audio.newMusic(Gdx.files.internal("sounds/musics/"+musicFile));
    }

    public void loadSounds(){
        FileHandle[] fileHandles= Gdx.files.internal("sounds/sfx").list();

        for(FileHandle fileHandle : fileHandles){
            //Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/"+ fileHandle.name()));
            //sfx.put(fileHandle.name(), sound);
            assetManager.load("sounds/sfx/" + fileHandle.name(), Sound.class);
            //Gdx.app.log("sounds", fileHandle.name());
            assetManager.finishLoadingAsset("sounds/sfx/"+fileHandle.name());
        }

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
            /*(!sfx.containsKey(soundFile)) {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundFile));
                sfx.put(soundFile, sound);
            }*/
            //Gdx.app.log("playing sound",soundFile+", "+loop+", "+vol);
            Sound fx= assetManager.get("sounds/sfx/" +soundFile, Sound.class);
            long id;
            if(loop)
                id= fx.loop();
            else
                id = fx.play();

            fx.setVolume(id, vol);
        }
    }

    public void stopSound(String soundFile){
        if(soundFile != null && !soundFile.equals("")) {
            assetManager.get("sounds/sfx/"+soundFile, Sound.class).stop();
        }
    }

    public void dispose(){
        assetManager.dispose();
        mainMusic.dispose();
    }
}
