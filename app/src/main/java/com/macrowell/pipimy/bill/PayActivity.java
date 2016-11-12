package com.macrowell.pipimy.bill;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.macrowell.pipimy.utility.FragmentUtility;

import tw.com.pipimy.app.android.R;

public class PayActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {

            // 進入付款頁
            FragmentUtility.addFragment(this, R.id.container,
                    new PayFragment(), this.getIntent());
        }
    }
}
