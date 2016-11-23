package com.nd.idolshow.facetest.util;

/**
 * Created by Administrator on 2016/11/3.
 */
public class ModelImagePosition {
    public static final PositionBean LEFT_EYEBROW_LEFT_CORNER = new PositionBean(170,315);
    public static final PositionBean LEFT_EYEBROW_CENTER = new PositionBean(300,320);
    public static final PositionBean LEFT_EYEBROW_RIGHT_CORNER = new PositionBean(440,350);

    public static final PositionBean LEFT_EYE_LEFT_CORNER = new PositionBean(200,400);
    public static final PositionBean LEFT_EYE_CENTER = new PositionBean(315,400);
    public static final PositionBean LEFT_EYE_RIGHT_CORNER = new PositionBean(400,420);

    public static final PositionBean RIGHT_EYEBROW_LEFT_CORNER = new PositionBean(580,350);
    public static final PositionBean RIGHT_EYEBROW_CENTER = new PositionBean(720,320);
    public static final PositionBean RIGHT_EYEBROW_RIGHT_CONRNER = new PositionBean(850,315);

    public static final PositionBean RIGHT_EYE_LEFT_CORNER = new PositionBean(620,420);
    public static final PositionBean RIGHT_EYE_CENTER = new PositionBean(705,400);
    public static final PositionBean RIGHT_EYE_RITHT_CORNER = new PositionBean(830,400);

    public static final PositionBean NOSE_LEFT_CORNER = new PositionBean(420,560);
    public static final PositionBean NOSE_TIP = new PositionBean(510,610);
    public static final PositionBean NOSE_RIGHT_CORNER = new PositionBean(605,560);

    public static final PositionBean MOUTH_LEFT_CORNER = new PositionBean(390,715);
    public static final PositionBean MOUTH_CENTER = new PositionBean(510,715);
    public static final PositionBean MOUTH_RIGHT_CORNER = new PositionBean(630,715);

    public static final PositionBean MOUTH_LOWER_LIP_BOTTOM = new PositionBean(610,820);

    public static class PositionBean{
        public int x;
        public int y;

        public PositionBean(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

}
