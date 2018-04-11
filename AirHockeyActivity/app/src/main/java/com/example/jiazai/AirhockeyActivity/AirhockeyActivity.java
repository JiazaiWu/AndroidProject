package com.example.jiazai.AirhockeyActivity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.view.MotionEvent;
import android.view.View;

public class AirhockeyActivity extends AppCompatActivity {

    MyGLSurfaceView mySurfaceView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySurfaceView = new MyGLSurfaceView(this);
        mySurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent != null) {
                    // Convert touch coordinates into normalized device coordinates
                    // keeping in mind that Android's Y coordinates are incerted
                    // Notice each axis should be scaled into [-1, 1]
                    final float normalizedX = (motionEvent.getX() / (float)view.getWidth())*2 - 1;
                    final float normalizedY = -((motionEvent.getY() /(float)view.getHeight()) * 2 - 1);
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mySurfaceView.handleTouchPress(normalizedX, normalizedY);
                        case MotionEvent.ACTION_MOVE:
                            mySurfaceView.handleTouchDrag(normalizedX, normalizedY);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        Log.d("jiazai", "gles version "+ configurationInfo.reqGlEsVersion);
        setContentView(mySurfaceView);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mySurfaceView.onResume();
    }
}
