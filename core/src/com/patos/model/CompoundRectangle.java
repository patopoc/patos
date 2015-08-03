package com.patos.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by steve on 03/08/2015.
 */
public class CompoundRectangle {
    float x=0;
    float y=0;

    private Array<Rectangle> rectangles;
    private Array<Vector2>rectOffset;

    public CompoundRectangle(){
        rectangles= new Array<Rectangle>();
        rectOffset= new Array<Vector2>();

    }

    public void setPosition(float x, float y){
        this.x=x;
        this.y=y;
        for(int i=0; i< rectangles.size; i++){
            getRect(i).setPosition(x + rectOffset.get(i).x, y + rectOffset.get(i).y);
        }
    }

    public void addRect(Rectangle rectangle){
        rectangles.add(rectangle);
        rectOffset.add(new Vector2(rectangle.x, rectangle.y));
    }

    public Rectangle getRect(int index){
        return rectangles.get(index);
    }

    public int size(){
        return rectangles.size;
    }
}
