package com.example.zhbeijing.detailpager;

import android.app.Activity;
import android.view.View;

public abstract class BaseDetailPager {
	
	public Activity mActivity;
	public View mRootView;
	
	public BaseDetailPager(Activity mActivity) {
		super();
		this.mActivity = mActivity;
		mRootView = initView();
	}

	public abstract View initView();
	
	public void initData(){
		
	}
}
