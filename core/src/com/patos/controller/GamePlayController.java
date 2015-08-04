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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.patos.MainGame;
import com.patos.model.Bullet;
import com.patos.model.Cartridge;
import com.patos.model.CounterDisplay;
import com.patos.model.Crosshair;
import com.patos.model.GunTrigger;
import com.patos.model.HUDButton;
import com.patos.model.TextFont;

/**
 * Created by steve on 27/07/2015.
 */
public class GamePlayController extends Group {

    public boolean isActive=false;

    private String duckControllerGroup="duck";
    private String boardControllerGroup= "board";
    private Array<TargetController> controllerArray;
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
    private boolean ouyaJoystickMoving=false;
    private float startX=100;
    private float startY=100;
    private float endX=700;
    private float endY=350;
    private Touchpad touchpad;
    private boolean timeup=false;
    private boolean timeupShowed=false;
    private boolean gameIsRunning=false;
    private Engine engine;
    private int currentCollisionPoints=0;
    private Table buttonContainer;


    public GamePlayController(final Engine engine, float duckIntervalSec, int targetPositions, float shotDuration){
        this.engine= engine;
        controllerArray= new Array<TargetController>();
        TargetController duckController= new TargetController(engine, "targettypes.json",duckControllerGroup);
        controllerArray.add(duckController);
        TargetController boardController= new TargetController(engine,"targettypes.json",boardControllerGroup, targetPositions);
        controllerArray.add(boardController);
        intervalSeconds= duckIntervalSec;
        crosshair= new Crosshair(shotDuration);
        time= engine.levelManager.getLevels().get(engine.levelManager.getCurrentLevel()).time;
        timer= new TextFont(TextFont.FontSize.Small);
        timer.setText(String.valueOf(time));
        timer.setPosition(MainGame.worldWidth / 2 - timer.getWidth() / 2, MainGame.worldHeight - 100);
        addActor(timer);

        scoreDisplay= new CounterDisplay(MainGame.hudAtlas.findRegion("icon_duck"),25
                , TextFont.FontSize.Small,20 ,400);
        Engine.score=0;
        scoreDisplay.setValue(Engine.score);
        addActor(scoreDisplay);

        cartridge= new Cartridge(20, Bullet.BulletType.Small, TextFont.FontSize.Small);
        cartridge.setPosition(MainGame.worldWidth - 120, 400);
        addActor(cartridge);

        trigger= new GunTrigger(engine,shotDuration);
        if(MainGame.forOuya)
            trigger.setPosition(800, MainGame.worldHeight);
        else
            trigger.setPosition(760, 20);
        //aadActor(trigger);

        addActor(crosshair);
        crosshairX = MainGame.worldWidth / 2;
        crosshairY = MainGame.worldHeight / 2;
        crosshair.setPosition(crosshairX, crosshairY);

        touchpad= createTouchpad();
        touchpad.addListener(createJoypadInputListener());
        touchpad.setBounds(20, 20, 140, 140);
        if(MainGame.forOuya)
            touchpad.setPosition(20, MainGame.worldHeight);
        else
            touchpad.setPosition(20, 20);
        //addActor(touchpad);

        final HUDButton pauseButton= new HUDButton("btn_small_clicked","btn_small_normal","btn_small_selected", "button.mp3");
        pauseButton.setIcon("icon_pause");

        pauseButton.setInputListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pauseButton.isPressed = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pauseButton.isPressed = false;
                    /*outAction(new Runnable() {
                        @Override
                        public void run() {
                            engine.show("levels");
                            remove();
                        }
                    });*/
                Engine.pause = !Engine.pause;
                if (Engine.pause) {
                    pauseButton.setIcon("icon_play");
                } else {
                    pauseButton.setIcon("icon_pause");
                }

            }
        });

        final HUDButton levelsButton= new HUDButton("btn_small_clicked","btn_small_normal","btn_small_selected", "button.mp3");
        levelsButton.setIcon("icon_levels");
        final Engine engineFinal=engine;
        levelsButton.setInputListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                levelsButton.setState(HUDButton.State.Clicked);
                levelsButton.isPressed = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isActive) {
                    isActive = false;
                    levelsButton.setState(HUDButton.State.Normal);
                    levelsButton.isPressed = false;
                    engineFinal.show("levels");
                    clearChildren();
                    remove();
                }
            }
        });

        buttonContainer= new Table();
        buttonContainer.add(pauseButton).pad(10);
        buttonContainer.add(levelsButton).pad(10);
        buttonContainer.setPosition(MainGame.worldWidth / 2 - buttonContainer.getWidth() / 2, 50);


        readyGo();
    }

    public void update(float delta){
        if(!Engine.pause) {
            accumTime += delta;
            if (accumTime >= 1 && time > 0 && gameIsRunning) {
                accumTime = 0;
                timer.setText(String.valueOf(--time));
            }

            if (time > 0 && !timeup) {
                for (TargetController targetController : controllerArray) {
                    targetController.spawnTarget(delta, intervalSeconds);
                }
                if (startMotion || ouyaJoystickMoving) {
                    ouyaJoystickMoving=false;
                    crosshairX += stepsX;
                    crosshairY += stepsY;
                    fixEdges();
                    crosshair.setPosition(crosshairX, crosshairY);
                }
                if (trigger.isShooting) {
                    if (!crosshair.isShooting && cartridge.getBullets() > 0) {
                        crosshair.shoot();
                        cartridge.removeBullet();

                        for (TargetController targetController : controllerArray) {
                            currentCollisionPoints = targetController.checkTargetCollision(crosshair.getBounds());
                            Engine.score += currentCollisionPoints;
                            if (currentCollisionPoints != 0)
                                break;
                        }

                        currentCollisionPoints = 0;
                        if (Engine.score < 0)
                            Engine.score = 0;
                        scoreDisplay.setValue(Engine.score);
                    }
                }
            } else {
                timeup = true;
            }

            if ((timeup && !timeupShowed) || cartridge.getBullets() == 0) {
                timeupShowed = true;
                int currentLevel=engine.levelManager.getCurrentLevel();
                int currentPoints= engine.levelManager.getLevel(currentLevel).currentPoints;
                int maxPoints= engine.levelManager.getLevel(currentLevel).maxPoints;

                //if(Engine.score > currentPoints) {
                    engine.levelManager.getLevel(currentLevel).currentPoints += Engine.score;
                    if(engine.levelManager.getLevel(currentLevel).currentPoints >= maxPoints){
                        engine.levelManager.getLevel(currentLevel).levelPassed=true;
                        if(currentLevel + 1 < engine.levelManager.getLevels().size) {
                            if (!engine.levelManager.getLevel(currentLevel + 1).levelEnabled) {
                                engine.levelManager.getLevel(currentLevel + 1).levelEnabled = true;
                            }
                        }
                    }
                    engine.levelManager.saveLevels();
                //}

                showTimeup();
                engine.soundManager.stopSound("ducks_quacking1.wav");
            }
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
                engine.soundManager.playSound("go.mp3",false,1);
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
                engine.soundManager.playSound("ducks_quacking1.wav", true, 1);
                addActor(buttonContainer);
                addActor(touchpad);
                addActor(trigger);

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
        engine.soundManager.playSound("ready.mp3",false,1);
    }

    private void showTimeup(){
        timer.remove();
        Image timeupImage= new Image(MainGame.hudAtlas.findRegion("text_timeup"));
        timeupImage.setPosition(MainGame.worldWidth/2 - timeupImage.getWidth()/2,
                MainGame.worldHeight/2 - timeupImage.getHeight()/2);
        engine.soundManager.playSound("bell.mp3",false,1);
        timeupImage.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 300, 1f),
                Actions.fadeOut(1f))
                , Actions.run(new Runnable() {
            @Override
            public void run() {
                Gdx.app.log("opa", "vya");
                engine.levelManager.getLevel(
                        engine.levelManager.getCurrentLevel()).stars= calculteStars();
                engine.show("score");
                remove();
            }
        })));
        addActor(timeupImage);

    }

    private int calculteStars(){
        int stars=0;
        int currentPoints= engine.levelManager.getLevel(engine.levelManager.getCurrentLevel()).currentPoints;
        int maxPoints= engine.levelManager.getLevel(engine.levelManager.getCurrentLevel()).maxPoints;
        int difference= maxPoints - currentPoints;
        if(currentPoints >= maxPoints)
            stars=3;
        else if(difference < 10)
            stars=2;
        else if(currentPoints > maxPoints /2)
            stars=1;
        return stars;
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
                if(!Engine.pause) {
                    stepsX = 0;
                    stepsY = 0;
                    startMotion = true;
                    return true;
                }
                return false;
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

    public void ouyaControllerMove(float movX, float movY){

        ouyaJoystickMoving=true;
        Gdx.app.log("ouya stick",movX+ " , "+movY);
        if(movX > joypadThreshold){
            stepsX=stepSize;
        }
        if(movX < -joypadThreshold){
            stepsX= -stepSize;
        }

        if(movY < joypadThreshold){
            stepsY = stepSize;
        }
        if(movY > -joypadThreshold){
            stepsY= -stepSize;
        }
    }

    public void ouyaControllerShoot(){
        trigger.shoot();
    }

    public void ouyaControllerReload(){

    }
}
