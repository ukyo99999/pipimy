package com.macrowell.pipimy.bill;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.BillBean;
import com.macrowell.pipimy.bean.ContactBean;
import com.macrowell.pipimy.imagecache.ImageCacheFragment;
import com.macrowell.pipimy.me.contact.MeContactListAdapter;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.JsonUtility;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class BillFragment extends ImageCacheFragment implements
        LoaderCallbacks<Map<String, List<BillBean>>> {

    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json
    private String loadContactApiUrl = Constant.API_URL + "MemberContactsS.ashx"; //讀取通訊錄api
    private String addContactApiUrl = Constant.API_URL + "MemberContactsI.ashx"; //新增通訊錄api


    private LogUtility log = LogUtility.getInstance(BillFragment.class);
    private ImageView btnBack;

    private BillAdapter billProductAdapter; //商品adapter
    private MeContactListAdapter mContactListAdapter; //通訊錄adapter
    private ListView contactList; //通訊錄list
    private LinearLayout layoutContact; //放通訊錄的Layout
    private ProgressDialog loadingDialog;
    private ProgressBar progressLoadingApi;
    private CheckBox checkBoxContactBackup;

    private TextView textProductNumbers; //共有幾項商品要結帳
    private ListView listProduct; //結帳商品的listview
    private TextView textProductPrice; //購物金額總計
    private Spinner spinnerShipment; //運費下拉選單
    private TextView textShippingFee; //運費
    private TextView textPayment; //應付金額
    private LinearLayout layoutMemberInfo; //購買人資料標頭
    private EditText editName; //購買人姓名
    private EditText editMobile;//購買人手機
    private Spinner spinnerCity; //購買人地址
    private Spinner spinnerTown; //購買人地址
    private EditText editAddress; //購買人地址
    private EditText editContactName; //收件人姓名
    private Button btnSelectContact; //從通訊錄選擇按鈕
    private EditText editContactMobile; //收件人手機
    private Spinner spinnerContactCity; //收件人地址
    private Spinner spinnerContactTown; //收件人地址
    private EditText editContactAddress; //收件人地址
    private EditText editNote; //備註
    private Button btnNext; //下一步按鈕

    private Bundle mFromTalkBundle; // 來自私訊對話的bundle
    Map<String, List<BillBean>> mapData;//用來存放Loader讀取完之後的map
    private DisplayImageOptions productImageOptions;

    private ArrayAdapter<String> shipFeeList; //運費adapter
    private String[] shipFeeData; //運費選項資料
    private String city; // 地址1:城市
    private String town; // 地址2:鄉鎮市區
    private String address; // 地址3:街道地址
    private String contactCity; // 地址1:城市
    private String contactTown; // 地址2:鄉鎮市區
    private String contactAddress; // 地址3:街道地址
    private ArrayAdapter<String> cityList;
    private ArrayAdapter<String> townList;
    private String[] cityData;
    private String[] townData;
    private ArrayAdapter<String> cityContactList;
    private ArrayAdapter<String> townContactList;
    private String[] cityContactData;
    private String[] townContactData;
    private boolean isBackupContact = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mFromTalkBundle = this.getArguments();

        billProductAdapter = new BillAdapter(getActivity());
        mContactListAdapter = new MeContactListAdapter(getActivity(), false);

        productImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.img_no_avatar)
                .showImageOnFail(R.drawable.img_no_avatar)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(getActivity()));


        getLoaderManager().initLoader(0, mFromTalkBundle, this);

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill, null);

		/*find views*/
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        progressLoadingApi = (ProgressBar) view.findViewById(R.id.progress_loading_api);
        checkBoxContactBackup = (CheckBox) view.findViewById(R.id.checkbox_backup);

        contactList = (ListView) view.findViewById(R.id.list_contact);
        textProductNumbers = (TextView) view.findViewById(R.id.text_product_numbers);
        listProduct = (ListView) view.findViewById(R.id.list_items);
        textProductPrice = (TextView) view.findViewById(R.id.text_product_price);
        spinnerShipment = (Spinner) view.findViewById(R.id.spinner_shipment);
        textShippingFee = (TextView) view.findViewById(R.id.text_shipping_fee);
        textPayment = (TextView) view.findViewById(R.id.text_payment);
        layoutMemberInfo = (LinearLayout) view.findViewById(R.id.layout_member_info_title);
        editName = (EditText) view.findViewById(R.id.edit_name);
        editMobile = (EditText) view.findViewById(R.id.edit_mobile);
        spinnerCity = (Spinner) view.findViewById(R.id.spinner_city);
        spinnerTown = (Spinner) view.findViewById(R.id.spinner_town);
        editAddress = (EditText) view.findViewById(R.id.edit_address);
        editContactName = (EditText) view.findViewById(R.id.edit_contact_name);
        btnSelectContact = (Button) view.findViewById(R.id.btn_select_contact);
        editContactMobile = (EditText) view.findViewById(R.id.edit_contact_mobile);
        spinnerContactCity = (Spinner) view.findViewById(R.id.spinner_contact_city);
        spinnerContactTown = (Spinner) view.findViewById(R.id.spinner_contact_town);
        editContactAddress = (EditText) view.findViewById(R.id.edit_contact_address);
        editNote = (EditText) view.findViewById(R.id.edit_note);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        listProduct.setAdapter(billProductAdapter);
        listProduct.setDividerHeight(0);

		/*set listener*/
        btnBack.setOnClickListener(listener);
        btnSelectContact.setOnClickListener(listener);
        spinnerCity.setOnItemSelectedListener(spinnerCitySelect);
        spinnerTown.setOnItemSelectedListener(spinnerTownSelect);
        spinnerContactCity.setOnItemSelectedListener(spinnerContactCitySelect);
        spinnerContactTown.setOnItemSelectedListener(spinnerContactTownSelect);
        spinnerShipment.setOnItemSelectedListener(spinnerShipFeeSelect);
        checkBoxContactBackup.setOnClickListener(listener);
        btnNext.setOnClickListener(listener);

        return view;
    }


    @Override
    public Loader<Map<String, List<BillBean>>> onCreateLoader(int arg0, Bundle bundle) {
        /* 跳出ProgressDialog */
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 設定ProgressDialog的style
        loadingDialog.setMax(100); // 設定最大單位
        loadingDialog.setProgress(0); // 設定初始值
        loadingDialog.setCanceledOnTouchOutside(false); // 設定不會被摸到就取消progress畫面
        loadingDialog.setMessage("訂單產生中 請稍候...");
        loadingDialog.show();

        return new BillLoader(getActivity(), bundle);
    }

    @Override
    public void onLoadFinished(Loader<Map<String, List<BillBean>>> loader, Map<String, List<BillBean>> data) {
        mapData = data;

        this.city = mapData.get("MemberInfo").get(0).getInfoCity();
        this.town = mapData.get("MemberInfo").get(0).getInfoTown();
        this.contactCity = mapData.get("MemberContacts").get(0).getContactCity();
        this.contactTown = mapData.get("MemberContacts").get(0).getContactTown();

        /*set view data*/
        textProductNumbers.setText("您共有" + String.valueOf(mapData.get("OrderItem").size()) + "樣商品");
        textProductPrice.setText("$" + String.valueOf(mapData.get("Others").get(0).getAmount()) + "元");

        editName.setText(mapData.get("MemberInfo").get(0).getInfoUsername());
        editMobile.setText(mapData.get("MemberInfo").get(0).getInfoMobile());
        editAddress.setText(mapData.get("MemberInfo").get(0).getInfoAddress());

        editContactName.setText(mapData.get("MemberContacts").get(0).getContactUsername());
        editContactMobile.setText(mapData.get("MemberContacts").get(0).getContactMobile());
        editContactAddress.setText(mapData.get("MemberContacts").get(0).getContactAddress());

        billProductAdapter.setData(mapData.get("OrderItem"));


        /*set shipment spinner data*/
        shipFeeData = new String[mapData.get("LogisticsType").size()];
        for (int i = 0; i < mapData.get("LogisticsType").size(); i++) {
            shipFeeData[i] = String.valueOf(mapData.get("LogisticsType").get(i).getLogisticsTypeName());
        }
        shipFeeList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                shipFeeData);
        spinnerShipment.setAdapter(shipFeeList);

        /*set city spinner data*/
        cityData = new String[CommonUtility.obtainZipcode(getActivity()).getCityList().size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getCityList().size(); i++) {
            cityData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getCityList().get(i));
        }
        cityList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                cityData);
        spinnerCity.setAdapter(cityList);
        spinnerCity.setSelection(cityPosition(this.city, cityData));

		/*set area spinner data*/
        townData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(0).size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(0).size(); i++) {
            townData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(0).get(i).getArea());
        }

        townList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                townData);
        spinnerTown.setAdapter(townList);

        /*set contact city spinner data*/
        cityContactData = new String[CommonUtility.obtainZipcode(getActivity()).getCityList().size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getCityList().size(); i++) {
            cityContactData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getCityList().get(i));
        }
        cityContactList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                cityContactData);
        spinnerContactCity.setAdapter(cityContactList);
        spinnerContactCity.setSelection(cityPosition(this.contactCity, cityContactData));

		/*set contact area spinner data*/
        townContactData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(0).size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(0).size(); i++) {
            townContactData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(0).get(i).getArea());
        }

        townContactList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                townContactData);
        spinnerContactTown.setAdapter(townContactList);

//        progressLoadingApi.setVisibility(View.GONE);

        loadingDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Map<String, List<BillBean>>> arg0) {

    }

    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    getActivity().finish();

                    break;

                case R.id.btn_select_contact:

                    // 產生通訊錄對話框
                    LoadContactTask loadContactTask = new LoadContactTask();
                    loadContactTask.execute();
                    break;

                case R.id.checkbox_backup:

                    if (checkBoxContactBackup.isChecked()) {
                        isBackupContact = true;
                    } else {
                        isBackupContact = false;
                    }
                    break;


                case R.id.btn_next:

                    //備份通訊錄
                    if (isBackupContact) {
                        AddContactTask addContactTask = new AddContactTask();
                        addContactTask.execute();
                    }

                    // 跳轉到付款流程頁面
                    Intent payIntent = new Intent(getActivity(),
                            PayActivity.class);

                    //製作夾帶參數
                    String orderNo = String.valueOf(mapData.get("Others").get(0).getOrderNo());
                    String itemNames = mapData.get("OrderItem").get(0).getItemName();
                    for (int i = 1; i < mapData.get("OrderItem").size(); i++) {
                        itemNames = itemNames + "," + mapData.get("OrderItem").get(i).getItemName();
                    }

                    String paymentMethod = "";
                    if (mapData.get("BillingType").size() > 1) {
                        paymentMethod = "ALL";
                    } else {
                        paymentMethod = mapData.get("BillingType").get(0).getBillingTypeCode();
                    }

                    String totalAmount = String.valueOf(mapData.get("Others").get(0).getAmount());
                    String buyerName = editName.getText().toString();
                    String buyerCellPhone = editMobile.getText().toString();
                    String buyerCity = spinnerCity.getSelectedItem().toString();
                    String buyerTown = spinnerTown.getSelectedItem().toString();
                    String buyerAddress = editAddress.getText().toString();
                    String receiverName = editContactName.getText().toString();
                    String receiverCellPhone = editContactMobile.getText().toString();
                    String receiverCity = spinnerContactCity.getSelectedItem().toString();
                    String receiverTown = spinnerContactTown.getSelectedItem().toString();
                    String receiverAddress = editContactAddress.getText().toString();
                    String remark = editNote.getText().toString();
                    String transportNo = String.valueOf(mapData.get("Others").get(0).getTransportNo());
                    String buyerUid = String.valueOf(mapData.get("Others").get(0).getBuyerUid());
                    String ownerUid = String.valueOf(mapData.get("Others").get(0).getOwnerUid());

                    // 送出夾帶參數
                    payIntent.putExtra("OrderNo", orderNo);
                    payIntent.putExtra("ItemName", itemNames);
                    payIntent.putExtra("PaymentMethod", paymentMethod);
                    payIntent.putExtra("TotalAmount", totalAmount);
                    payIntent.putExtra("BuyerName", buyerName);
                    payIntent.putExtra("BuyerCellPhone", buyerCellPhone);
                    payIntent.putExtra("BuyerCity", buyerCity);
                    payIntent.putExtra("BuyerTown", buyerTown);
                    payIntent.putExtra("BuyerAddress", buyerAddress);
                    payIntent.putExtra("ReceiverName", receiverName);
                    payIntent.putExtra("ReceiverCellPhone", receiverCellPhone);
                    payIntent.putExtra("ReceiverCity", receiverCity);
                    payIntent.putExtra("ReceiverTown", receiverTown);
                    payIntent.putExtra("ReceiverAddress", receiverAddress);
                    payIntent.putExtra("Remark", remark);
                    payIntent.putExtra("TransportNo", transportNo);
                    payIntent.putExtra("BuyerUid", buyerUid);
                    payIntent.putExtra("OwnerUid", ownerUid);

                    startActivity(payIntent);
                    break;
            }

        }
    };


    /**
     * 運費Spinner選項監聽
     */
    private AdapterView.OnItemSelectedListener spinnerShipFeeSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int shipment = mapData.get("LogisticsType").get(position).getLogisticsTypeCharge();
            int productPrice = mapData.get("Others").get(0).getAmount();

            textShippingFee.setText("$" + String.valueOf(shipment) + "元");
            textPayment.setText("$" + String.valueOf(shipment + productPrice) + "元");

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * 縣市Spinner選項監聽
     */
    private AdapterView.OnItemSelectedListener spinnerCitySelect = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            townData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(position).size()];

            for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(position).size(); i++) {
                townData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(position).get(i).getArea());
            }

            townList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                    townData);
            spinnerTown.setAdapter(townList);
            spinnerTown.setSelection(areaPosition(town, townData));

            city = spinnerCity.getSelectedItem().toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * 區域Spinner選項監聽
     */
    private AdapterView.OnItemSelectedListener spinnerTownSelect = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            town = spinnerTown.getSelectedItem().toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * Contact縣市Spinner選項監聽
     */
    private AdapterView.OnItemSelectedListener spinnerContactCitySelect = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            townContactData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(position).size()];

            for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(position).size(); i++) {
                townContactData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(position).get(i).getArea());
            }

            townContactList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                    townContactData);
            spinnerContactTown.setAdapter(townContactList);
            spinnerContactTown.setSelection(areaPosition(contactTown, townContactData));

            contactCity = spinnerContactCity.getSelectedItem().toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * Contact區域Spinner選項監聽
     */
    private AdapterView.OnItemSelectedListener spinnerContactTownSelect = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            contactTown = spinnerContactTown.getSelectedItem().toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
     * 讀取連絡人並加上progress bar
     *
     * @author Chris
     */
    private class LoadContactTask extends AsyncTask<Integer, Integer, String> {
        // 後面尖括號內分別是參數（例子裡是Thread休息時間），進度(publishProgress用到)，return值類型
        private ProgressDialog loadingDialog;
        private String encrytJsonString = ""; // 從Server抓取到的加密Json
        private String decryptJsonString = "";// 解密後的Json
        private String loadContactApiUrl = Constant.API_URL + "MemberContactsS.ashx"; //讀取通訊錄api
        private List<ContactBean> contactData = new ArrayList<ContactBean>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /* 跳出ProgressDialog */
            loadingDialog = new ProgressDialog(getActivity());
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 設定ProgressDialog的style
//            loadingDialog.setMax(100); // 設定最大單位
//            loadingDialog.setProgress(0); // 設定初始值
            loadingDialog.setCanceledOnTouchOutside(false); // 設定不會被摸到就取消progress畫面
            loadingDialog.setMessage("連絡人資料下載中 請稍候...");
            loadingDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {

			/* 建立api要用的hash JSONObject */
            Map<String, Object> jsonParams = new HashMap<String, Object>();
            jsonParams.put("Ts", CommonUtility.getTimeStamp());
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

                encrytJsonString = HttpUtility.post(loadContactApiUrl, apiParams);

				/* 解密 */
                try {
                    decryptJsonString = AESUtility.decrypt(
                            Constant.INIT_VECTOR, Constant.KEY,
                            encrytJsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            /* Parse JSON 並存入Bean*/
            try {
                JSONObject jsonObj = new JSONObject(decryptJsonString);
                JSONArray jsonDataArray = jsonObj.getJSONArray("DataArray");

                for (int i = 0; i < jsonDataArray.length(); i++) {
                    ContactBean items = new ContactBean();
                    JSONObject itemObj = jsonDataArray.getJSONObject(i);

                    items.setAutoID(JsonUtility.getInt(itemObj, "AutoID"));
                    items.setUsername(JsonUtility.getString(itemObj, "Username"));
                    items.setMobile(JsonUtility.getString(itemObj, "Mobile"));
                    items.setCity(JsonUtility.getString(itemObj, "City"));
                    items.setTown(JsonUtility.getString(itemObj, "Town"));
                    items.setAddress(JsonUtility.getString(itemObj, "Address"));

                    contactData.add(items);
                }


            } catch (JSONException e) {
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
            loadingDialog.dismiss();

            /*填入通訊錄資料*/
            mContactListAdapter.setData(contactData);
            layoutContact = new LinearLayout(getActivity());
            ListView listView = new ListView(getActivity());
            listView.setAdapter(mContactListAdapter);
            layoutContact.addView(listView);

            //彈出對話框
            final Dialog dialog = ViewUtility.listItemDialog(getActivity(), null, layoutContact);
            dialog.show();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //填入頁面資訊
                    editContactName.setText(contactData.get(position).getUsername());
                    editContactMobile.setText(contactData.get(position).getMobile());
                    editContactAddress.setText(contactData.get(position).getAddress());
                    spinnerContactCity.setSelection(cityPosition(contactData.get(position).getCity(), cityContactData));
                    contactTown = contactData.get(position).getTown();

                    //關閉對話框
                    dialog.dismiss();

                }
            });

        }

    }

    /**
     * 新增連絡人並加上progress bar
     *
     * @author Chris
     */
    private class AddContactTask extends AsyncTask<Integer, Integer, String> {
        // 後面尖括號內分別是參數（例子裡是Thread休息時間），進度(publishProgress用到)，return值類型

        private String encrytJsonString = ""; // 從Server抓取到的加密Json
        private String decryptJsonString = "";// 解密後的Json
        private List<ContactBean> contactData = new ArrayList<ContactBean>();
        private ProgressDialog loadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /* 跳出ProgressDialog */
            loadingDialog = new ProgressDialog(getActivity());
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 設定ProgressDialog的style
//            loadingDialog.setMax(100); // 設定最大單位
//            loadingDialog.setProgress(0); // 設定初始值
            loadingDialog.setCanceledOnTouchOutside(false); // 設定不會被摸到就取消progress畫面
            loadingDialog.setMessage("會員資料更新中 請稍候...");
            loadingDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {

			/* 建立api要用的hash JSONObject */
            Map<String, Object> jsonParams = new HashMap<String, Object>();
            jsonParams.put("sUserName", editContactName.getText().toString());
            jsonParams.put("sMobile", editContactMobile.getText().toString());
            jsonParams.put("sCity", spinnerContactCity.getSelectedItem().toString());
            jsonParams.put("sTown", spinnerContactTown.getSelectedItem().toString());
            jsonParams.put("sAddress", editContactAddress.getText().toString());
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

                encrytJsonString = HttpUtility.post(addContactApiUrl, apiParams);

				/* 解密 */
                try {
                    decryptJsonString = AESUtility.decrypt(
                            Constant.INIT_VECTOR, Constant.KEY,
                            encrytJsonString);
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
            loadingDialog.dismiss();
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
                ((ImageView) view).setImageBitmap(ViewUtility.getRoundedCornerBitmap(loadedImage, 1));
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
