package com.macrowell.pipimy.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.ProductListBean;
import com.macrowell.pipimy.imagecache.ImageFetcherWrapper;
import com.macrowell.pipimy.utility.ViewUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class ProductListAdapter extends ArrayAdapter<ProductListBean> {
    private LayoutInflater mLayoutInflater;
    private DisplayImageOptions productImageOptions;
    private DisplayImageOptions avatarImageOptions;

    public ProductListAdapter(Context context,
                              ImageFetcherWrapper imageFetcherWrapper) {
        super(context, 0);
        this.mLayoutInflater = LayoutInflater.from(context);

        productImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

        avatarImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.img_no_avatar)
                .showImageOnFail(R.drawable.img_no_avatar)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(context));

    }

    @SuppressLint("NewApi")
    public void setData(List<ProductListBean> data) {
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
        ProgressBar progressLoadingImage;

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
        progressLoadingImage = (ProgressBar) convertView.findViewById(R.id.progress_loading_image);

		/* 從Bean取出資料填入ListView items資訊 */
        ProductListBean itemData = getItem(position);

        ImageLoader.getInstance().displayImage(
                Constant.SERVER_URL + itemData.getItemPicUrl_1(),
                imageViewProductList, productImageOptions,
                loadingListener(progressLoadingImage));

        textViewProductListName.setText(itemData.getItemName());
        textViewProductListName.setEllipsize(TruncateAt.END);
        textViewProductListPrice.setText("NT $" + itemData.getOriginalPrice());

        ImageLoader.getInstance().displayImage(
                Constant.SERVER_URL + itemData.getOwnerPicUrl(),
                imageViewProductListOwnerPic, avatarImageOptions,
                avatarLoadingListener(progressLoadingImage));
        textViewProductListOwnerName.setText(itemData.getOwnerName());
        textViewProductListLikeNumber.setText(String.valueOf(itemData
                .getTrackingCount()));

        return convertView;
    }

    private SimpleImageLoadingListener loadingListener(
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
                ((ImageView) view).setImageBitmap(ViewUtility.getRoundedCornerBitmap(loadedImage, 2));
            }
        };
    }

    ;

    private SimpleImageLoadingListener avatarLoadingListener(
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

    ;

    private ImageLoadingProgressListener progressListener(
            final ProgressBar progressBar) {
        return new ImageLoadingProgressListener() {

            @Override
            public void onProgressUpdate(String imageUri, View view,
                                         int current, int total) {
                progressBar.setProgress(Math.round(100.0f * current / total));
            }
        };
    }

    ;

}
