package com.example.zhbeijing.utils;

import android.app.Activity;

public class CacheUtils {
	
	public static String getCache(Activity mActivity,String url){
		String json = new SharedPreferencesUtil(mActivity).getString(url, "");
		return json;
	}
	
	public static void setCache(Activity mActivity,String url,String json){
		new SharedPreferencesUtil(mActivity).putString(url, json);
	}
}
