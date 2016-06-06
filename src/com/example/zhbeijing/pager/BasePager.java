package com.example.zhbeijing.pager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhbeijing.MainActivity;
import com.example.zhbeijing.R;

public class BasePager {
	
	public Activity mActivity;
	public TextView tv_title;
	public ImageButton ibn_menu;
	public ImageButton iv_exchange;
	public View mRootView;
	public FrameLayout fl_container;

	public BasePager(Activity mActivity) {
		this.mActivity = mActivity;
		mRootView = initView();
	}
	
	public View initView(){
		View view = View.inflate(mActivity, R.layout.pager_base, null);
		
		tv_title = (TextView) view.findViewById(R.id.tv_base_title);
		ibn_menu = (ImageButton) view.findViewById(R.id.ibn_menu);
		iv_exchange = (ImageButton) view.findViewById(R.id.iv_exchange_photodetail);
		fl_container = (FrameLayout) view.findViewById(R.id.fl_containier);
		
		ibn_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity mMainUi = (MainActivity) mActivity;
				mMainUi.getSlidingMenu().toggle();
			}
		});
		
		return view;
	}
	
	public void initData(){
		
	}
}
