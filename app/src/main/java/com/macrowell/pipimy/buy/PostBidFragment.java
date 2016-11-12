package com.macrowell.pipimy.buy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.JsonUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class PostBidFragment extends Fragment {

    private String KEY_AUTH = "auth"; // api key
    private String KEY_HASH = "hash"; // api key
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值
    private int itemNo; // 商品編號
    private long privateMsgBoardId; // 私訊留言版編號
    private String productName; // 放在商品資訊檔頭的產品名稱
    private int productPrice; // 放在商品資訊檔頭的產品價格
    private String productImageUrl; // 放在商品資訊檔頭的產品照片網址
    private int ownerUid; //賣家的uid
    private String ownerName; //賣家名稱
    private int lastPrivateMsgId; //最後一則私訊留言編號

    private AQuery mAq;
    //    private TalkAdapter mTalkAdapter;
    private TextView textTitle; //標題文字
    private ImageView btnBack; //標題列返回按鈕
    private ImageView imgProduct; // 商品小圖
    private TextView textProductName; // 商品名稱
    private TextView textProductPrice; // 商品定價
    private Button btnBitSummit; // 送出出價
    private TextView textLine1; //賣家以多少錢在出售的第一列文字
    private EditText editTextPrice; //輸入的出價金額
    private TextView textNtd; //顯示幣值的文字

    private Bundle mFromTalkFragmentlBundle; // 來自私訊頁的bundle
//    private Bundle mForLoaderBundle; //送給Load去取得對話內容的bundle
//    private List<PrivateMessageBean> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAq = new AQuery(getActivity());
        mFromTalkFragmentlBundle = this.getArguments();
//        mForLoaderBundle = new Bundle();

//        mTalkAdapter = new TalkAdapter(getActivity());
        this.productName = mFromTalkFragmentlBundle.getString("productName");
        this.productPrice = mFromTalkFragmentlBundle.getInt("productPrice");
        this.productImageUrl = mFromTalkFragmentlBundle.getString("productImageUrl");
        this.itemNo = mFromTalkFragmentlBundle.getInt("productItemNo");
        this.privateMsgBoardId = mFromTalkFragmentlBundle.getLong("msgBoardId");
        this.ownerUid = mFromTalkFragmentlBundle.getInt("ownerUid");
        this.ownerName = mFromTalkFragmentlBundle.getString("ownerName");
        this.lastPrivateMsgId = mFromTalkFragmentlBundle.getInt("lastPrivateMsgId");

//        // 製作傳送給Load用的bundle
//        makePrivateMsgBunlde();

    }


    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_bid_message, null);

		/* find views */
        textTitle = (TextView) view.findViewById(R.id.title);
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        imgProduct = (ImageView) view.findViewById(R.id.img_product);
        textProductName = (TextView) view.findViewById(R.id.text_product_name);
        textProductPrice = (TextView) view
                .findViewById(R.id.text_product_price);
        btnBitSummit = (Button) view.findViewById(R.id.btn_bid_summit);
        textLine1 = (TextView) view.findViewById(R.id.text_bid_line1);
        editTextPrice = (EditText) view.findViewById(R.id.edittext_bid_price);
        textNtd = (TextView) view.findViewById(R.id.text_ntd);

		/* set data */
        textTitle.setText("與 " + ownerName + " 聊天中");
        textProductName.setText(productName);
        textProductPrice.setText("NT$" + String.valueOf(productPrice));
        mAq.id(imgProduct).image(Constant.SERVER_URL + productImageUrl, true,
                true, 0, R.drawable.ic_launcher);
        textLine1.setText(ownerName + "正以NT$" + String.valueOf(productPrice) + "出售該商品,");
        editTextPrice.setText(String.valueOf(productPrice));

		/* set listener */
        btnBack.setOnClickListener(listener);
        btnBitSummit.setOnClickListener(listener);
        textNtd.setOnClickListener(listener);

        return view;
    }


    /**
     * 按鈕監聽
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_back:
                    getActivity().finish();
                    break;


                case R.id.btn_bid_summit:
                    //如果沒有輸入金額
                    if (TextUtils.isEmpty(editTextPrice.getText().toString())) {
                        ViewUtility.showToast(getActivity(), "您沒有輸入價錢");
                    } else {
                        ViewUtility.showToast(getActivity(), "送出價錢");
                        sendPrice(editTextPrice.getText().toString());
                    }


                    break;

                case R.id.text_ntd:
                    editTextPrice.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) editTextPrice.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(editTextPrice, 0);
                    break;


            }

        }
    };

    /**
     * 傳送出價
     */
    private void sendPrice(String price) {

        if (!TextUtils.isEmpty(price)) { // 如果有輸入訊息
            new DoSendBidPriceTask(price).execute();


        }

    }

    private class DoSendBidPriceTask extends AsyncTask<Void, Void, Void> {
        String apiUrl = Constant.API_URL + "PostPrivateBidMsg.ashx";
        String encrytJsonString = ""; // 從Server抓取到的加密Json
        String decryptJsonString = "";// 解密後的Json
        String sendPrice;

        public DoSendBidPriceTask(String price) {
            this.sendPrice = price;
        }

        protected Void doInBackground(Void... msg) {

			/* 利用map的對照來產生JSONObject */
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("Itemno", itemNo);
            params.put("Msg", "出價NT$" + sendPrice);
            params.put("Price", Integer.valueOf(sendPrice));
            params.put("LastPrivateMsgId", lastPrivateMsgId);
            params.put("Ts", CommonUtility.getTimeStamp());


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

                //取得PrivateMsgBoardId (給全新留言的時候用)
                privateMsgBoardId = JsonUtility.getLong(JsonUtility.getJSONArray(jsonObj, "MsgBoard").getJSONObject(0), "PrivateMsgBoardId");

                //關閉這個頁面
                getActivity().finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
