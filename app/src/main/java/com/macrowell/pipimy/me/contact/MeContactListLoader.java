package com.macrowell.pipimy.me.contact;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.ContactBean;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.JsonUtility;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeContactListLoader extends
        AsyncTaskLoader<List<ContactBean>> {
    private LogUtility log = LogUtility.getInstance(MeContactListLoader.class);

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    String apiUrl = Constant.API_URL + "MemberContactsS.ashx";
    String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    String mDecryptJsonString = "";// 解密後的Json

    public MeContactListLoader(Context context) {
        super(context);
        authValue = SharedPreferenceUtility.getAuth(context);
    }

    @Override
    public List<ContactBean> loadInBackground() {

        List<ContactBean> meData = new ArrayList<ContactBean>();

		/* 利用map的對照來產生JSONObject */
        Map<String, Object> hashParams = new HashMap<String, Object>();
        hashParams.put("Ts", CommonUtility.getTimeStamp());
        JSONObject jsonObjectHash = new JSONObject(hashParams);

		/* 傳送api */
        try {
            hashValue = AESUtility.encrypt(Constant.INIT_VECTOR, Constant.KEY,
                    String.valueOf(jsonObjectHash));

        } catch (Exception e) {
            e.printStackTrace();
        }

		/* 從Server取得JSON資料 */
        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_AUTH, authValue);
        params.put(KEY_HASH, hashValue);

        try {
            mEncrytJsonString = HttpUtility.post(apiUrl, params);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

		/* 解密 */
        try {
            mDecryptJsonString = AESUtility.decrypt(Constant.INIT_VECTOR,
                    Constant.KEY, mEncrytJsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

		/* Parse JSON 並存入Bean*/
        try {
            JSONObject jsonObj = new JSONObject(mDecryptJsonString);
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

                meData.add(items);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return meData;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}

