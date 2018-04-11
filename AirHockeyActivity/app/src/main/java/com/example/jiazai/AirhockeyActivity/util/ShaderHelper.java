package com.example.jiazai.AirhockeyActivity.util;


import android.util.Log;

import static android.opengl.GLES20.*;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int[] compileStatus = new int[1];
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            if (LoggerConfig.on) {
                Log.w(TAG, "Could not create new shader");
            }
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        /* read the GL_COMPILE_STATUS and write back to compileStatus at 0th element(offset)*/
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        if (LoggerConfig.on) {
            Log.v(TAG, "Results of compiling source: \n" + shaderCode + "\n:"
                + glGetShaderInfoLog(shaderObjectId));
        }

        if(compileStatus[0] == 0) {
            //If it failed, delete the shader Object
            glDeleteShader(shaderObjectId);

            if (LoggerConfig.on) {
                Log.w(TAG, "Compilation of shader fail");
            }

            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int linkStatus[] = new int[1];
        final int programObjectId = glCreateProgram();

        if (programObjectId == 0) {
            if (LoggerConfig.on) {
                Log.w(TAG, "Could not create new program");
            }
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (LoggerConfig.on) {
            Log.v(TAG, "Results of link program:\n"
                + glGetProgramInfoLog(programObjectId));
        }
        if (linkStatus[0] == 0) {
            //If it failed, delete the program object
            glDeleteProgram(programObjectId);
            if(LoggerConfig.on) {
                Log.w(TAG, "Link program failec");
            }
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int validateStatus[] = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validate program: " + validateStatus[0] + "\nLog" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

    /**
     * Helper function that compiles the shaders, links and validates the
     * program, returning the program ID.
     */
    public static int buildProgram(String vertexShaderSource,
                                   String fragmentShaderSource) {
        int program;

        // Compile the shaders.
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        // Link them into a shader program.
        program = linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.on) {
            validateProgram(program);
        }

        return program;
    }
}
