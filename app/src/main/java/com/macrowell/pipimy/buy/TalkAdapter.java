package com.macrowell.pipimy.buy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.PrivateMessageBean;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class TalkAdapter extends ArrayAdapter<PrivateMessageBean> {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private AQuery mAq;
    private ImageView imgAvatar;
    private TextView textPostTime;
    private TextView textTalkContent;
    private boolean isOwner; //是賣家

    public TalkAdapter(Context context, Boolean isOwner) {
        super(context, 0);
        this.context = context;
        this.isOwner = isOwner;
        mAq = new AQuery(context);
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    @SuppressLint("NewApi")
    public void setData(List<PrivateMessageBean> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // /* 從Bean取出資料填入ListView items資訊 */
        PrivateMessageBean itemData = getItem(position);

        int type = getItemViewType(position);

        if (convertView == null) {

            if (type == 0) {
                convertView = mLayoutInflater.inflate(
                        R.layout.list_item_talk_right, null);
            } else {
                convertView = mLayoutInflater.inflate(
                        R.layout.list_item_talk_left, null);
            }

        }

		/* findViews */
        imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        textPostTime = (TextView) convertView.findViewById(R.id.text_post_time);
        textTalkContent = (TextView) convertView
                .findViewById(R.id.text_talk_content);

        ImageOptions options = new ImageOptions();
        options.round = 30;

        if (!isOwner) {
            mAq.id(imgAvatar).image(
                    Constant.SERVER_URL + itemData.getOwnerPicUrl(), true, true, 0,
                    0, null, AQuery.FADE_IN_NETWORK, 1.0f);
        } else {
            mAq.id(imgAvatar).image(
                    Constant.SERVER_URL + itemData.getBuyerPicUrl(), true, true, 0,
                    0, null, AQuery.FADE_IN_NETWORK, 1.0f);
        }


        textPostTime.setText(itemData.getCrtime());
        textTalkContent.setText(itemData.getMsg());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        PrivateMessageBean itemData = getItem(position);
        int ownerId = itemData.getUid();
        if (SharedPreferenceUtility.getUid(context).equals(
                String.valueOf(ownerId))) {
            return 0;
        } else {
            return 1;
        }
    }

}
