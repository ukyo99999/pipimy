package com.macrowell.pipimy.me.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.BottomPopupWindow;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.PickerView;
import com.macrowell.pipimy.utility.PickerView.onSelectListener;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class MeEditFragment extends Fragment implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    private LogUtility log = LogUtility.getInstance(MeEditFragment.class);

    //    private Bundle mBundle; // 來自個人資訊首頁的bundle
    private String nickName; // 暱稱(必填)
    private String myCity; // 我的城市(必填)
    private String webUrl; // 網站
    private String introduction; // 個人簡介
    private String photoUrl;
    private String photoName;
    private String userName;
    private String phone;
    private int sex; // 性別(男:1,女:0)
    private String birthday;
    private String email;
    private String city; // 地址1:城市(必填)
    private String town; // 地址2:鄉鎮市區
    private String address; // 地址3:街道地址

    private ProgressDialog loadingDialog;
    private ImageView btnCommit; // 完成
    private ImageView btnCancel; // 取消
    private EditText edittextNickName; // 使用者暱稱
    private TextView textMyCity; // 我的城市
    private EditText edittextWebSite; // 網站
    private EditText edittextIntro; // 個人簡介
    private ImageView imgAvatar; // 使用者頭像
    private EditText edittextName; // 真實姓名
    private EditText edittextPhone; // 手機
    private TextView textSex; // 性別
    private TextView textBirthday; // 生日
    private EditText edittextEmail; // email
    private Spinner spinnerAddress1; // 地址(縣市)
    private Spinner spinnerAddress2; // 地址(區鄉鎮)
    private EditText edittextAddress3; // 地址(街道路名)

    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    private String apiUrl = Constant.API_URL + "EditMyInfo.ashx"; //
    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json

    private final int TAKE_PHOTO = 99;
    private final int SELECT_PHOTO = 100;
    private final int PHOTO_CUT = 101;
    private Uri uriOringin; // 拍照後的檔案uri
    private Uri uriCrop; // 裁切後的檔案uri
    private String imageFilePath1 = ""; // 照片1的URI
    private String base64String1; // 照片1轉成base64字串

    private AQuery mAq;
    private int DERECTION_RIGHT = Gravity.RIGHT;
    private int DERECTION_LEFT = Gravity.LEFT;

    private DrawerLayout mDrawerLayout;
    private PopMenuCallBack mPopMenuCallBack;
    private Calendar calendar;
    private PickerView pickerBrithdayYear;
    private PickerView pickerBrithdayMonth;
    private PickerView pickerBrithdayDay;
    private int birthdayYear;
    private int birthdayMonth;
    private int birtthdayDay;

    private ArrayAdapter<String> cityList;
    private ArrayAdapter<String> areaList;
    private String[] cityData;
    private String[] areaData;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.nickName = SharedPreferenceUtility.getNickName(getActivity());
        this.myCity = SharedPreferenceUtility.getMyCity(getActivity());
        this.webUrl = SharedPreferenceUtility.getWebURL(getActivity());
        this.introduction = SharedPreferenceUtility.getIntroduction(getActivity());
        this.photoUrl = SharedPreferenceUtility.getMyPicUrl(getActivity());
        this.photoName = SharedPreferenceUtility.getMyPicName(getActivity());
        this.userName = SharedPreferenceUtility.getUsername(getActivity());
        this.phone = SharedPreferenceUtility.getMobile(getActivity());
        this.sex = Integer.valueOf(SharedPreferenceUtility.getSex(getActivity()));
        this.birthday = SharedPreferenceUtility.getBirthday(getActivity());
        this.email = SharedPreferenceUtility.getEmail(getActivity());
        this.city = SharedPreferenceUtility.getCity(getActivity());
        this.town = SharedPreferenceUtility.getTown(getActivity());
        this.address = SharedPreferenceUtility.getAddress(getActivity());

        mAq = new AQuery(getActivity());
        calendar = Calendar.getInstance();
        birthdayYear = calendar.get(Calendar.YEAR);
        birthdayMonth = calendar.get(Calendar.MONTH);
        birtthdayDay = calendar.get(Calendar.DAY_OF_MONTH);

    }


    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile, null);

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

		/* find views */
        btnCommit = (ImageView) view.findViewById(R.id.btn_commit);// 完成
        btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);// 取消
        edittextNickName = (EditText) view.findViewById(R.id.edittext_nickname_data);// 使用者暱稱
        textMyCity = (TextView) view.findViewById(R.id.text_city_data); // 我的城市
        edittextWebSite = (EditText) view.findViewById(R.id.edittext_website_data); // 網站
        edittextIntro = (EditText) view.findViewById(R.id.edittext_intro_data); // 個人簡介
        imgAvatar = (ImageView) view.findViewById(R.id.img_photo); // 使用者頭像
        edittextName = (EditText) view.findViewById(R.id.edittext_name_data); // 真實姓名
        edittextPhone = (EditText) view.findViewById(R.id.edittext_phone_data); // 手機
        textSex = (TextView) view.findViewById(R.id.text_sex_data); // 性別
        textBirthday = (TextView) view.findViewById(R.id.text_birthday_data); // 生日
        edittextEmail = (EditText) view.findViewById(R.id.edittext_email_data); // email
        spinnerAddress1 = (Spinner) view.findViewById(R.id.spinner_city);// 地址(縣市)
        spinnerAddress2 = (Spinner) view.findViewById(R.id.spinner_town);// 地址(區鄉鎮)
        edittextAddress3 = (EditText) view.findViewById(R.id.edittext_address); // 地址(街道路名)

		/* set data */
        mAq.id(imgAvatar).image(Constant.SERVER_URL + this.photoUrl, true, true, 0,
                R.drawable.img_no_avatar);
        edittextNickName.setText(this.nickName);
        textMyCity.setText(this.myCity);
        edittextWebSite.setText(this.webUrl);
        edittextIntro.setText(this.introduction);
        edittextName.setText(this.userName);
        edittextPhone.setText(this.phone);
        if (this.sex == 0) {
            textSex.setText("女性");
        } else {
            textSex.setText("男性");
        }
        textBirthday.setText(this.birthday);
        edittextEmail.setText(this.email);

		/*set city spinner data*/
        cityData = new String[CommonUtility.obtainZipcode(getActivity()).getCityList().size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getCityList().size(); i++) {
            cityData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getCityList().get(i));
        }
        cityList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                cityData);
        spinnerAddress1.setAdapter(cityList);
        spinnerAddress1.setSelection(cityPosition(this.city, cityData));

		/*set area spinner data*/
        areaData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(0).size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(0).size(); i++) {
            areaData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(0).get(i).getArea());
        }

        areaList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                areaData);
        spinnerAddress2.setAdapter(areaList);

        edittextAddress3.setText(address);

		/* set listener */
        btnCommit.setOnClickListener(listener); // 完成
        btnCancel.setOnClickListener(listener); // 取消
        edittextNickName.setOnClickListener(listener); // 使用者暱稱
        textMyCity.setOnClickListener(listener); // 我的城市
        edittextWebSite.setOnClickListener(listener); // 網站
        edittextIntro.setOnClickListener(listener); // 個人簡介
        imgAvatar.setOnClickListener(listener); // 使用者頭像
        edittextName.setOnClickListener(listener); // 真實姓名
        edittextPhone.setOnClickListener(listener); // 手機
        textSex.setOnClickListener(listener); // 性別
        textBirthday.setOnClickListener(listener); // 生日
        edittextEmail.setOnClickListener(listener); // email
        spinnerAddress1.setOnItemSelectedListener(spinnerAddress1Select); //地址(縣市)
        spinnerAddress2.setOnItemSelectedListener(spinnerAddress2Select);//地址(區鄉鎮)

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

                    imageFilePath1 = CommonUtility.uriToString(uriCrop).substring(6);
                    base64String1 = Base64.encodeToString(
                            CommonUtility.imageTransToByte(imageFilePath1), Base64.DEFAULT);

                    //貼上小縮圖
                    File file = new File(imageFilePath1);
                    mAq.id(imgAvatar).image(file, 250);

                    photoName = CommonUtility.getCurrentTime() + ".jpg";
                }
                break;

        }

    }

    /**
     * 縣市Spinner選項監聽
     */
    private OnItemSelectedListener spinnerAddress1Select = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            areaData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(position).size()];

            for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(position).size(); i++) {
                areaData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(position).get(i).getArea());
            }

            areaList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                    areaData);
            spinnerAddress2.setAdapter(areaList);
            spinnerAddress2.setSelection(areaPosition(town, areaData));

            city = spinnerAddress1.getSelectedItem().toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * 區域Spinner選項監聽
     */
    private OnItemSelectedListener spinnerAddress2Select = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            town = spinnerAddress2.getSelectedItem().toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_commit:

                    // 完成(送出api), 上傳商品資料
                    UpdatProfileProductTask updatProfileProductTask = new UpdatProfileProductTask();
                    updatProfileProductTask.execute();

                    break;

                case R.id.btn_cancel:
                    // 刪除暫時拍的照片資料夾
                    CommonUtility.delUploadImagesFolder();
                    // 關閉頁面
                    getActivity().finish();
                    break;

                case R.id.text_city_data:
                    // 我的城市
                    mDrawerLayout.openDrawer(Gravity.RIGHT); // 開啟側邊欄
                    // mNavigationDrawerFragment.open(DERECTION_RIGHT); //開啟側邊欄
                    break;

                case R.id.img_photo: // 使用者頭像
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

                                    // 刪除暫時拍的照片資料夾
                                    CommonUtility.delUploadImagesFolder();

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

                                    // 刪除暫時拍的照片資料夾
                                    CommonUtility.delUploadImagesFolder();

                                    break;
                            }

                        }
                    };

                    // 產生對話框
                    ViewUtility.simpleListDialog(getActivity(), "你想要如何取得你的照片?",
                            items, listener);

                    break;

                case R.id.text_sex_data:
                    // 性別
                    makeSexSelectPop();
                    break;

                case R.id.text_birthday_data:
                    // 生日
                    makeBirthdayPop();

                    break;

            }

        }
    };

    private void makeSexSelectPop() {
        BottomPopupWindow sexMenuWindow;
        PickerView pickerSex;
        TextView textFinish;
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.edit_sex, null);

        pickerSex = (PickerView) view.findViewById(R.id.picker_sex);
        textFinish = (TextView) view.findViewById(R.id.text_title);

        List<String> data = new ArrayList<String>();
        data.add("男性");
        data.add("女性");
        pickerSex.setData(data);

        // 實體化SelectPicPopupWindow
        sexMenuWindow = new BottomPopupWindow(getActivity(), view);
        mPopMenuCallBack = (PopMenuCallBack) sexMenuWindow;
        // 顯示視窗
        sexMenuWindow.showAtLocation(view.findViewById(R.id.root),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 設置layout在PopupWindow中顯示的位置

        pickerSex.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text, PickerView view) {

                if (text.equals("男性")) {
                    sex = 1;

                } else {
                    sex = 0;
                }
                textSex.setText(text);
            }

        });

        textFinish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mPopMenuCallBack.closeWindow();
            }
        });

    }

    private void makeBirthdayPop() {

        BottomPopupWindow birthdayMenuWindow;
        TextView text_finish;
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.edit_birthday, null);

        pickerBrithdayYear = (PickerView) view
                .findViewById(R.id.picker_birthday_year);
        pickerBrithdayMonth = (PickerView) view
                .findViewById(R.id.picker_birthday_month);
        pickerBrithdayDay = (PickerView) view
                .findViewById(R.id.picker_birthday_day);
        text_finish = (TextView) view.findViewById(R.id.text_title);

        List<String> yearData = new ArrayList<String>();
        List<String> monthData = new ArrayList<String>();
        List<String> dayData = new ArrayList<String>();

        // 設定年資料
        int curYear = calendar.get(Calendar.YEAR);

        for (int y = curYear - 50; y <= curYear; y++) {
            yearData.add(String.valueOf(y) + "年");
        }

        // 設定月資料
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[]{"1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"};

        for (int m = 0; m < months.length; m++) {
            monthData.add(months[m]);
        }

        // 填入到picker view
        pickerBrithdayYear.setData(yearData);
        pickerBrithdayMonth.setData(monthData);
        pickerBrithdayDay.setData(dayData);
        setDayData(curYear, curMonth, pickerBrithdayDay);

//        pickerBrithdayYear.setSelected(yearData.size() - 1);
//        pickerBrithdayMonth.setSelected(curMonth);
//        pickerBrithdayDay.setSelected(1);

        // 貼資料到生日的textview
        textBirthday.setText(curYear + "/" + curMonth + 1 + "/"
                + String.valueOf(birtthdayDay));

        // 實體化SelectPicPopupWindow
        birthdayMenuWindow = new BottomPopupWindow(getActivity(), view);
        mPopMenuCallBack = (PopMenuCallBack) birthdayMenuWindow;

        birthdayMenuWindow.showAtLocation(view.findViewById(R.id.root),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 設置layout在PopupWindow中顯示的位置

        pickerBrithdayYear.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text, PickerView view) {
                birthdayYear = Integer.valueOf(text.substring(0,
                        text.length() - 1));
                setDayData(birthdayYear, birthdayMonth, pickerBrithdayDay);

                textBirthday.setText(String.valueOf(birthdayYear) + "/"
                        + String.valueOf(birthdayMonth) + "/"
                        + String.valueOf(birtthdayDay));
            }

        });

        pickerBrithdayMonth.setOnSelectListener(new onSelectListener() {
            @Override
            public void onSelect(String text, PickerView view) {

                birthdayMonth = Integer.valueOf(text.substring(0,
                        text.length() - 1));
                setDayData(birthdayYear, birthdayMonth, pickerBrithdayDay);
                textBirthday.setText(String.valueOf(birthdayYear) + "/"
                        + String.valueOf(birthdayMonth) + "/"
                        + String.valueOf(birtthdayDay));
            }

        });

        pickerBrithdayDay.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text, PickerView view) {
                birtthdayDay = Integer.valueOf(text.substring(0,
                        text.length() - 1));

                textBirthday.setText(String.valueOf(birthdayYear) + "/"
                        + String.valueOf(birthdayMonth) + "/"
                        + String.valueOf(birtthdayDay));
            }
        });

        text_finish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mPopMenuCallBack.closeWindow();
            }
        });

    }

    public static interface PopMenuCallBack {
        public void closeWindow(); // 關閉視窗
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }


    /**
     * 取得每月最大的日數
     *
     * @param curYear
     * @param curMonth
     * @return
     */
    private int getMaxDays(int curYear, int curMonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, curYear);
        calendar.set(Calendar.MONTH, curMonth);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void setDayData(int year, int month, PickerView view) {
        // 設定日資料
        List<String> dayData = new ArrayList<String>();
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDays = getMaxDays(year, month - 1);

        for (int d = 1; d <= maxDays; d++) {
            dayData.add(String.valueOf(d) + "日");

        }
        view.setData(dayData);
    }


    /**
     * 取得city在spinner的位置
     *
     * @param cityName
     * @param list
     * @return
     */
    private int cityPosition(String cityName, String[] list) {
        int positon = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(cityName)) {
                positon = i;
            }
        }

        return positon;
    }

    /**
     * 取得area在spinner的位置
     *
     * @param areaName
     * @param list
     * @return
     */
    private int areaPosition(String areaName, String[] list) {
        int position = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(areaName)) {
                position = i;
            }
        }

        return position;
    }


    /**
     * 更新個人資料並加上progress bar
     *
     * @author Chris
     */
    private class UpdatProfileProductTask extends AsyncTask<Integer, Integer, String> {
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

            ViewUtility.hideSoftKeyboard(getActivity(), edittextAddress3);
        }

        @Override
        protected String doInBackground(Integer... params) {

			/* 建立api要用的hash JSONObject */
            Map<String, Object> jsonParams = new HashMap<String, Object>();
            jsonParams.put("Ts", CommonUtility.getTimeStamp());
            jsonParams.put("Username", edittextName.getText().toString());
            jsonParams.put("NickName", edittextNickName.getText().toString());
            jsonParams.put("MyCity", textMyCity.getText().toString());
            jsonParams.put("Mobile", edittextPhone.getText().toString());
            jsonParams.put("City", city);
            jsonParams.put("Town", town);
            jsonParams.put("Address", edittextAddress3.getText().toString());
            jsonParams.put("MyPicName", photoName);
            jsonParams.put("Email", edittextEmail.getText().toString());
            jsonParams.put("Sex", sex);
            jsonParams.put("Birthday", textBirthday.getText().toString());
            jsonParams.put("Introduction", edittextIntro.getText().toString());
            jsonParams.put("WebURL", edittextWebSite.getText().toString());

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
                apiParams.put("MyPic", base64String1);

                mEncrytJsonString = HttpUtility.post(apiUrl, apiParams);

				/* 解密 */
                try {
                    mDecryptJsonString = AESUtility.decrypt(
                            Constant.INIT_VECTOR, Constant.KEY,
                            mEncrytJsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //存入到SharedPreference
            SharedPreferenceUtility.setNickName(getActivity(), edittextNickName.getText().toString());
            SharedPreferenceUtility.setMyCity(getActivity(), textMyCity.getText().toString());
            SharedPreferenceUtility.setWebURL(getActivity(), edittextWebSite.getText().toString());
            SharedPreferenceUtility.setIntroduction(getActivity(), edittextIntro.getText().toString());
//            SharedPreferenceUtility.setMyPicUrl(getActivity(), list.get(0).getMyPicUrl());
            SharedPreferenceUtility.setMyPicName(getActivity(), photoName);
            SharedPreferenceUtility.setUsername(getActivity(), edittextName.getText().toString());
            SharedPreferenceUtility.setMobile(getActivity(), edittextPhone.getText().toString());
            SharedPreferenceUtility.setSex(getActivity(), String.valueOf(sex));
            SharedPreferenceUtility.setBirthday(getActivity(), textBirthday.getText().toString());
            SharedPreferenceUtility.setEmail(getActivity(), edittextEmail.getText().toString());
            SharedPreferenceUtility.setCity(getActivity(), city);
            SharedPreferenceUtility.setTown(getActivity(), town);
            SharedPreferenceUtility.setAddress(getActivity(), edittextAddress3.getText().toString());


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
}
