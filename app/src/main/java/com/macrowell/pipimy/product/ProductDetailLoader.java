package com.macrowell.pipimy.product;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.ProductDetailBean;
import com.macrowell.pipimy.utility.AESUtility;
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
 * 商品同分類下的清單
 *
 * @author chris.cheng
 */
public class ProductDetailLoader extends
        AsyncTaskLoader<List<ProductDetailBean>> {

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String mAuthValue = ""; // api value
    private String mHashValue = ""; // api value

    private String apiUrl = Constant.API_URL + "GetBidItem.ashx";
    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json


    public ProductDetailLoader(Context context, Bundle bundle) {
        super(context);

        if (bundle != null) {
            this.mAuthValue = bundle.getString("authValue");
            this.mHashValue = bundle.getString("hashValue");
        }

    }

    @Override
    public List<ProductDetailBean> loadInBackground() {

        List<ProductDetailBean> productDetailData = new ArrayList<ProductDetailBean>();

		/* 從Server取得JSON資料 */
        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_AUTH, mAuthValue);
        params.put(KEY_HASH, mHashValue);

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

            JSONArray jsonDataArray = jsonObj.getJSONArray("Data");

            for (int i = 0; i < jsonDataArray.length(); i++) {
                ProductDetailBean items = new ProductDetailBean();
                JSONObject itemObj = jsonDataArray.getJSONObject(i);

                items.setItemno(JsonUtility.getInt(itemObj, "Itemno"));
                items.setItemName(JsonUtility.getString(itemObj, "ItemName"));
                items.setItemDescription(JsonUtility.getString(itemObj, "ItemDescription"));

                items.setItemPicUrl_1(JsonUtility.getString(itemObj, "ItemPicUrl_1"));
                items.setItemPicUrl_2(JsonUtility.getString(itemObj, "ItemPicUrl_2"));
                items.setItemPicUrl_3(JsonUtility.getString(itemObj, "ItemPicUrl_3"));
                items.setItemPicUrl_4(JsonUtility.getString(itemObj, "ItemPicUrl_4"));
                items.setOriginalPrice(JsonUtility.getInt(itemObj, "OriginalPrice"));
                items.setCategoryId(JsonUtility.getInt(itemObj, "CategoryId"));
                items.setSubCategoryId(JsonUtility.getInt(itemObj, "SubCategoryId"));

                items.setOwnerUid(JsonUtility.getInt(itemObj, "OwnerUid"));
                items.setOwnerName(JsonUtility.getString(itemObj, "OwnerName"));
                items.setOwnerPicUrl(JsonUtility.getString(itemObj, "OwnerPicUrl"));

                items.setCrtime(JsonUtility.getString(itemObj, "Crtime"));
                items.setTrackingCount(JsonUtility.getInt(itemObj, "TrackingCount"));
                items.setIsTracked(JsonUtility.getInt(itemObj, "IsTracked"));
                items.setStatusCode(JsonUtility.getInt(itemObj, "StatusCode"));
                items.setPrivateMsgBoardId(jsonObj.getLong("PrivateMsgBoardId"));
                items.setBidCount(jsonObj.getInt("BidCount"));

                productDetailData.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productDetailData;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
