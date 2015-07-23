package com.patos.model;

/**
 * Created by steve on 23/07/2015.
 */
public class Bullet {
    public enum BulletType{
        Small, Large
    }

    private BulletType type;
    public Bullet(BulletType type){
        this.type= type;
    }

    public BulletType getType(){
        return type;
    }
}
