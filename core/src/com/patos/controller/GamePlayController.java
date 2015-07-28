package com.patos.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.patos.MainGame;
import com.patos.model.Bullet;
import com.patos.model.Cartridge;
import com.patos.model.CounterDisplay;
import com.patos.model.Crosshair;
import com.patos.model.GunTrigger;
import com.patos.model.TextFont;

/**
 * Created by steve on 27/07/2015.
 */
public class GamePlayController extends Group {

    public boolean isActive=false;

    private TargetController duckController;
    private String duckControllerGroup="duck";
    private TargetController boardController;
    private String boardControllerGroup= "board";
    private int time=-1;
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
    private boolean gameIsRunning=false;


    public GamePlayController(Engine engine, float duckIntervalSec, int targetPositions, float shotDuration){
        duckController= new TargetController(engine, "targettypes.json",duckControllerGroup);
        boardController= new TargetController(engine,"targettypes.json",boardControllerGroup, targetPositions);
        intervalSeconds= duckIntervalSec;
        crosshair= new Crosshair(shotDuration);
        time= engine.levelManager.getLevels().get(engine.levelManager.getCurrentLevel()).time;
        timer= new TextFont(TextFont.FontSize.Small);
        timer.setText(String.valueOf(time));
        timer.setPosition(MainGame.worldWidth / 2 - timer.getWidth() / 2, MainGame.worldHeight - 100);
        addActor(timer);

        scoreDisplay= new CounterDisplay(MainGame.hudAtlas.findRegion("icon_duck"),25
                , TextFont.FontSize.Small,20 ,400);
        scoreDisplay.setValue(Engine.score);
        addActor(scoreDisplay);

        cartridge= new Cartridge(20, Bullet.BulletType.Small, TextFont.FontSize.Small);
        cartridge.setPosition(MainGame.worldWidth - 120 ,400);
        addActor(cartridge);

        trigger= new GunTrigger();
        trigger.setPosition(800, 60);
        addActor(trigger);

        addActor(crosshair);
        crosshairX = MainGame.worldWidth / 2;
        crosshairY = MainGame.worldHeight / 2;
        crosshair.setPosition(crosshairX, crosshairY);

        touchpad= createTouchpad();
        touchpad.addListener(createJoypadInputListener());
        touchpad.setBounds(20, 20, 140, 140);
        touchpad.setPosition(20, 20);
        addActor(touchpad);
        readyGo();
    }

    public void update(float delta){
        accumTime += delta;
        if(accumTime >= 1 && time > 0 && gameIsRunning){
            accumTime=0;
            timer.setText(String.valueOf(--time));
        }

        if(time > 0 && !timeup) {
            duckController.spawnTarget(delta, intervalSeconds,duckControllerGroup);
            boardController.spawnTarget(delta, intervalSeconds, boardControllerGroup);
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
                    Engine.score += duckController.checkTargetCollision(crosshair.getBounds());
                    Engine.score += boardController.checkTargetCollision(crosshair.getBounds());
                    if (Engine.score < 0)
                        Engine.score = 0;
                    scoreDisplay.setValue(Engine.score);
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


    private void readyGo(){
        final Image image= new Image(MainGame.hudAtlas.findRegion("text_ready"));
        image.setPosition(-image.getWidth(), MainGame.worldHeight / 2);
        SequenceAction seq= new SequenceAction();

        DelayAction preDelay= new DelayAction();
        preDelay.setDuration(0.5f);
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(MainGame.worldWidth / 2 - image.getWidth() / 2, image.getY());
        moveToAction.setDuration(1f);
        moveToAction.setInterpolation(Interpolation.exp10Out);
        RunnableAction runnableAction= new RunnableAction(){
            @Override
            public void run() {
                image.setDrawable(new TextureRegionDrawable(MainGame.hudAtlas.findRegion("text_go")));
            }
        };

        DelayAction delayAction= new DelayAction();
        delayAction.setDuration(0.5f);
        DelayAction delayAction1= new DelayAction();
        delayAction1.setDuration(0.5f);
        MoveToAction moveToAction1= new MoveToAction();
        moveToAction1.setPosition(MainGame.worldWidth, image.getY());
        moveToAction1.setDuration(1f);
        moveToAction1.setInterpolation(Interpolation.exp10Out);
        RunnableAction runnableAction1= new RunnableAction(){
            @Override
            public void run(){
                gameIsRunning=true;
                image.remove();
            }
        };
        seq.addAction(moveToAction);
        seq.addAction(delayAction);
        seq.addAction(runnableAction);
        seq.addAction(delayAction1);
        seq.addAction(moveToAction1);
        seq.addAction(runnableAction1);
        image.addAction(seq);

        addActor(image);
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
        addActor(timeupImage);

    }

    private Touchpad createTouchpad(){
        Skin touchpadSkin= new Skin();
        touchpadSkin.add("touchBackground", MainGame.hudAtlas.findRegion("touchBackground"),TextureRegion.class);
        touchpadSkin.add("touchKnob", MainGame.hudAtlas.findRegion("touchKnob"), TextureRegion.class);
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
