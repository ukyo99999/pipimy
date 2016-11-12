package com.macrowell.pipimy.buy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.OwnerPrivateMessageListBean;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.JsonUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 賣家私訊清單
 *
 * @author chris.cheng
 */
public class OwnerPrivateMsgListLoader extends AsyncTaskLoader<List<OwnerPrivateMessageListBean>> {

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    // private boolean isLikeProduct=false; // 傳入哪個項目(true為喜愛商品)
    private String productName; // 放在商品資訊檔頭的產品名稱
    private int productPrice; // 放在商品資訊檔頭的產品價格
    private String productImageUrl; // 放在商品資訊檔頭的產品照片網址
    private int itemNo; // 商品編號

    private String apiUrl = Constant.API_URL + "ListPrivateMsgBoard.ashx";
    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json

    public OwnerPrivateMsgListLoader(Context context, Bundle bundle) {
        super(context);

        if (bundle != null) {
            this.authValue = bundle.getString("auth");
            this.itemNo = bundle.getInt("productItemNo");
            this.productName = bundle.getString("productName");
            this.productPrice = bundle.getInt("productPrice");
            this.productImageUrl = bundle.getString("productImageUrl");
        }

    }

    @Override
    public List<OwnerPrivateMessageListBean> loadInBackground() {

        List<OwnerPrivateMessageListBean> ownPrivateMsgListData = new ArrayList<OwnerPrivateMessageListBean>();

		/* 利用map的對照來產生JSONObject */
        Map<String, Object> hashParams = new HashMap<String, Object>();
        hashParams.put("Itemno", itemNo);
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

            // 取出對話區塊的DataArray(先只存對話內容，之後再加留言版資訊:arrayList[0])
            JSONArray jsonDataArray = jsonObj.getJSONArray("DataArray");

            for (int i = 0; i < jsonDataArray.length(); i++) {
                OwnerPrivateMessageListBean items = new OwnerPrivateMessageListBean();
                JSONObject itemObj = jsonDataArray.getJSONObject(i);

                items.setPrivateMsgBoardId(JsonUtility.getInt(itemObj, "PrivateMsgBoardId"));
                items.setItemno(JsonUtility.getInt(itemObj, "Itemno"));
                items.setBuyerUid(JsonUtility.getInt(itemObj, "BuyerUid"));
                items.setBuyerPicUrl(JsonUtility.getString(itemObj, "BuyerPicUrl"));
                items.setBuyerPrice(JsonUtility.getInt(itemObj, "BuyerPrice"));
                items.setStatusCode(JsonUtility.getInt(itemObj, "StatusCode"));
                items.setOwnerUid(JsonUtility.getInt(itemObj, "OwnerUid"));
                items.setOwnerPicUrl(JsonUtility.getString(itemObj, "OwnerPicUrl"));
                items.setItemPicUrl(JsonUtility.getString(itemObj, "ItemPicUrl"));
                items.setItemName(JsonUtility.getString(itemObj, "ItemName"));
                items.setOriginalPrice(JsonUtility.getInt(itemObj, "OriginalPrice"));
                items.setLastMsg(JsonUtility.getString(itemObj, "LastMsg"));
                items.setTypeCode(JsonUtility.getInt(itemObj, "TypeCode"));

                ownPrivateMsgListData.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ownPrivateMsgListData;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
