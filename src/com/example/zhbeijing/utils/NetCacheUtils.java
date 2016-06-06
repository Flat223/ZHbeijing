package com.example.zhbeijing.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;

public class NetCacheUtils {
	
	private MemoryCacheUtils mMemoryCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils){
		mMemoryCacheUtils = memoryCacheUtils;
		mLocalCacheUtils = localCacheUtils;
	}

	public void loadImageFromServer(final ImageView imageView, final String url) {

		imageView.setTag(url);

		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				Bitmap bitmap = downLoad(url);
				return bitmap;
			}

			protected void onPostExecute(Bitmap result) {

				String newUrl = (String) imageView.getTag();

				if (newUrl != url) {
					return;
				}
				
				if (result != null) {
					imageView.setImageBitmap(result);
					mLocalCacheUtils.putCacheToLocal(url, result);
					mMemoryCacheUtils.putCache(url, result);
					Log.e("TAG", "网络加载图片");
				}
			};
		}.execute();
	}

	public Bitmap downLoad(String url) {
		SystemClock.sleep(600);
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.connect();

			int responseCode = connection.getResponseCode();

			if (responseCode == 200) {
				InputStream is = connection.getInputStream();

				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
}
