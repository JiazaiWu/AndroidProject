package com.example.jiazai.patchupdate;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("Test patch update");

        Button diffB = findViewById(R.id.makediff);
        Button patchB = findViewById(R.id.makepatch);

        diffB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "to make diff patch", Toast.LENGTH_SHORT).show();
                new DiffTask().execute();
            }
        });

        patchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "to use patch", Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private class DiffTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            String appPath = getSelfApkPath();
            String newAppPath = Environment.getExternalStorageDirectory()+"/new.apk";
            String patchPath = Environment.getExternalStorageDirectory()+"/update.patch";
            Log.d("test", "new apk at " + newAppPath);
            Log.d("test", "patch at " + patchPath);

            int res = diff(appPath, newAppPath, patchPath);
            if (res == 0 ) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "patch made!", Toast.LENGTH_SHORT).show();
                    }
                });
                return 1;
            }
            return 0;
        }
    }



    public String getSelfApkPath() {
        String packagePath = getApplicationContext().getPackageResourcePath();
        return packagePath;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //生成差分包
    public native int diff(String oldpath,String newpath,String path);
    //旧apk和差分包合并
    public native int patchUpdate(String oldpath,String newpath,String path);
}
