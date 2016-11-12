package com.macrowell.pipimy.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.macrowell.pipimy.bean.CategoriesBean;
import com.macrowell.pipimy.imagecache.ImageFetcherWrapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import tw.com.pipimy.app.android.R;

public class ProductCategoryAdapter extends ArrayAdapter<CategoriesBean> {
    private LayoutInflater mLayoutInflater;
    private DisplayImageOptions options;

    public ProductCategoryAdapter(Context context, ImageFetcherWrapper imageFetcherWrapper) {
        super(context, 0);
        this.mLayoutInflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
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
        TextView textViewCategoryName;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_category, null);
        }

		/* findViews */
        imageViewProductCategory = (ImageView) convertView.findViewById(R.id.img_category);
        textViewCategoryName = (TextView) convertView.findViewById(R.id.text_category_name);

		/* 從Bean取出資料填入ListView items資訊 */
        CategoriesBean itemData = getItem(position);
        textViewCategoryName.setText(itemData.getCategoryName());

        ImageLoader.getInstance().displayImage(itemData.getPicUrl(), imageViewProductCategory, options);

        return convertView;
    }
}
