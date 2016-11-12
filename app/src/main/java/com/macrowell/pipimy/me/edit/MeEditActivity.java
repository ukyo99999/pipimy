package com.macrowell.pipimy.me.edit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.FragmentUtility;

import tw.com.pipimy.app.android.R;

public class MeEditActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {
            // 進入編輯個人資料檔案
            FragmentUtility.addFragment(this, R.id.container,
                    new MeEditFragment(), this.getIntent());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // 刪除暫時拍的照片資料夾
        CommonUtility.delUploadImagesFolder();

    }

}
