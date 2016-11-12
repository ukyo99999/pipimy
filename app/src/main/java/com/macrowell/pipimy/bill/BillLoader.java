package com.macrowell.pipimy.bill;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.BillBean;
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
 * 取得結帳頁內容
 *
 * @author chris.cheng
 */
public class BillLoader extends AsyncTaskLoader<Map<String, List<BillBean>>> {
    private LogUtility log = LogUtility.getInstance(BillLoader.class);

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    String apiUrl = Constant.API_URL + "CreateOrder.ashx";
    String mEncrytJsonString = ""; // 從Server抓取到的加密Json
    String mDecryptJsonString = "";// 解密後的Json
    private String itemNo;

    public BillLoader(Context context, Bundle bundle) {
        super(context);
        authValue = SharedPreferenceUtility.getAuth(context);
        itemNo = String.valueOf(bundle.getInt("productItemNo"));

    }

    @Override
    public Map<String, List<BillBean>> loadInBackground() {

        Map<String, List<BillBean>> mapData = new HashMap<String, List<BillBean>>();


		/* 利用map的對照來產生JSONObject */
        Map<String, Object> hashParams = new HashMap<String, Object>();
        hashParams.put("BuyerUid", SharedPreferenceUtility.getUid(getContext()));
        hashParams.put("Itemnos", itemNo);
//        hashParams.put("Itemnos", "");
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
            List<BillBean> orderItemList = new ArrayList<BillBean>();
            List<BillBean> memberInfoList = new ArrayList<BillBean>();
            List<BillBean> memberContactsList = new ArrayList<BillBean>();
            List<BillBean> billingTypeList = new ArrayList<BillBean>();
            List<BillBean> logisticsTypeList = new ArrayList<BillBean>();
            List<BillBean> othersList = new ArrayList<BillBean>();

            JSONObject jsonObj = new JSONObject(mDecryptJsonString);

            JSONArray jsonOrderItemArray = jsonObj.getJSONArray("OrderItem");
            JSONArray jsonMemberInfoArray = jsonObj.getJSONArray("MemberInfo");
            JSONArray jsonMemberContactsArray = jsonObj.getJSONArray("MemberContacts");
            JSONArray jsonBillingTypeArray = jsonObj.getJSONArray("BillingType");
            JSONArray jsonLogisticsTypeArray = jsonObj.getJSONArray("LogisticsType");

            //商品項目
            for (int i = 0; i < jsonOrderItemArray.length(); i++) {
                BillBean items = new BillBean();
                JSONObject orderItemObj = jsonOrderItemArray.getJSONObject(i);
                items.setItemno(JsonUtility.getInt(orderItemObj, "Itemno"));
                items.setItemName(JsonUtility.getString(orderItemObj, "ItemName"));
                items.setBuyerPrice(JsonUtility.getInt(orderItemObj, "BuyerPrice"));
                items.setProductPicUrl(JsonUtility.getString(orderItemObj, "ItemPicIconUrl_1"));

                orderItemList.add(items);
            }
            mapData.put("OrderItem", orderItemList);

            //購買人資料
            for (int i = 0; i < jsonMemberInfoArray.length(); i++) {
                BillBean items = new BillBean();
                JSONObject memberInfoObj = jsonMemberInfoArray.getJSONObject(i);

                items.setInfoUsername(JsonUtility.getString(memberInfoObj, "Username"));
                items.setInfoMobile(JsonUtility.getString(memberInfoObj, "Mobile"));
                items.setInfoCity(JsonUtility.getString(memberInfoObj, "City"));
                items.setInfoTown(JsonUtility.getString(memberInfoObj, "Town"));
                items.setInfoAddress(JsonUtility.getString(memberInfoObj, "Address"));

                memberInfoList.add(items);
            }
            mapData.put("MemberInfo", memberInfoList);

            //收件人資料
            for (int i = 0; i < jsonMemberContactsArray.length(); i++) {
                BillBean items = new BillBean();
                JSONObject memberContactsObj = jsonMemberContactsArray.getJSONObject(i);

                items.setContactUsername(JsonUtility.getString(memberContactsObj, "Username"));
                items.setContactMobile(JsonUtility.getString(memberContactsObj, "Mobile"));
                items.setContactCity(JsonUtility.getString(memberContactsObj, "City"));
                items.setContactTown(JsonUtility.getString(memberContactsObj, "Town"));
                items.setContactAddress(JsonUtility.getString(memberContactsObj, "Address"));

                memberContactsList.add(items);
            }
            mapData.put("MemberContacts", memberContactsList);

            //付款方式
            for (int i = 0; i < jsonBillingTypeArray.length(); i++) {
                BillBean items = new BillBean();
                JSONObject billingTypeObj = jsonBillingTypeArray.getJSONObject(i);

                items.setBillingTypeCode(JsonUtility.getString(billingTypeObj, "TypeCode"));
                items.setBillingTypeSubTypeCode(JsonUtility.getString(billingTypeObj, "SubTypeCode"));
                items.setBillingTypeName(JsonUtility.getString(billingTypeObj, "Name"));
                items.setBillingTypeShoreName(JsonUtility.getString(billingTypeObj, "ShortName"));

                billingTypeList.add(items);
            }
            mapData.put("BillingType", billingTypeList);

            //運費
            for (int i = 0; i < jsonLogisticsTypeArray.length(); i++) {
                BillBean items = new BillBean();
                JSONObject logisticsTypeObj = jsonLogisticsTypeArray.getJSONObject(i);

                items.setLogisticsTypeCode(JsonUtility.getString(logisticsTypeObj, "TypeCode"));
                items.setLogisticsTypeSubTypeCode(JsonUtility.getString(logisticsTypeObj, "SubTypeCode"));
                items.setLogisticsTypeName(JsonUtility.getString(logisticsTypeObj, "Name"));
                items.setLogisticsTypeShoreName(JsonUtility.getString(logisticsTypeObj, "ShortName"));
                items.setLogisticsTypeCharge(JsonUtility.getInt(logisticsTypeObj, "Charge"));

                logisticsTypeList.add(items);
            }
            mapData.put("LogisticsType", logisticsTypeList);


            //其他訂單資訊(賣家、賣家、訂單編號、購物金額總計)
            for (int i = 0; i < 1; i++) {
                BillBean items = new BillBean();

                items.setBuyerUid(JsonUtility.getInt(jsonObj, "BuyerUid"));
                items.setOwnerUid(JsonUtility.getInt(jsonObj, "OwnerUid"));
                items.setOrderNo(JsonUtility.getInt(jsonObj, "OrderNo"));
                items.setAmount(JsonUtility.getInt(jsonObj, "Amount"));
                items.setTransportNo(JsonUtility.getInt(jsonObj, "TransportNo"));

                othersList.add(items);
            }
            mapData.put("Others", othersList);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mapData;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}

