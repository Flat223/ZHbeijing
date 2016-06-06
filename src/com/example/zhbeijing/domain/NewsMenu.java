package com.example.zhbeijing.domain;

import java.util.ArrayList;

/**
 * 分类信息封装
 * @author MrSun
 *
 */

public class NewsMenu {
	public int retcode;
	public ArrayList<Integer> extend;
	public ArrayList<NewsMenuData> data; 
	
	public class NewsMenuData{
		public int id;
		public String title;
		public int type;
		public ArrayList<NewsTabData> children;
		
		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}
	}
	
	public class NewsTabData{
		public int id;
		public String title;
		public int type;
		public String url;
		
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + ", url=" + url + "]";
		}
	}

	@Override
	public String toString() {
		return "NewsMenu [data=" + data + "]";
	}
}
