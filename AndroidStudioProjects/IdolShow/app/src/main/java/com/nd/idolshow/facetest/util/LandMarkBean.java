package com.nd.idolshow.facetest.util;

import java.util.List;

public class LandMarkBean {
	
	public List<Result> result;
	public String session_id;
	
	public static class Result{
		public String face_id;
		public LandMark landMark;
		
	}
	
	public static class LandMark{
		public PositionBean contour_chin;
		
		public PositionBean contour_left1;
		public PositionBean contour_left2;
		public PositionBean contour_left3;
		
		public PositionBean contour_left4;
		public PositionBean contour_left5;
		public PositionBean contour_left6;
		
		public PositionBean contour_left7;
		public PositionBean contour_left8;
		public PositionBean contour_left9;
		
		public PositionBean contour_right1;
		public PositionBean contour_right2;
		public PositionBean contour_right3;
		
		public PositionBean contour_right4;
		public PositionBean contour_right5;
		public PositionBean contour_right6;
		
		public PositionBean contour_right7;
		public PositionBean contour_right8;
		public PositionBean contour_right9;
		
		public PositionBean left_eyebrow_left_corner;
		public PositionBean left_eyebrow_lower_left_quarter;
		public PositionBean left_eyebrow_lower_middle;
		public PositionBean left_eyebrow_lower_right_quarter;
		
		public PositionBean left_eyebrow_right_corner;
		public PositionBean left_eyebrow_upper_left_quarter;
		public PositionBean left_eyebrow_upper_middle;
		public PositionBean left_eyebrow_upper_right_quarter;
		
		public PositionBean left_eye_bottom;
		public PositionBean left_eye_center;
		public PositionBean left_eye_left_corner;
		
		public PositionBean left_eye_lower_left_quarter;
		public PositionBean left_eye_lower_right_quarter;
		public PositionBean left_eye_pupil;
		
		public PositionBean left_eye_right_corner;
		public PositionBean left_eye_top;
		public PositionBean left_eye_upper_left_quarter;
		public PositionBean left_eye_upper_right_quarter;
		
		public PositionBean mouth_left_corner;
		public PositionBean mouth_lower_lip_bottom;
		
		public PositionBean mouth_lower_lip_left_contour1;
		public PositionBean mouth_lower_lip_left_contour2;
		public PositionBean mouth_lower_lip_left_contour3;
		
		public PositionBean mouth_lower_lip_right_contour1;
		public PositionBean mouth_lower_lip_right_contour2;
		public PositionBean mouth_lower_lip_right_contour3;
		
		public PositionBean mouth_lower_lip_top;
		public PositionBean mouth_right_corner;
		public PositionBean mouth_upper_lip_bottom;
		
		public PositionBean mouth_upper_lip_left_contour1;
		public PositionBean mouth_upper_lip_left_contour2;
		public PositionBean mouth_upper_lip_left_contour3;
		
		public PositionBean mouth_upper_lip_right_contour1;
		public PositionBean mouth_upper_lip_right_contour2;
		public PositionBean mouth_upper_lip_right_contour3;
		public PositionBean mouth_upper_lip_top;
		
		public PositionBean nose_contour_left1;
		public PositionBean nose_contour_left2;
		public PositionBean nose_contour_left3;
		
		public PositionBean nose_contour_lower_middle;
		public PositionBean nose_contour_right1;
		public PositionBean nose_contour_right2;
		public PositionBean nose_contour_right3;
		
		public PositionBean nose_left;
		public PositionBean nose_right;
		public PositionBean nose_tip;
		
		public PositionBean right_eyebrow_left_corner;
		public PositionBean right_eyebrow_lower_left_quarter;
		public PositionBean right_eyebrow_lower_middle;
		
		public PositionBean right_eyebrow_lower_right_quarter;
		public PositionBean right_eyebrow_right_corner;
		public PositionBean right_eyebrow_upper_left_quarter;
		
		public PositionBean right_eyebrow_upper_middle;
		public PositionBean right_eyebrow_upper_right_quarter;
		
		public PositionBean right_eye_bottom;
		public PositionBean right_eye_center;
		public PositionBean right_eye_left_corner;
		
		public PositionBean right_eye_lower_left_quarter;
		public PositionBean right_eye_lower_right_quarter;
		
		public PositionBean right_eye_pupil;
		public PositionBean right_eye_right_corner;
		public PositionBean right_eye_top;
		
		public PositionBean right_eye_upper_left_quarter;
		public PositionBean right_eye_upper_right_quarter;
		
		
	}
	
	public static class PositionBean{
		public int x;
		public int y;
	}

}
