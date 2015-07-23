package com.patos.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.patos.MainGame;

/**
 * Created by steve on 23/07/2015.
 */
public class Cartridge extends Group {

    private CounterDisplay bulletsDisplay;
    private int iconSeparation=10;
    private int bullets;

    public Cartridge(int bullets, Bullet.BulletType bulletType, TextFont.FontSize fontSize){
        this.bullets= bullets;
        if(bulletType == Bullet.BulletType.Large){
            bulletsDisplay= new CounterDisplay(MainGame.hudAtlas.findRegion("icon_bullet_gold_long")
                                                ,iconSeparation ,fontSize,0,0);
        }
        else if(bulletType == Bullet.BulletType.Small){
            bulletsDisplay= new CounterDisplay(MainGame.hudAtlas.findRegion("icon_bullet_gold_short")
                                                ,iconSeparation, fontSize,0,0);
        }
        addActor(bulletsDisplay);
        updateCartridge();

    }

    private void updateCartridge(){
        bulletsDisplay.setValue(bullets);
    }

    public void removeBullet(){
        if(bullets > 0){
            bullets --;
            updateCartridge();
        }
    }

    public int getBullets(){
        return bullets;
    }
}
