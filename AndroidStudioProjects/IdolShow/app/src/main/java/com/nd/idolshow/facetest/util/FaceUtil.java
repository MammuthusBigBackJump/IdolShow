package com.nd.idolshow.facetest.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;

import com.nd.idolshow.NdLog;
import com.nd.idolshow.facetest.color.ColorArt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;


/**
 *
 */
public class FaceUtil {

    public static String FilePath = "/msc/";

    public static String FaceImage = "face.jpg";
    public static String ComposeImage = "compose.jpg";

    private final static int filtercounterlevel1 = 15;
    private final static int filtercounterlevel2 = 10;
    private final static int filtercounterlevel3 = 20;

    private final static int facefiltercounterlevel1 = 2;

    /**
     * 人脸图片的路径
     */
    public static String getFaceImagePath(Context context) {
        String path;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getFilesDir().getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + FilePath;
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        File folder = new File(path);
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        path += FaceImage;
        return path;
    }

    /**
     * 人脸图片的路径
     */
    public static String getFaceImagePathName(Context context,String name) {
        String path;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getFilesDir().getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + FilePath;
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        File folder = new File(path);
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        path += name;
        return path;
    }

    /**
     * 合成图片的路径
     */
    public static String getComposeImagePath(Context context) {
        String path;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getFilesDir().getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + FilePath;
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        File folder = new File(path);
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        path += ComposeImage;
        return path;
    }


    /**
     * 保存人脸图片
     */
    public static void saveFaceIamgeToFile(Context context, Bitmap bmp,String name) {
        String file_path = getFaceImagePathName(context, name);
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

    /**
     * 保存合成图片
     */
    public static void saveComposeImageToFile(Context context, Bitmap bmp) {
        String file_path = getComposeImagePath(context);

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

    private static Bitmap echelonCompress(FaceImageStruct faceImage,int medianColor){
        Bitmap nbitmap = null;
        int width = faceImage.bitmap.getWidth();
        int height = faceImage.bitmap.getHeight();
        int[] pixels = new int[width * height];
        int[] pixbar = new int[width];
        faceImage.bitmap.getPixels(pixels,0,width,0,0,width,height);
//        int cuty = (faceImage.nose_left.y + faceImage.mouth_upper_lip_top.y)/2;
        int cuty = faceImage.nose_left.y;
        int cutx1 = faceImage.mouth_left_corner.x - 60;
        int cutx2 = faceImage.mouth_right_corner.x+ 60;
        for(int p=cuty;p<height;p++){
            for (int q=0;q<width;q++){
                if(q<cutx1)
                    pixels[p*width+q] = medianColor;
                else if(q>cutx2)
                    pixels[p*width+q] = medianColor;
            }
        }
        int start = height/3;
        float coeffpace = (float) (0.15/(height-start));
        float coeff = 0;
        int move = 0;
        int pos = 0;
        int i,j=0;
        for(i=start;i<height;i++){
            move = (int) (coeff*width/2);
            for(j=0;j<width;j++){               //compress
                pixbar[j] = medianColor;                  //clear old data
                pos = (int) (j*(1-coeff));
                pixbar[pos] = pixels[i*width+j];
            }
            for(j =0;j<width;j++){              //move
                if(j<move)
                    pixels[i*width + j]= 0;
                else if(width-j<move)
                    pixels[i*width + j] =0;
                else
                    pixels[i*width + j] = pixbar[j-move];
            }
            coeff += coeffpace;
        }
        //change the frame color as mediaColor
        //vertical
        for(i=0;i<height;i++){
            pixels[i*width] = medianColor;
            pixels[i*width+width-1] = medianColor;
        }
        //horizontal
        for(j=0;j<width;j++){
            pixels[j]= medianColor;
            pixels[width*(height-1) + j] = medianColor;
        }


        nbitmap = Bitmap.createBitmap(pixels,0,width,width,height,Bitmap.Config.ARGB_8888);
        return nbitmap;
    }


    /**
     * @return
     */
    public static Bitmap contourImageClip(FaceImageStruct imageStruct){

        Path path = new Path();
        path.moveTo(imageStruct.contour_chin.x, imageStruct.contour_chin.y);
        path.lineTo(imageStruct.contour_left9.x, imageStruct.contour_left9.y);
        path.lineTo(imageStruct.contour_left8.x, imageStruct.contour_left8.y);
        path.lineTo(imageStruct.contour_left7.x, imageStruct.contour_left7.y);
        path.lineTo(imageStruct.contour_left6.x, imageStruct.contour_left6.y);
        path.lineTo(imageStruct.contour_left5.x, imageStruct.contour_left5.y);
        path.lineTo(imageStruct.contour_left4.x, imageStruct.contour_left4.y);
        path.lineTo(imageStruct.contour_left3.x, imageStruct.contour_left3.y);
        path.lineTo(imageStruct.contour_left2.x, imageStruct.contour_left2.y);
        path.lineTo(imageStruct.contour_left1.x, imageStruct.contour_left1.y);

        path.lineTo(imageStruct.left_eyebrow_left_corner.x, imageStruct.left_eyebrow_left_corner.y);
        path.lineTo(imageStruct.left_eyebrow_upper_left_quarter.x,
                imageStruct.left_eyebrow_upper_left_quarter.y);
        path.lineTo(imageStruct.left_eyebrow_upper_middle.x, imageStruct.left_eyebrow_upper_middle.y);
        path.lineTo(imageStruct.left_eyebrow_upper_right_quarter.x,
                imageStruct.left_eyebrow_upper_right_quarter.y);
        path.lineTo(imageStruct.left_eyebrow_right_corner.x, imageStruct.left_eyebrow_right_corner.y);

        path.lineTo(imageStruct.right_eyebrow_left_corner.x, imageStruct.right_eyebrow_left_corner.y);
        path.lineTo(imageStruct.right_eyebrow_upper_left_quarter.x,
                imageStruct.right_eyebrow_upper_left_quarter.y);
        path.lineTo(imageStruct.right_eyebrow_upper_middle.x, imageStruct.right_eyebrow_upper_middle.y);
        path.lineTo(imageStruct.right_eyebrow_upper_right_quarter.x,
                imageStruct.right_eyebrow_upper_right_quarter.y);
        path.lineTo(imageStruct.right_eyebrow_right_corner.x, imageStruct.right_eyebrow_right_corner.y);

        path.lineTo(imageStruct.contour_right1.x, imageStruct.contour_right1.y);
        path.lineTo(imageStruct.contour_right2.x, imageStruct.contour_right2.y);
        path.lineTo(imageStruct.contour_right3.x, imageStruct.contour_right3.y);
        path.lineTo(imageStruct.contour_right4.x, imageStruct.contour_right4.y);
        path.lineTo(imageStruct.contour_right5.x, imageStruct.contour_right5.y);
        path.lineTo(imageStruct.contour_right6.x, imageStruct.contour_right6.y);
        path.lineTo(imageStruct.contour_right7.x, imageStruct.contour_right7.y);
        path.lineTo(imageStruct.contour_right8.x, imageStruct.contour_right8.y);
        path.lineTo(imageStruct.contour_right9.x, imageStruct.contour_right9.y);
        //path.lineTo(imageStruct.contour_chin.x, imageStruct.contour_chin.y);
        path.close();
        if(path.isEmpty()){
            Log.d("contourImageClip", "path is empty.");
        }

        Bitmap result = Bitmap.createBitmap(imageStruct.bitmap.getWidth(),
                imageStruct.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(imageStruct.bitmap, 0, 0, null);
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        //canvas.clipRect(50, 50, 100, 100);
        canvas.drawColor(Color.BLACK);

        return result;
    }

    /**
     * @param clipBitmap
     * @return
     */
    public static Bitmap getColorClipRegion(Bitmap clipBitmap){
        int width = clipBitmap.getWidth();
        int height = clipBitmap.getHeight();

        int[] pixels = new int[width * height];
        clipBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        SparseIntArray map = new SparseIntArray();
        int mode = 0;
        for (int i = 0; i < pixels.length; i++){
            if (!isFaceColor(pixels[i])){
                //pixels[i] &= 0xff000000;
                pixels[i] = 0;
            }
//            if ((pixels[i] & 0x00ffffff) != 0 && mode == 0){
//                int count = map.get(pixels[i]) + 1;
//                map.put(pixels[i], count);
//            }
        }
//        int max = -1;
//        for (int i = 0; i < map.size();i++){
//            if(map.valueAt(i) > max){
//                max = map.valueAt(i);
//                mode = map.keyAt(i);
//            }
//        }
//        if (max != -1){
//            Log.d("OnlineFaceDemo", "mode = " + mode);
//        }
        Bitmap result = Bitmap.createBitmap(pixels, 0, width, width, height,
                Bitmap.Config.ARGB_8888);
//        Bitmap colorBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(colorBitmap);
//        canvas.clipRect(0,0,width,height);
//        canvas.drawColor(mode);
        return  result;
    }

    /**
     * 网上找来的野鸡人脸色调判别条件
     * @param pixel
     * @return
     */
    private  static  boolean isFaceColor(int pixel){
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        if(
                (r > 95 && g > 40 && b > 20
                        && Math.max(Math.max(r, g), b) - Math.min(Math.min(r, g), b) > 15
                        && Math.abs(r - g) > 15 && r > g && r > b)
                        ||
                        (r > 220 && g > 210 && b > 170 && Math.abs(r - g) <= 15 && r > g && g > b)
                ){
            return true;
        }
        return false;
    }

    /*
    * 截取包含一个梯形的多边形人脸位图
     */
    private static Bitmap echelonCompress(FaceImageStruct faceImage){
        Bitmap nbitmap = null;
        int width = faceImage.bitmap.getWidth();
        int height = faceImage.bitmap.getHeight();
        int[] pixels = new int[width * height];
        int[] pixbar = new int[width];
        faceImage.bitmap.getPixels(pixels,0,width,0,0,width,height);
//        int cuty = (faceImage.nose_left.y + faceImage.mouth_upper_lip_top.y)/2;
        int cuty = faceImage.nose_left.y;
        int cutx1 = faceImage.mouth_left_corner.x - 60;
        int cutx2 = faceImage.mouth_right_corner.x+ 60;
        for(int p=cuty;p<height;p++){
            for (int q=0;q<width;q++){
                if(q<cutx1)
                    pixels[p*width+q] = 0;
                else if(q>cutx2)
                    pixels[p*width+q] = 0;
            }
        }
        int start = height/3;
        float coeffpace = (float) (0.15/(height-start));
        float coeff = 0;
        int move = 0;
        int pos = 0;
        int j=0;
        for(int i=start;i<height;i++){
            move = (int) (coeff*width/2);
            for(j=0;j<width;j++){               //compress
                pixbar[j] = 0;                  //clear old data
                pos = (int) (j*(1-coeff));
                pixbar[pos] = pixels[i*width+j];
            }
            for(j =0;j<width;j++){              //move
                if(j<move)
                    pixels[i*width + j]= 0;
                else if(width-j<move)
                    pixels[i*width + j] =0;
                else
                    pixels[i*width + j] = pixbar[j-move];
            }
            coeff += coeffpace;
        }
        Log.i("*****coeff******", "" + coeff);
        nbitmap = Bitmap.createBitmap(pixels,0,width,width,height,Bitmap.Config.ARGB_8888);
        return nbitmap;
    }

    private static int getMedianBitmapColor(FaceImageStruct faceImage){
        int x1,y1,x2,y2;
        int width = faceImage.bitmap.getWidth();
        int height = faceImage.bitmap.getHeight();
        int pixels[] = new int[width*height];
        faceImage.bitmap.getPixels(pixels,0,width,0,0,width,height);
        x1 =0;
        y1 = (faceImage.left_eye_center.y + faceImage.nose_left.y)/2;
        if(faceImage.nose_left.x >150)
            x2 = faceImage.nose_left.x -50;
        else if(faceImage.nose_left.x >100)
            x2 = faceImage.nose_left.x -30;
        else
            x2 = faceImage.nose_left.x -20;
        y2 = faceImage.nose_left.y;
        int length = (x2-x1+1)*(y2-y1+1);
        int colors[] = new int[length];
        int k=0;
        for(int i=y1;i<=y2;i++){
            for(int j=x1;j<=x2;j++){
                colors[k++]= pixels[i*width+j];
            }
        }
        Arrays.sort(colors);
        return colors[(length+1)/2];
    }

    private static Bitmap createTransitFaceImage(Bitmap midBitmap, FaceImageStruct faceImage){
        Bitmap resultMidBitmap = null;
        int midwidth = midBitmap.getWidth();
        int midheight = midBitmap.getHeight();
        int facewidth = faceImage.bitmap.getWidth();
        int faceheight = faceImage.bitmap.getHeight();
        int[] midpixs = new int[midwidth * midheight];
        int[] facepixs = new int[facewidth * faceheight];
        int x1_pos = ModelImagePosition.LEFT_EYE_CENTER.x - faceImage.left_eye_center.x;
        int y1_pos = ModelImagePosition.LEFT_EYE_CENTER.y - faceImage.left_eye_center.y;
        int x2_pos = x1_pos + facewidth-1;
        int y2_pos = y1_pos + faceheight-1;

        int startx =0, starty = 0;
        int endx = 0, endy =0;
        int startR = 0, startG = 0, startB = 0;
        int endR = 0, endG = 0, endB = 0;
        int tmpR = 0, tmpG = 0, tmpB = 0;
        int i,j,k,p,q;
        int tmpcolor = 0;
        float diffR = 0, diffG = 0, diffB = 0;
        int filterindex = 0;


//        NdLog.info("midwidth: "+midwidth +", midheigh: "+midheight+", facewidth: "+facewidth+", faceheigh: "+faceheight);
//        NdLog.info("x1: "+x1_pos +", y1: "+y1_pos+", x2: "+x2_pos+", y2: "+y2_pos);
        faceImage.bitmap.getPixels(facepixs, 0, facewidth, 0, 0, facewidth, faceheight);
        midBitmap.getPixels(midpixs, 0, midwidth, 0, 0, midwidth, midheight);
        //横向处理
        /************************* left ***********************/
        for(p=y1_pos; p<=y2_pos; p++){
            filterindex = 0;
            for(q=0; q<x1_pos; q++){                            //find start point in midBitmap
                if(midpixs[p*midwidth + q] != 0){
                    if(filterindex == 0){
                        startx = q;
                    }
                        filterindex++;
                    if(filterindex == filtercounterlevel1){
                        tmpcolor = midpixs[p*midwidth + q];
                        startR = (tmpcolor & 0x00FF0000) >> 16;
                        startG = (tmpcolor & 0x0000FF00) >> 8;
                        startB = (tmpcolor & 0x000000FF);
                        break;
                    }
                }
            }

            filterindex = 0;
            i = p - y1_pos;
            for(j=0; j<facewidth; j++){                        //find end point in faceBitmap
                if(facepixs[i*facewidth + j] != 0){
                    if(filterindex == 0)
                        endx = j + x1_pos;
                    filterindex++;
                    if(filterindex == facefiltercounterlevel1){
                        tmpcolor = facepixs[i*facewidth +j];
                        endR = (tmpcolor & 0x00FF0000) >> 16;
                        endG = (tmpcolor & 0x0000FF00) >> 8;
                        endB = (tmpcolor & 0x000000FF);
                        break;
                    }
                }
            }

            diffR = ((float)(endR - startR))/(x1_pos - startx);
            diffG = ((float)(endG - startG))/(x1_pos - startx);
            diffB = ((float)(endB - startB))/(x1_pos - startx);

            for(k=startx; k<endx;k++){                              //change color
                if(k <= x1_pos){
                    tmpR = (int) (startR + diffR * (k - startx));
                    tmpG = (int) (startG + diffG * (k - startx));
                    tmpB = (int) (startB + diffB * (k - startx));
                    tmpcolor = 0xFF000000 + (tmpR << 16) + (tmpG << 8) + tmpB;
//                tmpcolor = 0xFF000000 + (startR << 16) + (startG << 8) + startB;
                }
                else {
                    tmpcolor = 0xFF000000 + (endR << 16) + (endG << 8) + endB;
                }
                midpixs[p*midwidth + k] = tmpcolor;
            }
        }
        /************************* right ***********************/
        for(p=y1_pos; p <= y2_pos; p++){
            filterindex = 0;
            for(q=midwidth-1; q > x2_pos; q--){                   //find end point in midBitmap
                if(midpixs[p*midwidth + q] != 0){
                    if(filterindex == 0){
                        endx = q;
                    }
                    filterindex++;
                    if(filterindex == filtercounterlevel1 ){
                        tmpcolor = midpixs[p*midwidth + q];
                        endR = (tmpcolor & 0x00FF0000) >> 16;
                        endG = (tmpcolor & 0x0000FF00) >> 8;
                        endB = (tmpcolor & 0x000000FF);
                        break;
                    }
                }
            }

            filterindex = 0;
            i = p - y1_pos;                         //change to faceBitmap axes
            for(j= facewidth -1; j >= 0; j--){                      //find start point in faceBitmap
                if(facepixs[i*facewidth +j] != 0){
                    if(filterindex == 0)
                        startx = j + x1_pos;                  //change midBitmap axes
                    filterindex++;
                    if(filterindex == facefiltercounterlevel1){
                        tmpcolor = facepixs[i*facewidth +j];
                        startR = (tmpcolor & 0x00FF0000) >> 16;
                        startG = (tmpcolor & 0x0000FF00) >> 8;
                        startB = (tmpcolor & 0x000000FF);
                        break;
                    }
                }
            }

            diffR = ((float)(endR - startR))/(endx - x2_pos);
            diffG = ((float)(endG - startG))/(endx - x2_pos);
            diffB = ((float)(endB - startB))/(endx - x2_pos);
            for(k=startx; k<endx; k++){
                if(k > x2_pos){
                    tmpR = (int) (startR + (k-x2_pos) * diffR);
                    tmpG = (int) (startG + (k-x2_pos) * diffG);
                    tmpB = (int) (startB + (k-x2_pos) * diffB);
                    tmpcolor = 0xFF000000 + (tmpR << 16) + (tmpG << 8) + tmpB;
                }
                else
                    tmpcolor = 0xFF000000 + (startR << 16) + (startG << 8) + startB;

                midpixs[p*midwidth + k] = tmpcolor;
            }
        }
        //纵向处理
        /************************* up ***********************/
        for(q=0; q<midwidth; q++){
            filterindex = 0;
            for(p=0; p<y1_pos; p++){        //find start point in midBitmap
                if(midpixs[p*midwidth + q] != 0){
                    if(filterindex == 0)
                        starty = p;
                    filterindex++;
                    if(filterindex == filtercounterlevel1){
                        tmpcolor = midpixs[p*midwidth + q];
                        startR = (tmpcolor & 0x00FF0000) >> 16;
                        startG = (tmpcolor & 0x0000FF00) >> 8;
                        startB = (tmpcolor & 0x000000FF);
                        break;
                    }
                }
            }

            if(p >= y1_pos)                                         //this line is all transparent
                continue;


            if((q >= x1_pos) && (q <= x2_pos)){           //find end point in midBitmap or faceBitmap
                j = q - x1_pos;                        //change to faceBitmap axes
                if(facepixs[j] != 0)
                    tmpcolor = facepixs[j];
                else
                    tmpcolor = midpixs[y1_pos*midwidth + q];
            }
            else
                tmpcolor = midpixs[y1_pos*midwidth + q];

            endR = (tmpcolor & 0x00FF0000) >> 16;
            endG = (tmpcolor & 0x0000FF00) >> 8;
            endB = (tmpcolor & 0x000000FF);

            diffR = ((float)(endR - startR))/(y1_pos - starty);
            diffG = ((float)(endG - startG))/(y1_pos - starty);
            diffB = ((float)(endB - startB))/(y1_pos - starty);

//            NdLog.info("diffR: "+diffR +", diffG: "+diffG +", diffB: "+diffB);


            for(k = starty; k<y1_pos; k++){                         //change color
                tmpR = (int) (startR + (k-starty) * diffR);
                tmpG = (int) (startG + (k-starty) * diffG);
                tmpB = (int) (startB + (k-starty) * diffB);
                tmpcolor = 0xFF000000 + (tmpR << 16) + (tmpG << 8) + tmpB;
//                tmpcolor = 0xFF000000 + (endR << 16) + (endG << 8) + endB;
                midpixs[k*midwidth + q] = tmpcolor;
            }
        }
        /************************* down ***********************/
        for(q=0; q<midwidth; q++){
            filterindex = 0;
            for(p=midheight-1; p>y2_pos; p--){
                if(midpixs[p * midwidth + q] != 0){
                    if(filterindex == 0)
                        endy = p;
                    filterindex++;
                    if(filterindex == 10){
                        tmpcolor = midpixs[p * midwidth + q];
                        endR = (tmpcolor & 0x00FF0000) >> 16;
                        endG = (tmpcolor & 0x0000FF00) >> 8;
                        endB = (tmpcolor & 0x000000FF);
                        break;
                    }
                }
            }

            if(p <= y2_pos)
                continue;

            if((q >= x1_pos) && (q <= x2_pos)){
                j = q-x1_pos;
                if(facepixs[facewidth*(faceheight -1) + j] != 0)
                    tmpcolor = facepixs[facewidth*(faceheight -1) + j];
                else
                    tmpcolor = midpixs[y2_pos*midwidth + q];
            }
            else {
                tmpcolor = midpixs[y2_pos*midwidth + q];
            }
            startR = (tmpcolor & 0x00FF0000) >> 16;
            startG = (tmpcolor & 0x0000FF00) >> 8;
            startB = (tmpcolor & 0x000000FF);

            diffR = ((float)(endR - startR))/(endy - y2_pos);
            diffG = ((float)(endG - startG))/(endy - y2_pos);
            diffB = ((float)(endB - startB))/(endy - y2_pos);
//            NdLog.info("diffR: "+diffR+", diffG: "+diffG + "diffB: "+diffB);

            for(k=y2_pos; k<endy; k++){
                tmpR = (int) (startR + (k-y2_pos) * diffR);
                tmpG = (int) (startG + (k-y2_pos) * diffG);
                tmpB = (int) (startB + (k-y2_pos) * diffB);
                tmpcolor = 0xFF000000 + (tmpR << 16) + (tmpG << 8) + tmpB;
//                tmpcolor = 0xFF000000 + (startR << 16) + (startG << 8) + startB;
//                tmpcolor = 0xFF000000 + (endR << 16) + (endG << 8) + endB;
                midpixs[k*midwidth + q] = tmpcolor;
            }
        }
//        for(p=0; p<midheight;p++){
//            for(q=0;q<midwidth;q++){
//                if(midpixs[p*midwidth + q] != 0)
//                    midpixs[p*midwidth + q] = 0xFF000000;
//            }
//        }

        resultMidBitmap = Bitmap.createBitmap(midpixs,0,midwidth,midwidth,midheight,Bitmap.Config.ARGB_8888);
        return resultMidBitmap;
    }

    public static void ComposeFaceImage(Context context,FaceImageStruct faceImage){
        //3d贴图
        Bitmap bitmap1 = getAssetsImage(context, "model_face.png");
        Bitmap midbitmap = getAssetsImage(context, "model_face_mid.png");

        if(faceImage != null){

            //匹配3d贴图的人脸图片
            float scaleWidth = ((float)(ModelImagePosition.RIGHT_EYE_CENTER.x - ModelImagePosition.LEFT_EYE_CENTER.x)/
                    (faceImage.right_eye_center.x - faceImage.left_eye_center.x));
            float scaleHeigh = ((float)(ModelImagePosition.MOUTH_LOWER_LIP_BOTTOM.y - ModelImagePosition.LEFT_EYEBROW_CENTER.y)/
                    (faceImage.mouth_lower_lip_bottom.y - faceImage.left_eyebrow_upper_middle.y));
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeigh);


            Bitmap bitmap2 =Bitmap.createBitmap(faceImage.bitmap,0,0,faceImage.bitmap.getWidth(),faceImage.bitmap.getHeight(),matrix,true);
            faceImage.positionChangeScale(scaleWidth, scaleHeigh);
            faceImage.bitmap = bitmap2;
            faceImage.bitmap = echelonCompress(faceImage);
//            int medianColor = getMedianBitmapColor(faceImage);
//            faceImage.bitmap = echelonCompress(faceImage,medianColor);

            Bitmap midfacebitmap = createTransitFaceImage(midbitmap, faceImage);

            //合成的图片
            Bitmap bitmap3 = Bitmap.createBitmap(1024, 1024, bitmap1.getConfig());
            Canvas canvas = new Canvas(bitmap3);
            canvas.drawBitmap(bitmap1, 0, 0, null);
            canvas.drawBitmap(midfacebitmap, 0, 0, null);
            int x_pos = ModelImagePosition.LEFT_EYE_CENTER.x - faceImage.left_eye_center.x;
            int y_pos = ModelImagePosition.LEFT_EYE_CENTER.y - faceImage.left_eye_center.y;
            NdLog.info("x_pos: "+x_pos+", y_pos: "+y_pos +", width: "+faceImage.bitmap.getWidth()+", heigh: "+faceImage.bitmap.getHeight());
            //               canvas.drawBitmap(bitmap2, x_pos, y_pos+5, null);
            canvas.drawBitmap(faceImage.bitmap, x_pos, y_pos, null);
            Log.i("*********", "start save Compose image");
            saveComposeImageToFile(context, bitmap3);
            Log.i("*********", "over save Compose image");
        }
    }


    /**
     * 合成图片
     */
    public static void ComposeFaceImage(Context context) {

        //3d贴图
        Bitmap bitmap1 = getAssetsImage(context, "model_face.png");
        //匹配3d贴图的人脸图片
        Bitmap bitmap2 = getScaleFace(getFaceImagePath(context));

        //合成的图片
        Bitmap bitmap3 = Bitmap.createBitmap(1024, 1024, bitmap1.getConfig());

        Canvas canvas = new Canvas(bitmap3);
        Paint paint = new Paint();

        //使用colorArt获取图片主色调，模拟皮肤
        ColorArt colorArt = new ColorArt(bitmap2);
        canvas.drawColor(colorArt.getBackgroundColor());
        canvas.drawBitmap(bitmap2, 352, 288, paint);

        saveComposeImageToFile(context, bitmap3);
    }

    /**
     *
     */
    public static Bitmap getAssetsImage(Context context, String fileSrc) {
        if (fileSrc == null) {
            return null;
        }
        Bitmap mImage = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileSrc);
            mImage = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mImage;
    }

    /**
     * 获取指定大小的人脸，用于合成
     */
    public static Bitmap getScaleFace(String fileSrc) {
        if (fileSrc == null) {
            return null;
        }
        Bitmap mImage = null;
        Options options = new Options();
        options.inJustDecodeBounds = false;
        mImage = BitmapFactory.decodeFile(fileSrc, options);
        int h = mImage.getHeight();
        int w = mImage.getWidth();

        float scaleWidth = ((float) 320) / w;
        float scaleHeight = ((float) 352) / h;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap mScaleFaceImage = null;
        if (mImage != null) {
            mScaleFaceImage = Bitmap.createBitmap(mImage, 0, 0, w, h, matrix, true);
        }

        return mScaleFaceImage;
    }

    public static Bitmap getScaleFace(Context context){
        return getScaleFace(getFaceImagePath(context));
    }

    /**
     * 合成图片
     */
    public static void testComposeFaceImage(Context context, Uri uri) {

        try {
            //3d贴图
            Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            //匹配3d贴图的人脸图片
            Bitmap bitmap2 = getScaleFace(getFaceImagePath(context));
            //合成的图片
            Bitmap bitmap3 = Bitmap.createBitmap(1024, 1024, bitmap1.getConfig());

            Canvas canvas = new Canvas(bitmap3);
            canvas.drawBitmap(bitmap1, 0, 0, null);
            canvas.drawBitmap(bitmap2, 352, 288, null);

            saveComposeImageToFile(context, bitmap3);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
