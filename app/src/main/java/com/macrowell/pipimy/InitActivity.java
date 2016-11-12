package com.macrowell.pipimy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.macrowell.pipimy.utility.SharedPreferenceUtility;

import tw.com.pipimy.app.android.R;

/**
 * 動畫頁 用來載入一些初始資料背景處理
 * 
 * @author chris.cheng
 * 
 */
public class InitActivity extends Activity {

	private RelativeLayout layoutInit;
	private String deviceId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);

		layoutInit = (RelativeLayout) findViewById(R.id.layout_init);

		/*取得Device ID*/
		deviceId= Secure.getString(this.getContentResolver(),Secure.ANDROID_ID); 

		/*Device ID存入到SharedPreference*/
		SharedPreferenceUtility.setDeviceId(this,deviceId);
		
		
		/* 設定動畫 */
		AnimationSet animationSet = new AnimationSet(true);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		animationSet.addAnimation(alphaAnimation);
		layoutInit.setAnimation(animationSet);

		layoutInit.postDelayed(new Runnable() {

			@Override
			public void run() {

				/* 切換到選擇頁 */
				go2Home();

			}
		}, 2000);

	}

	/**
	 * 跳轉至主畫面
	 */
	private void go2Home() {
		Intent intent = new Intent();
		intent.setClass(InitActivity.this, HomeActivity.class);
		startActivity(intent);
		this.finish();
	}

}
