package com.macrowell.pipimy.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.CategoriesBean;
import com.macrowell.pipimy.gcm.DemoActivity;
import com.macrowell.pipimy.imagecache.ImageCacheFragment;
import com.macrowell.pipimy.imagecache.ImageFetcherWrapper;
import com.macrowell.pipimy.me.MeActivity;
import com.macrowell.pipimy.message.InboxActivity;
import com.macrowell.pipimy.sell.AddProductActivity;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class ProductCategoryFragment extends ImageCacheFragment implements
        LoaderCallbacks<List<CategoriesBean>> {

    private ProductCategoryAdapter mProductCategoryAdapter;
    private GridView gridView;
    private ImageView btnSearch;
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

    private List<CategoriesBean> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageFetcherWrapper imageFetcherWrapper = this
                .createImageFetcherWrapper();

        mProductCategoryAdapter = new ProductCategoryAdapter(getActivity(), imageFetcherWrapper);
        getLoaderManager().initLoader(0, null, this);

        // 刪除暫時拍的照片資料夾
        CommonUtility.delUploadImagesFolder();

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_category, null);

		/*find views*/
        menuBrowse = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_browse);
        menu2 = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu2);
        menuSell = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_sell);
        menuInbox = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_inbox);
        menuMe = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_me);
        btnSearch = (ImageView) view.findViewById(R.id.btn_search);
        gridView = (GridView) view.findViewById(R.id.gird_content);

		/*set data*/
        gridView.setNumColumns(2);
        gridView.setVerticalSpacing(20);
        gridView.setHorizontalSpacing(20);
        gridView.setGravity(Gravity.CENTER_HORIZONTAL);
        gridView.setAdapter(mProductCategoryAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // {"CategoryId":1,"ts":1222233341}
                // 準備這樣的字串 AES加密後 就是 hash參數的值

                int categoryId = position; // (position要減1, position0為喜愛商品-追蹤清單)
                JSONObject jsonObjectApiInputHash = null;

                if (position != 0) { // 如果不是選到喜愛商品項目

					/* 利用map的對照來產生JSONObject */
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("CategoryId", String.valueOf(categoryId));
                    params.put("ts", CommonUtility.getTimeStamp());
                    jsonObjectApiInputHash = new JSONObject(params);

                } else { // 如果是按到喜好商品項目
                    /* 利用map的對照來產生JSONObject */
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ts", CommonUtility.getTimeStamp());
                    jsonObjectApiInputHash = new JSONObject(params);
                }

				/* api 參數加密 */
                try {
                    hashValue = AESUtility.encrypt(Constant.INIT_VECTOR,
                            Constant.KEY,
                            String.valueOf(jsonObjectApiInputHash));
                    authValue = SharedPreferenceUtility.getAuth(getActivity());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 跳轉到產品同分類清單頁面
                Intent intent = new Intent(getActivity(),
                        ProductListActivity.class);

                intent.putExtra("authValue", authValue);
                intent.putExtra("hashValue", hashValue);
                intent.putExtra("categoryName", list.get(position).getCategoryName());

                if (position == 0) {
                    intent.putExtra("isLikeValue", true);
                } else {
                    intent.putExtra("isLikeValue", false);
                }

                startActivity(intent);

            }
        });

		/*set listener*/
        btnSearch.setOnClickListener(listener);
        menuSell.setOnClickListener(listener);
        menuMe.setOnClickListener(listener);
        menu2.setOnClickListener(listener);
        menuInbox.setOnClickListener(listener);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 刪除暫時拍的照片資料夾
        CommonUtility.delUploadImagesFolder();
    }

    @Override
    public Loader<List<CategoriesBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new ProductCategoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<CategoriesBean>> arg0,
                               List<CategoriesBean> arg1) {
        mProductCategoryAdapter.setData(arg1);
        list = arg1; // 存入公用的List
    }

    @Override
    public void onLoaderReset(Loader<List<CategoriesBean>> arg0) {
        mProductCategoryAdapter.setData(null);
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

                    // 跳轉到gcm test頁面
                    Intent gcmIntent = new Intent(getActivity(),
                            DemoActivity.class);

                    startActivity(gcmIntent);
//                    getActivity().finish();


                    break;
                case R.id.layout_bottom_menu_inbox:

                    // 跳轉到收件匣頁面
                    Intent inboxIntent = new Intent(getActivity(), InboxActivity.class);

                    startActivity(inboxIntent);
//                    getActivity().finish();
                    break;

                case R.id.btn_search:

                    // 跳轉到產品搜尋頁面
                    Intent searchIntent = new Intent(getActivity(),
                            ProductSearchActivity.class);

                    startActivity(searchIntent);
                    break;
                case R.id.layout_bottom_menu_me:
                    // 跳轉到[我]首頁
                    Intent meIntent = new Intent(getActivity(),
                            MeActivity.class);

                    startActivity(meIntent);
//                    getActivity().finish();
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
}
