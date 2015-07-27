package com.patos.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

/**
 * Created by steve on 27/07/2015.
 */
public class LevelManager {

    private int currentLevel;
    private Array<Level> levels;
    private String levelContent;

    public LevelManager(){
        if(!Gdx.files.local("levels").exists()){
            levelContent= Gdx.files.internal("levels.json").readString();
            FileHandle file= Gdx.files.local("levels");
            file.writeString(levelContent,false);
            levelContent="";
        }

        levelContent= Gdx.files.local("levels").readString();
        setLevels();
    }

    public void saveLevels(){
        levelContent= createJsonContent();
        FileHandle file= Gdx.files.local("levels");
        file.writeString(levelContent,false);
    }

    public void updateLevels(){

    }

    private String createJsonContent(){
        String content="[";
        for(Level l : levels){
            content += "{";
            content += " \"levelPassed\":";
            content += l.levelPassed + ",";
            content += " \"levelEnabled\":";
            content += l.levelEnabled+",";
            content += " \"maxPoints\":";
            content += l.maxPoints + ",";
            content += " \"currentPoints\":";
            content += l.currentPoints + ",";
            content += " \"stars\":";
            content += l.stars;
            content += "},";
        }
        content = content.substring(0, content.length() -1);
        content += "]";
        return content;
    }

    private void setLevels(){
        levels= new Array<Level>();
        JsonReader reader= new JsonReader();
        JsonValue jsonLevels= reader.parse(levelContent);
        for(JsonValue jsonValue : jsonLevels){
            Level level= new Level();
            level.levelEnabled= jsonValue.getBoolean("levelEnabled");
            level.levelPassed= jsonValue.getBoolean("levelPassed");
            level.currentPoints= jsonValue.getInt("currentPoints");
            level.maxPoints=jsonValue.getInt("maxPoints");
            level.stars= jsonValue.getInt("stars");
            levels.add(level);
        }
    }

    public void setCurrentLevel(int currentLevel){
        this.currentLevel= currentLevel;
    }

    public void unlockNextLevel(){
        levels.get(currentLevel + 1).levelEnabled=true;
    }


}
