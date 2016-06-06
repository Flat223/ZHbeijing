package com.example.zhbeijing.pager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		Log.e("TAG", "智慧服务初始化");
		
		TextView textView = new TextView(mActivity);
		textView.setText("智慧服务");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);
		fl_container.addView(textView);
		
		tv_title.setText("生活");
	}
}
