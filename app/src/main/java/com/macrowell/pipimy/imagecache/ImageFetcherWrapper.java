package com.macrowell.pipimy.imagecache;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

public class ImageFetcherWrapper {
    private ImageFetcher imageFetcher;
    private Drawable loadingBackground;
    private Drawable emptyBackground;

    public ImageFetcherWrapper(ImageFetcher imageFetcher,
        Drawable loadingBackground, int color) {
        this.imageFetcher = imageFetcher;
        this.loadingBackground = loadingBackground;
        this.emptyBackground = new ColorDrawable(color);
    }

    public ImageFetcherWrapper(ImageFetcher imageFetcher,
        Drawable loadingBackground, Drawable emptyBackground) {
        this.imageFetcher = imageFetcher;
        this.loadingBackground = loadingBackground;
        this.emptyBackground = emptyBackground;
    }

    public void loadImage(String key, ImageView imageView) {
        this.loadImage(key, imageView, true, true);
    }

    @SuppressWarnings("deprecation")
    public void loadImage(String key, ImageView imageView, boolean setLoading,
        boolean setEmpty) {
        if (TextUtils.isEmpty(key)) {
            imageView.setImageBitmap(null);
            if (setEmpty && this.emptyBackground != null) {
                imageView.setBackgroundDrawable(this.emptyBackground);
            }
        } else {
            this.imageFetcher.loadImage(key, imageView);
            if (setLoading && this.loadingBackground != null) {
                imageView.setBackgroundDrawable(this.loadingBackground);
            }
        }
    }
}
