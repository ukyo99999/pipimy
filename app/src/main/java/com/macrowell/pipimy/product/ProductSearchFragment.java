package com.macrowell.pipimy.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.macrowell.pipimy.Constant;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tw.com.pipimy.app.android.R;

public class ProductSearchFragment extends Fragment {

    private ProductSearchAdapter productSearchAdapter;
    private ListView listView;
    private EditText edittextSearchInput;
    private String searchRecAll;
    private String[] textSearchRecodeItems;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchRecAll = SharedPreferenceUtility.getSearchRec(getActivity());
        textSearchRecodeItems = getNonDuplicateElementRecode(searchRecAll
                .split(","));

        productSearchAdapter = new ProductSearchAdapter(getActivity(),
                textSearchRecodeItems);

    }

    @Override
    public void onResume() {
        super.onResume();
        searchRecAll = SharedPreferenceUtility.getSearchRec(getActivity());
        textSearchRecodeItems = getNonDuplicateElementRecode(searchRecAll
                .split(","));
        productSearchAdapter = new ProductSearchAdapter(getActivity(),
                textSearchRecodeItems);
        productSearchAdapter.notifyDataSetChanged();

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_search, null);

		/* find views */
        edittextSearchInput = (EditText) view.findViewById(R.id.edittext_search_input);
        menuBrowse = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_browse);
        menu2 = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu2);
        menuSell = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_sell);
        menuInbox = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_inbox);
        menuMe = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_me);
        listView = (ListView) view.findViewById(R.id.list_content);

        edittextSearchInput
                .setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {

                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                            if (TextUtils.isEmpty(edittextSearchInput.getText()
                                    .toString()) == false) {
                                // 送出關鍵字查詢
                                doSearch(edittextSearchInput.getText()
                                        .toString());
                            } else {
                                Toast.makeText(getActivity(), "Oops! 你沒有輸入搜尋關鍵字喔",
                                        Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        }
                        return false;
                    }
                });

		/* set listview */
        listView.setAdapter(productSearchAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // 取得歷史搜尋字串
                String searchHistory = textSearchRecodeItems[position];

                // 送出api
                doSearch(searchHistory);

            }
        });

		/*set listener*/
        menuBrowse.setOnClickListener(listener);
        menuSell.setOnClickListener(listener);
        menuMe.setOnClickListener(listener);
        menu2.setOnClickListener(listener);
        menuInbox.setOnClickListener(listener);

        return view;
    }

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

    /**
     * 送出關鍵字搜尋api
     */
    private void doSearch(String keyword) {

        // 寫入到搜尋紀錄
        addSearchRecode();

        // 清除文字輸入框文字
        edittextSearchInput.setText("");

		/* 利用map的對照來產生JSONObject */
        Map<String, String> params = new HashMap<String, String>();
        params.put("Keyword", String.valueOf(keyword));
        params.put("ts", CommonUtility.getTimeStamp());
        JSONObject jsonObjectTest = new JSONObject(params);

		/* 傳送api */
        try {
            hashValue = AESUtility.encrypt(Constant.INIT_VECTOR, Constant.KEY,
                    String.valueOf(jsonObjectTest));
            authValue = SharedPreferenceUtility.getAuth(getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 跳轉到產品同分類清單頁面(搜尋結果列表)
        Intent intent = new Intent(getActivity(), ProductListActivity.class);

        intent.putExtra("authValue", authValue);
        intent.putExtra("hashValue", hashValue);

        startActivity(intent);
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

                case R.id.layout_bottom_menu_me:
                    // 跳轉到[我]首頁
                    Intent meIntent = new Intent(getActivity(),
                            MeActivity.class);

                    startActivity(meIntent);
                    break;

                case R.id.layout_bottom_menu_browse:
                    // 跳轉到瀏覽頁面
                    Intent browseIntent = new Intent(getActivity(),
                            ProductCategoryActivity.class);

                    startActivity(browseIntent);
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

    /**
     * 新增搜尋紀錄。 存入搜尋字串，先把全部的抓出來並且要加上逗號分隔
     */
    private void addSearchRecode() {
        // 如果輸入的不為空值才加入歷史清單
        if (!TextUtils.isEmpty(edittextSearchInput.getText().toString())) {
            SharedPreferenceUtility.setSearchRec(getActivity(), searchRecAll
                    + edittextSearchInput.getText().toString() + ",");
        }
    }

    /**
     * 去除相同的歷史查詢紀錄
     *
     * @return
     */
    private String[] getNonDuplicateElementRecode(String[] recodeList) {

        // 利用 Set 的特性，將所有項目放入 Set 中即可移除重複的項目
        Set<String> stringSet = new HashSet<String>();
        for (String element : recodeList) {
            stringSet.add(element);
        }

        // stringSet.size() 為不重複項目的個數
        String[] nonDuplicateElementString = new String[stringSet.size()];

        // 將 Set 中的項目取出放到 nonDuplicateArray 中
        // 這裡也可以利用 iterator 來達成
        Object[] tempArray = stringSet.toArray();
        for (int i = 0; i < tempArray.length; i++) {
            nonDuplicateElementString[i] = (String) tempArray[i];
        }

        return nonDuplicateElementString;
    }

}
