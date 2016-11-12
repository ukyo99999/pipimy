package com.macrowell.pipimy.sell;

import tw.com.pipimy.app.android.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.FragmentUtility;

public class AddProductActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {
			// 進入產品同分類頁
			FragmentUtility.addFragment(this, R.id.container,
					new AddProductFragment(), this.getIntent());
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// 刪除暫時拍的照片資料夾
		CommonUtility.delUploadImagesFolder();
	}

}
