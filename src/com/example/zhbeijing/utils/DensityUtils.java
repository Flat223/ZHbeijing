package com.example.zhbeijing.utils;

import android.app.Activity;

public class DensityUtils {
	
	public static int dpTopx(float dp,Activity maActivity){
		float density = maActivity.getResources().getDisplayMetrics().density;
		int px = (int) (density * dp + 0.5f);
		return px;
	} 
	
	public static float pxTodp(int px,Activity maActivity){
		float density = maActivity.getResources().getDisplayMetrics().density;
		float dp  = (density / px + 0.5f);
		return dp;
	} 
}		
