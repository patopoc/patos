package com.patos.android;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.patos.MainGame;
import com.patos.interfaces.OuyaInterface;

import tv.ouya.console.api.OuyaController;

public class AndroidLauncher extends AndroidApplication {
    private OuyaInterface callBack;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        OuyaController.init(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        MainGame mainGame= new MainGame();
        callBack= (OuyaInterface)mainGame;
		initialize(mainGame, config);
	}

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int player = OuyaController.getPlayerNumByDeviceId(event.getDeviceId());
        boolean handled=false;
        switch(keyCode){
            case OuyaController.BUTTON_A:
                // shoot
                callBack.shootGun();
                handled=true;
                break;
            case OuyaController.BUTTON_O:
                callBack.select();
                handled=true;
                break;
        }
        return handled || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        boolean handled=false;
        switch(keyCode){
            case OuyaController.BUTTON_A:
                // reload gun
                callBack.reloadGun();
                handled=true;
                break;
        }
        return handled || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event){
        int player = OuyaController.getPlayerNumByDeviceId(event.getDeviceId());

        float leftX= event.getAxisValue(OuyaController.AXIS_LS_X);
        float leftY= event.getAxisValue(OuyaController.AXIS_LS_Y);

        //if(leftX*leftX + leftY*leftY < OuyaController.STICK_DEADZONE * OuyaController.STICK_DEADZONE ){
        //    leftX = leftY = 0f;
        //}

        // move crosshair
        callBack.moveCrosshair(leftX,leftY);

        return true;
    }*/
}
