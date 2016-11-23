package com.nd.idolshow.facetest.util;

import java.util.List;

public class DetectBean {
	
	public List<Face> face;
	public int img_width;
	public int img_height;
	
	public String img_id;
	public String session_id;
	public String url;
	
	public int response_code;
	
	public static class Face{
		public String face_id;
		public Position position;
	}
	
	public static class Position{
		public PositionBean center;
		public PositionBean eye_left;
		public PositionBean eye_right;
		
		public PositionBean mouth_left;
		public PositionBean mouth_right;
		public PositionBean nose;
	}
	
	public static class PositionBean{
		public int x;
		public int y;
	}
	

}
