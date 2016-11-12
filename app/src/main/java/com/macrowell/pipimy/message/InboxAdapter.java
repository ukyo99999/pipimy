package com.macrowell.pipimy.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.InboxBean;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.ViewUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class InboxAdapter extends ArrayAdapter<InboxBean> {
    private LogUtility log = LogUtility.getInstance(InboxAdapter.class);

    private LayoutInflater mLayoutInflater;
    private DisplayImageOptions productImageOptions, avatarImageOptions;

    public InboxAdapter(Context context) {
        super(context, 0);
        this.mLayoutInflater = LayoutInflater.from(context);

        productImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        avatarImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.img_no_avatar).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(context));
    }

    @SuppressLint("NewApi")
    public void setData(List<InboxBean> data, Boolean isHasMsgData) {
        clear();
        if (data != null) {
            if (isHasMsgData) {
                addAll(data);
            }
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageViewProduct = null; //商品圖片
        ImageView imageViewAvatar = null; //留言者頭像
        TextView textViewUserName;  //留言者名稱
        TextView textViewProductName;  //商品名稱
        TextView textViewMessage;  //留言內容
        TextView textViewProductStatus; //商品狀態
        TextView textViewProductBidPrice; //商品已被出價的價格
        TextView textViewMsgCreateTime; //留言時間
        ProgressBar progressLoadingImage;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_inbox, null);
        }

		/* findViews */
        imageViewProduct = (ImageView) convertView.findViewById(R.id.img_inbox_product);
        imageViewAvatar = (ImageView) convertView.findViewById(R.id.img_inbox_avatar);
        textViewUserName = (TextView) convertView.findViewById(R.id.text_inbox_user_name);
        textViewProductName = (TextView) convertView.findViewById(R.id.text_inbox_product_name);
        textViewMessage = (TextView) convertView.findViewById(R.id.text_inbox_message);
        textViewProductStatus = (TextView) convertView.findViewById(R.id.text_inbox_bid_status);
        textViewProductBidPrice = (TextView) convertView.findViewById(R.id.text_inbox_bid_price);
        textViewMsgCreateTime = (TextView) convertView.findViewById(R.id.text_inbox_message_creat_time);
        progressLoadingImage = (ProgressBar) convertView.findViewById(R.id.progress_loading_image);

		/* 從Bean取出資料填入ListView items資訊 */
        InboxBean itemData = getItem(position);

        textViewProductName.setText(itemData.getItemName());
        textViewMessage.setText(itemData.getLastMsg());
        textViewMsgCreateTime.setText(itemData.getCrtime());


        ImageLoader.getInstance().displayImage(
                Constant.SERVER_URL + itemData.getItemPicUrl_1(),
                imageViewProduct, productImageOptions,
                productListener(progressLoadingImage));

        //買賣方填入不同的資料
        switch (itemData.getTypeCode()) {
            case 1: //買方
                textViewUserName.setText(itemData.getOwnerName());
                ImageLoader.getInstance().displayImage(
                        Constant.SERVER_URL + itemData.getOwnerPicUrl(),
                        imageViewAvatar, avatarImageOptions,
                        avtarListener(progressLoadingImage));

                break;
            case 2: //賣方
                textViewUserName.setText(itemData.getBuyerName());
                ImageLoader.getInstance().displayImage(
                        Constant.SERVER_URL + itemData.getBuyerPicUrl(),
                        imageViewAvatar, avatarImageOptions,
                        avtarListener(progressLoadingImage));
                break;
        }

        //填入商品狀態
        switch (itemData.getStatusCode()) {
            case 0: //待買方出價
                textViewProductStatus.setText("");
                textViewProductBidPrice.setText("");
                break;
            case 1: //待賣方接受
                textViewProductStatus.setText("");
                if (itemData.getBuyerPrice() >= 0) {
                    textViewProductBidPrice.setText("已出價 NT$" + String.valueOf(itemData.getBuyerPrice()));
                }
                break;
            case 2: //已接受出價
                textViewProductStatus.setText(" 接受出價 ");
                if (itemData.getBuyerPrice() >= 0) {
                    textViewProductBidPrice.setText("已出價 NT$" + String.valueOf(itemData.getBuyerPrice()));
                }
                break;
            case 3: //商品已出售
                textViewProductStatus.setText(" 已售出 ");
                break;
        }


        return convertView;
    }

    private SimpleImageLoadingListener productListener(
            final ProgressBar progressBar) {
        return new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    private SimpleImageLoadingListener avtarListener(
            final ProgressBar progressBar) {
        return new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                ((ImageView) view).setImageBitmap(ViewUtility.getRoundedCornerBitmap(loadedImage, 1));
            }
        };
    }

}
