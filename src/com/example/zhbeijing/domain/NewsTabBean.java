package com.example.zhbeijing.domain;

import java.util.ArrayList;

/**
 * 页签详情数据对象
 * 
 * @author Kevin
 * @date 2015-10-20
 */
public class NewsTabBean {

	public NewsTab data;

	public class NewsTab {
		public String more;
		public ArrayList<NewsData> news;
		public ArrayList<TopNew> topnews;
	}

	// 新闻列表对象
	public class NewsData {
		public int id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
		
		@Override
		public String toString() {
			return "NewsData [id=" + id + "]";
		}
	}

	// 头条新闻
	public class TopNew {
		public int id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
		
		@Override
		public String toString() {
			return "TopNew [id=" + id + ", topimage=" + topimage + ", title="
					+ title + "]";
		}
	}

	@Override
	public String toString() {
		return "NewsTabBean [data=" + data + "]";
	}
}
