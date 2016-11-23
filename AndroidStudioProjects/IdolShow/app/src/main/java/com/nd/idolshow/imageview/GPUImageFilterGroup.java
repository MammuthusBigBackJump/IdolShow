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

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.util.Log;

import com.nd.idolshow.OpenGlUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;


import static com.nd.idolshow.imageview.GPUImageRenderer.CUBE;
import static com.nd.idolshow.imageview.TextureRotationUtil.TEXTURE_NO_ROTATION;

/**
 * Resembles a filter that consists of multiple filters applied after each
 * other.
 */
public class GPUImageFilterGroup extends GPUImageFilter
{
    protected List<GPUImageFilter> mFilters;
    protected List<GPUImageFilter> mMergedFilters;
    private int[] mFrameBuffers;
    private int[] mFrameBufferTextures;

    private final FloatBuffer mGLCubeBuffer;
    private final FloatBuffer mGLTextureBuffer;
    private final FloatBuffer mGLTextureFlipBuffer;

    private boolean mbIsWhiteImageProcess;
    private boolean mbIsPortraitureImageProcess;
    private boolean mbIsGrayImageProcess;

    private static int[] mWhiteFrameBuffer;
    protected static int[] mWhiteImageTexture;
    private static int[] mPortraitureFrameBuffer;
    protected static int[] mPortraitureImageTexture;
    private static int[] mGrayFrameBuffer;
    protected static int[] mGrayImageTexture;

    private boolean mbSavePicture;

    /**
     * Instantiates a new GPUImageFilterGroup with no filters.
     */
    public GPUImageFilterGroup()
    {
        this(null);
    }

    /**
     * Instantiates a new GPUImageFilterGroup with the given filters.
     *
     * @param filters the filters which represent this filter
     */
    public GPUImageFilterGroup(List<GPUImageFilter> filters)
    {
        mFilters = filters;
        if (mFilters == null)
        {
            mFilters = new ArrayList<GPUImageFilter>();
        } else
        {
            updateMergedFilters();
        }

        mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLCubeBuffer.put(CUBE).position(0);

        mGLTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLTextureBuffer.put(TEXTURE_NO_ROTATION).position(0);

        float[] flipTexture = TextureRotationUtil.getRotation(Rotation.NORMAL, false, true);
        mGLTextureFlipBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLTextureFlipBuffer.put(flipTexture).position(0);

        mbIsGrayImageProcess = false;
        mbIsPortraitureImageProcess = false;

        mbSavePicture = false;
    }

    @Override
    public void onInit()
    {
        super.onInit();
        for (GPUImageFilter filter : mFilters)
        {
            filter.init();
        }
    }

    @Override
    public void onDestroy()
    {
        destroyFramebuffers();
        for (GPUImageFilter filter : mFilters)
        {
            filter.destroy();
        }
        super.onDestroy();
    }

    private void destroyFramebuffers()
    {
        if (mFrameBufferTextures != null)
        {
            GLES20.glDeleteTextures(mFrameBufferTextures.length, mFrameBufferTextures, 0);
            mFrameBufferTextures = null;
        }
        if (mFrameBuffers != null)
        {
            GLES20.glDeleteFramebuffers(mFrameBuffers.length, mFrameBuffers, 0);
            mFrameBuffers = null;
        }
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height)
    {
        super.onOutputSizeChanged(width, height);
        if (mFrameBuffers != null)
        {
            destroyFramebuffers();
        }

        int size = mFilters.size();
        for (int i = 0; i < size; i++)
        {
            mFilters.get(i).onOutputSizeChanged(width, height);
        }

        if (mMergedFilters != null && mMergedFilters.size() > 0)
        {
            if(mbIsWhiteImageProcess)
            {
                createExtraFrameBufferObj(width, height);
            }

            if(mbIsPortraitureImageProcess)
            {
                createExtraFrameBufferObj(width, height);
            }

            if (mbIsGrayImageProcess)
            {
                createExtraFrameBufferObj(width, height);
            }

            size = mMergedFilters.size(); // 着色器总数
            mFrameBuffers = new int[size-1];
            mFrameBufferTextures = new int[size-1];
            int nCreateFrameBufferTotalNum = size - 1;

            for (int i = 0; i < nCreateFrameBufferTotalNum; i++)
            {
                GLES20.glGenFramebuffers(1, mFrameBuffers, i);
                GLES20.glGenTextures(1, mFrameBufferTextures, i);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[i]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES20.GL_TEXTURE_2D, mFrameBufferTextures[i], 0);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        }
    }

    private void createExtraFrameBufferObj(final int width, final int height)
    {
        if (mbIsWhiteImageProcess)
        {
            mWhiteImageTexture = new int[1];
            mWhiteFrameBuffer = new int[1];
            GLES20.glGenFramebuffers(1, mWhiteFrameBuffer, 0);
            GLES20.glGenTextures(1, mWhiteImageTexture, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mWhiteImageTexture[0]);
        }

        if (mbIsPortraitureImageProcess)
        {
            mPortraitureImageTexture = new int[1];
            mPortraitureFrameBuffer = new int[1];
            GLES20.glGenFramebuffers(1, mPortraitureFrameBuffer, 0);
            GLES20.glGenTextures(1, mPortraitureImageTexture, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPortraitureImageTexture[0]);
        }

        if (mbIsGrayImageProcess)
        {
            mGrayImageTexture = new int[1];
            mGrayFrameBuffer = new int[1];
            GLES20.glGenFramebuffers(1, mGrayFrameBuffer, 0);
            GLES20.glGenTextures(1, mGrayImageTexture, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mGrayImageTexture[0]);
        }

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        if (mbIsWhiteImageProcess)
        {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mWhiteFrameBuffer[0]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D, mWhiteImageTexture[0], 0);
        }

        if (mbIsPortraitureImageProcess)
        {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mPortraitureFrameBuffer[0]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D, mPortraitureImageTexture[0], 0);
        }

        if (mbIsGrayImageProcess)
        {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mGrayFrameBuffer[0]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D, mGrayImageTexture[0], 0);
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    @SuppressLint("WrongCall")
    @Override
    public void onDraw(final int textureId, final FloatBuffer cubeBuffer, final FloatBuffer textureBuffer)
    {
        runPendingOnDrawTasks();
        if (!isInitialized() || mFrameBuffers == null || mFrameBufferTextures == null)
        {
            return;
        }

        if (mMergedFilters != null)
        {
            int size = mMergedFilters.size();
            int previousTexture = textureId;
            for (int i = 0; i < (size + 1); i++)
            {
                if (i < size)
                {
                    GPUImageFilter filter = mMergedFilters.get(i);

                    boolean isNotLast = i < (size - 1);

                    if (isNotLast)
                    {
                        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
                        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                    }

                    if (i == 0) // 第一个
                    {
                        if (mbIsGrayImageProcess || mbIsPortraitureImageProcess || mbIsWhiteImageProcess)
                        {
                            filter.onDraw(previousTexture, cubeBuffer, textureBuffer);
                        }
                        else
                        {
                            filter.onDraw(mPortraitureImageTexture[0], mGrayImageTexture[0], cubeBuffer, mGLTextureFlipBuffer);
                        }

                    }
                    else if (i == size - 1)  // 最后一个
                    {
                        //mGLTextureFlipBuffer---垂直翻转纹理坐标; mGLTextureBuffer---正常纹理坐标
                        filter.onDraw(previousTexture, mGLCubeBuffer, (size % 2 == 0) ? mGLTextureFlipBuffer : mGLTextureBuffer);
                    }
                    else // 中间
                    {
                        filter.onDraw(previousTexture, mGLCubeBuffer, mGLTextureBuffer);
                    }

                    if (isNotLast)
                    {
                        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                        previousTexture = mFrameBufferTextures[i];
                    }
                }
                else
                {
                    GPUImageFilter filter = mMergedFilters.get(i-1);
                    if (mbIsWhiteImageProcess)
                    {
                        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mWhiteFrameBuffer[0]);
                        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                        filter.onDraw(previousTexture, mGLCubeBuffer, (size % 2 == 0) ? mGLTextureBuffer : mGLTextureFlipBuffer);
                    }

                    if (mbIsPortraitureImageProcess)
                    {
                        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mPortraitureFrameBuffer[0]);
                        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                        filter.onDraw(previousTexture, mGLCubeBuffer, (size % 2 == 0) ? mGLTextureBuffer : mGLTextureFlipBuffer);
                    }

                    if (mbIsGrayImageProcess)
                    {
                        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mGrayFrameBuffer[0]);
                        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                        filter.onDraw(previousTexture, mGLCubeBuffer, mGLTextureBuffer);
                    }

                    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

                    if (mbSavePicture)
                    {
                        long startTime = System.currentTimeMillis(); // 开始保存图片时间

                        OpenGlUtils.savePicture(2592, 1944);

                        long endTime = System.currentTimeMillis(); // 停止保存图片时间
                        Log.i("保存图片时长", (endTime - startTime) + " ms");

                        mbSavePicture = false;
                    }
                }
            }
        }
    }

    public void addFilter(GPUImageFilter aFilter)
    {
        if (aFilter == null)
        {
            return;
        }

        mFilters.add(aFilter);
        updateMergedFilters();
    }

    public void clearFilters()
    {
        if (mFilters != null)
        {
            mFilters.clear();
        }
    }

    public List<GPUImageFilter> getFilters()
    {
        return mFilters;
    }

    public List<GPUImageFilter> getMergedFilters()
    {
        return mMergedFilters;
    }

    public void updateMergedFilters()
    {
        if (mMergedFilters == null)
        {
            mMergedFilters = new ArrayList<GPUImageFilter>();
        } else
        {
            mMergedFilters.clear();
        }

        List<GPUImageFilter> filters;
        for (GPUImageFilter filter : mFilters)
        {
            if (filter instanceof GPUImageFilterGroup)
            {
                ((GPUImageFilterGroup) filter).updateMergedFilters();
                filters = ((GPUImageFilterGroup) filter).getMergedFilters();
                if (filters == null || filters.isEmpty())
                    continue;
                mMergedFilters.addAll(filters); // 往着色器集合中添加另一着色器集合
                continue;
            }
            mMergedFilters.add(filter); // 往着色器集合中添加单个着色器
        }
    }

    public void setIsWhiteImageProcess(final boolean bIsWhiteProcess)
    {
        mbIsWhiteImageProcess = bIsWhiteProcess;
    }

    public void setIsPortraitureImageProcess(final boolean bIsPortraitureProcess)
    {
        mbIsPortraitureImageProcess = bIsPortraitureProcess;
    }

    public void setIsGrayImageProcess(final boolean bIsGrayProcess)
    {
        mbIsGrayImageProcess = bIsGrayProcess;
    }
}
