package com.nd.idolshow.facetest.util;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Administrator on 2016/11/3.
 */
public class FaceImageStruct {
    public Bitmap bitmap;

    public PositionBean imageleftup;
    public PositionBean imageleftdown;
    public PositionBean imagerightup;
    public PositionBean imagerightdown;

    public PositionBean left_eyebrow_left_corner = new PositionBean();
    //    public PositionBean left_eyebrow_lower_middle = new PositionBean();
    public PositionBean left_eyebrow_right_corner = new PositionBean();
    public PositionBean left_eyebrow_upper_left_quarter = new PositionBean();
    public PositionBean left_eyebrow_upper_middle = new PositionBean();
    public PositionBean left_eyebrow_upper_right_quarter = new PositionBean();

    //    public PositionBean left_eye_bottom = new PositionBean();
    public PositionBean left_eye_center = new PositionBean();
    public PositionBean left_eye_left_corner = new PositionBean();
    public PositionBean left_eye_right_corner = new PositionBean();
//    public PositionBean left_eye_top = new PositionBean();

    public PositionBean right_eyebrow_left_corner = new PositionBean();
    //    public PositionBean right_eyebrow_lower_middle = new PositionBean();
    public PositionBean right_eyebrow_right_corner = new PositionBean();
    public PositionBean right_eyebrow_upper_left_quarter = new PositionBean();
    public PositionBean right_eyebrow_upper_middle = new PositionBean();
    public PositionBean right_eyebrow_upper_right_quarter = new PositionBean();

    //    public PositionBean right_eye_bottom = new PositionBean();
    public PositionBean right_eye_center = new PositionBean();
    public PositionBean right_eye_left_corner = new PositionBean();
    public PositionBean right_eye_right_corner = new PositionBean();
//    public PositionBean right_eye_top = new PositionBean();

    public PositionBean nose_left = new PositionBean();
    public PositionBean nose_right = new PositionBean();
    public PositionBean nose_tip = new PositionBean();

    public PositionBean mouth_left_corner = new PositionBean();
    public PositionBean mouth_right_corner = new PositionBean();
    public PositionBean mouth_lower_lip_bottom = new PositionBean();
    //    public PositionBean mouth_lower_lip_top = new PositionBean();
//    public PositionBean mouth_upper_lip_bottom = new PositionBean();
    public PositionBean mouth_upper_lip_top = new PositionBean();

    //added by tao
    public PositionBean contour_chin = new PositionBean();
    public PositionBean contour_left1 = new PositionBean();
    public PositionBean contour_left2 = new PositionBean();
    public PositionBean contour_left3 = new PositionBean();
    public PositionBean contour_left4 = new PositionBean();
    public PositionBean contour_left5 = new PositionBean();
    public PositionBean contour_left6 = new PositionBean();
    public PositionBean contour_left7 = new PositionBean();
    public PositionBean contour_left8 = new PositionBean();
    public PositionBean contour_left9 = new PositionBean();
    public PositionBean contour_right1 = new PositionBean();
    public PositionBean contour_right2 = new PositionBean();
    public PositionBean contour_right3 = new PositionBean();
    public PositionBean contour_right4 = new PositionBean();
    public PositionBean contour_right5 = new PositionBean();
    public PositionBean contour_right6 = new PositionBean();
    public PositionBean contour_right7 = new PositionBean();
    public PositionBean contour_right8 = new PositionBean();
    public PositionBean contour_right9 = new PositionBean();


    public FaceImageStruct(Bitmap bitmap, LandMarkBean.LandMark landmark){
        this.bitmap = bitmap;
        float hscale = (float) (bitmap.getHeight()/100.0);
        float wscale = (float) (bitmap.getWidth()/100.0);

        left_eyebrow_left_corner.x = (int) (landmark.left_eyebrow_left_corner.x * wscale);
        left_eyebrow_left_corner.y = (int) (landmark.left_eyebrow_left_corner.y * hscale);
        left_eyebrow_right_corner.x = (int) (landmark.left_eyebrow_right_corner.x * wscale);
        left_eyebrow_right_corner.y = (int) (landmark.left_eyebrow_right_corner.y * hscale);
        left_eyebrow_upper_left_quarter.x = (int) (landmark.left_eyebrow_upper_left_quarter.x * wscale);
        left_eyebrow_upper_left_quarter.y = (int) (landmark.left_eyebrow_upper_left_quarter.y * hscale);
        left_eyebrow_upper_middle.x = (int) (landmark.left_eyebrow_upper_middle.x * wscale);
        left_eyebrow_upper_middle.y = (int) (landmark.left_eyebrow_upper_middle.y * hscale);
        left_eyebrow_upper_right_quarter.x = (int) (landmark.left_eyebrow_upper_right_quarter.x * wscale);
        left_eyebrow_upper_right_quarter.y = (int) (landmark.left_eyebrow_upper_right_quarter.y * hscale);

        left_eye_center.x = (int) (landmark.left_eye_center.x * wscale);
        left_eye_center.y = (int) (landmark.left_eye_center.y * hscale);
        left_eye_left_corner.x = (int) (landmark.left_eye_left_corner.x * wscale);
        left_eye_left_corner.y = (int) (landmark.left_eye_left_corner.y * hscale);
        left_eye_right_corner.x = (int) (landmark.left_eye_right_corner.x * wscale);
        left_eye_right_corner.y = (int) (landmark.left_eye_right_corner.y * hscale);

        right_eyebrow_left_corner.x = (int) (landmark.right_eyebrow_left_corner.x * wscale);
        right_eyebrow_left_corner.y = (int) (landmark.right_eyebrow_left_corner.y * hscale);
        right_eyebrow_right_corner.x = (int) (landmark.right_eyebrow_right_corner.x * wscale);
        right_eyebrow_right_corner.y = (int) (landmark.right_eyebrow_right_corner.y * hscale);
        right_eyebrow_upper_left_quarter.x = (int)(landmark.right_eyebrow_upper_right_quarter.x * wscale);
        right_eyebrow_upper_left_quarter.y = (int)(landmark.right_eyebrow_upper_right_quarter.y * hscale);
        right_eyebrow_upper_middle.x = (int) (landmark.right_eyebrow_upper_middle.x * wscale);
        right_eyebrow_upper_middle.y = (int) (landmark.right_eyebrow_upper_middle.y * hscale);
        right_eyebrow_upper_right_quarter.x = (int) (landmark.right_eyebrow_upper_right_quarter.x * wscale);
        right_eyebrow_upper_right_quarter.y = (int) (landmark.right_eyebrow_upper_right_quarter.y * hscale);

        right_eye_center.x = (int) (landmark.right_eye_center.x * wscale);
        right_eye_center.y = (int) (landmark.right_eye_center.y * hscale);
        right_eye_left_corner.x = (int) (landmark.right_eye_left_corner.x * wscale);
        right_eye_left_corner.y = (int) (landmark.right_eye_left_corner.y * hscale);
        right_eye_right_corner.x = (int) (landmark.right_eye_right_corner.x * wscale);
        right_eye_right_corner.y = (int) (landmark.right_eye_right_corner.y * hscale);

        nose_left.x = (int) (landmark.nose_left.x * wscale);
        nose_left.y = (int) (landmark.nose_left.y * hscale);
        nose_right.x = (int) (landmark.nose_right.x * wscale);
        nose_right.y = (int) (landmark.nose_right.y * hscale);
        nose_tip.x = (int) (landmark.nose_tip.x * wscale);
        nose_tip.y = (int) (landmark.nose_tip.y * hscale);

        mouth_left_corner.x = (int) (landmark.mouth_left_corner.x * wscale);
        mouth_left_corner.y = (int) (landmark.mouth_left_corner.y * hscale);
        mouth_lower_lip_bottom.x = (int) (landmark.mouth_lower_lip_bottom.x * wscale);
        mouth_lower_lip_bottom.y = (int) (landmark.mouth_lower_lip_bottom.y * hscale);
        mouth_right_corner.x = (int) (landmark.mouth_right_corner.x * wscale);
        mouth_right_corner.y = (int) (landmark.mouth_right_corner.y * hscale);
        mouth_upper_lip_top.x = (int) (landmark.mouth_upper_lip_top.x * wscale);
        mouth_upper_lip_top.y = (int) (landmark.mouth_upper_lip_top.y * hscale);

        //added by tao
        contour_chin.x = (int) (landmark.contour_chin.x * wscale);
        contour_chin.y = (int) (landmark.contour_chin.y * hscale);
        contour_left1.x  = (int) (landmark.contour_left1.x  * wscale);
        contour_left1.y  = (int) (landmark.contour_left1.y  * hscale);
        contour_left2.x  = (int) (landmark.contour_left2.x  * wscale);
        contour_left2.y  = (int) (landmark.contour_left2.y  * hscale);
        contour_left3.x  = (int) (landmark.contour_left3.x  * wscale);
        contour_left3.y  = (int) (landmark.contour_left3.y  * hscale);
        contour_left4.x  = (int) (landmark.contour_left4.x  * wscale);
        contour_left4.y  = (int) (landmark.contour_left4.y  * hscale);
        contour_left5.x  = (int) (landmark.contour_left5.x  * wscale);
        contour_left5.y  = (int) (landmark.contour_left5.y  * hscale);
        contour_left6.x  = (int) (landmark.contour_left6.x  * wscale);
        contour_left6.y  = (int) (landmark.contour_left6.y  * hscale);
        contour_left7.x  = (int) (landmark.contour_left7.x  * wscale);
        contour_left7.y  = (int) (landmark.contour_left7.y  * hscale);
        contour_left8.x  = (int) (landmark.contour_left8.x  * wscale);
        contour_left8.y  = (int) (landmark.contour_left8.y  * hscale);
        contour_left9.x  = (int) (landmark.contour_left9.x  * wscale);
        contour_left9.y  = (int) (landmark.contour_left9.y  * hscale);
        contour_right1.x = (int) (landmark.contour_right1.x * wscale);
        contour_right1.y = (int) (landmark.contour_right1.y * hscale);
        contour_right2.x = (int) (landmark.contour_right2.x * wscale);
        contour_right2.y = (int) (landmark.contour_right2.y * hscale);
        contour_right3.x = (int) (landmark.contour_right3.x * wscale);
        contour_right3.y = (int) (landmark.contour_right3.y * hscale);
        contour_right4.x = (int) (landmark.contour_right4.x * wscale);
        contour_right4.y = (int) (landmark.contour_right4.y * hscale);
        contour_right5.x = (int) (landmark.contour_right5.x * wscale);
        contour_right5.y = (int) (landmark.contour_right5.y * hscale);
        contour_right6.x = (int) (landmark.contour_right6.x * wscale);
        contour_right6.y = (int) (landmark.contour_right6.y * hscale);
        contour_right7.x = (int) (landmark.contour_right7.x * wscale);
        contour_right7.y = (int) (landmark.contour_right7.y * hscale);
        contour_right8.x = (int) (landmark.contour_right8.x * wscale);
        contour_right8.y = (int) (landmark.contour_right8.y * hscale);
        contour_right9.x = (int) (landmark.contour_right9.x * wscale);
        contour_right9.y = (int) (landmark.contour_right9.y * hscale);
    }

    public void positionChangeScale(float wscale,float hscale){
        left_eyebrow_left_corner.x = (int) (left_eyebrow_left_corner.x * wscale);
        left_eyebrow_left_corner.y = (int) (left_eyebrow_left_corner.y * hscale);
        left_eyebrow_right_corner.x = (int) (left_eyebrow_right_corner.x * wscale);
        left_eyebrow_right_corner.y = (int) (left_eyebrow_right_corner.y * hscale);
        left_eyebrow_upper_left_quarter.x = (int) (left_eyebrow_upper_left_quarter.x * wscale);
        left_eyebrow_upper_left_quarter.y = (int) (left_eyebrow_upper_left_quarter.y * hscale);
        left_eyebrow_upper_middle.x = (int) (left_eyebrow_upper_middle.x * wscale);
        left_eyebrow_upper_middle.y = (int) (left_eyebrow_upper_middle.y * hscale);
        left_eyebrow_upper_right_quarter.x = (int) (left_eyebrow_upper_right_quarter.x * wscale);
        left_eyebrow_upper_right_quarter.y = (int) (left_eyebrow_upper_right_quarter.y * hscale);

        left_eye_center.x = (int) (left_eye_center.x * wscale);
        left_eye_center.y = (int) (left_eye_center.y * hscale);
        left_eye_left_corner.x = (int) (left_eye_left_corner.x * wscale);
        left_eye_left_corner.y = (int) (left_eye_left_corner.y * hscale);
        left_eye_right_corner.x = (int) (left_eye_right_corner.x * wscale);
        left_eye_right_corner.y = (int) (left_eye_right_corner.y * hscale);

        right_eyebrow_left_corner.x = (int) (right_eyebrow_left_corner.x * wscale);
        right_eyebrow_left_corner.y = (int) (right_eyebrow_left_corner.y * hscale);
        right_eyebrow_right_corner.x = (int) (right_eyebrow_right_corner.x * wscale);
        right_eyebrow_right_corner.y = (int) (right_eyebrow_right_corner.y * hscale);
        right_eyebrow_upper_left_quarter.x = (int)(right_eyebrow_upper_left_quarter.x * wscale);
        right_eyebrow_upper_left_quarter.y = (int)(right_eyebrow_upper_left_quarter.y * hscale);
        right_eyebrow_upper_middle.x = (int) (right_eyebrow_upper_middle.x * wscale);
        right_eyebrow_upper_middle.y = (int) (right_eyebrow_upper_middle.y * hscale);
        right_eyebrow_upper_right_quarter.x = (int) (right_eyebrow_upper_right_quarter.x * wscale);
        right_eyebrow_upper_right_quarter.y = (int) (right_eyebrow_upper_right_quarter.y * hscale);

        right_eye_center.x = (int) (right_eye_center.x * wscale);
        right_eye_center.y = (int) (right_eye_center.y * hscale);
        right_eye_left_corner.x = (int) (right_eye_left_corner.x * wscale);
        right_eye_left_corner.y = (int) (right_eye_left_corner.y * hscale);
        right_eye_right_corner.x = (int) (right_eye_right_corner.x * wscale);
        right_eye_right_corner.y = (int) (right_eye_right_corner.y * hscale);

        nose_left.x = (int) (nose_left.x * wscale);
        nose_left.y = (int) (nose_left.y * hscale);
        nose_right.x = (int) (nose_right.x * wscale);
        nose_right.y = (int) (nose_right.y * hscale);
        nose_tip.x = (int) (nose_tip.x * wscale);
        nose_tip.y = (int) (nose_tip.y * hscale);

        mouth_left_corner.x = (int) (mouth_left_corner.x * wscale);
        mouth_left_corner.y = (int) (mouth_left_corner.y * hscale);
        mouth_lower_lip_bottom.x = (int) (mouth_lower_lip_bottom.x * wscale);
        mouth_lower_lip_bottom.y = (int) (mouth_lower_lip_bottom.y * hscale);
        mouth_right_corner.x = (int) (mouth_right_corner.x * wscale);
        mouth_right_corner.y = (int) (mouth_right_corner.y * hscale);
        mouth_upper_lip_top.x = (int) (mouth_upper_lip_top.x * wscale);
        mouth_upper_lip_top.y = (int) (mouth_upper_lip_top.y * hscale);

        //added by tao
        contour_chin.x = (int)   (contour_chin.x * wscale);
        contour_chin.y = (int)   (contour_chin.y * hscale);
        contour_left1.x  = (int) (contour_left1.x  * wscale);
        contour_left1.y  = (int) (contour_left1.y  * hscale);
        contour_left2.x  = (int) (contour_left2.x  * wscale);
        contour_left2.y  = (int) (contour_left2.y  * hscale);
        contour_left3.x  = (int) (contour_left3.x  * wscale);
        contour_left3.y  = (int) (contour_left3.y  * hscale);
        contour_left4.x  = (int) (contour_left4.x  * wscale);
        contour_left4.y  = (int) (contour_left4.y  * hscale);
        contour_left5.x  = (int) (contour_left5.x  * wscale);
        contour_left5.y  = (int) (contour_left5.y  * hscale);
        contour_left6.x  = (int) (contour_left6.x  * wscale);
        contour_left6.y  = (int) (contour_left6.y  * hscale);
        contour_left7.x  = (int) (contour_left7.x  * wscale);
        contour_left7.y  = (int) (contour_left7.y  * hscale);
        contour_left8.x  = (int) (contour_left8.x  * wscale);
        contour_left8.y  = (int) (contour_left8.y  * hscale);
        contour_left9.x  = (int) (contour_left9.x  * wscale);
        contour_left9.y  = (int) (contour_left9.y  * hscale);
        contour_right1.x = (int) (contour_right1.x * wscale);
        contour_right1.y = (int) (contour_right1.y * hscale);
        contour_right2.x = (int) (contour_right2.x * wscale);
        contour_right2.y = (int) (contour_right2.y * hscale);
        contour_right3.x = (int) (contour_right3.x * wscale);
        contour_right3.y = (int) (contour_right3.y * hscale);
        contour_right4.x = (int) (contour_right4.x * wscale);
        contour_right4.y = (int) (contour_right4.y * hscale);
        contour_right5.x = (int) (contour_right5.x * wscale);
        contour_right5.y = (int) (contour_right5.y * hscale);
        contour_right6.x = (int) (contour_right6.x * wscale);
        contour_right6.y = (int) (contour_right6.y * hscale);
        contour_right7.x = (int) (contour_right7.x * wscale);
        contour_right7.y = (int) (contour_right7.y * hscale);
        contour_right8.x = (int) (contour_right8.x * wscale);
        contour_right8.y = (int) (contour_right8.y * hscale);
        contour_right9.x = (int) (contour_right9.x * wscale);
        contour_right9.y = (int) (contour_right9.y * hscale);
    }

    //move left x<0, move right x>0 move up y<0 move down y>0
    public void positionMove(int x, int y){
        left_eyebrow_left_corner.x =  (left_eyebrow_left_corner.x + x);
        left_eyebrow_left_corner.y = (left_eyebrow_left_corner.y + y);
        left_eyebrow_right_corner.x = (left_eyebrow_right_corner.x + x);
        left_eyebrow_right_corner.y = (left_eyebrow_right_corner.y + y);
        left_eyebrow_upper_left_quarter.x = (left_eyebrow_upper_left_quarter.x + x);
        left_eyebrow_upper_left_quarter.y = (left_eyebrow_upper_left_quarter.y + y);
        left_eyebrow_upper_middle.x = (left_eyebrow_upper_middle.x + x);
        left_eyebrow_upper_middle.y = (left_eyebrow_upper_middle.y + y);
        left_eyebrow_upper_right_quarter.x = (left_eyebrow_upper_right_quarter.x + x);
        left_eyebrow_upper_right_quarter.y = (left_eyebrow_upper_right_quarter.y + y);

        left_eye_center.x = (left_eye_center.x + x);
        left_eye_center.y = (left_eye_center.y + y);
        left_eye_left_corner.x = (left_eye_left_corner.x + x);
        left_eye_left_corner.y = (left_eye_left_corner.y + y);
        left_eye_right_corner.x = (left_eye_right_corner.x + x);
        left_eye_right_corner.y = (left_eye_right_corner.y + y);

        right_eyebrow_left_corner.x = (right_eyebrow_left_corner.x + x);
        right_eyebrow_left_corner.y = (right_eyebrow_left_corner.y + y);
        right_eyebrow_right_corner.x = (right_eyebrow_right_corner.x + x);
        right_eyebrow_right_corner.y = (right_eyebrow_right_corner.y + y);
        right_eyebrow_upper_left_quarter.x =(right_eyebrow_upper_left_quarter.x + x);
        right_eyebrow_upper_left_quarter.y =(right_eyebrow_upper_left_quarter.y + y);
        right_eyebrow_upper_middle.x = (right_eyebrow_upper_middle.x + x);
        right_eyebrow_upper_middle.y = (right_eyebrow_upper_middle.y + y);
        right_eyebrow_upper_right_quarter.x = (right_eyebrow_upper_right_quarter.x + x);
        right_eyebrow_upper_right_quarter.y = (right_eyebrow_upper_right_quarter.y + y);

        right_eye_center.x = (right_eye_center.x + x);
        right_eye_center.y = (right_eye_center.y + y);
        right_eye_left_corner.x = (right_eye_left_corner.x + x);
        right_eye_left_corner.y = (right_eye_left_corner.y + y);
        right_eye_right_corner.x = (right_eye_right_corner.x + x);
        right_eye_right_corner.y = (right_eye_right_corner.y + y);

        nose_left.x = (nose_left.x + x);
        nose_left.y = (nose_left.y + y);
        nose_right.x = (nose_right.x + x);
        nose_right.y = (nose_right.y + y);
        nose_tip.x = (nose_tip.x + x);
        nose_tip.y = (nose_tip.y + y);

        mouth_left_corner.x = (mouth_left_corner.x + x);
        mouth_left_corner.y = (mouth_left_corner.y + y);
        mouth_lower_lip_bottom.x = (mouth_lower_lip_bottom.x + x);
        mouth_lower_lip_bottom.y = (mouth_lower_lip_bottom.y + y);
        mouth_right_corner.x = (mouth_right_corner.x + x);
        mouth_right_corner.y = (mouth_right_corner.y + y);
        mouth_upper_lip_top.x = (mouth_upper_lip_top.x + x);
        mouth_upper_lip_top.y = (mouth_upper_lip_top.y + y);

        //added by tao
        contour_chin.x =  (contour_chin.x + x);
        contour_chin.y =  (contour_chin.y + y);
        contour_left1.x  = (contour_left1.x  + x);
        contour_left1.y  = (contour_left1.y  + y);
        contour_left2.x  = (contour_left2.x  + x);
        contour_left2.y  = (contour_left2.y  + y);
        contour_left3.x  = (contour_left3.x  + x);
        contour_left3.y  = (contour_left3.y  + y);
        contour_left4.x  = (contour_left4.x  + x);
        contour_left4.y  = (contour_left4.y  + y);
        contour_left5.x  = (contour_left5.x  + x);
        contour_left5.y  = (contour_left5.y  + y);
        contour_left6.x  = (contour_left6.x  + x);
        contour_left6.y  = (contour_left6.y  + y);
        contour_left7.x  = (contour_left7.x  + x);
        contour_left7.y  = (contour_left7.y  + y);
        contour_left8.x  = (contour_left8.x  + x);
        contour_left8.y  = (contour_left8.y  + y);
        contour_left9.x  = (contour_left9.x  + x);
        contour_left9.y  = (contour_left9.y  + y);
        contour_right1.x = (contour_right1.x + x);
        contour_right1.y = (contour_right1.y + y);
        contour_right2.x = (contour_right2.x + x);
        contour_right2.y = (contour_right2.y + y);
        contour_right3.x = (contour_right3.x + x);
        contour_right3.y = (contour_right3.y + y);
        contour_right4.x = (contour_right4.x + x);
        contour_right4.y = (contour_right4.y + y);
        contour_right5.x = (contour_right5.x + x);
        contour_right5.y = (contour_right5.y + y);
        contour_right6.x = (contour_right6.x + x);
        contour_right6.y = (contour_right6.y + y);
        contour_right7.x = (contour_right7.x + x);
        contour_right7.y = (contour_right7.y + y);
        contour_right8.x = (contour_right8.x + x);
        contour_right8.y = (contour_right8.y + y);
        contour_right9.x = (contour_right9.x + x);
        contour_right9.y = (contour_right9.y + y);
    }

    public Bitmap tailorRecImage(int x, int y, int width, int height){
        Bitmap tmpBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
        bitmap = tmpBitmap;
        positionMove(-x,-y);
        return bitmap;
    }

    public void printPosition(){
        Log.i("**********","x= "+left_eyebrow_left_corner.x + ", y= "+left_eyebrow_left_corner.y + "  left_eyebrow_left_corner");
        Log.i("**********","x= "+left_eyebrow_right_corner.x + ", y= "+left_eyebrow_right_corner.y +"  left_eyebrow_right_corner");
        Log.i("**********","x= "+left_eyebrow_upper_left_quarter.x + ", y= "+left_eyebrow_upper_left_quarter.y +"  left_eyebrow_upper_left_quarter");
        Log.i("**********","x= "+left_eyebrow_upper_middle.x + ", y= "+left_eyebrow_upper_middle.y + "  left_eyebrow_upper_middle");
        Log.i("**********","x= "+left_eyebrow_upper_right_quarter.x + ", y= "+left_eyebrow_upper_right_quarter.y +"  left_eyebrow_upper_right_quarter");

        Log.i("**********","x= "+left_eye_center.x + ", y= "+left_eye_center.y + "  left_eye_center");
        Log.i("**********", "x= " + left_eye_left_corner.x + ", y= " + left_eye_left_corner.y + "  left_eye_left_corner");
        Log.i("**********","x= "+left_eye_right_corner.x + ", y= "+left_eye_right_corner.y + "  left_eye_right_corner");


        Log.i("**********","x= "+right_eyebrow_left_corner.x + ", y= "+right_eyebrow_left_corner.y + "  right_eyebrow_left_corner");
        Log.i("**********","x= "+right_eyebrow_right_corner.x + ", y= "+right_eyebrow_right_corner.y + "  right_eyebrow_right_corner");
        Log.i("**********","x= "+right_eyebrow_upper_left_quarter.x + ", y= "+right_eyebrow_upper_left_quarter.y +"  right_eyebrow_upper_left_quarter");
        Log.i("**********","x= "+right_eyebrow_upper_middle.x + ", y= "+right_eyebrow_upper_middle.y + "  right_eyebrow_upper_middle");
        Log.i("**********","x= "+right_eyebrow_upper_right_quarter.x + ", y= "+right_eyebrow_upper_right_quarter.y +"  right_eyebrow_upper_right_quarter");

        Log.i("**********","x= "+right_eye_center.x + ", y= "+right_eye_center.y + "  right_eye_center");
        Log.i("**********","x= "+right_eye_left_corner.x + ", y= "+right_eye_left_corner.y + "  right_eye_left_corner");
        Log.i("**********","x= "+right_eye_right_corner.x + ", y= "+right_eye_right_corner.y + "  right_eye_right_corner");

        Log.i("**********","x= "+nose_left.x + ", y= "+nose_left.y + "  nose_left");
        Log.i("**********","x= "+nose_right.x + ", y= "+nose_right.y + "  nose_right");
        Log.i("**********","x= "+nose_tip.x + ", y= "+nose_tip.y + "  nose_tip");

        Log.i("**********","x= "+mouth_left_corner.x + ", y= "+mouth_left_corner.y + "  mouth_left_corner");
        Log.i("**********","x= "+mouth_lower_lip_bottom.x + ", y= "+mouth_lower_lip_bottom.y + "  mouth_lower_lip_bottom");
        Log.i("**********","x= "+mouth_right_corner.x + ", y= "+mouth_right_corner.y + "  mouth_right_corner");
        Log.i("**********","x= "+mouth_upper_lip_top.x + ", y= "+mouth_upper_lip_top.y + "  mouth_upper_lip_top");
    }

    public static class PositionBean{
        public int x;
        public int y;
    }
}
