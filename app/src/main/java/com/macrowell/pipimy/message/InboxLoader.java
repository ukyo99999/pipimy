package com.macrowell.pipimy.message;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.InboxBean;
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

/**
 * 取得收件匣內容
 *
 * @author chris.cheng
 */
public class InboxLoader extends
        AsyncTaskLoader<List<InboxBean>> {
    private LogUtility log = LogUtility.getInstance(InboxLoader.class);

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    String apiUrl = Constant.API_URL + "ListPrivateMsgBoard.ashx";
    String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    String mDecryptJsonString = "";// 解密後的Json

    public InboxLoader(Context context) {
        super(context);
        authValue = SharedPreferenceUtility.getAuth(context);
    }

    @Override
    public List<InboxBean> loadInBackground() {

        List<InboxBean> meData = new ArrayList<InboxBean>();

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

            if (jsonDataArray.length() > 0) { //如果收件匣有訊息內容
                for (int i = 0; i < jsonDataArray.length(); i++) {
                    InboxBean items = new InboxBean();
                    JSONObject itemObj = jsonDataArray.getJSONObject(i);

                    items.setPrivateMsgBoardId(JsonUtility.getInt(itemObj, "PrivateMsgBoardId"));
                    items.setItemno(JsonUtility.getInt(itemObj, "Itemno"));
                    items.setBuyerUid(JsonUtility.getInt(itemObj, "BuyerUid"));
                    items.setBuyerName(JsonUtility.getString(itemObj, "BuyerName"));
                    items.setBuyerPicUrl(JsonUtility.getString(itemObj, "BuyerPicUrl"));
                    items.setBuyerPrice(JsonUtility.getInt(itemObj, "BuyerPrice"));
                    items.setStatusCode(JsonUtility.getInt(itemObj, "StatusCode"));
                    items.setOwnerUid(JsonUtility.getInt(itemObj, "OwnerUid"));
                    items.setOwnerName(JsonUtility.getString(itemObj, "OwnerName"));
                    items.setOwnerPicUrl(JsonUtility.getString(itemObj, "OwnerPicUrl"));
                    items.setItemPicUrl_1(JsonUtility.getString(itemObj, "ItemPicUrl_1"));
                    items.setItemPicName_1(JsonUtility.getString(itemObj, "ItemPicName_1"));
                    items.setItemName(JsonUtility.getString(itemObj, "ItemName"));

                    items.setOriginalPrice(JsonUtility.getInt(itemObj, "OriginalPrice"));
                    items.setLastMsg(JsonUtility.getString(itemObj, "LastMsg"));
                    items.setTypeCode(JsonUtility.getInt(itemObj, "TypeCode"));

                    items.setCrtime(JsonUtility.getString(jsonObj, "Crtime"));

                    meData.add(items);
                }

            } else {
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

