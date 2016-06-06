package com.example.zhbeijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.zhbeijing.utils.SharedPreferencesUtil;

public class SplashActivity extends Activity implements AnimationListener {
	
	private RelativeLayout rl_splash;
	private SharedPreferencesUtil utils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		init();
	}

	private void init() {
		
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
		utils = new SharedPreferencesUtil(this);
		
		RotateAnimation rotateAnimation = new RotateAnimation(
				0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(2000);
		rotateAnimation.setFillAfter(true);
		
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(2000);
		scaleAnimation.setFillAfter(true);
		
		AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setDuration(3000);
		
		AnimationSet animationSet = new AnimationSet(true);
		
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);
		
		rl_splash.startAnimation(animationSet);
		
		animationSet.setAnimationListener(this);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if(utils.getBoolean("is_first_enter", true)){
			startActivity(new Intent(this, GuideActivity.class));
		} else {
			startActivity(new Intent(this, MainActivity.class));
		}
		finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
}
