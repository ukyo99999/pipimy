package com.macrowell.pipimy.bill;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.macrowell.pipimy.utility.FragmentUtility;

import tw.com.pipimy.app.android.R;

public class BillActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        if (FragmentUtility.getContainerFragment(this, R.id.container) == null) {
            // 進入結帳單
            FragmentUtility.addFragment(this, R.id.container,
                    new BillFragment(), this.getIntent());
        }
    }

}
