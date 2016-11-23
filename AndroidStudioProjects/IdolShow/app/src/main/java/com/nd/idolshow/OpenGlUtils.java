/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nd.idolshow;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

public class OpenGlUtils
{
    public static final int NO_TEXTURE = -1;

    public static int loadTexture(final Bitmap img, final int usedTexId)
    {
        return loadTexture(img, usedTexId, true);
    }

    public static int loadTexture(final Bitmap img, final int usedTexId, final boolean recycle)
    {
        int textures[] = new int[1];
        if (usedTexId == NO_TEXTURE)
        {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
        }
        else
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
            GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, img);
            textures[0] = usedTexId;
        }
        if (recycle)
        {
            img.recycle();
        }
        return textures[0];
    }

    public static int loadTexture(final IntBuffer data, final Size size, final int usedTexId)
    {
        int textures[] = new int[1];
        if (usedTexId == NO_TEXTURE)
        {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, size.width, size.height,
                    0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
        }
        else
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, size.width,
                    size.height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
            textures[0] = usedTexId;
        }
        return textures[0];
    }

    public static int loadTextureAsBitmap(final IntBuffer data, final Size size, final int usedTexId)
    {
        Bitmap bitmap = Bitmap.createBitmap(data.array(), size.width, size.height, Config.ARGB_8888);
        return loadTexture(bitmap, usedTexId);
    }

    public static int loadShader(final String strSource, final int iType)
    {
        int iShader = GLES20.glCreateShader(iType);//create a shader object and record id
        if (iShader != 0)
        {
            GLES20.glShaderSource(iShader, strSource);

            GLES20.glCompileShader(iShader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(iShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0)
            {
                Log.d("ES20_ERROR", "Compilation Shader Error " + GLES20.glGetShaderInfoLog(iShader));
                GLES20.glDeleteShader(iShader);
                return 0;
            }
        }
        else
        {
            Log.d("ES20_ERROR", "Create Shader Error " + GLES20.glGetShaderInfoLog(iShader));
        }

        return iShader;
    }

    //VertexSource--vertex shader  FragmentSource--fragment shader
    public static int loadProgram(final String VertexSource, final String FragmentSource)
    {
        //load vertex shader into GPU
        int iVertexShader = loadShader(VertexSource, GLES20.GL_VERTEX_SHADER);
        if (iVertexShader == 0)
        {
            Log.d("Load Program", "Vertex Shader Failed");
            return 0;
        }

        //load fragment shader into GPU
        int iFragmentShader = loadShader(FragmentSource, GLES20.GL_FRAGMENT_SHADER);
        if (iFragmentShader == 0)
        {
            Log.d("Load Program", "Fragment Shader Failed");
            return 0;
        }

        int iProgId = GLES20.glCreateProgram();//create program
        if (iProgId != 0)
        {
            GLES20.glAttachShader(iProgId, iVertexShader);//add vertex shader into program
            GLES20.glAttachShader(iProgId, iFragmentShader);//add fragment shader into program

            GLES20.glLinkProgram(iProgId);//link program---two shader is linked as a whole shader
        }

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(iProgId, GLES20.GL_LINK_STATUS, linkStatus, 0);//check link status
        if (linkStatus[0] <= 0)
        {
            Log.d("Load Program", "Linking Failed");
            GLES20.glDeleteProgram(iProgId);
            return 0;
        }

        //free no longer needed shader resources
        GLES20.glDeleteShader(iVertexShader);
        GLES20.glDeleteShader(iFragmentShader);

        return iProgId;
    }

    public static float rnd(final float min, final float max)
    {
        float fRandNum = (float) Math.random();
        return min + (max - min) * fRandNum;
    }

    public static void savePicture(final int picWidth, final int picHeight)
    {
        IntBuffer pixelBuffer = IntBuffer.allocate(picWidth * picHeight);
        pixelBuffer.position(0);
        GLES20.glReadPixels(0, 0, picWidth, picHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);

        pixelBuffer.position(0);
        int pix[] = new int[picWidth * picHeight];
        pixelBuffer.get(pix);// 将intBuffer中的数据赋值到pix数组中

        Bitmap bmp = Bitmap.createBitmap(pix, picWidth, picHeight, Config.ARGB_8888);// pix是上面读到的像素
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream("/sdcard/savePicture.png");
        }
        catch (FileNotFoundException e)
        {
            Log.e("OpenGLUtil", "Save Picture Failed");
            e.printStackTrace();
        }

        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos); // 压缩成png, 100%显示效果
        try
        {
            fos.flush();
        }
        catch (IOException e)
        {
            Log.e("OpenGLUtil", "an error occurs while flushing this stream");
            e.printStackTrace();
        }
    }
}
