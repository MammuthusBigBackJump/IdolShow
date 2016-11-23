package com.nd.idolshow.facetest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.nd.idolshow.NdLog;
import com.nd.idolshow.R;
import com.nd.idolshow.facetest.color.ColorArt;
import com.nd.idolshow.facetest.util.DetectBean;
import com.nd.idolshow.facetest.util.FaceImageStruct;
import com.nd.idolshow.facetest.util.FaceUtil;
import com.nd.idolshow.facetest.util.LandMarkBean;
import com.nd.idolshow.imageview.GPUImageFilter;
import com.nd.idolshow.imageview.GPUImageFilterGroup;
import com.nd.idolshow.imageview.GPUImageLevelsFilter;
import com.nd.idolshow.imageview.GPUImageFilterTools.FilterAdjuster;
import com.nd.idolshow.imageview.GPUImageView;
import com.nd.idolshow.imageview.ImagePortraiture;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class OnlineFaceDemo extends Activity implements SeekBar.OnSeekBarChangeListener, OnClickListener
        ,GPUImageView.OnPictureSavedListener
        ,Callback {

    private Bitmap mImage = null;
    private FaceImageStruct faceImage = null;
    private byte[] mImageData = null;

    private ProgressDialog mProDialog;
    private Handler mHandler;
    private LandMarkBean landMarkBean;

    private float scalf = 6.44f;
    Uri uri = null;

    private GPUImageView mGPUImageView;
    private GPUImageFilterGroup mWhiteFilterGroup = null;       // 美白着色器集合


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                if (landMarkBean != null) {
                    align();
                }

                break;
            case 1:
                if (null != mProDialog) {
                    mProDialog.dismiss();
                }
                Toast.makeText(OnlineFaceDemo.this, "聚焦失败", Toast.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online_demo);
        mHandler = new Handler(this);

        uri = getIntent().getData();
        findViewById(R.id.online_pick).setOnClickListener(OnlineFaceDemo.this);

        mGPUImageView =(GPUImageView) findViewById(R.id.online_gpuimage);
        mWhiteFilterGroup = new GPUImageFilterGroup();
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");

        setImageView();
    }

    //人脸识别
    public void detect(byte[] b) {
        HttpRequests httpRequests = new HttpRequests(
                "324d5b2783733b4be6d902ab6263aecb",
                "vKZPl0fgSB1l5cC0nEqGQ9DZwHc1OWkk", true, true);
        JSONObject result = null;
        JSONObject result2 = null;
        DetectBean detectBean = null;

        try {
            // detection/compose
            PostParameters parameters = new PostParameters().setImg(b);
            result = httpRequests.detectionDetect(parameters);
            System.out.println(result);

            detectBean = JSON.parseObject(result.toString(), DetectBean.class);
            result2 = httpRequests.detectionLandmark(new PostParameters()
                    .setFaceId(detectBean.face.get(0).face_id));

            landMarkBean = JSON.parseObject(result2.toString(),
                    LandMarkBean.class);
            mHandler.sendEmptyMessage(0);
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(1);
        }

    }


    // 人脸检测
    private void align() {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

        Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                mImage.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mImage, new Matrix(), null);

        if (null != mImageData) {
            compose();
        } else {
            Toast.makeText(this, "请选择图片后再检测", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.online_pick:

                if (null != mImageData && null != mImage) {
                    mProDialog.setMessage("聚焦中...");
                    mProDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            detect(mImageData);
                        }
                    }).start();

                } else {
                    Toast.makeText(this, "请选择图片后再聚集", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void finish() {
        if (null != mProDialog) {
            mProDialog.dismiss();
        }
        super.finish();
    }

    // 图片合成
    private void compose() {

        Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                mImage.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mImage, new Matrix(), null);
        Bitmap newBitmap = null;

        if (landMarkBean != null) {
            LandMarkBean.LandMark landMark = landMarkBean.result.get(0).landMark;
            faceImage = new FaceImageStruct(mImage,landMark);
            FaceUtil.saveFaceIamgeToFile(OnlineFaceDemo.this, faceImage.bitmap,"face_origin.jpg");
            Bitmap faceClip = FaceUtil.contourImageClip(faceImage);
            Bitmap colorClip = FaceUtil.getColorClipRegion(faceClip);
            FaceUtil.saveFaceIamgeToFile(OnlineFaceDemo.this, colorClip, "face_color_clip.png");
            Log.d("OnlineFaceDemo", "compose - faceclip");
            faceImage.printPosition();
//            float x1 = (float) (landMark.left_eyebrow_upper_left_quarter.x * scalf);
//            float y1 = (float) (landMark.left_eyebrow_upper_middle.y * scalf);
//            float x2 = (float) (landMark.right_eyebrow_upper_right_quarter.x * scalf);
//            float y2 = (float) (landMark.mouth_lower_lip_bottom.y * scalf);
//
//            newBitmap = Bitmap.createBitmap(bitmap, (int) (x1), (int) (y1 - 5),
//                    (int) (x2 - x1), (int) (y2 - y1 + 15));

//            faceImage.tailorRecImage(faceImage.left_eyebrow_upper_left_quarter.x, faceImage.left_eyebrow_upper_middle.y - 5,
//                    faceImage.right_eyebrow_upper_right_quarter.x - faceImage.left_eyebrow_upper_left_quarter.x,
//                    faceImage.mouth_lower_lip_bottom.y - faceImage.left_eyebrow_upper_middle.y + 20);
            faceImage.tailorRecImage(faceImage.left_eyebrow_left_corner.x -7, faceImage.left_eyebrow_upper_middle.y - 10,
                    faceImage.right_eyebrow_right_corner.x - faceImage.left_eyebrow_left_corner.x + 14,
                    faceImage.mouth_lower_lip_bottom.y - faceImage.left_eyebrow_upper_middle.y + 50);
        }

        if (faceImage.bitmap != null) {
            FaceUtil.saveFaceIamgeToFile(OnlineFaceDemo.this, faceImage.bitmap,"face1.jpg");
            mImage = faceImage.bitmap;
//            ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
        } else {
            mImage = bitmap;
//            ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
        }

        mGPUImageView.getGPUImage().deleteImage();
        mGPUImageView.setImage(faceImage.bitmap);

        mWhiteFilterGroup.setIsWhiteImageProcess(true);
        FilterAdjuster whiteFilterAdjuster = null;
        GPUImageFilter WhiteFilter = new GPUImageLevelsFilter();
        //mWhiteFilterGroup.addFilter(WhiteFilter);
        mWhiteFilterGroup.addFilter(WhiteFilter);



        whiteFilterAdjuster = new FilterAdjuster(WhiteFilter);
        whiteFilterAdjuster.adjust(5);

        mWhiteFilterGroup.setIsPortraitureImageProcess(true);
        GPUImageFilter portraitureFilter = new ImagePortraiture();
        mWhiteFilterGroup.addFilter(portraitureFilter);



        mGPUImageView.setFilter(mWhiteFilterGroup);
        mGPUImageView.saveToPictures("GPUImage", "test.jpg", faceImage.bitmap.getWidth(), faceImage.bitmap.getHeight(), this);
//        FaceUtil.ComposeFaceImage(OnlineFaceDemo.this,faceImage);

//        if (null != mProDialog) {
//            mProDialog.dismiss();
//        }
//
//
//        ColorArt colorArt = new ColorArt(FaceUtil.getScaleFace(this));
//        int color1 = colorArt.getPrimaryColor();
////        int color2 = colorArt.getSecondaryColor();
////        int color3 = colorArt.getDetailColor();
//
//
//
//        int red1 = (color1 & 0xff0000) >> 16;
//        int green1 = (color1 & 0x00ff00) >> 8;
//        int blue1 = (color1 & 0x0000ff);
//
////        int red2 = (color2 & 0xff0000) >> 16;
////        int green2 = (color2 & 0x00ff00) >> 8;
////        int blue2 = (color2 & 0x0000ff);
//
////        int red3 = (color3 & 0xff0000) >> 16;
////        int green3 = (color3 & 0x00ff00) >> 8;
////        int blue3 = (color3 & 0x0000ff);
//
////        int red = (red1 + red2 ) / 2;
////        int green = (green1 + green2 ) / 2;
////        int blue = (blue1 + blue2 ) / 2;
//
//
//        System.out.println(" red1 : " + red1 + " green1 : " + green1 + " blue1 : " + blue1);
////        System.out.println(" red2 : " + red2 + " green2 : " + green2 + " blue2 : " + blue2);
////        System.out.println(" red : " + red + " green : " + green + " blue : " + blue);
//
////        mGPUImageView.getGPUImage().deleteImage();
////        mGPUImageView.setImage(FaceUtil.getAssetsImage(this, "body_head_d_3.png"));
////
////        mGPUImageRGBFilter = new GPUImageRGBFilter((float) red1 / 255, (float) green1 / 255, (float) blue1 / 255);
////        mGPUImageView.setFilter(mGPUImageRGBFilter);
////        mGPUImageView.requestRender();
////
////        String fileName =  "test.jpg";
////        mGPUImageView.saveToPictures("GPUImage", fileName,1024,1024, this);
//
//        finish();

    }

    private void setImageView() {

        try {
            mImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//            mImage = FaceUtil.getAssetsImage(this, "body_head_d_3.png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            mImageData = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mImage != null) {
            ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
        }

    }

    private float range(final int percentage, final float start, final float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        if (mFilter != null) {
//            mFilter.setBlurSize(range(progress, 0.0f, 1.0f));
//        }
//        mGPUImageView.requestRender();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onPictureSaved(Uri uri) {
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            faceImage.bitmap = bitmap;
            FaceUtil.saveFaceIamgeToFile(OnlineFaceDemo.this, faceImage.bitmap,"face.jpg");
            FaceUtil.ComposeFaceImage(OnlineFaceDemo.this, faceImage);

            if (null != mProDialog) {
                mProDialog.dismiss();
            }
            finish();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
