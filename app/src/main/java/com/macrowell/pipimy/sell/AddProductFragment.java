package com.macrowell.pipimy.sell;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class AddProductFragment extends Fragment {

    private AQuery mAq;
    private ImageView imgUploadPicture1;
    private ImageView imgUploadPicture2;
    private ImageView imgUploadPicture3;
    private ImageView imgUploadPicture4;
    private ImageView imgDelPicture2;
    private ImageView imgDelPicture3;
    private ImageView imgDelPicture4;
    private TextView textSellTetailItem1Hint;
    private TextView textSellTetailItem2Hint;
    private TextView textSellTetailItem3Hint;
    private TextView textSellTetailItem4Hint;
    private TextView textSellTetailItem5Hint;
    private ImageView btnCommit;
    private ImageView btnCancel;

    private RelativeLayout layoutDetail1;
    private RelativeLayout layoutDetail2;
    private RelativeLayout layoutDetail3;
    private RelativeLayout layoutDetail4;
    private RelativeLayout layoutDetail5;
    private RelativeLayout layoutDetail6;

    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值

    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json
    private String apiUrl = Constant.API_URL + "SetItem.ashx"; //

    private Uri uriOringin; // 拍照後的檔案uri
    private Uri uriCrop; // 裁切後的檔案uri
    private final int TAKE_PHOTO = 99; // 照相
    private final int SELECT_PHOTO = 100; // 從相簿選擇照片
    private final int PHOTO_CUT = 101; // 裁切圖片
    private final int SELECT_CATEGORY = 200; // 選擇分類
    private final int SELECT_ITEM_NAME = 300; // 選擇項目名稱
    private final int SELECT_ITEM_PRICE = 400; // 選擇項目名稱
    private String setItemName = ""; // 設定商品物品名稱
    private String setItemDescription = ""; // 設定物品敘述
    private int setCategoryId = 1; // 設定主分類編號
    private int setSubCategoryId = 0; // 設定副分類編號
    private int setOriginalPrice = 0; // 設定商品定價
    private String setImgName1 = ""; // 設定圖檔名(yyyyMMddHHmmss+亂碼+副檔名)
    private String setImgName2 = ""; // 設定圖檔名(yyyyMMddHHmmss+亂碼+副檔名)
    private String setImgName3 = ""; // 設定圖檔名(yyyyMMddHHmmss+亂碼+副檔名)
    private String setImgName4 = "";// 設定圖檔名(yyyyMMddHHmmss+亂碼+副檔名)
    private ProgressDialog loadingDialog;
    private Bundle mBundle;

    private String imageFilePath1 = ""; // 照片1的URI
    private String imageFilePath2 = ""; // 照片2的URI
    private String imageFilePath3 = ""; // 照片3的URI
    private String imageFilePath4 = ""; // 照片4的URI
    private String base64String1; // 照片1轉成base64字串
    private String base64String2; // 照片2轉成base64字串
    private String base64String3; // 照片3轉成base64字串
    private String base64String4; // 照片4轉成base64字串
    private int currentSelectUploadPicture = 1; //選到哪一張照片位置準備上傳(預設一開始都沒有選就是1)


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAq = new AQuery(getActivity());

        mBundle = this.getArguments();

        imageFilePath1 = mBundle.getString("photoUri").substring(6);
        base64String1 = Base64.encodeToString(
                CommonUtility.imageTransToByte(imageFilePath1), Base64.DEFAULT);

    }

    @SuppressLint({"InflateParams", "NewApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_product, null);

		/* findViews */
        imgUploadPicture1 = (ImageView) view.findViewById(R.id.img_upload_pic1);
        imgUploadPicture2 = (ImageView) view.findViewById(R.id.img_upload_pic2);
        imgUploadPicture3 = (ImageView) view.findViewById(R.id.img_upload_pic3);
        imgUploadPicture4 = (ImageView) view.findViewById(R.id.img_upload_pic4);
        imgDelPicture2 = (ImageView) view.findViewById(R.id.img_del_pic2);
        imgDelPicture3 = (ImageView) view.findViewById(R.id.img_del_pic3);
        imgDelPicture4 = (ImageView) view.findViewById(R.id.img_del_pic4);
        textSellTetailItem1Hint = (TextView) view.findViewById(R.id.text_detail_item1_hint);
        textSellTetailItem2Hint = (TextView) view.findViewById(R.id.text_detail_item2_hint);
        textSellTetailItem3Hint = (TextView) view.findViewById(R.id.text_detail_item3_hint);
        textSellTetailItem4Hint = (TextView) view.findViewById(R.id.text_detail_item4_hint);
        textSellTetailItem5Hint = (TextView) view.findViewById(R.id.text_detail_item5_hint);
        btnCommit = (ImageView) view.findViewById(R.id.btn_commit);
        btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);
        layoutDetail1 = (RelativeLayout) view.findViewById(R.id.layout_detail_1);
        layoutDetail2 = (RelativeLayout) view.findViewById(R.id.layout_detail_2);
        layoutDetail3 = (RelativeLayout) view.findViewById(R.id.layout_detail_3);
        layoutDetail4 = (RelativeLayout) view.findViewById(R.id.layout_detail_4);
        layoutDetail5 = (RelativeLayout) view.findViewById(R.id.layout_detail_5);
        layoutDetail6 = (RelativeLayout) view.findViewById(R.id.layout_detail_6);

		/* set listener */
        btnCancel.setOnClickListener(listener); // 按下取消
        btnCommit.setOnClickListener(listener); // 按下送出
        imgUploadPicture1.setOnClickListener(listener); // 上傳第一張照片
        imgUploadPicture2.setOnClickListener(listener);// 上傳第二張照片
        imgUploadPicture3.setOnClickListener(listener);// 上傳第三張照片
        imgUploadPicture4.setOnClickListener(listener);// 上傳第四張照片
        imgDelPicture2.setOnClickListener(listener);// 刪除第二張照片
        imgDelPicture3.setOnClickListener(listener);// 刪除第三張照片
        imgDelPicture4.setOnClickListener(listener);// 刪除第四張照片

        layoutDetail1.setOnClickListener(listener); // 按下分類
        layoutDetail2.setOnClickListener(listener); // 設定商品名稱
        layoutDetail3.setOnClickListener(listener);
        layoutDetail4.setOnClickListener(listener);
        layoutDetail5.setOnClickListener(listener); // 按下商品售價
        layoutDetail6.setOnClickListener(listener);

        // 如果有從上個頁面來的選擇照片路徑就貼上小縮圖
        if (mBundle != null) {

            patsePreview(mBundle.getString("photoUri").substring(6),
                    imgUploadPicture1);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    File imageSelected = new File(CommonUtility.getImageUri(
                            getActivity(), data));
                    uriOringin = Uri.fromFile(imageSelected);


                    startActivityForResult(cropPicture(), PHOTO_CUT);
                }

                break;

            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    startActivityForResult(cropPicture(), PHOTO_CUT);
                }
                break;

            case PHOTO_CUT:
                if (resultCode == Activity.RESULT_OK) {

                    switch (currentSelectUploadPicture) {
                        case 1:
                            imageFilePath1 = CommonUtility.uriToString(uriCrop).substring(6);
                            base64String1 = Base64.encodeToString(
                                    CommonUtility.imageTransToByte(imageFilePath1), Base64.DEFAULT);

                            //貼上預覽小縮圖
                            patsePreview(imageFilePath1, imgUploadPicture1);
                            break;

                        case 2:
                            imageFilePath2 = CommonUtility.uriToString(uriCrop).substring(6);
                            base64String2 = Base64.encodeToString(
                                    CommonUtility.imageTransToByte(imageFilePath2), Base64.DEFAULT);

                            //貼上預覽小縮圖
                            patsePreview(imageFilePath2, imgUploadPicture2);
                            imgUploadPicture2.setPadding(0, 10, 10, 0);
                            showDelPictureIcon(2);
                            break;

                        case 3:
                            imageFilePath3 = CommonUtility.uriToString(uriCrop).substring(6);
                            base64String3 = Base64.encodeToString(
                                    CommonUtility.imageTransToByte(imageFilePath3), Base64.DEFAULT);

                            //貼上預覽小縮圖
                            patsePreview(imageFilePath3, imgUploadPicture3);
                            imgUploadPicture3.setPadding(0, 10, 10, 0);
                            showDelPictureIcon(3);
                            break;

                        case 4:
                            imageFilePath4 = CommonUtility.uriToString(uriCrop).substring(6);
                            base64String4 = Base64.encodeToString(
                                    CommonUtility.imageTransToByte(imageFilePath4), Base64.DEFAULT);

                            //貼上預覽小縮圖
                            patsePreview(imageFilePath4, imgUploadPicture4);
                            imgUploadPicture4.setPadding(0, 10, 10, 0);
                            showDelPictureIcon(4);
                            break;
                    }


                }
                break;

            case SELECT_CATEGORY:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        textSellTetailItem1Hint.setText(data.getExtras().getString(
                                "CategoryName"));
                        setCategoryId = data.getExtras().getInt("CategoryId");
                    }
                }
                break;

            case SELECT_ITEM_NAME:
                if (data != null) {
                    textSellTetailItem2Hint.setText(data.getExtras().getString(
                            "ProductName"));

                    setItemName = data.getExtras().getString("ProductName");
                    setItemDescription = data.getExtras().getString(
                            "ProductDescription");
                }

                break;

            case SELECT_ITEM_PRICE:

                if (data != null) {
                    textSellTetailItem5Hint.setText(data.getExtras().getString(
                            "Price"));
                    setOriginalPrice = Integer.valueOf(data.getExtras().getString(
                            "Price"));
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 新增商品並加上progress bar
     *
     * @author Chris
     */
    private class AddProductTask extends AsyncTask<Integer, Integer, String> {
        // 後面尖括號內分別是參數（例子裡是Thread休息時間），進度(publishProgress用到)，return值類型

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /* 跳出ProgressDialog */
            loadingDialog = new ProgressDialog(getActivity());
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 設定ProgressDialog的style
            loadingDialog.setMax(100); // 設定最大單位
            loadingDialog.setProgress(0); // 設定初始值
            loadingDialog.setCanceledOnTouchOutside(false); // 設定不會被摸到就取消progress畫面
            loadingDialog.setMessage("資料上傳中 請稍候...");
            loadingDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {

			/* 建立api要用的hash JSONObject */
            Map<String, Object> jsonParams = new HashMap<String, Object>();
            jsonParams.put("Itemno", 0);
            jsonParams.put("ItemName", setItemName);
            jsonParams.put("ItemDescription", setItemDescription);
            jsonParams.put("CategoryId", setCategoryId);
            jsonParams.put("SubCategoryId", 1);
            jsonParams.put("OriginalPrice", setOriginalPrice);
            jsonParams.put("ImgName1", CommonUtility.getCurrentTime() + ".jpg");
            jsonParams.put("ImgName2", CommonUtility.getCurrentTime() + ".jpg");
            jsonParams.put("ImgName3", CommonUtility.getCurrentTime() + ".jpg");
            jsonParams.put("ImgName4", CommonUtility.getCurrentTime() + ".jpg");
            jsonParams.put("ts", CommonUtility.getTimeStamp());
            JSONObject hashJsonObject = new JSONObject(jsonParams);

			/* 加密hash與auth */
            try {
                hashValue = AESUtility.encrypt(Constant.INIT_VECTOR,
                        Constant.KEY, String.valueOf(hashJsonObject));
                authValue = SharedPreferenceUtility.getAuth(getActivity());

                // 結合api參數
                Map<String, String> apiParams = new HashMap<String, String>();
                apiParams.put("auth", authValue);
                apiParams.put("hash", hashValue);
                apiParams.put("Pic1", base64String1);
                apiParams.put("Pic2", base64String2);
                apiParams.put("Pic3", base64String3);
                apiParams.put("Pic4", base64String4);

                mEncrytJsonString = HttpUtility.post(apiUrl, apiParams);

				/* 解密 */
                try {
                    mDecryptJsonString = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                            mEncrytJsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // 刪除暫時拍的照片資料夾
            CommonUtility.delUploadImagesFolder();

            loadingDialog.dismiss();

            getActivity().finish();

        }

    }

    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_commit:

                    // 加上限制內容(不能沒填資料就上傳)
                    if (!setItemName.equals("") && setCategoryId != 0) {

                        // 上傳商品資料
                        AddProductTask addProductTask = new AddProductTask();
                        addProductTask.execute();
                    } else {
                        ViewUtility.showToast(getActivity(), "Oops! 你沒有填入必要的商品資訊喔");
                    }
                    break;

                case R.id.btn_cancel:
                    // 關閉頁面
                    getActivity().finish();
                    break;

                case R.id.img_upload_pic1:

                    currentSelectUploadPicture = 1;
                    addPictureSelector();

                    break;

                case R.id.img_upload_pic2:

                    currentSelectUploadPicture = 2;
                    addPictureSelector();
                    break;

                case R.id.img_upload_pic3:

                    currentSelectUploadPicture = 3;
                    addPictureSelector();
                    break;
                case R.id.img_upload_pic4:

                    currentSelectUploadPicture = 4;
                    addPictureSelector();
                    break;

                case R.id.img_del_pic2: //刪除照片2

                    delPicture(2);
                    break;

                case R.id.img_del_pic3: //刪除照片3

                    delPicture(3);
                    break;

                case R.id.img_del_pic4: //刪除照片4

                    delPicture(4);
                    break;

                case R.id.layout_detail_1:
                    // 跳轉到設定分類清單頁面
                    Intent setCategoryIntent = new Intent();
                    setCategoryIntent.setClass(getActivity(),
                            SetCategoryActivity.class);

                    startActivityForResult(setCategoryIntent, SELECT_CATEGORY);
                    break;
                case R.id.layout_detail_2:
                    // 跳轉到設定商品名稱頁面
                    Intent setProductNameIntent = new Intent(getActivity(),
                            SetProductNameActivity.class);

                    startActivityForResult(setProductNameIntent, SELECT_ITEM_NAME);
                    break;
                case R.id.layout_detail_3:
                    ViewUtility.showToast(getActivity(), "尚未開放 敬請期待");
                    break;
                case R.id.layout_detail_4:
                    ViewUtility.showToast(getActivity(), "尚未開放 敬請期待");
                    break;
                case R.id.layout_detail_5:
                    // 跳轉到設定商品售價頁面
                    Intent setPriceIntent = new Intent(getActivity(),
                            SetProductPriceActivity.class);

                    startActivityForResult(setPriceIntent, SELECT_ITEM_PRICE);
                    break;
                case R.id.layout_detail_6:
                    ViewUtility.showToast(getActivity(), "尚未開放 敬請期待");
                    break;
            }

        }
    };

    /**
     * 貼上預覽小縮圖
     *
     * @param filePath
     */
    private void patsePreview(String filePath, ImageView imageView) {
        File file = new File(filePath);
        mAq.id(imageView).image(file, 200);
    }

    /**
     * 選擇新增照片方式
     */
    private void addPictureSelector() {
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
                        File image = new File(imagesFolder, "image_" + currentSelectUploadPicture + "_" + CommonUtility.getCurrentTime() + ".jpg");
                        uriOringin = Uri.fromFile(image);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriOringin);

                        startActivityForResult(cameraIntent, TAKE_PHOTO);

                        break;
                    case 1:
                        // 選取手機中的相片
                        Intent photoPickerIntent = new Intent(
                                Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");

                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                        break;
                }

            }
        };

        // 產生對話框
        ViewUtility.simpleListDialog(getActivity(), "你想要如何取得你的照片?",
                items, listener);
    }


    /**
     * 裁切照片
     */
    private Intent cropPicture() {

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
        File imageOutput = new File(imagesFolder, "image_" + currentSelectUploadPicture + "_" + CommonUtility.getCurrentTime() + "_crop.jpg");
        uriCrop = Uri.fromFile(imageOutput);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);

        return cropIntent;
    }

    /**
     * 顯示已上傳的照片刪除icon
     *
     * @param pictureNumber
     */
    private void showDelPictureIcon(int pictureNumber) {
        switch (pictureNumber) {

            case 2:

                imgDelPicture2.setVisibility(View.VISIBLE);
                break;

            case 3:

                imgDelPicture3.setVisibility(View.VISIBLE);
                break;

            case 4:

                imgDelPicture4.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 刪除已上傳照片
     */
    private void delPicture(int currentSelectDelPicture) {

        switch (currentSelectDelPicture) {

            case 2:
                imageFilePath2 = "";
                base64String2 = "";

                //貼回原本的上傳照片預設圖
                imgUploadPicture2.setImageDrawable(getResources().getDrawable(R.drawable.img_photo_add));
                imgDelPicture2.setVisibility(View.GONE);
                break;

            case 3:
                imageFilePath3 = "";
                base64String3 = "";

                //貼回原本的上傳照片預設圖
                imgUploadPicture3.setImageDrawable(getResources().getDrawable(R.drawable.img_photo_add));
                imgDelPicture3.setVisibility(View.GONE);
                break;

            case 4:
                imageFilePath4 = "";
                base64String4 = "";

                //貼回原本的上傳照片預設圖
                imgUploadPicture4.setImageDrawable(getResources().getDrawable(R.drawable.img_photo_add));
                imgDelPicture4.setVisibility(View.GONE);
                break;
        }

    }


}
