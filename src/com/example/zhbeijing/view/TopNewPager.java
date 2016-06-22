package com.example.zhbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewPager extends ViewPager {
	
	private float startX;
	private float startY;

	public TopNewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public TopNewPager(Context context) {
		super(context);
		
	}
	

	/**
	 * 	1. 上下滑动需要拦截
	 * 	2. 向右滑动并且当前是第一个页面,需要拦截
	 *  3. 向左滑动并且当前是最后一个页面,需要拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			startX = ev.getX();
			startY = ev.getY();
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			float endX = ev.getX();
			float endY = ev.getY();
			
			float dx = endX - startX;
			float dy = endY - startY;
			
			if (Math.abs(dy) < Math.abs(dx)) {
				int currentItem = getCurrentItem();
				// 左右滑动
				if (dx > 0) {
					// 向右划
					if (currentItem == 0) {
						// 第一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 向左划
					int count = getAdapter().getCount();// item总数
					if (currentItem == count - 1) {
						// 最后一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else {
				// 上下滑动,需要拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
