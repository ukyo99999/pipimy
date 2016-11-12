package com.macrowell.pipimy.bill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.BillBean;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.ViewUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class BillAdapter extends ArrayAdapter<BillBean> {
    private LogUtility log = LogUtility.getInstance(BillAdapter.class);

    private LayoutInflater mLayoutInflater;
    private DisplayImageOptions productImageOptions;

    public BillAdapter(Context context) {
        super(context, 0);
        this.mLayoutInflater = LayoutInflater.from(context);

        productImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(context));
    }

    public void setData(List<BillBean> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageProduct = null; //商品圖片
        TextView textProductName;  //商品名稱
        TextView textProductPrice;  //商品價格
        ProgressBar progressLoadingImage;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_bill_product, null);
        }

		/* findViews */
        imageProduct = (ImageView) convertView.findViewById(R.id.img_product);
        textProductName = (TextView) convertView.findViewById(R.id.text_product_name);
        textProductPrice = (TextView) convertView.findViewById(R.id.text_product_price);
        progressLoadingImage = (ProgressBar)convertView.findViewById(R.id.progress_loading_image);

		/* 從Bean取出資料填入ListView items資訊 */
        BillBean itemData = getItem(position);

        textProductName.setText(itemData.getItemName());
        textProductPrice.setText("NT$"+String.valueOf(itemData.getBuyerPrice()));

        ImageLoader.getInstance().displayImage(
                Constant.SERVER_URL + itemData.getProductPicUrl(),
                imageProduct, productImageOptions,
                productListener(progressLoadingImage));


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


}
