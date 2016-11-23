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

public class GPUImageFilterTools
{
    public static GPUImageFilter createBlendFilter(Class<? extends GPUImageTwoInputFilter> filterClass, final Bitmap subtractBitmap)
    {
        try
        {
            GPUImageTwoInputFilter filter = filterClass.newInstance();
            filter.setBitmap(subtractBitmap);
            return filter;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static GPUImageFilter createBlendFilter(Class<? extends GPUImageTwoInputFilter> filterClass)
    {
        try
        {
            GPUImageTwoInputFilter filter = filterClass.newInstance();
            return filter;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static class FilterAdjuster
    {
        private final Adjuster<? extends GPUImageFilter> adjuster;

        public FilterAdjuster(final GPUImageFilter filter)
        {
            if (filter instanceof GPUImageLevelsFilter)
            {
                adjuster = new WhiteAdjuster().filter(filter);
            }
            else if (filter instanceof ImagePortraiture)
            {
                adjuster = new PortraitureAdjuster().filter(filter);
            }
            else
            {
                adjuster = null;
            }
        }

        public void adjust(final int percentage)
        {
            if (adjuster != null)
            {
                adjuster.adjust(percentage);
            }
        }

        private abstract class Adjuster<T extends GPUImageFilter>
        {
            private T filter;

            @SuppressWarnings("unchecked")
            public Adjuster<T> filter(final GPUImageFilter filter)
            {
                this.filter = (T) filter;
                return this;
            }

            public T getFilter()
            {
                return filter;
            }

            public abstract void adjust(int percentage);

            protected float adjustRangef(final int percentage, final float start, final float end)
            {
                return (end - start) * percentage / 10.0f + start;
            }
        }

        private class WhiteAdjuster extends Adjuster<GPUImageLevelsFilter>
        {
            @Override
            public void adjust(final int percentage)
            {
                getFilter().setMin(0.0f, 1.0f + adjustRangef(percentage, 0.0f, 1.0f), 1.0f);
            }
        }

        private class PortraitureAdjuster extends Adjuster<ImagePortraiture>
        {
            @Override
            public void adjust(final int percentage)
            {
               //getFilter().setDistanceNormalizationFactor(10.0f - adjustRangef(percentage, 0.0f, 10.0f));
            }
        }
    }
}