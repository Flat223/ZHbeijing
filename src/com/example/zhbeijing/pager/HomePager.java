package com.example.zhbeijing.pager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class HomePager extends BasePager {

	public HomePager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		Log.e("TAG", "首页初始化");
		
		TextView textView = new TextView(mActivity);
		textView.setText("首页");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);
		fl_container.addView(textView);
		
		ibn_menu.setVisibility(View.GONE);
	}
}
