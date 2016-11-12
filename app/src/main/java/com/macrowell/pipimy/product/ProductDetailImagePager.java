package com.macrowell.pipimy.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import tw.com.pipimy.app.android.R;

/**
 * Created by ukyo on 15/1/21.
 */
public class ProductDetailImagePager extends PagerAdapter {


    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;

    ProductDetailImagePager(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.imageUrls = imageUrls;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }


    @Override
    public int getCount() {
        return imageUrls.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageLayout = layoutInflater.inflate(R.layout.image_pager, container, false);
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final ProgressBar loadingProgress = (ProgressBar) imageLayout.findViewById(R.id.progress_loading_image);

        ImageLoader.getInstance().displayImage(imageUrls.get(position), imageView, options, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
//                super.onLoadingStarted(imageUri, view);
                loadingProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                super.onLoadingFailed(imageUri, view, failReason);
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
                loadingProgress.setVisibility(View.GONE);
            }

        });

        container.addView(imageLayout);
        return imageLayout;


    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
