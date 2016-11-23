package com.nd.idolshow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.graphics.Palette;

import org.michaelevans.colorart.library.ColorArt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainPalette extends Activity {

    Bitmap bitmap_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        bitmap_photo = getBitmap(this);

        ColorArt colorArt = new ColorArt(bitmap_photo);

        // get the colors
        saveBitmap(colorArt.getBackgroundColor(), "getBackgroundColor");
        saveBitmap(colorArt.getPrimaryColor(), "getPrimaryColor");
        saveBitmap(colorArt.getSecondaryColor(), "getSecondaryColor");
        saveBitmap(colorArt.getDetailColor(), "getDetailColor");

        if (bitmap_photo != null) {
            Palette.Builder builder = Palette.from(bitmap_photo);
            builder.generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch getVibrantSwatch = palette.getVibrantSwatch();       //获取到充满活力的这种色调
                    Palette.Swatch getDarkVibrantSwatch = palette.getDarkVibrantSwatch();    //获取充满活力的黑
                    Palette.Swatch getLightVibrantSwatch = palette.getLightVibrantSwatch();   //获取充满活力的亮

                    Palette.Swatch getMutedSwatch = palette.getMutedSwatch();           //获取柔和的色调
                    Palette.Swatch getDarkMutedSwatch = palette.getDarkMutedSwatch();      //获取柔和的黑
                    Palette.Swatch getLightMutedSwatch = palette.getLightMutedSwatch();    //获取柔和的亮

                    if (getVibrantSwatch != null) {
                        System.out.println("getVibrantSwatch : " + getVibrantSwatch.getRgb());
                        saveBitmap(getVibrantSwatch.getRgb(), "getVibrantSwatch");
                    }else {
                        System.out.println("getVibrantSwatch is null: ");
                    }

                    if (getDarkVibrantSwatch!=null) {
                        System.out.println("getDarkVibrantSwatch : " + getDarkVibrantSwatch.getRgb());
                        saveBitmap(getDarkVibrantSwatch.getRgb(), "getDarkVibrantSwatch");
                    }else {
                        System.out.println("getDarkVibrantSwatch is null: ");
                    }

                    if (getLightVibrantSwatch!=null) {
                        System.out.println("getLightVibrantSwatch : " + getLightVibrantSwatch.getRgb());
                        saveBitmap(getLightVibrantSwatch.getRgb(), "getLightVibrantSwatch");
                    }else {
                        System.out.println("getLightVibrantSwatch is null: ");
                    }

                    if (getMutedSwatch!=null) {
                        System.out.println("getMutedSwatch : " + getMutedSwatch.getRgb());
                        saveBitmap(getMutedSwatch.getRgb(), "getMutedSwatch");
                    }else {
                        System.out.println("getMutedSwatch is null: ");
                    }

                    if (getDarkMutedSwatch!=null) {
                        System.out.println("getDarkMutedSwatch : " + getDarkMutedSwatch.getRgb());
                        saveBitmap(getDarkMutedSwatch.getRgb(), "getDarkMutedSwatch");
                    }else {
                        System.out.println("getDarkMutedSwatch is null: ");
                    }

                    if (getLightMutedSwatch!=null) {
                        System.out.println("getLightMutedSwatch : " + getLightMutedSwatch.getRgb());
                        saveBitmap(getLightMutedSwatch.getRgb(), "getLightMutedSwatch");
                    }else {
                        System.out.println("getLightMutedSwatch is null: ");
                    }

                }
            });

        } else {
            System.out.println("bitmap_photo is null");
        }

    }


    public Bitmap getBitmap(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap mImage = BitmapFactory.decodeFile(getImagePath2(context), options);

        return mImage;
    }

    public String getImagePath2(Context context) {
        String path;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getFilesDir().getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/";
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        File folder = new File(path);
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        path += "ifd2_1476778549097.jpg";
        System.out.println("_____________________________________________________________" + path);
        return path;
    }

    public void saveBitmap(int color, String filePath) {
        Bitmap bitmap = Bitmap.createBitmap(300, 300, bitmap_photo.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        saveBitmapToFile(this, bitmap, filePath);

    }

    public void saveBitmapToFile(Context context, Bitmap bmp, String filePath) {
        String file_path = getImagePath(context, filePath);
        System.out.println("合成图片地址 : " + file_path);
        File file = new File(file_path);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImagePath(Context context, String filePath) {
        String path;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getFilesDir().getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/";
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        File folder = new File(path);
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        path += filePath + ".jpg";
        return path;
    }
}
