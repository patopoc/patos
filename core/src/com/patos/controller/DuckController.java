package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.patos.MainGame;
import com.patos.model.Duck;

/**
 * Created by steve on 21/07/2015.
 */
public class DuckController {

    private Array<Duck> ducks;
    private float elapsedTime=0;

    public DuckController(int ducksNum){
        ducks= new Array<Duck>();
        for(int i=0; i< ducksNum; i++){
            int selectType= MathUtils.random(1,3);
            boolean isBad=false;
            Duck.DuckType duckType=null;
            switch (selectType){
                case 1:
                    duckType= Duck.DuckType.Brown;
                    break;
                case 2:
                    duckType= Duck.DuckType.Yellow;
                    break;
                case 3:
                    duckType= Duck.DuckType.Colored;
            }
            switch (MathUtils.random(1)){
                case 1:
                    isBad= true;
            }
            ducks.add(new Duck(duckType, isBad));
        }
    }

    // spawn a duck every x seconds specified by intervalSeconds

    public void spawnDuck(float delta, float intervalSeconds){
        elapsedTime += delta;
        if(elapsedTime >= intervalSeconds && ducks.size > 0){
            elapsedTime=0;
            //spawn duck
            MainGame.ducksLayer.addActor(ducks.pop());
        }
    }
}
