package com.example.jiazai.AirhockeyActivity;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.jiazai.AirhockeyActivity.util.LoggerConfig;
import com.example.jiazai.AirhockeyActivity.util.ShaderHelper;
import com.example.jiazai.AirhockeyActivity.util.TexrResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLES20.*;

public class MyRenderer implements GLSurfaceView.Renderer {
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final String U_COLOR = "u_Color";
    private int uColorLocation;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexdata;
    private final Context context;
    private int program;

    public MyRenderer(Context context) {
        this.context = context;
        float[] tableVertices = {
                //Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                //Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f,  0.25f
        };
        vertexdata = ByteBuffer.allocateDirect(tableVertices.length*BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexdata.put(tableVertices);
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 0f);
        String vertexShaderSource = TexrResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TexrResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (LoggerConfig.on) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        vertexdata.position(0);
        /* we decided to use two floating
           point values per vertex: an x coordinate and a y coordinate to represent the position. This means that we have
           two components (POSITION_COMPONENT_COUNT = 2)*/
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexdata);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        /* draw triangle*/
        glUniform4f(uColorLocation,1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        /* draw line*/
        glUniform4f(uColorLocation, 1.0f,1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);
        /* draw two point*/
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);

    }
}
