package com.macrowell.pipimy.me.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.MeBean;
import com.macrowell.pipimy.utility.LogUtility;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class MeEditAdapter extends ArrayAdapter<MeBean> {
    private LogUtility log = LogUtility.getInstance(MeEditAdapter.class);

    private LayoutInflater mLayoutInflater;
    private AQuery mAq;

    public MeEditAdapter(Context context) {
        super(context, 0);
        mAq = new AQuery(context);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("NewApi")
    public void setData(List<MeBean> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageViewProduct = null; //商品圖片
        TextView textViewProductName;  //商品名稱
        TextView textViewPrice; //商品售價
        TextView textViewLikeCount; //喜愛次數
        int isTracked; //目前登入的使用者 0:未按過讚 1:按過讚
//		TextView textViewCommentCount;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_me_post, null);
        }

		/* findViews */
        imageViewProduct = (ImageView) convertView.findViewById(R.id.img_product);
        textViewProductName = (TextView) convertView.findViewById(R.id.text_product_name);
        textViewPrice = (TextView) convertView.findViewById(R.id.text_product_price);
        textViewLikeCount = (TextView) convertView.findViewById(R.id.text_like_count);

		/* 從Bean取出資料填入ListView items資訊 */
        MeBean itemData = getItem(position);

        textViewProductName.setText(itemData.getItemName());
        textViewPrice.setText("NT $" + String.valueOf(itemData.getOriginalPrice()));
        textViewLikeCount.setText(String.valueOf(itemData.getTrackingCount()));

        isTracked = itemData.getIsTracked();

        if (isTracked == 0) {
            textViewLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_favorite_normal, 0, 0, 0);
        } else {
            textViewLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_favorite_pressed, 0, 0, 0);
        }

        mAq.id(imageViewProduct).image(Constant.SERVER_URL + itemData.getItemPicUrl_1(), true, true, 0, R.drawable.ic_launcher);

        return convertView;
    }
}
