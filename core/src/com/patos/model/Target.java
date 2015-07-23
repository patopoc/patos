package com.patos.model;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.patos.MainGame;
import com.patos.controller.TargetController;
import com.patos.handlers.MoveToSin;

/**
 * Created by steve on 21/07/2015.
 */
public class Target extends Group{

    public TargetType type;
    public boolean shot=false;

    public enum TargetType{
        Red(6, 10), Colored(3, 30);

        float speed;
        int points;

        TargetType(float speed, int points){
            this.speed= speed;
            this.points= points;
        }

        float getSpeed(){
            return speed;
        }

        float getPoints(){
            return points;
        }
    }

    public Target(TargetType type){
        this.type= type;
        Image stick= new Image(MainGame.objectAtlas.findRegion("stick_metal_outline"));
        Image targetImage=null;
        switch (type){
            case Red:
                targetImage = new Image(MainGame.objectAtlas.findRegion("target_red1_outline"));
                break;
            case Colored:
                targetImage = new Image(MainGame.objectAtlas.findRegion("target_colored_outline"));
                break;
        }
        addActor(stick);
        addActor(targetImage);

        targetImage.setSize(targetImage.getWidth() / 2, targetImage.getHeight() / 2);
        targetImage.setPosition(getWidth() / 2 - targetImage.getWidth() / 2, stick.getHeight() - 5);
        stick.setPosition(getWidth() / 2 - stick.getWidth() / 2, 0);

        addAction(Actions.sequence(Actions.moveBy(0,150,1f),Actions.delay(1), Actions.moveBy(0, -150, 1f),Actions.run(new Runnable() {
            @Override
            public void run() {
                removeActor();
            }
        })));

    }

    private void removeActor(){
        TargetController.targets.removeValue(this,false);
        this.remove();
    }
}
