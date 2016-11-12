package com.macrowell.pipimy.buy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.PrivateMessageBean;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.JsonUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;

/**
 * 私訊留言板內容
 *
 * @author chris.cheng
 */
public class TalkLoader extends AsyncTaskLoader<List<PrivateMessageBean>> {

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    private String productName; // 放在商品資訊檔頭的產品名稱
    private int productPrice; // 放在商品資訊檔頭的產品價格
    private String productImageUrl; // 放在商品資訊檔頭的產品照片網址
    private long privateMsgBoardId; // 私訊留言板編號

    private String apiUrl = Constant.API_URL+"ListPrivateMsg.ashx";
    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json


    public TalkLoader(Context context, Bundle bundle) {
        super(context);

        if (bundle != null) {
            this.authValue = SharedPreferenceUtility.getAuth(context);
            this.productName = bundle.getString("productName");
            this.productPrice = bundle.getInt("productPrice");
            this.productImageUrl = bundle.getString("productImageUrl");
            this.privateMsgBoardId = bundle.getLong("msgBoardId");
        }

    }

    @Override
    public List<PrivateMessageBean> loadInBackground() {

        List<PrivateMessageBean> talkData = new ArrayList<PrivateMessageBean>();

		/* 利用map的對照來產生JSONObject */
        Map<String, Object> hashParams = new HashMap<String, Object>();
        hashParams.put("PrivateMsgBoardId", privateMsgBoardId);
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

		/* Parse JSON 並存入Bean */
        try {
            JSONObject jsonObj = new JSONObject(mDecryptJsonString);

            // 取出留言版資訊MsgBoard
//			JSONArray jsonDataMsgBoard = jsonObj.getJSONArray("MsgBoard");  //這個之後都要改寫，用錯方法會造成取不到值的警告
            JSONArray jsonDataMsgBoard = JsonUtility.getJSONArray(jsonObj, "MsgBoard");

            // 取出對話區塊的DataArray(先只存對話內容，之後再加留言版資訊:arrayList[0])
            JSONArray jsonDataArray = jsonObj.getJSONArray("DataArray");

            for (int i = 0; i < jsonDataArray.length(); i++) {
                PrivateMessageBean items = new PrivateMessageBean();
                JSONObject itemObj = jsonDataArray.getJSONObject(i);

                items.setPrivateMsgId(JsonUtility.getInt(itemObj, "PrivateMsgId"));
                items.setUid(JsonUtility.getInt(itemObj, "Uid"));
                items.setMsg(JsonUtility.getString(itemObj, "Msg"));
                items.setCrtime(JsonUtility.getString(itemObj, "Crtime"));

                // 設定買賣家頭像網址
                items.setBuyerPicUrl(JsonUtility.getString(
                        jsonDataMsgBoard.getJSONObject(0), "BuyerPicUrl"));
                items.setOwnerPicUrl(JsonUtility.getString(
                        jsonDataMsgBoard.getJSONObject(0), "OwnerPicUrl"));

                // 設定買賣家ID
                items.setBuyerUid(JsonUtility.getInt(jsonDataMsgBoard.getJSONObject(0), "BuyerUid"));
                items.setOwnerUid(JsonUtility.getInt(jsonDataMsgBoard.getJSONObject(0), "OwnerUid"));

                //設定最後一則的私訊留言編號
                items.setLastPrivateMsgId(JsonUtility.getInt(jsonDataMsgBoard.getJSONObject(0), "LastPrivateMsgId"));

                //設定物品的出價狀態
                items.setStatusCode(JsonUtility.getInt(jsonDataMsgBoard.getJSONObject(0), "StatusCode"));

                talkData.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return talkData;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
