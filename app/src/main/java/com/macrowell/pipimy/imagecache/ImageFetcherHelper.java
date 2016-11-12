package com.macrowell.pipimy.imagecache;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentActivity;

public class ImageFetcherHelper {
    private FragmentActivity activity;
    private List<ImageFetcher> list;

    public ImageFetcherHelper(final FragmentActivity activity) {
        this.activity = activity;
        this.list = new ArrayList<ImageFetcher>();
    }

    public ImageFetcher createImageFetcher() {
        // The ImageFetcher takes care of loading remote images into our
        // ImageView
        ImageFetcher imageFetcher = new ImageFetcher(this.activity);
        imageFetcher.addImageCache(activity);
        this.list.add(imageFetcher);

        return imageFetcher;
    }

    public void handleOnResume() {
        for (ImageFetcher imageFetcher : list) {
            imageFetcher.setExitTasksEarly(false);
        }
    }

    public void handleOnPause() {
        for (ImageFetcher imageFetcher : list) {
            imageFetcher.setExitTasksEarly(true);
            imageFetcher.flushCache();
        }
    }

    public void handleOnDestroy() {
        for (ImageFetcher imageFetcher : list) {
            imageFetcher.closeCache();
        }
    }

    public void handleScrollStateOnFling(boolean onFling) {
        for (ImageFetcher imageFetcher : list) {
            if (onFling) {
                imageFetcher.setPauseWork(true);
            } else {
                imageFetcher.setPauseWork(false);
            }
        }
    }
}
