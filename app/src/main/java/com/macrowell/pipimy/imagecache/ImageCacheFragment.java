package com.macrowell.pipimy.imagecache;



import tw.com.pipimy.app.android.R;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ImageCacheFragment extends Fragment {
    private ImageFetcherHelper imageFetcherHelper;
    private ImageFetcher imageFetcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.imageFetcherHelper = new ImageFetcherHelper(this.getActivity());
        this.imageFetcher = this.imageFetcherHelper.createImageFetcher();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.imageFetcherHelper.handleOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.imageFetcherHelper.handleOnPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.imageFetcherHelper.handleOnDestroy();
    }

    public void handleScrollStateOnFling(boolean onFling) {
        this.imageFetcherHelper.handleScrollStateOnFling(onFling);
    }

    public ImageFetcherWrapper createImageFetcherWrapper() {
        return new ImageFetcherWrapper(this.imageFetcher, this.getResources()
            .getDrawable(R.drawable.bg_loading), this.getResources().getColor(
            R.color.general_bg_gray));
    }

    public ImageFetcherWrapper createImageFetcherWrapper(
        Drawable loadingBackground, int color) {
        return new ImageFetcherWrapper(this.imageFetcher, loadingBackground,
            color);
    }

    public ImageFetcherWrapper createImageFetcherWrapper(
        Drawable loadingBackground, Drawable emptyBackground) {
        return new ImageFetcherWrapper(this.imageFetcher, loadingBackground,
            emptyBackground);
    }
}
