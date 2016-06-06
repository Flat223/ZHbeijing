package com.example.zhbeijing.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {

	// private Map<String, SoftReference<Bitmap>> cacheMap =
	// new HashMap<String, SoftReference<Bitmap>>();

	private LruCache<String, Bitmap> memoryCache;

	public MemoryCacheUtils() {
		long maxMemory = Runtime.getRuntime().maxMemory();

		memoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
//				value.getByteCount();
				int sizeCount = value.getRowBytes() * value.getHeight();//计算图片大小:每行字节数 * 高度;
				return sizeCount;
			}
		};
	}

	public void putCache(String url, Bitmap bitmap) {
		// SoftReference<Bitmap> reference = new SoftReference<Bitmap>(bitmap);

		memoryCache.put(url, bitmap);
	}

	public Bitmap getCache(String url){
//		SoftReference<Bitmap> reference = cacheMap.get(url);
//		if (reference != null) {
//			Bitmap bitmap = reference.get();
//			return bitmap;
//		}
		
		return memoryCache.get(url);
	}
}
