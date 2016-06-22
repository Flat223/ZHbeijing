package com.example.zhbeijing.detailpager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbeijing.NewsDetailActivity;
import com.example.zhbeijing.R;
import com.example.zhbeijing.domain.NewsMenu.NewsTabData;
import com.example.zhbeijing.domain.NewsTabBean;
import com.example.zhbeijing.domain.NewsTabBean.NewsData;
import com.example.zhbeijing.domain.NewsTabBean.TopNew;
import com.example.zhbeijing.global.GlobalConstant;
import com.example.zhbeijing.utils.CacheUtils;
import com.example.zhbeijing.utils.SharedPreferencesUtil;
import com.example.zhbeijing.view.PullToRefreshListView;
import com.example.zhbeijing.view.PullToRefreshListView.OnRefreshListener;
import com.example.zhbeijing.view.TopNewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class TabDetailPager extends BaseDetailPager implements
		OnPageChangeListener {

	@ViewInject(R.id.vp_tab_detail)
	private TopNewPager mViewPager;

	@ViewInject(R.id.tv_tabpager_title)
	private TextView tv_title;

	@ViewInject(R.id.indicator_circle)
	private CirclePageIndicator mIndicator;

	@ViewInject(R.id.lv_news_detail)
	private PullToRefreshListView mListView;

	private NewsTabData data;
	private String url;
	private ArrayList<TopNew> mTopNewArray;

	private NewsTabBean newsTabBean;

	private ArrayList<NewsData> mNewsDataArray;

	private NewsAdapter adapter;

	private String mMoreUrl;

	private SharedPreferencesUtil spUtils;

	private Handler mHandler;

	public TabDetailPager(Activity mActivity, NewsTabData newsTabData) {
		super(mActivity);
		data = newsTabData;
		spUtils = new SharedPreferencesUtil(mActivity);
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);

		View headView = View
				.inflate(mActivity, R.layout.list_item_header, null);
		ViewUtils.inject(this, headView);
		mListView.addHeaderView(headView);
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					mListView.onRefreshComplete(true);
					Toast.makeText(mActivity, "没有更多数据", 0).show();
				}
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int headerViewsCount = mListView.getHeaderViewsCount();// 获取头布局数量
				NewsData newsData = mNewsDataArray.get(position - 2);
				int newsId = newsData.id;

				String readId = spUtils.getString("readId", "");

				if (!readId.contains("" + newsId)) {
					readId = readId + newsId;
					spUtils.putString("readId", readId + ",");
				}
				
				TextView tv_title = (TextView) view
						.findViewById(R.id.tv_item_news);
				tv_title.setTextColor(Color.GRAY);

				String mWebUrl = newsData.url;
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("WebUrl", mWebUrl);
				mActivity.startActivity(intent);
			}
		});
		
		return view;
	}

	@Override
	public void initData() {
		url = GlobalConstant.SERVER_URL + data.url;
		String cache = CacheUtils.getCache(mActivity, url);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache, false);
			// Log.e("TAG", "服务器返回结果: " + cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(2500);
		// utils.configSoTimeout(1000);//请求到数据后读取的延迟时间
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// Toast.makeText(mActivity, "有网", Toast.LENGTH_SHORT).show();
				String result = responseInfo.result;
				// Log.e("TAG", "服务器返回结果: " + result);
				processData(result, false);
				CacheUtils.setCache(mActivity, url, result);

				mListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// error.getStackTrace();
				// Toast.makeText(mActivity, "没网", Toast.LENGTH_SHORT).show();
				mListView.onRefreshComplete(false);
			}
		});
	}

	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(2500);
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result, true);
				CacheUtils.setCache(mActivity, url, result);

				mListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				mListView.onRefreshComplete(true);
			}
		});
	}

	protected void processData(String result, boolean isLoadMore) {
		Gson gson = new Gson();

		newsTabBean = gson.fromJson(result, NewsTabBean.class);

		String moreUrl = newsTabBean.data.more;

		if (!TextUtils.isEmpty(moreUrl)) {
			mMoreUrl = GlobalConstant.SERVER_URL + moreUrl;
		} else {
			mMoreUrl = null;
		}

		// Log.e("TAG", "解析结果: " + newsTabBean);
		if (!isLoadMore) {

			mTopNewArray = newsTabBean.data.topnews;

			if (mTopNewArray != null) {

				mViewPager.setAdapter(new TopNewsAdapter());

				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);

				mIndicator.setOnPageChangeListener(this);
				mIndicator.onPageSelected(0);
			}
			adapter = new NewsAdapter();

			mNewsDataArray = newsTabBean.data.news;
			if (mNewsDataArray != null) {
				mListView.setAdapter(adapter);
			}

			if (mHandler == null) {
				mHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);

						int currentItem = mViewPager.getCurrentItem();
						currentItem++;

						if (currentItem > (mTopNewArray.size() - 1)) {
							currentItem = 0;
						}

						mViewPager.setCurrentItem(currentItem);

						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
				};
				mHandler.sendEmptyMessageDelayed(0, 3000);

				mViewPager.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							mHandler.removeCallbacksAndMessages(null);
							break;

						case MotionEvent.ACTION_CANCEL:
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;

						case MotionEvent.ACTION_UP:
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;

						default:
							break;
						}
						return false;
					}
				});
			}

		} else {
			ArrayList<NewsData> mMoreNewsArray = newsTabBean.data.news;
			mNewsDataArray.addAll(mMoreNewsArray);
			adapter.notifyDataSetChanged();
		}
	}

	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils
					.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mTopNewArray.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);

			view.setScaleType(ScaleType.FIT_XY);

			String imageUrl = mTopNewArray.get(position).topimage;
			String newUrl = imageUrl.replace("10.0.2.2", "192.168.2.102");
			// Log.e("TAG", "newUrl : " + newUrl);

			mBitmapUtils.display(view, newUrl);

			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		String title = mTopNewArray.get(position).title;
		// Log.e("TAG", title);
		tv_title.setText(title);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	class NewsAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public NewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils
					.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mNewsDataArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mNewsDataArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {

				convertView = View.inflate(mActivity, R.layout.list_item_news,
						null);

				holder = new ViewHolder();

				holder.iv_image = (ImageView) convertView
						.findViewById(R.id.iv_item_image);
				holder.tv_news = (TextView) convertView
						.findViewById(R.id.tv_item_news);
				holder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_item_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			NewsData newsData = mNewsDataArray.get(position);

			holder.tv_news.setText(newsData.title);

			String readId = spUtils.getString("readId", "");

			if (readId.contains(newsData.id + "")) {
				holder.tv_news.setTextColor(Color.GRAY);
			} else {
				holder.tv_news.setTextColor(Color.BLACK);
			}

			String pubdate = newsData.pubdate;
			String newDate = "2016-06-01 " + pubdate.substring(10);
			holder.tv_date.setText(newDate);
			// Log.e("TAG", "newUrl : " + newDate);

			String imageUrl = newsData.listimage;
			String newUrl = imageUrl.replace("10.0.2.2", "192.168.2.102");
			// Log.e("TAG", "newUrl : " + newUrl);

			mBitmapUtils.display(holder.iv_image, newUrl);
			return convertView;
		}
	}

	class ViewHolder {
		ImageView iv_image;
		TextView tv_news;
		TextView tv_date;
	}
}
