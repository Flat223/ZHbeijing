package com.example.zhbeijing.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.zhbeijing.MainActivity;
import com.example.zhbeijing.R;
import com.example.zhbeijing.pager.BasePager;
import com.example.zhbeijing.pager.GovServicePager;
import com.example.zhbeijing.pager.HomePager;
import com.example.zhbeijing.pager.NewsCenterPager;
import com.example.zhbeijing.pager.SettingPager;
import com.example.zhbeijing.pager.SmartServicePager;
import com.example.zhbeijing.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ContentFragment extends BaseFragment {
	
	public List<BasePager> mPagerArray = new ArrayList<BasePager>();
	private View view;
	private NoScrollViewPager mViewPager;
	private RadioGroup mRadioGroup;
	
	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.fragment_content,null);
		mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_content);
		
		return view;
	}

	@Override
	public void initData() {
		mPagerArray.add(new HomePager(mActivity));
		mPagerArray.add(new NewsCenterPager(mActivity)); 
		mPagerArray.add(new SmartServicePager(mActivity)); 
		mPagerArray.add(new GovServicePager(mActivity)); 
		mPagerArray.add(new SettingPager(mActivity));
		
		mPagerArray.get(0).initData();
		setSlidingMenuEnable(false);
		mViewPager.setAdapter(new ContentAdapter());
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbn_content_Home:
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.rbn_content_News:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rbn_content_SmartServ:
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.rbn_content_GovServ:
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.rbn_content_Setting:
					mViewPager.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});
		
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//为了节省流量，只在滑倒当前页面时采取加载页面
				mPagerArray.get(position).initData();
				
				if (position == 0 || position == mPagerArray.size() - 1){
					setSlidingMenuEnable(false);
				} else {
					setSlidingMenuEnable(true);
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
	
	protected void setSlidingMenuEnable(boolean enable) {
		MainActivity mMainUi = (MainActivity) mActivity;
		if(enable){
			mMainUi.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			mMainUi.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	class ContentAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mPagerArray.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager basePager = mPagerArray.get(position);
			//为了节省流量,初始化时不加载页面
//			basePager.initData();
			container.addView(basePager.mRootView);
			return basePager.mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	public NewsCenterPager getNewsCenterPager(){
		NewsCenterPager pager = (NewsCenterPager) mPagerArray.get(1);
		return pager;
	}
}








