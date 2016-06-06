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
			
			if (Math.abs(dy) > Math.abs(dx)) {
				getParent().requestDisallowInterceptTouchEvent(false);	
			} else {
				int currentItem = getCurrentItem();
				if (currentItem == 0 && dx > 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
						
				} else {
					int count = getAdapter().getCount();
					if (currentItem == (count - 1) && dx < 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			}
			
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
