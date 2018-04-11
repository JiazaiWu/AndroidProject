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

    public void handleTouchPress(final float normalizedX, final float normalizedY) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.handleTouchPress(normalizedX, normalizedY);
            }
        });
    }

    public void handleTouchDrag(final float normalizedX, final float normalizedY) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.handleTouchDrag(normalizedX, normalizedY);
            }
        });
    }
}
