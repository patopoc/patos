package com.patos.controller;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.patos.MainGame;
import com.patos.model.Bullet;
import com.patos.model.Duck;
import com.patos.model.Target;
import com.patos.utils.Funcs;

/**
 * Created by steve on 21/07/2015.
 */
public class TargetController {

    public static Array<Target> targets;
    private float elapsedTime=0;
    private float timeSeed;
    private Array<Vector2> availablePositions;
    private int targetPositions;
    private int targetSpawned=0;

    public TargetController(int targetsNum, int targetPositions){
        targets= new Array<Target>();
        timeSeed= MathUtils.random(1,5);
        this.targetPositions= targetPositions;

        for(int i=0; i< targetsNum; i++){
            int selectType= MathUtils.random(1,2);
            Target.TargetType targetType=null;
            switch (selectType){
                case 1:
                    targetType= Target.TargetType.Red;
                    break;
                case 2:
                    targetType= Target.TargetType.Colored;
            }
            targets.add(new Target(targetType));
        }
        createTargetPositions();
    }

    // spawn a target every x seconds specified by intervalSeconds

    public void spawnTarget(float delta, float intervalSeconds){
        elapsedTime += delta;
        if(elapsedTime >= intervalSeconds + timeSeed && targetSpawned < targets.size){
            elapsedTime=0;
            //spawn target
            int selectPos= MathUtils.random(1,targetPositions-1);
            Target target= targets.get(targetSpawned);
            targetSpawned++;
            target.setPosition(availablePositions.get(selectPos-1).x
                              ,availablePositions.get(selectPos-1).y);
            MainGame.targetsLayer.addActor(target);
        }
    }

    private void createTargetPositions(){
        availablePositions= new Array<Vector2>();
        float targetSeparation= MainGame.worldWidth / (targetPositions+2);
        for(int i= 1; i< targetPositions;i++){
            Vector2 position= new Vector2();
            position.set(i*targetSeparation, 0);
            availablePositions.add(position);
        }
    }

    public int checkTargetCollision(Rectangle bounds){
        int points=0;
        Rectangle intersection= new Rectangle();
        for(Target t : targets){
            if(bounds.overlaps(t.getBounds())){
                Funcs.intersect(bounds, t.getBounds(), intersection);
                t.killTarget(Bullet.BulletType.Small,intersection.x, intersection.y);
                points= t.type.getPoints();
                break;
            }
        }
        return points;
    }
}
