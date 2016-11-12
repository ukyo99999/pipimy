package com.macrowell.pipimy.me;

import java.util.List;

import tw.com.pipimy.app.android.R;
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
import com.macrowell.pipimy.bean.MeBean;
import com.macrowell.pipimy.utility.LogUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MeAdapter extends ArrayAdapter<MeBean> {
	private LogUtility log = LogUtility.getInstance(MeAdapter.class);
	
	private LayoutInflater mLayoutInflater;
	private DisplayImageOptions productImageOptions;
	
	public MeAdapter(Context context) {
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

	@SuppressLint("NewApi")
	public void setData(List<MeBean> data, Boolean isPostData) {
		clear();
		if (data != null) {
			if (isPostData) {
				addAll(data);
			}
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageViewProduct = null; //商品圖片
		TextView textViewProductName;  //商品名稱
		TextView textViewPrice; //商品售價
		TextView textViewLikeCount; //喜愛次數
		int isTracked; //目前登入的使用者 0:未按過讚 1:按過讚
//		TextView textViewCommentCount;
		ProgressBar progressLoadingImage;

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_item_me_post,null);
		}
		
		/* findViews */
		imageViewProduct = (ImageView)convertView.findViewById(R.id.img_product);
		textViewProductName = (TextView) convertView.findViewById(R.id.text_product_name);
		textViewPrice = (TextView) convertView.findViewById(R.id.text_product_price);
		textViewLikeCount= (TextView) convertView.findViewById(R.id.text_like_count);
		progressLoadingImage = (ProgressBar) convertView
				.findViewById(R.id.progress_loading_image);

		/* 從Bean取出資料填入ListView items資訊 */
		MeBean itemData = getItem(position);
		
			textViewProductName.setText(itemData.getItemName());
			textViewProductName.setEllipsize(TruncateAt.END);
			textViewPrice.setText("NT $"+String.valueOf(itemData.getOriginalPrice()));
			textViewLikeCount.setText(String.valueOf(itemData.getTrackingCount()));
			
			isTracked = itemData.getIsTracked();
			
			if(isTracked ==0){
				textViewLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_favorite_normal, 0, 0, 0);
			}else{
				textViewLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_favorite_pressed, 0, 0, 0);
			}
			
			ImageLoader.getInstance().displayImage(
					Constant.SERVER_URL+itemData.getItemPicUrl_1(),
					imageViewProduct, productImageOptions,
					loadingListener(progressLoadingImage));
		
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
			}
		};
	}

}
