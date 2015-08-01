package com.patos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.patos.controller.Engine;
import com.patos.interfaces.OuyaInterface;
import com.patos.model.Curtain;

public class MainGame extends ApplicationAdapter implements OuyaInterface{

    private final float SCALE= 1f;

    private SpriteBatch batch;
    private Engine engine;
    private int ducksNum=10;
    private float ducksInterval=3;
    private int targetsNum=10;
    private int targetPositions=4;
    private float shotDuration=.5f;

    public static boolean debug=true;
    public static boolean forOuya=false;
    public static int time=120;
    public static OrthographicCamera mainCam;
    public static float worldWidth=900f;
    public static float worldHeight=500f;
    public static TextureAtlas stallAtlas;
    public static TextureAtlas objectAtlas;
    public static TextureAtlas hudAtlas;
    public static Stage stage;
    public static Group ducksLayer;
    public static Group targetsLayer;
    public static boolean selectAction=false;

	@Override
	public void create () {
		batch = new SpriteBatch();
        //mainCam= new OrthographicCamera();
        //mainCam.setToOrtho(false, worldWidth, worldHeight);
        //mainCam.update();

        stage= new Stage(new FitViewport(worldWidth, worldHeight));

        setStage();
        Gdx.input.setInputProcessor(stage);
        engine= new Engine(ducksNum, ducksInterval, targetsNum, targetPositions,shotDuration);

	}

    private void setStage(){
        stallAtlas= new TextureAtlas(Gdx.files.internal("stall.atlas"));
        objectAtlas= new TextureAtlas(Gdx.files.internal("objects.atlas"));
        hudAtlas= new TextureAtlas(Gdx.files.internal("hud.atlas"));

        Image bgImage = new Image(new TiledDrawable(stallAtlas.findRegion("bg_wood")));
        bgImage.setSize(worldWidth, worldHeight);
        stage.addActor(bgImage);

        targetsLayer= new Group();
        targetsLayer.setPosition(0, 0);
        stage.addActor(targetsLayer);

        drawCompoundBackground("grass", 0, 60);

        Image treePine= new Image(new TextureRegion(stallAtlas.findRegion("tree_pine")));
        treePine.setPosition(worldWidth - 180, 150);
        stage.addActor(treePine);

        Image backWave= new Image(new TiledDrawable(stallAtlas.findRegion("water1")));
        backWave.setPosition(backWave.getWidth() / 2, -10);
        backWave.setSize(worldWidth, backWave.getHeight());
        backWave.addAction(Actions.forever(Actions.sequence(
                Actions.moveBy(15, 0, 2),
                Actions.moveBy(-15, 0, 2)
        )));
        stage.addActor(backWave);

        ducksLayer= new Group();
        ducksLayer.setPosition(0, 80);
        stage.addActor(ducksLayer);

        Image frontWave= new Image(new TiledDrawable(stallAtlas.findRegion("water2")));
        frontWave.setPosition(0, -30);
        frontWave.setSize(worldWidth, frontWave.getHeight());
        frontWave.addAction(Actions.forever(Actions.sequence(
                Actions.moveBy(-15, 0, 2),
                Actions.moveBy(15, 0, 2)
        )));
        stage.addActor(frontWave);

        Image botomWood= new Image(new TiledDrawable(stallAtlas.findRegion("botom_wood")));
        botomWood.setPosition(0, 0);
        botomWood.setWidth(worldWidth);
        stage.addActor(botomWood);

        drawOverlapedActors("curtain_top",0);

        Curtain leftCurtain= new Curtain(0,20,stallAtlas);
        stage.addActor(leftCurtain);
        Curtain rightCurtain= new Curtain(worldWidth, 20,stallAtlas);
        rightCurtain.setScale(-1, 1);
        stage.addActor(rightCurtain);


        Image curtainStraight= new Image(new TiledDrawable(stallAtlas.findRegion("curtain_straight")));
        curtainStraight.setPosition(0,worldHeight - curtainStraight.getHeight());
        curtainStraight.setWidth(worldWidth);
        stage.addActor(curtainStraight);



    }
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //mainCam.update();
        engine.update(Gdx.graphics.getDeltaTime());

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        //batch.setProjectionMatrix(mainCam.combined);
        batch.begin();
        //bgTiled.draw(batch, 0, 0, worldWidth, worldHeight);
        batch.end();
	}

    @Override
    public void resize(int width, int height){
        //mainCam.viewportWidth=worldWidth;
        //mainCam.viewportHeight= worldHeight;
        stage.getViewport().update(width, height,true);
        //mainCam.update();
    }

    private void drawCompoundBackground(String spriteSeqBaseName, float x, float y){
        Array<TextureRegion> regions= new Array<TextureRegion>();
        TextureRegion region;
        int i=0;
        do{
            i++;
            region= stallAtlas.findRegion(spriteSeqBaseName + i);
            if(region != null)
                regions.add(region);

        }while (region != null);

        if(regions.size > 0){
            float textureWidth= regions.first().getRegionWidth();
            float currTextureX=x;
            int cols= (int)(worldWidth/(textureWidth * regions.size));
            for(int j=0; j<= cols; j++){
                for(TextureRegion r : regions){
                    Image image= new Image(r);
                    image.setPosition(currTextureX,y);
                    stage.addActor(image);
                    currTextureX += textureWidth;
                }
            }
        }
    }

    private void drawOverlapedActors(String spriteName, float x){
        TextureRegion region= stallAtlas.findRegion(spriteName);
        if(region != null){
            float currentX=x;
            int cols= (int)(worldWidth / region.getRegionWidth());
            for (int i=0; i<= cols; i++){
                Image image= new Image(region);
                image.setPosition(currentX, worldHeight - (region.getRegionHeight()+50));
                stage.addActor(image);
                currentX += region.getRegionWidth()-40;
            }
        }
    }

    @Override
    public void shootGun() {
        engine.gamePlayController.ouyaControllerShoot();
    }

    @Override
    public void reloadGun() {

    }

    @Override
    public void moveCrosshair(float x, float y) {
        engine.gamePlayController.ouyaControllerMove(x,y);
    }

    @Override
    public void select() {
        selectAction=true;
    }
}
