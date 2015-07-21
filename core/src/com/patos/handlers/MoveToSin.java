package com.patos.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

/**
 * Created by steve on 21/07/2015.
 */
public class MoveToSin extends MoveToAction {

    public float period;
    private float time=0;
    private float periodSteps;
    private float range=10;
    private float angle=0;
    private float startY;

    public MoveToSin(){
        super();
    }

    public void setPeriod(float period){
        this.period= period;
        periodSteps= period / 36;   //36 instead of 360 because the later gives
                                    //periodSteps way smaller than deltaTime

    }

    public void setStartPosition(float x, float y){
        startY=y;
    }

    public void setRange(float range){
        this.range=range;
    }

    @Override
    protected void update(float percent){
        super.update(percent);

        time += Gdx.graphics.getDeltaTime();
        if(time >= periodSteps){
            time=0;
            angle +=10;     // counting degrees by 10 because of the limitation with periodSteps

            setY(startY + range * MathUtils.sin(angle * MathUtils.degreesToRadians));
            if(angle >= 360)
                angle=0;
        }

    }

}
