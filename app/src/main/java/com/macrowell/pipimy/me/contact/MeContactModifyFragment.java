package com.macrowell.pipimy.me.contact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class MeContactModifyFragment extends Fragment {

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json

    private TextView textTitle; //頁面的標題文字
    private TextView textCancel;
    private TextView textCommit;
    private EditText editName;
    private EditText editMobile;
    private Spinner spinnerCity;
    private Spinner spinnerTown;
    private EditText editAddress;
    private Button btn_del;

    private Bundle mContactListBundle; // 來自通訊錄列表頁的bundle
    private int id;
    private String name;
    private String mobile;
    private String city;
    private String town;
    private String address;

    private final int ADD_CONTACT = 0; //狀態為:新增連絡人
    private final int MODIFY_CONTACT = 1; //狀態為:修改連絡人
    private final int DEL_CONTACT = 2; //狀態為:刪除連絡人
    private int modifyAction; //目前要做的是哪個狀態行為


    private ArrayAdapter<String> cityList;
    private ArrayAdapter<String> areaList;
    private String[] cityData;
    private String[] areaData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContactListBundle = this.getArguments();

        this.modifyAction = mContactListBundle.getInt("modifyAction");

        switch (modifyAction) {
            case ADD_CONTACT:

                break;
            case MODIFY_CONTACT:
                this.id = mContactListBundle.getInt("id");
                this.name = mContactListBundle.getString("name");
                this.mobile = mContactListBundle.getString("mobile");
                this.city = mContactListBundle.getString("city");
                this.town = mContactListBundle.getString("town");
                this.address = mContactListBundle.getString("address");
                break;
            case DEL_CONTACT:
                this.id = mContactListBundle.getInt("id");
                break;
        }

    }


    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_contact_modify, null);

		/* find views */
        textTitle = (TextView) view.findViewById(R.id.title);
        textCancel = (TextView) view.findViewById(R.id.text_back);
        textCommit = (TextView) view.findViewById(R.id.text_commit);
        editName = (EditText) view.findViewById(R.id.edit_name);
        editMobile = (EditText) view.findViewById(R.id.edit_mobile);
        spinnerCity = (Spinner) view.findViewById(R.id.spinner_city);
        spinnerTown = (Spinner) view.findViewById(R.id.spinner_town);
        editAddress = (EditText) view.findViewById(R.id.edittext_address);
        btn_del = (Button) view.findViewById(R.id.btn_del);

        /*set city spinner data*/
        cityData = new String[CommonUtility.obtainZipcode(getActivity()).getCityList().size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getCityList().size(); i++) {
            cityData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getCityList().get(i));
        }
        cityList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                cityData);
        spinnerCity.setAdapter(cityList);

		/*set area spinner data*/
        areaData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(0).size()];

        for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(0).size(); i++) {
            areaData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(0).get(i).getArea());
        }

        areaList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                areaData);
        spinnerTown.setAdapter(areaList);
//		spinnerTown.setSelection(areaPosition(this.town, areaData));

        /* set data*/
        switch (modifyAction) {
            case ADD_CONTACT:
                textTitle.setText("新增通訊錄");
                break;
            case MODIFY_CONTACT:
                textTitle.setText("編輯通訊錄");
                editName.setText(name);
                editMobile.setText(mobile);
                btn_del.setVisibility(View.VISIBLE);

                spinnerCity.setSelection(cityPosition(this.city, cityData));
                spinnerTown.setSelection(areaPosition(this.town, areaData));
                editAddress.setText(address);
                break;
        }

		/* set listener */
        textCancel.setOnClickListener(listener);
        textCommit.setOnClickListener(listener);
        btn_del.setOnClickListener(listener);
        spinnerCity.setOnItemSelectedListener(spinnerCitySelect); //地址(縣市)
        spinnerTown.setOnItemSelectedListener(spinnerTownSelect);//地址(區鄉鎮)

        return view;
    }


    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.text_back:
                    getActivity().finish();
                    break;

                case R.id.text_commit:

                    ViewUtility.hideSoftKeyboard(getActivity(), editAddress);
                    name = editName.getText().toString();
                    mobile = editMobile.getText().toString();
                    address = editAddress.getText().toString();

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(mobile)
                            && !TextUtils.isEmpty(address)) {
                        new DoModifyContactTask().execute();
                    } else {
                        ViewUtility.showToast(getActivity(), "尚有欄位沒有填完整");
                    }
                    break;

                case R.id.btn_del:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("確定刪除嗎?");
//                    builder.setTitle("提示");
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            modifyAction = DEL_CONTACT;
                            new DoModifyContactTask().execute();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                    break;

            }

        }
    };

    /**
     * 縣市Spinner選項監聽
     */
    private AdapterView.OnItemSelectedListener spinnerCitySelect = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            areaData = new String[CommonUtility.obtainZipcode(getActivity()).getDataList(position).size()];

            for (int i = 0; i < CommonUtility.obtainZipcode(getActivity()).getDataList(position).size(); i++) {
                areaData[i] = String.valueOf(CommonUtility.obtainZipcode(getActivity()).getDataList(position).get(i).getArea());
            }

            areaList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                    areaData);
            spinnerTown.setAdapter(areaList);
            spinnerTown.setSelection(areaPosition(town, areaData));

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


    private class DoModifyContactTask extends AsyncTask<Void, Void, Void> {
        String apiUrl = "";
        String encrytJsonString = ""; // 從Server抓取到的加密Json
        String decryptJsonString = "";// 解密後的Json

        public DoModifyContactTask() {

            switch (modifyAction) {
                case ADD_CONTACT:
                    apiUrl = Constant.API_URL + "MemberContactsI.ashx";
                    break;
                case MODIFY_CONTACT:
                    apiUrl = Constant.API_URL + "MemberContactsU.ashx";
                    break;
                case DEL_CONTACT:
                    apiUrl = Constant.API_URL + "MemberContactsD.ashx";
                    break;
            }

        }

        protected Void doInBackground(Void... msg) {

			/* 利用map的對照來產生JSONObject */
            Map<String, Object> params = new HashMap<String, Object>();


            switch (modifyAction) {
                case ADD_CONTACT:
                    params.put("sUserName", editName.getText().toString());
                    params.put("sMobile", editMobile.getText().toString());
                    params.put("sCity", city);
                    params.put("sTown", town);
                    params.put("sAddress", editAddress.getText().toString());
                    params.put("ts", CommonUtility.getTimeStamp());
                    break;
                case MODIFY_CONTACT:
                    params.put("iAutoID", id);
                    params.put("sUserName", editName.getText().toString());
                    params.put("sMobile", editMobile.getText().toString());
                    params.put("sCity", city);
                    params.put("sTown", town);
                    params.put("sAddress", editAddress.getText().toString());
                    params.put("ts", CommonUtility.getTimeStamp());
                    break;
                case DEL_CONTACT:
                    params.put("iAutoID", id);
                    params.put("ts", CommonUtility.getTimeStamp());
                    break;
            }


            JSONObject jsonObjectHash = new JSONObject(params);

			/* 傳送api */
            try {
                hashValue = AESUtility.encrypt(Constant.INIT_VECTOR,
                        Constant.KEY, String.valueOf(jsonObjectHash));
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

                //關閉這個頁面
                getActivity().finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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
        int positon = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(areaName)) {
                positon = i;
            }
        }

        return positon;
    }


}
