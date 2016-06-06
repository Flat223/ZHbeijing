package com.example.zhbeijing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class LocalCacheUtils {
	
	public static final String CACHEPATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/zhbeijing_Cache";;
			
	public void putCacheToLocal(String url , Bitmap bitmap) {
		
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return ;
		}
		
		File dir = new File(CACHEPATH);
		
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
		
		try {
			String fileName = MD5Util.passwordMD5(url);
			File cacheFile = new File(dir, fileName);
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
		} catch (FileNotFoundException e) {
			Log.e("TAG", "FileNotFound");
			e.printStackTrace();
		}
	}
	
	public Bitmap getCacheFromLocal(String url) {
		File cacheFile = new File(CACHEPATH, MD5Util.passwordMD5(url));
		try {
			if (cacheFile.exists()) {
				String path = cacheFile.getAbsolutePath();
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
				return bitmap;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
