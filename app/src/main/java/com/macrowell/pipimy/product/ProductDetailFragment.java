package com.macrowell.pipimy.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.ProductDetailBean;
import com.macrowell.pipimy.buy.OwnerPrivateMsgListActivity;
import com.macrowell.pipimy.buy.TalkActivity;
import com.macrowell.pipimy.imagecache.ImageCacheFragment;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.JsonUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class ProductDetailFragment extends ImageCacheFragment implements
        LoaderCallbacks<List<ProductDetailBean>> {

    private Bundle mBundle; // 來自商品分類清單頁的bundle
    private ImageView btnBack;
    private ImageView btnReport;
    private ImageView imgAvater;
    private TextView textSellerName;
    private ImageView imgProduct;
    private ViewPager imgPager;
    private TextView textProductName;
    private TextView textPrice;
    private TextView textGetLikeNumber;
    private Button btnPm;
    private ImageButton btnLike;
    private TextView textBidNumber; //有多少出價數(賣家畫面)
    private ProgressBar progressLoadingImage;

    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    private int itemNo; // 項目編號(用來按讚的參數)
    private int likeCount;//有多少的按讚數
    private int bitCount; //有多少的出價數
    private boolean isOwner = false; //是否點進來這個頁面的是賣家

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String mAuthValue = ""; // api value
    private String mHashValue = ""; // api value
    private ProductDetailBean productData;
    private DisplayImageOptions productImageOptions;
    private DisplayImageOptions avatarImageOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = this.getArguments();

        productImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        avatarImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.img_no_avatar)
                .showImageOnFail(R.drawable.img_no_avatar).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(getActivity()));

        getLoaderManager().initLoader(0, null, this);
        // SharedPreferenceUtility.getUid(getActivity());
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail, null);

		/* findViews */
        imgAvater = (ImageView) view.findViewById(R.id.img_avatar);
        textSellerName = (TextView) view.findViewById(R.id.text_seller_name);
        imgProduct = (ImageView) view.findViewById(R.id.img_product);
        imgPager = (ViewPager) view.findViewById(R.id.img_pager);
        textProductName = (TextView) view.findViewById(R.id.text_product_name);
        textPrice = (TextView) view.findViewById(R.id.text_price);
        textGetLikeNumber = (TextView) view.findViewById(R.id.text_like_number);
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        btnReport = (ImageView) view.findViewById(R.id.btn_report);
        btnPm = (Button) view.findViewById(R.id.btn_pm);
        btnLike = (ImageButton) view.findViewById(R.id.btn_like);
        textBidNumber = (TextView) view.findViewById(R.id.text_bid_number);
        progressLoadingImage = (ProgressBar) view
                .findViewById(R.id.progress_loading_image);

		/*set Views*/


		/* set listener */
        btnBack.setOnClickListener(listener);
        btnPm.setOnClickListener(listener);
        btnLike.setOnClickListener(listener);
        btnReport.setOnClickListener(listener);

        return view;
    }

    @Override
    public Loader<List<ProductDetailBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new ProductDetailLoader(getActivity(), mBundle);
    }

    @Override
    public void onLoadFinished(Loader<List<ProductDetailBean>> arg0,
                               List<ProductDetailBean> arg1) {

        // 取得Bean資料
        productData = arg1.get(0);

        ArrayList<String> imagesList = new ArrayList<String>(); //放置圖片url的字串陣列
        imagesList.add(Constant.SERVER_URL + productData.getItemPicUrl_1());
        imagesList.add(Constant.SERVER_URL + productData.getItemPicUrl_2());
        imagesList.add(Constant.SERVER_URL + productData.getItemPicUrl_3());
        imagesList.add(Constant.SERVER_URL + productData.getItemPicUrl_4());

        //刪除空的
        CommonUtility.delArrayListElement(imagesList, Constant.SERVER_URL);


        // 填入UI資訊
        textSellerName.setText(productData.getOwnerName());
        imgPager.setAdapter(new ProductDetailImagePager(getActivity(), imagesList));

        if (imagesList.size() > 1) {
            //Bind the title indicator to the adapter
            CirclePageIndicator titleIndicator = (CirclePageIndicator) getActivity().findViewById(R.id.indicator);
            titleIndicator.setViewPager(imgPager);
        }


        ImageLoader.getInstance().displayImage(
                Constant.SERVER_URL + productData.getOwnerPicUrl(),
                imgAvater, avatarImageOptions,
                avatarLoadingListener(progressLoadingImage));

        textProductName.setText(productData.getItemName());
        textPrice.setText("NT $" + productData.getOriginalPrice());
        textGetLikeNumber.setText(String.valueOf(productData.getTrackingCount()) + "個讚");


        this.itemNo = productData.getItemno();
        likeCount = productData.getTrackingCount();

        //判斷買賣家畫面
        if (String.valueOf(productData.getOwnerUid()).equals(
                SharedPreferenceUtility.getUid(getActivity()))) {
            isOwner = true;
            btnPm.setText("查看私訊與出價");
            textBidNumber.setText("你有 " + String.valueOf(productData.getBidCount()) + " 個出價");
            btnPm.setVisibility(View.VISIBLE);
            textBidNumber.setVisibility(View.VISIBLE);
        } else {
            isOwner = false;
            btnPm.setText("私訊購買");
            btnPm.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<ProductDetailBean>> arg0) {

    }

    /**
     * 監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_back: // 按下返回
                    getActivity().finish();
                    break;

                case R.id.btn_report: // 按下舉報
                    ViewUtility.showToast(getActivity(), "檢舉商品(尚未開放，敬請期待)");
                    break;

                case R.id.btn_pm: // 私訊購買

                    if (!isOwner) { //我是買家

                        // 跳轉到私訊購買頁面
                        Intent intent = new Intent(getActivity(), TalkActivity.class);

                        // 送出夾帶參數
                        intent.putExtra("productName", productData.getItemName());
                        intent.putExtra("productPrice", productData.getOriginalPrice());
                        intent.putExtra("productImageUrl", productData.getItemPicUrl_1());
                        intent.putExtra("auth", SharedPreferenceUtility.getAuth(getActivity()));
                        intent.putExtra("msgBoardId", productData.getPrivateMsgBoardId());
                        intent.putExtra("productItemNo", itemNo);
                        intent.putExtra("isOwner", false); //送出不是賣家參數
                        intent.putExtra("ownerUid", productData.getOwnerUid());
                        intent.putExtra("ownerName", productData.getOwnerName());
                        intent.putExtra("talkerName", productData.getOwnerName());

                        startActivity(intent);
                    } else { //我是賣家

                        // 跳轉到私訊列表頁面
                        Intent intent = new Intent(getActivity(),
                                OwnerPrivateMsgListActivity.class);
                        intent.putExtra("auth", SharedPreferenceUtility.getAuth(getActivity()));
                        intent.putExtra("productItemNo", itemNo);
                        intent.putExtra("productName", productData.getItemName());
                        intent.putExtra("productPrice", productData.getOriginalPrice());
                        intent.putExtra("productImageUrl", productData.getItemPicUrl_1());
                        intent.putExtra("auth", SharedPreferenceUtility.getAuth(getActivity()));
                        intent.putExtra("msgBoardId", productData.getPrivateMsgBoardId());
                        intent.putExtra("productItemNo", itemNo);


                        startActivity(intent);
                    }

                    break;

                case R.id.btn_like: // 按讚
                    new DoLikeTask().execute();
                    break;
            }

        }
    };

    private class DoLikeTask extends AsyncTask<Void, Void, Void> {
        String apiUrl = Constant.API_URL + "TrackingItem.ashx";
        String encrytJsonString = ""; // 從Server抓取到的加密Json
        String decryptJsonString = "";// 解密後的Json

        protected Void doInBackground(Void... item) {

			/* 利用map的對照來產生JSONObject */
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("Itemno", itemNo);
            params.put("ts", CommonUtility.getTimeStamp());
            JSONObject jsonObjectTest = new JSONObject(params);

			/* 傳送api */
            try {
                hashValue = AESUtility.encrypt(Constant.INIT_VECTOR,
                        Constant.KEY, String.valueOf(jsonObjectTest));
                authValue = SharedPreferenceUtility.getAuth(getActivity());

            } catch (Exception e) {
                e.printStackTrace();
            }

			/* 從Server取得JSON資料 */
            Map<String, String> paramsToServer = new HashMap<String, String>();
            paramsToServer.put(KEY_AUTH, authValue);
            paramsToServer.put(KEY_HASH, hashValue);

            try {
                encrytJsonString = HttpUtility.post(apiUrl, paramsToServer);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

			/* 解密 */
            try {
                decryptJsonString = AESUtility.decrypt(Constant.INIT_VECTOR,
                        Constant.KEY, encrytJsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /* Parse JSON */
            try {
                JSONObject jsonObj = new JSONObject(decryptJsonString);
                int IsTracked = JsonUtility.getInt(jsonObj, "IsTracked");
                int RtnCode = JsonUtility.getInt(jsonObj, "RtnCode ");
                String RtnMsg = JsonUtility.getString(jsonObj, "RtnMsg");

                //刷新按讚的結果
                if (IsTracked == 1) {
                    textGetLikeNumber.setText(String.valueOf(likeCount + 1) + "個讚");
                } else {
                    textGetLikeNumber.setText(String.valueOf(likeCount) + "個讚");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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


}
