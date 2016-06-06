package com.example.zhbeijing.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zhbeijing.MainActivity;
import com.example.zhbeijing.detailpager.BaseDetailPager;
import com.example.zhbeijing.detailpager.InteractionDetailPager;
import com.example.zhbeijing.detailpager.NewsDetailPager;
import com.example.zhbeijing.detailpager.PhotoDetailPager;
import com.example.zhbeijing.detailpager.SubjectDetailPager;
import com.example.zhbeijing.domain.NewsMenu;
import com.example.zhbeijing.domain.NewsMenu.NewsMenuData;
import com.example.zhbeijing.fragment.LeftMenuFragment;
import com.example.zhbeijing.global.GlobalConstant;
import com.example.zhbeijing.utils.CacheUtils;
import com.example.zhbeijing.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	public NewsMenu mNewsMenu;
	public ArrayList<BaseDetailPager> mDetailPagerArray;
	private ArrayList<NewsMenuData> mDataArray;
	private SharedPreferencesUtil spUtils;
	

	public NewsCenterPager(Activity mActivity) {
		super(mActivity);
		
		spUtils = new SharedPreferencesUtil(mActivity);
	}
	
	@Override
	public void initData() {
		Log.e("TAG", "新闻中心初始化");
		
//		TextView textView = new TextView(mActivity);
//		textView.setText("新闻中心");
//		textView.setTextColor(Color.RED);
//		textView.setTextSize(22);
//		textView.setGravity(Gravity.CENTER);
//		fl_container.addView(textView);
		
//		tv_title.setText("新闻");
		
		String json = CacheUtils.getCache(mActivity,GlobalConstant.CATEGORY_URL);
		if(!TextUtils.isEmpty(json)){
			Log.e("TAG", "发现缓存");
			processData(json);
		}
		getDataFromSevvice();
	}

	private void getDataFromSevvice() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstant.CATEGORY_URL,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
//				Log.e("TAG", "服务器返回结果: " + result);
				processData(result);
				
				CacheUtils.setCache( mActivity, GlobalConstant.CATEGORY_URL,result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if(spUtils.getBoolean("IsFirst", true)){
					Toast.makeText(mActivity, "网络未连接", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void processData(String json) {
		Gson gson = new Gson();
		mNewsMenu = gson.fromJson(json, NewsMenu.class);
//		Log.e("TAG", "解析结果: " + mNewsMenu);
		
		mDataArray = mNewsMenu.data;
	
		MainActivity mMainUi = (MainActivity) mActivity;
		LeftMenuFragment fragment = mMainUi.getLeftMenuFragment();
		fragment.setMenuData(mDataArray);
		
		mDetailPagerArray = new ArrayList<BaseDetailPager>();
		
		mDetailPagerArray.add(new NewsDetailPager(mActivity,mDataArray.get(0).children));
		mDetailPagerArray.add(new SubjectDetailPager(mActivity));
		mDetailPagerArray.add(new PhotoDetailPager(mActivity,iv_exchange));
		mDetailPagerArray.add(new InteractionDetailPager(mActivity));
		
		if(spUtils.getBoolean("IsFirst", true)){
			setDetailPager(0);
		}
		spUtils.putBoolean("IsFirst", false);
	}
	
	public void setDetailPager(int position){
		BaseDetailPager detailPager = mDetailPagerArray.get(position);
		View view = detailPager.mRootView;
		
		fl_container.removeAllViews();
		fl_container.addView(view);
		
		detailPager.initData();
		tv_title.setText(mDataArray.get(position).title);
		
		if (detailPager instanceof PhotoDetailPager) {
			iv_exchange.setVisibility(View.VISIBLE);
		} else {
			iv_exchange.setVisibility(View.GONE);
		}
	}
}
