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

// filter description:
// 该过滤器需两张输入图像
// 这两张图像为过滤器最初第一次和第二次遇到的图像数据, 即使后面修改了图像数据也无济于事
// 过滤器将这两幅图像的像素数据进行相减混合
public class GPUImageSubtractBlendFilter extends GPUImageTwoInputFilter
{
    public static final String SUBTRACT_BLEND_FRAGMENT_SHADER =
            "varying highp vec2 textureCoordinate;\n" +
            " varying highp vec2 textureCoordinate2;\n" +
            "\n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "   lowp vec4 processedTextureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "   lowp vec4 originalTextureColor = texture2D(inputImageTexture2, textureCoordinate2);\n" +
            "   lowp vec3 colorDelta = processedTextureColor.rgb - originalTextureColor.rgb;\n"+ // r,g,b任一分量 < 0,则将默认修改该分量值为0
            " \n" +
            "   gl_FragColor = vec4(colorDelta + 0.5, processedTextureColor.a);\n" +
            " }";

    public GPUImageSubtractBlendFilter()
    {
        super(SUBTRACT_BLEND_FRAGMENT_SHADER);
    }
}