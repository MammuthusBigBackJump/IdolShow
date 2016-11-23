package com.nd.idolshow.facetest.util;

import com.alibaba.fastjson.JSON;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import java.nio.charset.Charset;

public class HttpUtil {
	
	public static LandMarkBean detect(byte[] b){
		HttpRequests httpRequests = new HttpRequests("324d5b2783733b4be6d902ab6263aecb", "vKZPl0fgSB1l5cC0nEqGQ9DZwHc1OWkk", true, true);
		JSONObject result = null;
		JSONObject result2 = null;
		DetectBean detectBean = null;
		LandMarkBean landMarkBean = null;
		try {
			System.out.println(Charset.forName("UTF-8").name());
			//detection/detect
			result = httpRequests.detectionDetect(new PostParameters().setImg(b));
			System.out.println(result);
			
			detectBean = JSON.parseObject(result.toString(),DetectBean.class);
			result2 = httpRequests.detectionLandmark(new PostParameters().setFaceId(detectBean.face.get(0).face_id)); 
			
			landMarkBean = JSON.parseObject(result2.toString(),LandMarkBean.class);
			
		}catch (Exception e){
			
		}
		
		return landMarkBean;
	}

}
