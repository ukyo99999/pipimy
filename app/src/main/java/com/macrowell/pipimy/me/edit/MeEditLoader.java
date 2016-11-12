package com.macrowell.pipimy.me.edit;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.MeBean;
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

public class MeEditLoader extends AsyncTaskLoader<List<MeBean>> {
    private LogUtility log = LogUtility.getInstance(MeEditLoader.class);

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    String apiUrl = Constant.API_URL + "MyInfo.ashx";
    String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    String mDecryptJsonString = "";// 解密後的Json

    public MeEditLoader(Context context) {
        super(context);
        authValue = SharedPreferenceUtility.getAuth(context);
    }

    @Override
    public List<MeBean> loadInBackground() {

        List<MeBean> meData = new ArrayList<MeBean>();

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
                MeBean items = new MeBean();
                JSONObject itemObj = jsonDataArray.getJSONObject(i);

                items.setItemno(JsonUtility.getInt(itemObj, "Itemno"));
                items.setItemName(JsonUtility.getString(itemObj, "ItemName"));
                items.setOriginalPrice(JsonUtility.getInt(itemObj, "OriginalPrice"));
                items.setItemPicName_1(JsonUtility.getString(itemObj, "ItemPicName_1"));
                items.setItemPicUrl_1(JsonUtility.getString(itemObj, "ItemPicUrl_1"));
                items.setTrackingCount(JsonUtility.getInt(itemObj, "TrackingCount"));
                items.setIsTracked(JsonUtility.getInt(itemObj, "IsTracked"));

                items.setUsername(JsonUtility.getString(jsonObj, "Username"));
                items.setNickName(JsonUtility.getString(jsonObj, "NickName"));
                items.setIntroduction(JsonUtility.getString(jsonObj, "Introduction"));
                items.setMyCity(JsonUtility.getString(jsonObj, "MyCity"));
                items.setCrtime(JsonUtility.getString(jsonObj, "Crtime"));
                items.setEmail(JsonUtility.getString(jsonObj, "Email"));
                items.setFans(JsonUtility.getInt(jsonObj, "Fans"));
                items.setTracking(JsonUtility.getInt(jsonObj, "Tracking"));
                items.setTradedCount(JsonUtility.getInt(jsonObj, "TradedCount"));
                items.setUnpaidCount(JsonUtility.getInt(jsonObj, "UnpaidCount"));
                items.setRtnCode(JsonUtility.getInt(jsonObj, "RtnCode"));
                items.setRtnMsg(JsonUtility.getString(jsonObj, "RtnMsg"));

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

