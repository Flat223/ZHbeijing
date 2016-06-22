package com.example.zhbeijing.detailpager;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhbeijing.MainActivity;
import com.example.zhbeijing.R;
import com.example.zhbeijing.domain.NewsMenu.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

public class NewsDetailPager extends BaseDetailPager{
	
	@ViewInject(R.id.vp_news_detail)
	private ViewPager mViewPager;
	
	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;
	
	private TabDetailPager detailPager;
	private NewsDetailAdapter adapter;
	
	public ArrayList<NewsTabData> mTabDataArray;
	public ArrayList<TabDetailPager> mPagerArray;
	
	public NewsDetailPager(Activity mActivity, ArrayList<NewsTabData> children) {
		super(mActivity);
		mTabDataArray = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity,R.layout.pager_news_detail,null);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void initData() {
		mPagerArray = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabDataArray.size(); i++) {
			NewsTabData data = mTabDataArray.get(i);
			detailPager = new TabDetailPager(mActivity,data);
//			Log.e("TAG","title : " + data.title);
			mPagerArray.add(detailPager); 
		}
		
		adapter = new NewsDetailAdapter();
		mViewPager.setAdapter(adapter);
		indicator.setViewPager(mViewPager);//将指示器与ViewPager绑定,一定要在setAdapter之后进行
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					setSlidingMenuEnable(true);
				} else {
					setSlidingMenuEnable(false);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
	}
	
	class NewsDetailAdapter extends PagerAdapter{
		
		@Override
		public CharSequence getPageTitle(int position) {
			
			NewsTabData tabData = mTabDataArray.get(position);
			
			return tabData.title;//设置 tab的title
		}

		@Override
		public int getCount() {
			return mPagerArray.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return object == view;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagerArray.get(position);
			pager.initData();
			View view = pager.mRootView;
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	protected void setSlidingMenuEnable(boolean enable) {
		MainActivity mMainUi = (MainActivity) mActivity;
		if(enable){
			mMainUi.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			mMainUi.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	@OnClick(R.id.ibn_next)
	public void next(View view){
		mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
	}
}








