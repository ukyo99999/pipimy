package com.macrowell.pipimy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.macrowell.pipimy.product.ProductCategoryActivity;

import tw.com.pipimy.app.android.R;

public class HomeFragment extends Fragment {

    private Button btnLogin;
    private Button btnRegister;
    private Button btnVisitor;
    private TextView textVersion;
    private String version;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, null);

		/* find views */
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
        btnVisitor = (Button) view.findViewById(R.id.btn_visitor);
        textVersion = (TextView) view.findViewById(R.id.text_version);

		/* set version code */
        try {
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo info = pm.getPackageInfo(getActivity()
                    .getApplicationContext().getPackageName(), 0);
            version = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        String versionName = getResources().getString(R.string.version) + version;
        textVersion.setText(versionName);

		/* set listener */
        btnLogin.setOnClickListener(listener);
        btnRegister.setOnClickListener(listener);
        btnVisitor.setOnClickListener(listener);

        btnRegister.setVisibility(View.INVISIBLE);
        btnVisitor.setVisibility(View.INVISIBLE);

        return view;
    }

    /**
     * 按鈕的監聽
     */
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    // 跳轉到登入頁
                    Intent intentLogin = new Intent(getActivity(),
                            LoginActivity.class);
                    startActivity(intentLogin);

                    // 清除目前頁面
                    getActivity().finish();
                    break;

                case R.id.btn_register:
                    break;

                case R.id.btn_visitor:
                    // 跳轉到產品大分類頁
                    Intent intentVisitor = new Intent(getActivity(),
                            ProductCategoryActivity.class);
                    startActivity(intentVisitor);

                    // 清除目前頁面
                    getActivity().finish();
                    break;
            }

        }
    };

}
