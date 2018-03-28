package com.example.jiazai.firstopengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

public class MainActivity extends AppCompatActivity {

    MyGLSurfaceView mySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySurfaceView = new MyGLSurfaceView(this);
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
