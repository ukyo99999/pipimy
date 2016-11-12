package com.macrowell.pipimy.me.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.macrowell.pipimy.HomeActivity;
import com.macrowell.pipimy.me.contact.MeContactListActivity;
import com.macrowell.pipimy.me.edit.MeEditActivity;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;

import java.util.ArrayList;

import tw.com.pipimy.app.android.R;

public class MeSettingFragment extends Fragment {

    private LogUtility log = LogUtility.getInstance(MeSettingFragment.class);


    private ImageView btnCommit; // 完成
    private TextView textTitle; // 標題文字
    private ListView listView; //每個選項的ListView
    private MeSettingAdapter adapter; //選項的ListView用的adapter
    private ArrayList<String> items; //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String version = "";

        /* set version code */
        try {
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo info = pm.getPackageInfo(getActivity()
                    .getApplicationContext().getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        items = new ArrayList<String>();
        items.add("");
        items.add("編輯檔案");
        items.add("物流設定");
        items.add("通訊錄設定");
        items.add("通知設定");
        items.add("登出");
        items.add("Version: " + version);
        items.add("入門指南");
        items.add("常見Q&A");
        items.add("使用者服務條款");
        items.add("關於PiPiMy");
        items.add("");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_setting, null);
        adapter = new MeSettingAdapter(getActivity(), items);


		/* find views */
        btnCommit = (ImageView) view.findViewById(R.id.btn_commit);
        textTitle = (TextView) view.findViewById(R.id.title);
        listView = (ListView) view.findViewById(R.id.list);

		/* set data */
        textTitle.setText("設定");
        listView.setAdapter(adapter);

		/* set listener */
        btnCommit.setOnClickListener(listener); // 完成
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1: //編輯檔案
                        // 跳轉到編輯檔案頁面
                        Intent intent = new Intent(getActivity(), MeEditActivity.class);

                        startActivity(intent);

                        // 清除目前頁面
                        getActivity().finish();
                        break;

                    case 2: //物流設定
                        ViewUtility.showToast(getActivity(), "物流設定(尚未開放，敬請期待)");
                        break;

                    case 3: //通訊錄設定
//                        ViewUtility.showToast(getActivity(), "通訊錄設定(尚未開放，敬請期待)");
                        // 跳轉到設定頁面
                        Intent contactListIntent = new Intent(getActivity(),
                                MeContactListActivity.class);

                        startActivity(contactListIntent);
                        break;

                    case 4: //通知設定
                        ViewUtility.showToast(getActivity(), "通知設定(尚未開放，敬請期待)");
                        break;

                    case 5: //登出
                        //清除Token
                        SharedPreferenceUtility.setAuth(getActivity(), "");

                        // 跳轉到首頁
                        Intent intentHome = new Intent(getActivity(),
                                HomeActivity.class);

//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//                        fm.popBackStack();
//                    }
//
//                    FragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);

                        // 清除目前頁面
                        getActivity().finish();

                        break;

                    case 7: //入門指南
                        ViewUtility.showToast(getActivity(), "入門指南(尚未開放，敬請期待)");
                        break;

                    case 8: //常見Q&A
                        ViewUtility.showToast(getActivity(), "常見Q&A(尚未開放，敬請期待)");
                        break;

                    case 9: //使用者服務條款
                        ViewUtility.showToast(getActivity(), "使用者服務條款(尚未開放，敬請期待)");
                        break;

                    case 10: //關於PiPiMy
                        ViewUtility.showToast(getActivity(), "關於PiPiMy(尚未開放，敬請期待)");
                        break;
                }

            }
        });

        return view;
    }


    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_commit:

                    // 關閉頁面
                    getActivity().finish();
                    break;


            }

        }
    };


}
