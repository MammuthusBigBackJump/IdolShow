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

package com.nd.idolshow.imageview;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.nd.idolshow.OpenGlUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


import static com.nd.idolshow.imageview.TextureRotationUtil.TEXTURE_NO_ROTATION;

public class GPUImageTwoInputFilter extends GPUImageFilter
{
    private static final String VERTEX_SHADER =
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            "attribute vec4 inputTextureCoordinate2;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 textureCoordinate2;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    textureCoordinate2 = inputTextureCoordinate2.xy;\n" +
            "}";

    public int mFilterSecondTextureCoordinateAttribute;
    public int mFilterInputTextureUniform2;
    public int mFilterSourceTexture2 = OpenGlUtils.NO_TEXTURE;
    private ByteBuffer mTexture2CoordinatesBuffer;
    private Bitmap mBitmap;

    public GPUImageTwoInputFilter(String fragmentShader)
    {
        this(VERTEX_SHADER, fragmentShader);
    }

    public GPUImageTwoInputFilter(String vertexShader, String fragmentShader)
    {
        super(vertexShader, fragmentShader);
        setRotation(Rotation.NORMAL, false, false);
    }

    @Override
    public void onInit()
    {
        super.onInit();

        mFilterSecondTextureCoordinateAttribute = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
        mFilterInputTextureUniform2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2"); // This does assume a name of "inputImageTexture2" for second input texture in the fragment shader
        GLES20.glEnableVertexAttribArray(mFilterSecondTextureCoordinateAttribute);

        if (mBitmap != null && !mBitmap.isRecycled())
        {
            setBitmap(mBitmap);
        }
    }

    public void setBitmap(final Bitmap bitmap)
    {
        mBitmap = bitmap;
        runOnDraw(new Runnable()
        {
            public void run()
            {
                if (mFilterSourceTexture2 == OpenGlUtils.NO_TEXTURE)
                {
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
                    mFilterSourceTexture2 = OpenGlUtils.loadTexture(bitmap, OpenGlUtils.NO_TEXTURE, false);
                }
            }
        });
    }

    @Override
    public void setTexture(final int textureId)
    {
        runOnDraw(new Runnable()
        {
            public void run()
            {
                if (mFilterSourceTexture2 == OpenGlUtils.NO_TEXTURE)
                {
                    mFilterSourceTexture2 = textureId;
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFilterSourceTexture2);
                }
            }
        });
    }

    public void setRotation(final Rotation rotation, final boolean flipHorizontal, final boolean flipVertical)
    {
        float[] buffer = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);// 加载纹理坐标

        ByteBuffer bBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder());
        FloatBuffer fBuffer = bBuffer.asFloatBuffer();
        fBuffer.put(buffer);
        fBuffer.flip();

        mTexture2CoordinatesBuffer = bBuffer;
    }

    @Override
    protected void onDrawArraysPre()
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFilterSourceTexture2);
        GLES20.glUniform1i(mFilterInputTextureUniform2, 3);

        mTexture2CoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mFilterSecondTextureCoordinateAttribute, 2, GLES20.GL_FLOAT, false, 0, mTexture2CoordinatesBuffer);
        GLES20.glEnableVertexAttribArray(mFilterSecondTextureCoordinateAttribute);
    }

    public void onDestroy()
    {
        super.onDestroy();
        GLES20.glDeleteTextures(1, new int[]{mFilterSourceTexture2}, 0);
        mFilterSourceTexture2 = OpenGlUtils.NO_TEXTURE;
    }
}
