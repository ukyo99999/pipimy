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
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.ProductListBean;
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

public class ProductListFragment extends ImageCacheFragment implements
        LoaderCallbacks<List<ProductListBean>> {

    private ProductListAdapter mProductListAdapter;
    private GridView mGridView;
    private TextView textNoMsg; //該分類沒有產品時顯示資訊
    private TextView textTitle; //分類標題
    private ImageView btnBack;
    private RelativeLayout menuBrowse;
    private RelativeLayout menu2;
    private RelativeLayout menuSell;
    private RelativeLayout menuInbox;
    private RelativeLayout menuMe;
    private Bundle mBundle; // 來自商品分類頁的bundle
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    private List<ProductListBean> list;
    private String categoryName; //分類名稱

    private final int TAKE_PHOTO = 99;
    private final int SELECT_PHOTO = 100;
    private final int PHOTO_CUT = 101;
    private Uri uriOringin; // 拍照或是選擇相簿圖片後的檔案uri
    private Uri uriCrop; // 裁切後的檔案uri

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageFetcherWrapper imageFetcherWrapper = this
                .createImageFetcherWrapper();


        mBundle = this.getArguments();
        categoryName = mBundle.getString("categoryName");
        mProductListAdapter = new ProductListAdapter(getActivity(), imageFetcherWrapper);
        getLoaderManager().initLoader(0, null, this);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list, null);

		/*find views*/
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        textNoMsg = (TextView) view.findViewById(R.id.text_no_msg);
        textTitle = (TextView) view.findViewById(R.id.text_title);
        menuBrowse = (RelativeLayout) view
                .findViewById(R.id.layout_bottom_menu_browse);
        menu2 = (RelativeLayout) view
                .findViewById(R.id.layout_bottom_menu2);
        menuSell = (RelativeLayout) view
                .findViewById(R.id.layout_bottom_menu_sell);
        menuInbox = (RelativeLayout) view
                .findViewById(R.id.layout_bottom_menu_inbox);
        menuMe = (RelativeLayout) view
                .findViewById(R.id.layout_bottom_menu_me);
        mGridView = (GridView) view.findViewById(R.id.gird_content);

		/*set data*/
        textTitle.setText(categoryName);
        mGridView.setNumColumns(2);
        mGridView.setGravity(Gravity.CENTER_HORIZONTAL);
        mGridView.setAdapter(mProductListAdapter);

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // 取得Itemno
                int itemNo = list.get(position).getItemno();

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("Itemno", itemNo);
                params.put("Ts", CommonUtility.getTimeStamp());
                JSONObject jsonObjectTest = new JSONObject(params);

				/* 傳送api */
                try {
                    hashValue = AESUtility.encrypt(Constant.INIT_VECTOR,
                            Constant.KEY, String.valueOf(jsonObjectTest));
                    authValue = SharedPreferenceUtility.getAuth(getActivity());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 跳轉到商品詳細頁
                Intent intent = new Intent(getActivity(),
                        ProductDetailActivity.class);

                intent.putExtra("authValue", authValue);
                intent.putExtra("hashValue", hashValue);

                startActivity(intent);

            }
        });

		/*set listener*/
        btnBack.setOnClickListener(listener);
        menuBrowse.setOnClickListener(listener);
        menuSell.setOnClickListener(listener);
        menuMe.setOnClickListener(listener);
        menu2.setOnClickListener(listener);
        menuInbox.setOnClickListener(listener);

        return view;
    }

    @Override
    public Loader<List<ProductListBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new ProductListLoader(getActivity(), mBundle);
    }

    @Override
    public void onLoadFinished(Loader<List<ProductListBean>> arg0,
                               List<ProductListBean> arg1) {
        mProductListAdapter.setData(arg1);
        list = arg1; // 存入公用的List

        if (list.size() == 0) {
            textNoMsg.setText("很抱歉，此分類目前沒有商品");
            textNoMsg.setVisibility(View.VISIBLE);
        } else {
            textNoMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ProductListBean>> arg0) {
        mProductListAdapter.setData(null);
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
                case R.id.layout_bottom_menu_inbox:
                    // 跳轉到收件匣頁面
                    Intent inboxIntent = new Intent(getActivity(),
                            InboxActivity.class);

                    startActivity(inboxIntent);

                    break;

                case R.id.layout_bottom_menu_browse:
                    getActivity().finish();
                    break;

                case R.id.layout_bottom_menu_me:
                    // 跳轉到[我]首頁
                    Intent meIntent = new Intent(getActivity(),
                            MeActivity.class);

                    startActivity(meIntent);
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
                                            MediaStore.ACTION_IMAGE_CAPTURE);

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

                case R.id.btn_back:
                    getActivity().finish();
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
                    File imageOutput = new File(imagesFolder, "image_001_" + CommonUtility.getCurrentTime() + "_crop.jpg");
                    uriCrop = Uri.fromFile(imageOutput);
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
