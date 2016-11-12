package com.macrowell.pipimy.me.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tw.com.pipimy.app.android.R;

/**
 * Created by Chris on 15/1/26.
 */
public class MeSettingAdapter extends BaseAdapter {

    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private LayoutInflater layoutInflater;
    private ArrayList<String> strItmes;

    MeSettingAdapter(Context context, ArrayList<String> itmes) {
        this.layoutInflater = LayoutInflater.from(context);
        this.strItmes = itmes;

    }

    /*有多少條項目顯示*/
    @Override
    public int getCount() {
        return strItmes.size();
    }

    /*如果要取得某一列的內容*/
    @Override
    public Object getItem(int position) {
        return strItmes.get(position);
    }

    /*取得某一列的id*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*總共有多少不同類型的項目*/
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /*取得不同的項目類型*/
    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 6 || position == 11) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }

    /*產生每一列的內容*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //取得每個position的項目類型
        int type = getItemViewType(position);

        switch (type) {
            case TYPE_1:
                //個別的ListView item裡的view物件
                ViewHolder1 viewHolder1 = null;

                if (convertView == null) {
                    convertView = this.layoutInflater.inflate(R.layout.list_item_me_setting1, null);
                    viewHolder1 = new ViewHolder1();
                    viewHolder1.textItemName = (TextView) convertView.findViewById(R.id.text_title);
                    convertView.setTag(viewHolder1);
                } else {
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                }
                viewHolder1.textItemName.setText(strItmes.get(position));
                break;
            case TYPE_2:
                //個別的ListView item裡的view物件
                ViewHolder2 viewHolder2 = null;

                if (convertView == null) {
                    convertView = this.layoutInflater.inflate(R.layout.list_item_me_setting2, null);
                    viewHolder2 = new ViewHolder2();
                    viewHolder2.textItemName = (TextView) convertView.findViewById(R.id.text_title);
                    viewHolder2.imgNext = (ImageView) convertView.findViewById(R.id.img_next);
                    convertView.setTag(viewHolder2);

                } else {
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                }
                viewHolder2.textItemName.setText(strItmes.get(position));
                viewHolder2.imgNext.setImageDrawable(convertView.getResources().getDrawable(R.drawable.btn_next));
                break;
        }


        return convertView;
    }


    /*自訂類別，用來做為項目的UI元件:(樣式1)*/
    class ViewHolder1 {
        TextView textItemName;
    }

    /*自訂類別，用來做為項目的UI元件:(樣式2)*/
    class ViewHolder2 {
        TextView textItemName;
        ImageView imgNext;
    }

}
