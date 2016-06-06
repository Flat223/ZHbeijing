package com.example.zhbeijing.utils;

import android.R.raw;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhbeijing.R;

public class PhotoBitmapUtils {

	private Activity mActivity;
	private MemoryCacheUtils mMemoryCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private NetCacheUtils mNetCacheUtils;
	
	public PhotoBitmapUtils(Activity mActivity) {
		this.mActivity = mActivity;
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
	}


	public void display(ImageView imageView, String url) {
		
		imageView.setImageResource(R.drawable.pic_list_item_bg);
		
		Bitmap bitmap = mMemoryCacheUtils.getCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			Log.e("TAG", "从内存加载图片");
			return;
		}
		
		bitmap = mLocalCacheUtils.getCacheFromLocal(url);
		if(bitmap != null) {
			imageView.setImageBitmap(bitmap);
			Log.e("TAG", "本地加载图片");
			mMemoryCacheUtils.putCache(url, bitmap);
			return;
		}
		
		mNetCacheUtils.loadImageFromServer(imageView,url);
	}
}


