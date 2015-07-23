package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.patos.MainGame;
import com.patos.model.Bullet;
import com.patos.model.Cartridge;
import com.patos.model.Crosshair;
import com.patos.model.GunTrigger;
import com.patos.model.CounterDisplay;
import com.patos.model.TextFont;

import javax.swing.Action;

/**
 * Created by steve on 21/07/2015.
 */
public class Engine {

    private DuckController duckController;
    private TargetController targetController;
    private int time=20;
    private float accumTime;
    TextFont timer;
    private float intervalSeconds;
    private Crosshair crosshair;
    private GunTrigger trigger;
    private CounterDisplay scoreDisplay;
    private Cartridge cartridge;
    private float crosshairX=0;
    private float crosshairY=0;
    private float stepSize=5;
    private float joypadThreshold=0.2f;
    private float stepsX=0;
    private float stepsY=0;
    private boolean startMotion=false;
    private float startX=100;
    private float startY=100;
    private float endX=700;
    private float endY=350;
    private Touchpad touchpad;
    private boolean timeup=false;
    private boolean timeupShowed=false;

    public int score=0;


    public Engine(int ducksNum, float duckIntervalSec, int targetsNum, int targetPositions, float shotDuration){
        duckController= new DuckController(ducksNum);
        targetController= new TargetController(targetsNum, targetPositions);
        intervalSeconds= duckIntervalSec;
        crosshair= new Crosshair(shotDuration);

        timer= new TextFont(TextFont.FontSize.Small);
        timer.setText(String.valueOf(time));
        timer.setPosition(MainGame.worldWidth / 2 - timer.getWidth() / 2, MainGame.worldHeight - 100);
        MainGame.stage.addActor(timer);

        scoreDisplay= new CounterDisplay(MainGame.hudAtlas.findRegion("icon_duck"),25
                , TextFont.FontSize.Small,20 ,400);
        scoreDisplay.setValue(score);
        MainGame.stage.addActor(scoreDisplay);

        cartridge= new Cartridge(20, Bullet.BulletType.Small, TextFont.FontSize.Small);
        cartridge.setPosition(MainGame.worldWidth - 120 ,400);
        MainGame.stage.addActor(cartridge);

        trigger= new GunTrigger();
        trigger.setPosition(800,60);
        MainGame.stage.addActor(trigger);

        MainGame.stage.addActor(crosshair);
        crosshairX = MainGame.worldWidth / 2;
        crosshairY = MainGame.worldHeight / 2;
        crosshair.setPosition(crosshairX, crosshairY);

        touchpad= createTouchpad();
        touchpad.addListener(createJoypadInputListener());
        touchpad.setBounds(20, 20, 140, 140);
        touchpad.setPosition(20, 20);
        MainGame.stage.addActor(touchpad);
    }

    public void update(float delta){
        accumTime += delta;
        if(accumTime >= 1 && time > 0){
            accumTime=0;
            timer.setText(String.valueOf(--time));
        }

        if(time > 0 && !timeup) {
            duckController.spawnDuck(delta, intervalSeconds);
            targetController.spawnTarget(delta, intervalSeconds);
            if (startMotion) {
                crosshairX += stepsX;
                crosshairY += stepsY;
                fixEdges();
                crosshair.setPosition(crosshairX, crosshairY);
            }
            if (trigger.isShooting) {
                if (!crosshair.isShooting && cartridge.getBullets() > 0) {
                    crosshair.shoot();
                    cartridge.removeBullet();
                    score += duckController.checkDuckCollision(crosshair.getBounds());
                    score += targetController.checkTargetCollision(crosshair.getBounds());
                    if (score < 0)
                        score = 0;
                    scoreDisplay.setValue(score);
                }
            }
        }
        else{
            timeup=true;
        }

        if(timeup && !timeupShowed) {
            timeupShowed=true;
            showTimeup();
        }
    }

    private void showTimeup(){
        timer.remove();
        Image timeupImage= new Image(MainGame.hudAtlas.findRegion("text_timeup"));
        timeupImage.setPosition(MainGame.worldWidth/2 - timeupImage.getWidth()/2,
                                MainGame.worldHeight/2 - timeupImage.getHeight()/2);
        timeupImage.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 300, 1f),
                Actions.fadeOut(1f))
                , Actions.run(new Runnable() {
            @Override
            public void run() {
                Gdx.app.log("opa", "vya");
            }
        })));
        MainGame.stage.addActor(timeupImage);

    }

    private Touchpad createTouchpad(){
        Skin touchpadSkin= new Skin();
        touchpadSkin.add("touchBackground", MainGame.hudAtlas.findRegion("touchBackground"),TextureRegion.class);
        touchpadSkin.add("touchKnob", MainGame.hudAtlas.findRegion("touchKnob"),TextureRegion.class);
        Touchpad.TouchpadStyle style= new Touchpad.TouchpadStyle();
        style.background= touchpadSkin.getDrawable("touchBackground");
        style.knob= touchpadSkin.getDrawable("touchKnob");
        return new Touchpad(5, style);
    }

    public InputListener createJoypadInputListener(){
        InputListener listener= new InputListener(){

            float percentX;
            float percentY;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                stepsX=0;
                stepsY=0;
                startMotion=true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                startMotion=false;
            }

            public void touchDragged(InputEvent event, float touchX, float touchY, int pointer){
                percentX= ((Touchpad) event.getTarget()).getKnobPercentX();
                percentY= ((Touchpad) event.getTarget()).getKnobPercentY();

                if(percentX > joypadThreshold){
                    stepsX=stepSize;
                }
                if(percentX < -joypadThreshold){
                    stepsX= -stepSize;
                }

                if(percentY > joypadThreshold){
                    stepsY = stepSize;
                }
                if(percentY < -joypadThreshold){
                    stepsY= -stepSize;
                }
            }

        };
        return listener;
    }

    private void fixEdges(){
        if(crosshairX <= startX ){
            crosshairX= startX;
        }
        else if(crosshairX >= endX){
            crosshairX= endX;
        }
        if(crosshairY <= startY){
            crosshairY= startY;
        }
        else if(crosshairY >= endY){
            crosshairY= endY;
        }
    }
}
