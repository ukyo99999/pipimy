package com.macrowell.pipimy.buy;

import java.util.List;

import tw.com.pipimy.app.android.R;
import android.annotation.SuppressLint;
import android.content.Context;
//import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.OwnerPrivateMessageListBean;

public class OwnerPrivateMsgListAdapter extends ArrayAdapter<OwnerPrivateMessageListBean> {
	private LayoutInflater mLayoutInflater;
	private AQuery mAq;
	private ImageView imgAvatar;
	private TextView textBuyerName;
	private TextView textPostMsg;
	private TextView textBidPrice; 

	public OwnerPrivateMsgListAdapter(Context context) {
		super(context, 0);
		mAq = new AQuery(context);
		this.mLayoutInflater = LayoutInflater.from(context);

	}

	@SuppressLint("NewApi")
	public void setData(List<OwnerPrivateMessageListBean> data) {
		clear();
		if (data != null) {
			addAll(data);
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mLayoutInflater
					.inflate(R.layout.list_item_owner_msg_list, null);

		}

		/* findViews */
		imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
		textBuyerName = (TextView) convertView.findViewById(R.id.text_buyer_name);
		textPostMsg = (TextView) convertView.findViewById(R.id.text_post_msg);
		textBidPrice = (TextView) convertView.findViewById(R.id.text_bid_price);

		/* 從Bean取出資料填入ListView items資訊 */
		OwnerPrivateMessageListBean itemData = getItem(position);

		ImageOptions options = new ImageOptions();
		options.round = 30;


		mAq.id(imgAvatar).image(
				Constant.SERVER_URL + itemData.getBuyerPicUrl(), true, true, 0,
				0, null, AQuery.FADE_IN_NETWORK, 1.0f);
		
		textBuyerName.setText(String.valueOf(itemData.getBuyerUid()));
		textPostMsg.setText(itemData.getLastMsg());
		int buyerPrice = itemData.getBuyerPrice();
		if(buyerPrice == -1){
			buyerPrice = itemData.getOriginalPrice();
		}
		textBidPrice.setText("NT$"+String.valueOf(buyerPrice));
		textBidPrice.setTextColor(convertView.getResources().getColor(R.color.text_red));

		return convertView;
	}
}
