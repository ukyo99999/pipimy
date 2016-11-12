package com.macrowell.pipimy.sell;

import tw.com.pipimy.app.android.R;
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

import com.macrowell.pipimy.utility.CallBacks.PriceCallBack;

public class SetProductPriceFragment extends Fragment {

	private ImageView imageCancel;
	private ImageView imageOk;
	private EditText editProductPrice;
//	private PriceCallBack priceCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.set_product_price, null);

		/* findViews */
		imageOk = (ImageView) view.findViewById(R.id.btn_ok);
		imageCancel = (ImageView) view.findViewById(R.id.btn_cancel);
		editProductPrice = (EditText) view
				.findViewById(R.id.edit_product_price);

		/* 按下完成 */
		imageOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("Price", editProductPrice.getText().toString());
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

				// 送出設定值(callback)
				// mSetCallBack.setCategoryId(String.valueOf(categoryId));
				// 關閉頁面
				getActivity().finish();
			}
		});

		return view;

	}

}
