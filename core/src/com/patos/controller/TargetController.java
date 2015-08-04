package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.patos.MainGame;
import com.patos.handlers.Level;
import com.patos.handlers.LevelTarget;
import com.patos.model.Bullet;
import com.patos.model.Target;
import com.patos.model.TargetType;
import com.patos.utils.Funcs;

/**
 * Created by steve on 21/07/2015.
 */
public class TargetController {

    public Array<Target> targets;
    public String targetGroup;
    public Array<TargetType> targetTypes;
    private Array<LevelTarget> levelTargets;
    private float elapsedTime=0;
    private int targetsSpawned=0;
    private Engine engine;
    private int totalTargets=0;
    private float timeSeed;
    private Array<Vector2> availablePositions;
    private int targetPositions;
    private Array<Target> targetsOnCollision;

    public TargetController(Engine engine, String targetTypesFile, String targetGroup){
        targets= new Array<Target>();
        targetsOnCollision= new Array<Target>();
        this.engine=engine;
        this.targetGroup= targetGroup;
        loadTargetTypes(targetTypesFile);
        loadLevelTargets();
        for(LevelTarget levelTarget: levelTargets){
            totalTargets += levelTarget.num;
        }

        for(int i=0; i< totalTargets; i++){
            int chance;
            String targetType="";

            while(targetType.equals("")) {
                chance= MathUtils.random(1,100);
                //Gdx.app.log("Grp: "+targetGroup+", chance " + i, "" + chance);

                int selectType= MathUtils.random(levelTargets.size - 1);
                if(levelTargets.get(selectType).chance >= chance && levelTargets.get(selectType).num > 0){
                    targetType= levelTargets.get(selectType).targetType;
                    levelTargets.get(selectType).num--;
                }

            }

            //get targetType according specifications in file
            //Gdx.app.log("target type: "+targetType,getTargetType(targetType)+"");

            targets.add(new Target(getTargetType(targetType), targetGroup));
        }
    }

    public TargetController(Engine engine, String targetTypesFile, String targetGroup, int targetPositions){
        this(engine, targetTypesFile, targetGroup);
        timeSeed= MathUtils.random(1,5);
        this.targetPositions= targetPositions;
        createTargetPositions();
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

    // spawn a target every x seconds specified by intervalSeconds
    public void spawnTarget(float delta, float intervalSeconds){
        elapsedTime += delta;
        if(elapsedTime >= intervalSeconds && targetsSpawned < targets.size){
            elapsedTime=0;
            Target target=targets.get(targetsSpawned);
            targetsSpawned++;
            if(targetGroup.equals("duck")) {
                //spawn target
                MainGame.ducksLayer.addActor(target);
            }
            else if(targetGroup.equals("board")){
                int selectPos = MathUtils.random(1,targetPositions-1);
                //target= targets.get(targetsSpawned);
                //targetsSpawned++;
                target.setPosition(availablePositions.get(selectPos - 1).x
                        , availablePositions.get(selectPos - 1).y);
                MainGame.targetsLayer.addActor(target);
            }
            engine.soundManager.playSound(target.type.getNormalSound(),false, 1);
        }
    }

    public TargetType getTargetType(String targetType){
        for(TargetType type : targetTypes){
            //Gdx.app.log("getTargetType",targetType+ " || "+type.getImageName());
            if(type.getType().equals(targetType))
                return type;
        }
        return null;
    }

    public void loadLevelTargets(){
        levelTargets= new Array<LevelTarget>();
        Array<LevelTarget> fullLevelTarget=engine.levelManager.getLevels().get(engine.levelManager
                .getCurrentLevel()).targets;

        for(int i=0; i< fullLevelTarget.size; i++){
            if(fullLevelTarget.get(i).targetGroup.equals(targetGroup)) {
                LevelTarget levelTarget= new LevelTarget();
                levelTarget.chance= fullLevelTarget.get(i).chance;
                levelTarget.num= fullLevelTarget.get(i).num;
                levelTarget.targetGroup= fullLevelTarget.get(i).targetGroup;
                levelTarget.targetType= fullLevelTarget.get(i).targetType;

                levelTargets.add(levelTarget);
            }
        }
    }

    public void loadTargetTypes(String file){
        targetTypes= new Array<TargetType>();
        JsonReader reader= new JsonReader();
        JsonValue jsonTypes= reader.parse(Gdx.files.internal(file).readString());
        for(JsonValue jsonValue : jsonTypes){
            TargetType type= new TargetType(
                    jsonValue.getString("type"),
                    jsonValue.getString("collisionMask"),
                    jsonValue.getFloat("speed"),
                    jsonValue.getInt("points"),
                    jsonValue.getBoolean("bad"),
                    jsonValue.getString("imageName"),
                    jsonValue.getString("normalSound"),
                    jsonValue.getString("deadSound")
            );
            targetTypes.add(type);
        }
    }

    public int checkTargetCollision(Rectangle bounds){
        int points=0;
        int nearestTarget=0;
        targetsOnCollision.clear();
        Rectangle intersection= new Rectangle();
        for(Target target : targets){
            for(int i=0; i< target.getBounds().size(); i++){
                if(bounds.overlaps(target.getBounds().getRect(i))){
                    Funcs.intersect(bounds, target.getBounds().getRect(i), intersection);
                    targetsOnCollision.add(target);
                    break;
                }
            }
        }

        for(int i=0; i< targetsOnCollision.size;i++){
            if(targetsOnCollision.get(i).getZIndex() > nearestTarget)
                nearestTarget=i;
        }

        if(targetsOnCollision.size > 0) {
            targetsOnCollision.get(nearestTarget).killTarget(Bullet.BulletType.Small, intersection.x, intersection.y);
            engine.soundManager.playSound(targetsOnCollision.get(nearestTarget).type.getDeadSound(), false, 1);
            if (targetsOnCollision.get(nearestTarget).type.isBad())
                points = targetsOnCollision.get(nearestTarget).type.getPoints();
            else
                points = -targetsOnCollision.get(nearestTarget).type.getPoints();
        }
        return points;
    }
}
