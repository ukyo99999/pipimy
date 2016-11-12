package com.macrowell.pipimy.sell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.macrowell.pipimy.bean.CategoriesBean;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class SetCategoryAdapter extends ArrayAdapter<CategoriesBean> {
    private LayoutInflater mLayoutInflater;
    private int mPosition = 100;

    public SetCategoryAdapter(Context context) {
        super(context, 0);
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    @SuppressLint("NewApi")
    public void setData(List<CategoriesBean> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageViewProductCategory;
        TextView textCategroyItem;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.list_item_select_category, null);

        }

		/* findViews */
        imageViewProductCategory = (ImageView) convertView
                .findViewById(R.id.img_selected);
        textCategroyItem = (TextView) convertView
                .findViewById(R.id.text_categroy_item);

		/* 從Bean取出資料填入ListView items資訊 */
        CategoriesBean itemData = getItem(position);
        textCategroyItem.setText(itemData.getCategoryName());
        imageViewProductCategory.setImageResource(R.drawable.ic_action_selected);
        imageViewProductCategory.setVisibility(View.INVISIBLE); // 一開始先都隱藏

        if (position == mPosition) {
            imageViewProductCategory.setVisibility(View.VISIBLE); //
        }
        return convertView;
    }

    protected void setCheckedVisibile(int position) {
        mPosition = position;
    }
}
