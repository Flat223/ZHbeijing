package com.example.zhbeijing.detailpager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class InteractionDetailPager extends BaseDetailPager{

	public InteractionDetailPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(mActivity);
		textView.setText("菜单详情页-互动");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);
		
		return textView;
	}
	
}
