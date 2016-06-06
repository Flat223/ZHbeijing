package com.example.zhbeijing.domain;

import java.util.ArrayList;

public class PhotosBean {
	
	public PhotosData data;
	
	public class PhotosData{
		public String title;
		public ArrayList<PhotosNewsData> news;
		
		@Override
		public String toString() {
			return "PhotosData [title=" + title + ", news=" + news + "]";
		}
	}
	
	public class PhotosNewsData{
		public String listimage;
		public String title;
		
		@Override
		public String toString() {
			return "PhotosNewsData [listimage=" + listimage + ", title="
					+ title + "]";
		}
	}

	@Override
	public String toString() {
		return "PhotosBean [data=" + data + "]";
	}
}
