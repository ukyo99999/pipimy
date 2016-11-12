package com.macrowell.pipimy.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tw.com.pipimy.app.android.R;

public class ProductSearchAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private String[] data;

    public ProductSearchAdapter(Context context, String[] data) {
        super();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textViewSearchRecode;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.list_item_search_recode, null);

        }

		/* findViews */
        textViewSearchRecode = (TextView) convertView
                .findViewById(R.id.text_search_item);

		/* 填入ListView items資訊 */
        textViewSearchRecode.setText(data[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
