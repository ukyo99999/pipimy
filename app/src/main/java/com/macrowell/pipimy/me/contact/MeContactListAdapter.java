package com.macrowell.pipimy.me.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.macrowell.pipimy.bean.ContactBean;
import com.macrowell.pipimy.utility.LogUtility;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class MeContactListAdapter extends ArrayAdapter<ContactBean> {
    private LogUtility log = LogUtility.getInstance(MeContactListAdapter.class);

    private final int MODIFY_CONTACT = 1; //狀態為:修改連絡人
    private LayoutInflater mLayoutInflater;
    private boolean isFromMe; //從我的頁面進入

    public MeContactListAdapter(Context context, boolean isFromMe) {
        super(context, 0);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isFromMe = isFromMe;

    }

    public void setData(List<ContactBean> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewName;  //姓名
        TextView textViewMobile; //手機
        TextView textViewAddress; //地址
        ImageView imgContactEdit; //編輯連絡人
//        ProgressBar progressLoadingImage;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_contact, null);
        }

		/* findViews */
        textViewName = (TextView) convertView.findViewById(R.id.text_contact_name);
        textViewMobile = (TextView) convertView.findViewById(R.id.text_contact_mobile);
        textViewAddress = (TextView) convertView.findViewById(R.id.text_contact_address);
        imgContactEdit = (ImageView) convertView.findViewById(R.id.img_contact_edit);

		/* 從Bean取出資料填入ListView items資訊 */
        final ContactBean itemData = getItem(position);

        textViewName.setText(itemData.getUsername());
        textViewMobile.setText(itemData.getMobile());
        textViewAddress.setText(itemData.getCity() + itemData.getTown() + itemData.getAddress());

        if (isFromMe) {
            imgContactEdit.setVisibility(View.VISIBLE);
        } else {
            imgContactEdit.setVisibility(View.GONE);
        }
        /*設定編輯連絡人按鈕監聽*/
        imgContactEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 跳轉到編輯通訊錄
                Intent modifyIntent = new Intent(getContext(),
                        MeContactModifyActivity.class);
                modifyIntent.putExtra("modifyAction", MODIFY_CONTACT);
                modifyIntent.putExtra("id", itemData.getAutoID());
                modifyIntent.putExtra("name", itemData.getUsername());
                modifyIntent.putExtra("mobile", itemData.getMobile());
                modifyIntent.putExtra("city", itemData.getCity());
                modifyIntent.putExtra("town", itemData.getTown());
                modifyIntent.putExtra("address", itemData.getAddress());

                getContext().startActivity(modifyIntent);

            }
        });


        return convertView;
    }

}
