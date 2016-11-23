package com.nd.idolshow.imageview;

import android.opengl.GLES20;

/**
 * Created by Administrator on 2015/10/27.
 */

public class ImageWhite extends GPUImageFilter
{
    /**
     * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
     */
    public static final String BRIGHTNESS_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform lowp float brightness;\n" +
            " \n" +

            //"皮肤检测算法(较粗糙)"
            "lowp vec4 skinDetectAndWhite(lowp vec4 textureColor) \n" +
            "{\n"+
            //"     if(textureColor.r > 0.37254 && textureColor.g > 0.15686 && textureColor.b > 0.07843 && \n"+
            "     if(textureColor.r > 0.13725 && textureColor.g > 0.07843 && textureColor.b > 0.03921 && \n"+
            "        textureColor.r > textureColor.b && textureColor.r > textureColor.g && \n" +
            "        abs(textureColor.r - textureColor.g) > 0.048 && abs(textureColor.g - textureColor.b) > 0.03922)\n" +
            "     {\n "+
            "        mediump float max, min;\n" +
            "        if(textureColor.b >= textureColor.g)\n" +
            "        {\n" +
            "           max = textureColor.b;\n" +
            "           min = textureColor.g;\n" +
            "        }\n" +
            "        else\n" +
            "        {\n"    +
            "           max = textureColor.g;\n" +
            "           min = textureColor.b;\n" +
            "        }\n"    +
            "        if(textureColor.r > max)\n" +
            "        {\n"    +
            "          max = textureColor.r;\n"   +
            "        }\n"    +
            "        else if(textureColor.r < min)\n" +
            "        {\n"    +
            "           min = textureColor.r;\n"  +
            "        }\n"    +
            "        \n"     +
            "        if(max - min > 0.05882)\n" +
            "        {\n"    +
            "            textureColor.rgb = textureColor.rgb + vec3(brightness);\n"  +
            "        }\n"    +
            "    }\n" +
            "    return textureColor;\n"  +
            "}\n "+

            " void main()\n" +
            " {\n" +
            "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            //"     textureColor = skinDetectAndWhite(textureColor);\n" +
            "     textureColor.rgb = textureColor.rgb + vec3(brightness);\n"  +
            "     \n" +
            "     gl_FragColor = textureColor;\n" +
            " }";

    private int mBrightnessLocation;
    private float mBrightness;

    private boolean mbWhite;

    public ImageWhite()
    {
        this(0.15f);
    }

    public ImageWhite(final float brightness)
    {
        super(NO_FILTER_VERTEX_SHADER, BRIGHTNESS_FRAGMENT_SHADER);
        mBrightness = brightness;

        mbWhite = false;
    }

    @Override
    public void onInit()
    {
        super.onInit();
        mBrightnessLocation = GLES20.glGetUniformLocation(getProgram(), "brightness");

        onInitialized();
    }

    @Override
    public void onInitialized()
    {
        super.onInitialized();
        setBrightness(mBrightness);
    }

    public void setBrightness(final float brightness)
    {
        mBrightness = brightness;
        setFloat(mBrightnessLocation, mBrightness);
    }

    public void setIsWhite(final boolean bWhite)
    {
        mbWhite = bWhite;
    }

    public boolean getIsWhite()
    {
        return mbWhite;
    }
}
