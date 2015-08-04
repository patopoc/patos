package com.patos.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.patos.MainGame;


/**
 * Created by steve on 27/07/2015.
 */
public class LevelManager {

    private int currentLevel;
    private Array<Level> levels;
    private String levelContent;

    public LevelManager(){
        if(MainGame.debug)
            Gdx.files.local("levels").delete();

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
        file.writeString(levelContent, false);
        updateLevels();
    }

    public void updateLevels(){
        levels.clear();
        levels=null;
        setLevels();
    }

    private String createJsonContent(){
        String content="[";
        for(Level l : levels){
            content += "{";
            content += " \"levelPassed\":";
            content += l.levelPassed + ",";
            content += " \"levelEnabled\":";
            content += l.levelEnabled+",";
            content += " \"bullets\":";
            content += l.bullets + ",";
            content += " \"maxPoints\":";
            content += l.maxPoints + ",";
            content += " \"currentPoints\":";
            content += l.currentPoints + ",";
            content += " \"stars\":";
            content += l.stars + ",";
            content += " \"time\":";
            content += l.time +",";
            content += " \"targets\":[";
            for(LevelTarget levelTarget : l.targets){
                content += "{\"targetGroup\":\""+levelTarget.targetGroup+"\", ";
                content += "\"targetType\":\""+levelTarget.targetType+"\", ";
                content += "\"num\":"+levelTarget.num + ",";
                content += "\"chance\":"+levelTarget.chance + "},";
            }
            content = content.substring(0, content.length() -1);
            content +="]";
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
            level.bullets=jsonValue.getInt("bullets");
            level.currentPoints= jsonValue.getInt("currentPoints");
            level.maxPoints=jsonValue.getInt("maxPoints");
            level.stars= jsonValue.getInt("stars");
            level.time= jsonValue.getInt("time");
            JsonValue jsonTypes= jsonValue.get("targets");
            level.targets= new Array<LevelTarget>();
            for(JsonValue typeVal : jsonTypes){
                LevelTarget levelTarget= new LevelTarget();
                levelTarget.targetGroup= typeVal.getString("targetGroup");
                levelTarget.targetType= typeVal.getString("targetType");
                levelTarget.num= typeVal.getInt("num");
                levelTarget.chance= typeVal.getInt("chance");
                level.targets.add(levelTarget);
            }
            levels.add(level);
        }
    }

    public void setCurrentLevel(int currentLevel){
        this.currentLevel= currentLevel;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

    public void unlockNextLevel(){
        levels.get(currentLevel + 1).levelEnabled=true;
    }

    public Array<Level> getLevels(){
        return levels;
    }

    public Level getLevel(int index){
        return levels.get(index);
    }


}
