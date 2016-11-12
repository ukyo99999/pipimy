package com.macrowell.pipimy.sell;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.macrowell.pipimy.bean.CategoriesBean;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class SetCategoryFragment extends Fragment implements
        LoaderCallbacks<List<CategoriesBean>> {

    private ListView listViewItems;
    private ImageView imageBack;
    private SetCategoryAdapter setCategoryAdapter;
    private List<CategoriesBean> list;
    private int categoryId = 0; // 分類項目id
    private String categoryName = ""; // 分類項目名稱

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCategoryAdapter = new SetCategoryAdapter(getActivity());
        getLoaderManager().initLoader(0, null, this);

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.set_categroy_items, null);

		/* findViews */
        listViewItems = (ListView) view
                .findViewById(R.id.listview_categroy_items);
        imageBack = (ImageView) view.findViewById(R.id.btn_back);

		/* 設定listview */
        listViewItems.setAdapter(setCategoryAdapter);

        listViewItems.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // 設定按下項目顯示打勾位置
                setCategoryAdapter.setCheckedVisibile(position);

                // 取得分類項目的id
                categoryId = list.get(position).getCategoryId();
                categoryName = list.get(position).getCategoryName();

                // 刷新Listview
                setCategoryAdapter.notifyDataSetChanged();
            }
        });

		/* 按下返回 */
        imageBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // 送出設定值
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("CategoryId", categoryId);
                bundle.putString("CategoryName", categoryName);
                intent.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK, intent);

                // 關閉頁面
                getActivity().finish();
            }
        });

        return view;

    }

    @Override
    public Loader<List<CategoriesBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new SetCategoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<CategoriesBean>> arg0,
                               List<CategoriesBean> arg1) {
        setCategoryAdapter.setData(arg1);
        list = arg1; // 存入公用的List
    }

    @Override
    public void onLoaderReset(Loader<List<CategoriesBean>> arg0) {
        setCategoryAdapter.setData(null);

    }

}
