package com.macrowell.pipimy.sell;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import tw.com.pipimy.app.android.R;
import com.macrowell.pipimy.bean.CategoriesBean;

public class SetProductNameFragment extends Fragment {

	private ImageView imageCancel;
	private ImageView imageOk;
	private EditText editProductName;
	private EditText editProductProductDescription;

	private List<CategoriesBean> list;
	private String categoryId = ""; // 分類項目id


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setCategoryAdapter = new SetCategoryAdapter(getActivity());
		// this.mSetCallBack = (SetCallBack) getActivity();

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.set_product_name, null);

		/* findViews */
		imageOk = (ImageView) view.findViewById(R.id.btn_ok);
		imageCancel = (ImageView) view.findViewById(R.id.btn_cancel);
		editProductName = (EditText) view.findViewById(R.id.edit_product_name);
		editProductProductDescription = (EditText) view.findViewById(R.id.edit_product_description);

		/* 按下完成 */
		imageOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				bundle.putString("ProductName", editProductName.getText().toString());
				bundle.putString("ProductDescription", editProductProductDescription.getText().toString());
				intent.putExtras(bundle);
				getActivity().setResult(Activity.RESULT_OK, intent);
				
				// 關閉頁面
				getActivity().finish();
			}
		});

		/* 按下取消 */
		imageCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// 關閉頁面
				getActivity().finish();
			}
		});

		return view;

	}

}
