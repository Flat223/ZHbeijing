package com.example.zhbeijing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.zhbeijing.fragment.ContentFragment;
import com.example.zhbeijing.fragment.LeftMenuFragment;
import com.example.zhbeijing.utils.DensityUtils;
import com.example.zhbeijing.utils.SharedPreferencesUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		setBehindContentView(R.layout.slidemenu_left);
		
		new SharedPreferencesUtil(this).putBoolean("IsFirst", true);
		
		SlidingMenu slidingMenu = getSlidingMenu();
//		slidingMenu.setMode(slidingMenu.LEFT_RIGHT);
//		slidingMenu.setSecondaryMenu(R.layout.slidemenu_right);
		
		WindowManager manager = getWindowManager();
		int width = manager.getDefaultDisplay().getWidth() * 5 /8 ;
		Log.e("TAG", "width : " + width);
		
		slidingMenu.setBehindOffset(width);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		initFragment();
	}

	private void initFragment() {
		LeftMenuFragment leftMenuFragment = new LeftMenuFragment();
		ContentFragment contentFragment = new ContentFragment();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		transaction.replace(R.id.fl_left_menu, leftMenuFragment, TAG_LEFT_MENU);
		transaction.replace(R.id.fl_main, contentFragment, TAG_CONTENT);
		
		transaction.commit();
	}
	
	public LeftMenuFragment getLeftMenuFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fragmentManager.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
	}
	
	public ContentFragment getContentFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fragmentManager.findFragmentByTag(TAG_CONTENT);
		return fragment;
	}
}
