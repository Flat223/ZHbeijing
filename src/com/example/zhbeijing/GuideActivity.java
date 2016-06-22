package com.example.zhbeijing;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zhbeijing.utils.DensityUtils;
import com.example.zhbeijing.utils.SharedPreferencesUtil;

public class GuideActivity extends Activity {

	private SharedPreferencesUtil utils;
	private ViewPager mViewPager;
	private Button btn_start;
	private LinearLayout ll_point_container;

	private List<ImageView> mImageViewArray;
	private int[] mImageIds;
	private int mPointDis;
	private ImageView mIvRedPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		utils = new SharedPreferencesUtil(this);
		initViews();
		initData();
		mViewPager.setAdapter(new GuideAdapter());

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			// 当页面被选中
			@Override
			public void onPageSelected(int position) {

				if (position == mImageViewArray.size() - 1) {
					btn_start.setVisibility(View.VISIBLE);
				} else {
					btn_start.setVisibility(View.INVISIBLE);
				}
			}

			/**
			 * 当页面滑动时
			 *  positionOffset : 页面偏移量百分比; 
			 *  positionOffsetPixels : 页面偏移量具体像素;
			 * 
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

				int leftMargin = (int) (mPointDis * (positionOffset + position));
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvRedPoint
						.getLayoutParams();
				layoutParams.leftMargin = leftMargin;

				mIvRedPoint.setLayoutParams(layoutParams);
			}

			// 当页面状态改变(eg:不滑变成滑动)
			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		mIvRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						mIvRedPoint.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						int left = ll_point_container.getChildAt(0).getLeft();
						int medium = ll_point_container.getChildAt(1).getLeft();

						mPointDis = medium - left;
					}
				});
	}

	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		ll_point_container = (LinearLayout) findViewById(R.id.ll_point_container);
		mIvRedPoint = (ImageView) findViewById(R.id.iv_guide_redpoint);

		btn_start = (Button) findViewById(R.id.btn_guide_start);
		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				utils.putBoolean("is_first_enter", false);
				finish();
			}
		});
	}

	private void initData() {
		mImageViewArray = new ArrayList<ImageView>();
		mImageIds = new int[] { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3 };

		for (int i = 0; i < mImageIds.length; i++) {
			ImageView mImageView = new ImageView(this);
			mImageView.setBackgroundResource(mImageIds[i]);
			mImageViewArray.add(mImageView);

			ImageView mPointView = new ImageView(this);
			mPointView.setImageResource(R.drawable.shape_point_gray);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			if (i > 0) {
				params.leftMargin = DensityUtils.dpTopx(10, this);
			}
			mPointView.setLayoutParams(params);
			ll_point_container.addView(mPointView);
		}

	}

	class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViewArray.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// 初始化item布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViewArray.get(position);
			container.addView(view);
			return view;
		}

		// 销毁item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
