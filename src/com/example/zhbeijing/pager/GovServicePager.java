package com.example.zhbeijing.pager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class GovServicePager extends BasePager {

	public GovServicePager(Activity mActivity) {
		super(mActivity);
	}
	
	@Override
	public void initData() {
		Log.e("TAG", "政务初始化");
		
		TextView textView = new TextView(mActivity);
		textView.setText("政务");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);
		fl_container.addView(textView);
		
		tv_title.setText("人口管理");
	}
}
