package com.nd.idolshow.imageview;

import android.opengl.GLES20;

/**
 * Created by Administrator on 2015/11/27.
 */
public class ImagePortraiture extends GPUImageFilter
{
    public static final String BILATERAL_VERTEX_SHADER = "" +
        "attribute vec4 position;\n" +
        "attribute vec4 inputTextureCoordinate;\n" +

        "const int GAUSSIAN_SAMPLES = 24;\n" +

        "uniform vec2 singleStepOffset;\n" +  //采样步长

        "varying vec2 textureCoordinate;\n" +
        "varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +

        "void main()\n" +
        "{\n" +
        "	gl_Position = position;\n" +
        "	textureCoordinate = inputTextureCoordinate.xy;\n" +

        "   int samplesNum = 0;\n" +
        "   int radius = (int(sqrt(float(GAUSSIAN_SAMPLES))) - 1) / 2;\n" +
        "   for(int i = -radius; i <= radius; ++i)\n" +
        "   {\n" +
        "      for(int j = -radius; j <= radius; ++j)\n" +
        "      {\n" +
        "		     int multiplier = (samplesNum - ((GAUSSIAN_SAMPLES - 1) / 2));\n" +
        "            blurCoordinates[samplesNum].x = inputTextureCoordinate.x + float(i) * singleStepOffset.x * float(multiplier);\n"+
        "            blurCoordinates[samplesNum++].y = inputTextureCoordinate.y + float(j) * singleStepOffset.y * float(multiplier);\n"+
        "      }\n" +
        "   }\n" +
        "}";

    public static final String BILATERAL_FRAGMENT_SHADER = "" +
		"uniform sampler2D inputImageTexture;\n" +

		"const lowp int GAUSSIAN_SAMPLES = 24;\n" +

		"varying lowp vec2 textureCoordinate;\n" +
		"varying lowp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +

		"uniform lowp float distanceSigma;\n" + // 坐标空间标准差
		"uniform lowp float colorSigma;\n" +    // 颜色空间标准差

        //"lowp float distanceWeight[GAUSSIAN_SAMPLES];\n"+

        /*"void initDistanceWeight()\n"+
        "{\n"+
            //sigma = 0.84089642
        "   for(int i = 0; i < GAUSSIAN_SAMPLES; ++i)\n" +
        "   {\n" +
        "       if(i == 0 || i == 4 || i == 20 || i == 24) \n" +
        "       {\n"+
        "           distanceWeight[i] = 0.00000658573;\n" +
        "       }\n"+

        "       if(i == 1 || i == 3 || i == 5 || i == 9 || i == 15 || i == 19 || i == 21 || i == 23) \n" +
        "       {\n"+
        "           distanceWeight[i] = 0.000424781;\n" +
        "       }\n"+

        "       if(i == 2 || i == 10 || i == 14 || i == 22) \n" +
        "       {\n"+
        "           distanceWeight[i] = 0.00170354;\n" +
        "       }\n"+

        "       if(i == 6 || i == 8 || i == 16 || i == 18) \n" +
        "       {\n"+
        "           distanceWeight[i] = 0.0273984;\n" +
        "       }\n"+

        "       if(i == 7 || i == 11 || i == 13 || i == 17) \n" +
        "       {\n"+
        "           distanceWeight[i] = 0.109878;\n" +
        "       }\n"+

        "       if(i == 12) \n" +
        "       {\n"+
        "           distanceWeight[i] = 0.440655;\n" +
        "       }\n"+
        "   }\n"+
        "}\n"+*/

        // 距离权重
        "lowp float calDistanceFactor(const lowp vec2 centralCoordinates, const lowp vec2 sampleCoordinates)\n" +
        "{\n" +
        "	lowp float distanceValue = distance(centralCoordinates, sampleCoordinates);\n" +
        "	lowp float distanceValueSquare = pow(distanceValue, 2.0);\n" +
        "	return distanceValueSquare / (2.0 * pow(distanceSigma, 2.0));\n" +
        "}\n" +

        // 颜色相似度权值
        "lowp float calColorFactor(const lowp vec4 centralColor, const lowp vec4 sampleColor)\n" +
        "{\n" +
        "	lowp float colorSimilarity = distance(centralColor, sampleColor);\n" +
        "	lowp float colorSimilaritySquare = pow(colorSimilarity, 2.0);\n" +
        "	return colorSimilaritySquare / (2.0 * pow(colorSigma, 2.0));\n" +
        "}\n" +

        // 距离与颜色权重乘积
		"lowp float calWeightFactor(const lowp vec2 centralCoordinates, const lowp vec2 sampleCoordinates,\n" +
		"                           const lowp vec4 centralColor, const lowp vec4 sampleColor)\n" +
		"{\n" +
		"	lowp float distanceWeightFactor = calDistanceFactor(centralCoordinates, sampleCoordinates);\n" +
		"	lowp float colorWeightFactor = calColorFactor(centralColor, sampleColor);\n" +
		"	lowp float weightFactor = exp(-distanceWeightFactor - colorWeightFactor);\n" +
		"	return weightFactor;\n" +
		"}\n" +

        // 距离与颜色权重乘积
       /* "lowp float calWeightFactor(const int i, const lowp vec4 centralColor, const lowp vec4 sampleColor)\n" +
        "{\n" +
        "	lowp float distanceWeightFactor = distanceWeight[i];\n" +
        "	lowp float colorWeightFactor = calColorFactor(centralColor, sampleColor);\n" +
        "	lowp float weightFactor = distanceWeightFactor * exp(-colorWeightFactor);\n" +
        "	return weightFactor;\n" +
        "}\n" +*/

        // sigma = 0.84089642
		"void main()\n" +
		"{\n" +
        "    lowp vec4 centralColor; //卷积核中心像素颜色值 \n"+
        "    lowp float gaussianWeightTotal; \n" +
        "    lowp vec4 sum;\n" +
        "    lowp vec4 sampleColor; //邻域像素颜色值\n" +
        "    lowp float distanceFromCentralColor;\n"+
        "    lowp float gaussianWeight;\n"+

        "    centralColor = texture2D(inputImageTexture, blurCoordinates[4]);\n"+
        "    gaussianWeightTotal = 0.253502;\n"+
        "    sum = centralColor * 0.253502;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[0]);\n" +
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.0616306 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[1]);\n" +
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.124994 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[2]);\n"+
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.0616306 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[3]);\n"+
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.124994 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[5]);\n"+
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.124994 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[6]);\n"+
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.0616306 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[7]);\n"+
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.124994 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    sampleColor = texture2D(inputImageTexture, blurCoordinates[8]);\n"+
        "    //distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n"+
        "    distanceFromCentralColor = calColorFactor(centralColor, sampleColor);\n"+
        "    //gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n"+
        "    gaussianWeight = 0.0616306 * exp(-distanceFromCentralColor);\n"+
        "    gaussianWeightTotal += gaussianWeight;\n"+
        "    sum += sampleColor * gaussianWeight;\n"+

        "    gl_FragColor = sum / gaussianWeightTotal;\n"+
		"}";

    private float mDistanceSigma;
    private float mColorSigma;
    private int mDistanceSigmaLocation;
    private int mColorSigmaLocation;
    private int mSingleStepOffsetLocation;

    public ImagePortraiture()
    {
        this(30.0f, 0.1f);
    }

    public ImagePortraiture(final float distanceSigma, final float colorSigma)
    {
        super(BILATERAL_VERTEX_SHADER, BILATERAL_FRAGMENT_SHADER);
        mDistanceSigma = distanceSigma;
        mColorSigma = colorSigma;
    }

    @Override
    public void onInit()
    {
        super.onInit();
        mDistanceSigmaLocation = GLES20.glGetUniformLocation(getProgram(), "distanceSigma");
        mColorSigmaLocation = GLES20.glGetUniformLocation(getProgram(), "colorSigma");
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");

        onInitialized();
    }

    @Override
    public void onInitialized()
    {
        super.onInitialized();
        setDistanceSigma(mDistanceSigma);
        setColorSigma(mColorSigma);
    }

    public void setDistanceSigma(final float newValue)
    {
        mDistanceSigma = newValue;
        setFloat(mDistanceSigmaLocation, newValue);
    }

    public void setColorSigma(final float newValue)
    {
        mColorSigma = newValue;
        setFloat(mColorSigmaLocation, newValue);
    }

    private void setTexelSize(final float width, final float height)
    {
        // 修改TexelSize能调整模糊效果, (如3.0)此值一定程度上越大,模糊效果越明显
        setFloatVec2(mSingleStepOffsetLocation, new float[]{1.0f / width, 1.0f / height});
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height)
    {
        super.onOutputSizeChanged(width, height);
        setTexelSize(width, height);
    }
}
