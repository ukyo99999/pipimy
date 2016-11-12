package com.macrowell.pipimy.product;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import tw.com.pipimy.app.android.R;
import com.macrowell.pipimy.utility.FragmentUtility;

public class ProductDetailActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {
			// 進入產品同分類頁
			FragmentUtility.addFragment(this, R.id.container,
					new ProductDetailFragment(), this.getIntent());
		}
	}

}
