package com.example.jiazai.AirhockeyActivity;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
    MyRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        init();
    }
    private void init(){
        // 创建一个OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        //创建渲染器实例
        mRenderer = new MyRenderer(getContext());
        // 设置渲染器
        setRenderer(mRenderer);
        //设置渲染模式
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
