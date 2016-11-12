package com.macrowell.pipimy.me.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.macrowell.pipimy.utility.FragmentUtility;

import tw.com.pipimy.app.android.R;

public class MeSettingActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {
            // 進入設定
            FragmentUtility.addFragment(this, R.id.container,
                    new MeSettingFragment(), this.getIntent());
        }
    }


}
