package com.macrowell.pipimy.me.contact;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.macrowell.pipimy.utility.FragmentUtility;

import tw.com.pipimy.app.android.R;

public class MeContactModifyActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {
			// 進入新增/修改連絡人
			FragmentUtility.addFragment(this, R.id.container,
					new MeContactModifyFragment(), this.getIntent());
		}
	}

}
