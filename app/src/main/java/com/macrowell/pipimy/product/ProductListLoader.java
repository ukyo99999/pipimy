package com.macrowell.pipimy.product;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.ProductListBean;
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
public class ProductListLoader extends AsyncTaskLoader<List<ProductListBean>> {

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String mAuthValue = ""; // api value
    private String mHashValue = ""; // api value
    private boolean isLikeProduct = false; // 傳入哪個項目(true為喜愛商品)

    private String apiLikeUrl = Constant.API_URL + "ListTrackingItem.ashx";
    private String apiUrl = Constant.API_URL + "QueryBidItem.ashx";
    private String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    private String mDecryptJsonString = "";// 解密後的Json


    public ProductListLoader(Context context, Bundle bundle) {
        super(context);

        if (bundle != null) {
            this.mAuthValue = bundle.getString("authValue");
            this.mHashValue = bundle.getString("hashValue");
            this.isLikeProduct = bundle.getBoolean("isLikeValue");
        }

    }

    @Override
    public List<ProductListBean> loadInBackground() {

        List<ProductListBean> productListData = new ArrayList<ProductListBean>();

		/* 從Server取得JSON資料 */
        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_AUTH, mAuthValue);
        params.put(KEY_HASH, mHashValue);

        if (isLikeProduct) { //如果是喜愛商品
            try {
                mEncrytJsonString = HttpUtility.post(apiLikeUrl, params);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else {
            try {
                mEncrytJsonString = HttpUtility.post(apiUrl, params);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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
                ProductListBean items = new ProductListBean();
                JSONObject itemObj = jsonDataArray.getJSONObject(i);

                items.setItemno(JsonUtility.getInt(itemObj, "Itemno"));
                items.setItemName(JsonUtility.getString(itemObj, "ItemName"));
                items.setItemPicUrl_1(JsonUtility.getString(itemObj,
                        "ItemPicUrl_1"));
                items.setItemPicName_1(JsonUtility.getString(itemObj,
                        "ItemPicName_1"));
                items.setOriginalPrice(JsonUtility.getString(itemObj,
                        "OriginalPrice"));
                items.setOwnerUid(JsonUtility.getString(itemObj, "OwnerUid"));
                items.setOwnerName(JsonUtility.getString(itemObj, "OwnerName"));
                items.setOwnerPicUrl(JsonUtility.getString(itemObj,
                        "OwnerPicUrl"));
                items.setIsTracked(JsonUtility.getInt(itemObj, "IsTracked"));
                items.setTrackingCount(JsonUtility.getInt(itemObj,
                        "TrackingCount"));

                productListData.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productListData;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
