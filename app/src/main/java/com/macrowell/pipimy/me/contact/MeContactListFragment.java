package com.macrowell.pipimy.me.contact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.macrowell.pipimy.bean.ContactBean;
import com.macrowell.pipimy.imagecache.ImageCacheFragment;
import com.macrowell.pipimy.utility.LogUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class MeContactListFragment extends ImageCacheFragment implements
        LoaderCallbacks<List<ContactBean>> {

    private LogUtility log = LogUtility.getInstance(MeContactListFragment.class);

    private ImageView btnBack; //返回上一頁
    private TextView textAdd; //新增通訊錄
    private RelativeLayout nodata; //沒有連絡人時顯示
    private ListView listView; //連絡人項目清單

    private MeContactListAdapter mContactListAdapter;
    private ProgressBar progressLoadingImage;

    private final int ADD_CONTACT = 0; //狀態為:新增連絡人
    private final int MODIFY_CONTACT = 1; //狀態為:修改連絡人
    private final int DEL_CONTACT = 2; //狀態為:刪除連絡人

    List<ContactBean> list; //用來存放Loader讀取完之後的list

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContactListAdapter = new MeContactListAdapter(getActivity(),true);

    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_contact_list, null);

		/*find views*/
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        textAdd = (TextView) view.findViewById(R.id.text_add);
        nodata = (RelativeLayout) view.findViewById(R.id.layout_no_data);
        listView = (ListView) view.findViewById(R.id.list);

        /*set data*/
        listView.setAdapter(mContactListAdapter);
        listView.setDividerHeight(0);

		/*set listener*/
        btnBack.setOnClickListener(listener);
        textAdd.setOnClickListener(listener);


        return view;
    }

    @Override
    public Loader<List<ContactBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new MeContactListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ContactBean>> arg0,
                               List<ContactBean> arg1) {

        list = arg1;

		/*set view data*/
        mContactListAdapter.setData(arg1);
        if (list.size() == 0) {
            nodata.setVisibility(View.VISIBLE);
        } else {
            nodata.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<ContactBean>> arg0) {
        mContactListAdapter.setData(null);

    }

    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_back:

                    // 清除目前頁面
                    getActivity().finish();
                    break;

                case R.id.text_add:
                    // 跳轉到新增通訊錄
                    Intent addIntent = new Intent(getActivity(),
                            MeContactModifyActivity.class);
//                    addIntent.putExtra("isModify", false);
                    addIntent.putExtra("modifyAction", ADD_CONTACT);
                    startActivity(addIntent);

//                    ViewUtility.showToast(getActivity(), "新增通訊錄(尚未開放，敬請期待)");
                    break;

            }

        }
    };


}
