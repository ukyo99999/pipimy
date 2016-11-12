package com.macrowell.pipimy.me;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.HomeActivity;
import com.macrowell.pipimy.bean.MeBean;
import com.macrowell.pipimy.imagecache.ImageCacheFragment;
import com.macrowell.pipimy.me.edit.MeEditActivity;
import com.macrowell.pipimy.me.setting.MeSettingActivity;
import com.macrowell.pipimy.message.InboxActivity;
import com.macrowell.pipimy.product.ProductCategoryActivity;
import com.macrowell.pipimy.product.ProductDetailActivity;
import com.macrowell.pipimy.sell.AddProductActivity;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class MeFragment extends ImageCacheFragment implements
        LoaderCallbacks<List<MeBean>> {

    private LogUtility log = LogUtility.getInstance(MeFragment.class);

    private TextView textUserId; //用戶ID(顯示於actionbar中間)
    private ImageView btnPublish; //宣傳
    private ImageView imgAvatar; //使用者頭像
    private TextView textUserNickName; //使用者暱稱
    private TextView textUserLocation; //使用者位置
    private TextView textDealCount; //成交次數
    private TextView textUnpaidCount; //棄標次數
    private TextView textEdit; //編輯
    private TextView textFansCount; //粉絲人數
    private TextView textTracedCount; //追踨中的賣家人數
    private TextView textOrder; //我的訂單
    private TextView textSetting; //設定
    private TextView textLogout; //登出
    private TextView textJoinData; //加入日期
    private TextView textUrl; //個人網址
    private TextView textAddProductCount;

    private MeAdapter mMeAdapter;
    private GridView gridView;
    private ProgressBar progressLoadingImage;

    private RelativeLayout menuBrowse;
    private RelativeLayout menu2;
    private RelativeLayout menuSell;
    private RelativeLayout menuInbox;
    private RelativeLayout menuMe;

    // private String KEY_AUTH = "auth"; // api key:auth
    // private String KEY_HASH = "hash"; // api key:hash
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值

    private final int TAKE_PHOTO = 99;
    private final int SELECT_PHOTO = 100;
    private final int PHOTO_CUT = 101;
    private Uri uriOringin; // 拍照後的檔案uri
    private Uri uriCrop; // 裁切後的檔案uri

    List<MeBean> list; //用來存放Loader讀取完之後的list
    private DisplayImageOptions avatarImageOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        mMeAdapter = new MeAdapter(getActivity());
        avatarImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.img_no_avatar)
                .showImageOnFail(R.drawable.img_no_avatar)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(getActivity()));


        getLoaderManager().initLoader(0, null, this);

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me, null);

		/*find views*/
        menuBrowse = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_browse);
        menu2 = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu2);
        menuSell = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_sell);
        menuInbox = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_inbox);
        menuMe = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_me);
        btnPublish = (ImageView) view.findViewById(R.id.btn_publish);

        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        textUserNickName = (TextView) view.findViewById(R.id.text_nick_name);
        textUserLocation = (TextView) view.findViewById(R.id.text_location);
        textDealCount = (TextView) view.findViewById(R.id.text_deal_count_number);
        textUnpaidCount = (TextView) view.findViewById(R.id.text_unpaid_count_number);
        textEdit = (TextView) view.findViewById(R.id.text_edit);
        textFansCount = (TextView) view.findViewById(R.id.text_fans_number);
        textTracedCount = (TextView) view.findViewById(R.id.text_traced_number);
        textOrder = (TextView) view.findViewById(R.id.text_order);
        textSetting = (TextView) view.findViewById(R.id.text_settings);
        textLogout = (TextView) view.findViewById(R.id.text_logout);
        textJoinData = (TextView) view.findViewById(R.id.text_join_data);
        textUrl = (TextView) view.findViewById(R.id.text_url);
        textAddProductCount = (TextView) view.findViewById(R.id.text_add_product_count);
        progressLoadingImage = (ProgressBar) view.findViewById(R.id.progress_loading_image);

        gridView = (GridView) view.findViewById(R.id.gird_content);
        gridView.setNumColumns(2);
        gridView.setGravity(Gravity.CENTER_HORIZONTAL);
        gridView.setHorizontalSpacing(10);
        gridView.setVerticalSpacing(10);
        gridView.setAdapter(mMeAdapter);


		/*set listener*/
        btnPublish.setOnClickListener(listener);
        menuSell.setOnClickListener(listener);
        menuBrowse.setOnClickListener(listener);
        imgAvatar.setOnClickListener(listener);
        textUserNickName.setOnClickListener(listener);
        textUserLocation.setOnClickListener(listener);
        textDealCount.setOnClickListener(listener);
        textUnpaidCount.setOnClickListener(listener);
        textEdit.setOnClickListener(listener);
        textFansCount.setOnClickListener(listener);
        textTracedCount.setOnClickListener(listener);
        textOrder.setOnClickListener(listener);
        textSetting.setOnClickListener(listener);
        textLogout.setOnClickListener(listener);
        textUrl.setOnClickListener(listener);
        menu2.setOnClickListener(listener);
        menuInbox.setOnClickListener(listener);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                JSONObject jsonObjectApiInputHash = null;

				/* 利用map的對照來產生JSONObject */
                Map<String, String> params = new HashMap<String, String>();
                params.put("Itemno", String.valueOf(list.get(position).getItemno()));
                params.put("ts", CommonUtility.getTimeStamp());
                jsonObjectApiInputHash = new JSONObject(params);

				/* api 參數加密 */
                try {
                    hashValue = AESUtility.encrypt(Constant.INIT_VECTOR,
                            Constant.KEY,
                            String.valueOf(jsonObjectApiInputHash));
                    authValue = SharedPreferenceUtility.getAuth(getActivity());

                } catch (Exception e) {
                    e.printStackTrace();
                }

				/*跳轉到商品詳細頁*/
                Intent intent = new Intent(getActivity(),
                        ProductDetailActivity.class);

                intent.putExtra("authValue", authValue);
                intent.putExtra("hashValue", hashValue);

                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public Loader<List<MeBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new MeLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MeBean>> arg0, List<MeBean> arg1) {

        list = arg1;
        Boolean isPostData = null; //判斷用戶有沒有刊登資料

		/*set view data*/
        if (list.size() > 1) {
            textAddProductCount.setText(String.valueOf(list.size()) + "個刊登項目");
            isPostData = true;
        } else {
            if (String.valueOf(list.get(0).getItemName()).equals("null")) { // 沒有刊登商品
                textAddProductCount.setText("0個刊登項目");
                isPostData = false;
            } else {
                textAddProductCount.setText(String.valueOf(list.size())
                        + "個刊登項目");
                isPostData = true;
            }

        }

        mMeAdapter.setData(arg1, isPostData);

        textUserNickName.setText(list.get(0).getNickName());
        textUserLocation.setText(list.get(0).getMyCity());

        ImageLoader.getInstance().displayImage(
                Constant.SERVER_URL + list.get(0).getMyPicUrl(),
                imgAvatar, avatarImageOptions,
                avatarLoadingListener(progressLoadingImage));
        textDealCount.setText(String.valueOf(list.get(0).getTradedCount()));
        textUnpaidCount.setText(String.valueOf(list.get(0).getUnpaidCount()));
        textFansCount.setText(String.valueOf(list.get(0).getFans()));
        textTracedCount.setText(String.valueOf(list.get(0).getTracking()));
        textJoinData.setText("在" + String.valueOf(list.get(0).getCrtime()) + "加入");
        textUrl.setText(list.get(0).getWebURL());
    }

    @Override
    public void onLoaderReset(Loader<List<MeBean>> arg0) {
        mMeAdapter.setData(null, false);

    }

    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_bottom_menu2:
                    ViewUtility.showToast(getActivity(), "購物車(尚未開放，敬請期待)");

                    break;

                case R.id.layout_bottom_menu_browse:
                    // 跳轉到瀏覽頁面
                    Intent browseIntent = new Intent(getActivity(),
                            ProductCategoryActivity.class);

                    startActivity(browseIntent);
                    break;

                case R.id.layout_bottom_menu_inbox:
                    // 跳轉到收件匣頁面
                    Intent inboxIntent = new Intent(getActivity(),
                            InboxActivity.class);

                    startActivity(inboxIntent);

                    break;

                case R.id.btn_publish:
                    // 跳轉到宣傳頁面
                    // Intent searchIntent = new Intent(getActivity(),
                    // ProductSearchActivity.class);

                    // startActivity(searchIntent);
                    ViewUtility.showToast(getActivity(), "宣傳(尚未開放，敬請期待)");
                    break;

                case R.id.text_edit:
                    // 跳轉到編輯檔案頁面
                    Intent intent = new Intent(getActivity(), MeEditActivity.class);

                    //存入到SharedPreference
                    SharedPreferenceUtility.setNickName(getActivity(), list.get(0).getNickName());
                    SharedPreferenceUtility.setMyCity(getActivity(), list.get(0).getMyCity());
                    SharedPreferenceUtility.setWebURL(getActivity(), list.get(0).getWebURL());
                    SharedPreferenceUtility.setIntroduction(getActivity(), list.get(0).getIntroduction());
                    SharedPreferenceUtility.setMyPicUrl(getActivity(), list.get(0).getMyPicUrl());
                    SharedPreferenceUtility.setMyPicName(getActivity(), list.get(0).getMyPicName());
                    SharedPreferenceUtility.setUsername(getActivity(), list.get(0).getUsername());
                    SharedPreferenceUtility.setMobile(getActivity(), list.get(0).getMobile());
                    SharedPreferenceUtility.setSex(getActivity(), String.valueOf(list.get(0).getSex()));
                    SharedPreferenceUtility.setBirthday(getActivity(), list.get(0).getBirthday());
                    SharedPreferenceUtility.setEmail(getActivity(), list.get(0).getEmail());
                    SharedPreferenceUtility.setCity(getActivity(), list.get(0).getCity());
                    SharedPreferenceUtility.setTown(getActivity(), list.get(0).getTown());
                    SharedPreferenceUtility.setAddress(getActivity(), list.get(0).getAddress());

                    startActivity(intent);

                    break;
                case R.id.text_order:
                    ViewUtility.showToast(getActivity(), "我的訂單(尚未開放，敬請期待)");
                    break;
                case R.id.text_settings:
//                    ViewUtility.showToast(getActivity(), "設定(尚未開放，敬請期待)");

                    // 跳轉到設定頁面
                    Intent settingIntent = new Intent(getActivity(),
                            MeSettingActivity.class);

                    startActivity(settingIntent);

                    break;

                case R.id.text_logout:
                    //清除Token
                    SharedPreferenceUtility.setAuth(getActivity(), "");

                    // 跳轉到首頁
                    Intent intentHome = new Intent(getActivity(),
                            HomeActivity.class);

//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//                        fm.popBackStack();
//                    }
//
//                    FragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentHome);

                    // 清除目前頁面
                    getActivity().finish();
                    break;

                case R.id.layout_bottom_menu_sell:
                    // 產生對話框的選項文字
                    String[] items = new String[]{"使用相機拍照", "從圖庫中選擇一張舊有的照片"};

                    // 產生對話框的選項監聽
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int itemId) {

                            switch (itemId) {
                                case 0:
                                    // 使用Intent呼叫其他服務幫忙拍照
                                    Intent cameraIntent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                                    // 建立拍照上傳的照片放置資料夾
                                    File imagesFolder = new File(
                                            Environment.getExternalStorageDirectory(),
                                            "PipimyImages");
                                    imagesFolder.mkdirs();

                                    // 設定照片路徑與檔名
                                    File image = new File(imagesFolder, "image_001_" + CommonUtility.getCurrentTime() + ".jpg");
                                    uriOringin = Uri.fromFile(image);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            uriOringin);

                                    startActivityForResult(cameraIntent, TAKE_PHOTO);

                                    break;
                                case 1:
                                    // 選取手機中的相片
                                    Intent photoPickerIntent = new Intent(
                                            Intent.ACTION_PICK);
                                    photoPickerIntent.setType("image/*");
                                    startActivityForResult(photoPickerIntent,
                                            SELECT_PHOTO);

                                    break;
                            }

                        }
                    };

                    // 產生對話框
                    ViewUtility.simpleListDialog(getActivity(), "你想要如何取得你的照片?",
                            items, listener);

                    break;
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = null;
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    File imageSelected = new File(CommonUtility.getImageUri(
                            getActivity(), data));
                    uriOringin = Uri.fromFile(imageSelected);

                    // 裁切
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(uriOringin, "image/*");
                    cropIntent.putExtra("crop", "true");

                    // 裁剪框的比例，正方型
                    cropIntent.putExtra("aspectX", 1);
                    cropIntent.putExtra("aspectY", 1);
                    cropIntent.putExtra("outputX", 640);// 回傳照片解析度X
                    cropIntent.putExtra("outputY", 640);// 回傳照片解析度Y
                    cropIntent.putExtra("outputFormat", "JPEG");// 圖片格式

                    // 建立拍照上傳的照片放置資料夾
                    File imagesFolder = new File(
                            Environment.getExternalStorageDirectory(),
                            "PipimyImages");
                    imagesFolder.mkdirs();

                    // 設定照片路徑與檔名
                    File imageOutput = new File(imagesFolder, "image_001_" + CommonUtility.getCurrentTime() + "_crop.jpg");
                    uriCrop = Uri.fromFile(imageOutput);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);

                    startActivityForResult(cropIntent, PHOTO_CUT);
                }

                break;

            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    // 裁切
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(uriOringin, "image/*");
                    cropIntent.putExtra("crop", "true");

                    // 裁剪框的比例，正方型
                    cropIntent.putExtra("aspectX", 1);
                    cropIntent.putExtra("aspectY", 1);
                    cropIntent.putExtra("outputX", 640);// 回傳照片解析度X
                    cropIntent.putExtra("outputY", 640);// 回傳照片解析度Y
                    cropIntent.putExtra("outputFormat", "JPEG");// 圖片格式

                    // 建立拍照上傳的照片放置資料夾
                    File imagesFolder = new File(
                            Environment.getExternalStorageDirectory(),
                            "PipimyImages");
                    imagesFolder.mkdirs();

                    // 設定照片路徑與檔名
                    File image = new File(imagesFolder, "image_001_" + CommonUtility.getCurrentTime() + "_crop.jpg");
                    uriCrop = Uri.fromFile(image);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);

                    startActivityForResult(cropIntent, PHOTO_CUT);

                }
                break;

            case PHOTO_CUT:
                if (resultCode == Activity.RESULT_OK) {

                    bundle = new Bundle();
                    bundle.putString("photoUri", CommonUtility.uriToString(uriCrop));

                    // 跳轉到新增商品清單頁面
                    Intent addProductIntent = new Intent(getActivity(),
                            AddProductActivity.class);

                    addProductIntent.putExtras(bundle);
                    startActivity(addProductIntent);

                }
                break;

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

}
