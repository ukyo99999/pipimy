package com.macrowell.pipimy.product;

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
import com.macrowell.pipimy.bean.ProductDetailBean;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class ProductDetailAdapter extends ArrayAdapter<ProductDetailBean> {
    private LayoutInflater mLayoutInflater;
    private AQuery mAq;

    public ProductDetailAdapter(Context context) {
        super(context, 0);
        mAq = new AQuery(context);
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    @SuppressLint("NewApi")
    public void setData(List<ProductDetailBean> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageViewProductList;
        TextView textViewProductListName;
        TextView textViewProductListPrice;
        ImageView imageViewProductListOwnerPic;
        TextView textViewProductListOwnerName;
        TextView textViewProductListLikeNumber;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.list_item_product_list, null);

        }

		/* findViews */
        imageViewProductList = (ImageView) convertView.findViewById(R.id.img_product_list);
        textViewProductListName = (TextView) convertView.findViewById(R.id.text_product_list_name);
        textViewProductListPrice = (TextView) convertView.findViewById(R.id.text_product_list_price);
        imageViewProductListOwnerPic = (ImageView) convertView.findViewById(R.id.img_product_list_owner_pic);
        textViewProductListOwnerName = (TextView) convertView.findViewById(R.id.text_product_list_owner_name);
        textViewProductListLikeNumber = (TextView) convertView.findViewById(R.id.text_product_list_like);

		/* 從Bean取出資料填入ListView items資訊 */
        ProductDetailBean itemData = getItem(position);

        mAq.id(imageViewProductList).image(Constant.SERVER_URL + itemData.getItemPicUrl_1(), true,
                true, 0, R.drawable.ic_launcher);
        textViewProductListName.setText(itemData.getItemName());
        textViewProductListPrice.setText(itemData.getOriginalPrice());

        mAq.id(imageViewProductListOwnerPic).image(Constant.SERVER_URL + itemData.getOwnerPicUrl(), true,
                true, 0, R.drawable.ic_launcher);

        textViewProductListOwnerName.setText(itemData.getOwnerName());
        textViewProductListLikeNumber.setText(String.valueOf(itemData.getTrackingCount()));

        return convertView;
    }
}
