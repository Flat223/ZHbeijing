package com.example.zhbeijing.pager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SettingPager extends BasePager {

	public SettingPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		Log.e("TAG", "设置初始化");
		
		TextView textView = new TextView(mActivity);
		textView.setText("设置");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);
		fl_container.addView(textView);
		
		tv_title.setText("设置");
		
		ibn_menu.setVisibility(View.GONE);
	}
}
