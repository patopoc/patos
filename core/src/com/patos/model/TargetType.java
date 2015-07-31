package com.patos.model;

/**
 * Created by steve on 28/07/2015.
 */
public class TargetType {

    private String type;
    private float speed;
    private int points;
    private boolean bad;
    private String imageName;
    private String normalSound;
    private String deadSound;

    public TargetType(String type, float speed, int points, boolean bad, String imageName, String normalSound, String deadSound){
        this.type=type;
        this.speed= speed;
        this.points= points;
        this.bad= bad;
        this.imageName= imageName;
        this.normalSound=normalSound;
        this.deadSound=deadSound;
    }

    public float getSpeed(){
        return speed;
    }

    public int getPoints(){
        return points;
    }

    public boolean isBad() {
        return bad;
    }

    public String getImageName(){
        return imageName;
    }

    public String getType(){
        return type;
    }

    public String getNormalSound(){
        return normalSound;
    }

    public String getDeadSound(){
        return deadSound;
    }

}
