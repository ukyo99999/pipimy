package com.macrowell.pipimy.buy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import tw.com.pipimy.app.android.R;
import com.macrowell.pipimy.utility.FragmentUtility;

public class OwnerPrivateMsgListActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {
			// 進入賣家私訊列表頁
			FragmentUtility.addFragment(this, R.id.container,
					new OwnerPrivateMsgListFragment(), this.getIntent());
		}
	}

}
