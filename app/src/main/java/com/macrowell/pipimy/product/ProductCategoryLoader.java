package com.macrowell.pipimy.product;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.CategoriesBean;
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

public class ProductCategoryLoader extends AsyncTaskLoader<List<CategoriesBean>> {

    String apiUrl = Constant.API_URL + "ListCategory.ashx";
    String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    String mDecryptJsonString = "";// 解密後的Json

    public ProductCategoryLoader(Context context) {
        super(context);
    }

    @Override
    public List<CategoriesBean> loadInBackground() {

        List<CategoriesBean> categoriesData = new ArrayList<CategoriesBean>();

		/* 從Server取得JSON資料 */
        Map<String, String> params = new HashMap<String, String>();
        try {
            mEncrytJsonString = HttpUtility.get(apiUrl, params);
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
            JSONArray jsonDataArray = jsonObj.getJSONArray("Categories");

            for (int i = 0; i < jsonDataArray.length(); i++) {
                CategoriesBean items = new CategoriesBean();
                JSONObject itemObj = jsonDataArray.getJSONObject(i);

                items.setCategoryId(JsonUtility.getInt(itemObj, "CategoryId"));
                items.setCategoryName(JsonUtility.getString(itemObj, "CategoryName"));
                items.setBidItemCount(JsonUtility.getInt(itemObj, "BidItemCount"));
                items.setPicUrl(JsonUtility.getString(itemObj, "PicUrl"));
                categoriesData.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categoriesData;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
