package com.macrowell.pipimy.buy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.PrivateMessageBean;
import com.macrowell.pipimy.bill.BillActivity;
import com.macrowell.pipimy.utility.AESUtility;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.JsonUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class TalkFragment extends Fragment implements
        LoaderCallbacks<List<PrivateMessageBean>> {

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
    private String talkerName; //對談者名稱
    private int lastPrivateMsgId; //最後一則私訊留言編號

    private AQuery mAq;
    private TalkAdapter mTalkAdapter;
    private ListView mListView;
    private TextView textTitle; //標題文字
    private ImageView btnBack;
    private ImageView imgProduct; // 商品小圖
    private TextView textProductName; // 商品名稱
    private TextView textProductPrice; // 商品定價
    private RelativeLayout layoutBidWaittingForSeller; //等待賣方接受出價
    private RelativeLayout layoutAcceptBid; //賣方已接受出價的提示訊息
    private Button btnBid; // 出價
    private Button btnExit; // 退出
    private Button btnAcceptBit; // 接受出價
    private Button btnRejectBit; // 拒絕出價
    private Button btnGoToPay; // 前往付款
    private ImageView imgSend; // 送出私訊
    private ImageView imgTakePhoto; // 送出圖片私訊
    private EditText edittextMsgInput; // 私訊文字內容輸入框
    private boolean isOwner; // 是否為賣家


    private Bundle mFromProductDetailBundle; // 來自商品詳細頁的bundle
    private Bundle mForLoaderBundle; //送給Load去取得對話內容的bundle
    private List<PrivateMessageBean> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAq = new AQuery(getActivity());
        mFromProductDetailBundle = this.getArguments();
        mForLoaderBundle = new Bundle();
        this.isOwner = mFromProductDetailBundle.getBoolean("isOwner");
        this.productName = mFromProductDetailBundle.getString("productName");
        this.productPrice = mFromProductDetailBundle.getInt("productPrice");
        this.productImageUrl = mFromProductDetailBundle.getString("productImageUrl");
        this.itemNo = mFromProductDetailBundle.getInt("productItemNo");
        this.privateMsgBoardId = mFromProductDetailBundle.getLong("msgBoardId");
        this.ownerUid = mFromProductDetailBundle.getInt("ownerUid");
        this.ownerName = mFromProductDetailBundle.getString("ownerName");
        this.talkerName = mFromProductDetailBundle.getString("talkerName");

        mTalkAdapter = new TalkAdapter(getActivity(), isOwner);


    }

    @Override
    public void onResume() {
        super.onResume();

        // 製作傳送給Load用的bundle
        makePrivateMsgBunlde();

        //啟動Loader
        getLoaderManager().initLoader(0, null, this);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.private_message, null);

		/* find views */
        textTitle = (TextView) view.findViewById(R.id.title);
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        imgProduct = (ImageView) view.findViewById(R.id.img_product);
        imgSend = (ImageView) view.findViewById(R.id.img_bottom_menu_send);
        imgTakePhoto = (ImageView) view
                .findViewById(R.id.img_bottom_take_picture);
        edittextMsgInput = (EditText) view
                .findViewById(R.id.edittext_talk_input);
        textProductName = (TextView) view.findViewById(R.id.text_product_name);
        textProductPrice = (TextView) view
                .findViewById(R.id.text_product_price);
        layoutBidWaittingForSeller = (RelativeLayout) view.findViewById(R.id.layout_wait_for_seller);
        layoutAcceptBid = (RelativeLayout) view.findViewById(R.id.layout_accept_bid);

        btnBid = (Button) view.findViewById(R.id.btn_bid);
        btnExit = (Button) view.findViewById(R.id.btn_exit);
        btnAcceptBit = (Button) view.findViewById(R.id.btn_accept_bid);
        btnRejectBit = (Button) view.findViewById(R.id.btn_reject_bid);
        btnGoToPay = (Button) view.findViewById(R.id.btn_go_pay);
        mListView = (ListView) view.findViewById(R.id.list_talk);

		/* set data */
        mListView.setAdapter(mTalkAdapter);
        mListView.setDividerHeight(0); // 清除分隔線
        textProductName.setText(productName);
        textProductPrice.setText("NT$" + String.valueOf(productPrice));
        mAq.id(imgProduct).image(Constant.SERVER_URL + productImageUrl, true,
                true, 0, R.drawable.ic_launcher);

		/* set listener */
        btnBack.setOnClickListener(listener);
        imgSend.setOnClickListener(listener);
        btnBid.setOnClickListener(listener);
        btnExit.setOnClickListener(listener);
        btnAcceptBit.setOnClickListener(listener);
        btnRejectBit.setOnClickListener(listener);
        btnGoToPay.setOnClickListener(listener);
        imgTakePhoto.setOnClickListener(listener);

        return view;
    }

    @Override
    public Loader<List<PrivateMessageBean>> onCreateLoader(int arg0, Bundle arg1) {
        //加個讀取中的動畫
        return new TalkLoader(getActivity(), mForLoaderBundle);
    }

    @Override
    public void onLoadFinished(Loader<List<PrivateMessageBean>> arg0,
                               List<PrivateMessageBean> arg1) {
        mTalkAdapter.setData(arg1);
        list = arg1; // 存入公用的List

        if (list.size() > 0) {
            lastPrivateMsgId = list.get(0).getLastPrivateMsgId();
        }

        //設定標題對話者名稱
        textTitle.setText("與 " + talkerName + " 聊天中");

		/* 判斷買賣家看到的不同畫面 */
        if (isOwner) { // 賣家
//            textTitle.setText("與" + String.valueOf(list.get(0).getBuyerUid()) + "聊天中");
//            textTitle.setText("與 " + talkerName + " 聊天中");
            btnBid.setVisibility(View.GONE);


            //如果有人出價
//            if (list.get(0).getBuyerPrice() >= 0 && list.get(0).getStatusCode()==1) {
            if (list.get(0).getStatusCode() == 1) {
                btnAcceptBit.setVisibility(View.VISIBLE);
                btnRejectBit.setVisibility(View.VISIBLE);
            } else {
                btnAcceptBit.setVisibility(View.GONE);
                btnRejectBit.setVisibility(View.GONE);
            }

        } else { //買家


            if (list.size() > 0) { //如果有人留過言
                //等待買方出價
                if (list.get(0).getStatusCode() == 0) {
                    btnBid.setVisibility(View.VISIBLE);
                }

                //等待賣方出價中
                if (list.get(0).getStatusCode() == 1) {
                    btnBid.setVisibility(View.GONE);
                    layoutBidWaittingForSeller.setVisibility(View.VISIBLE);
                }

                //賣方已接受出價
                if (list.get(0).getStatusCode() == 2) {
                    btnBid.setVisibility(View.GONE);
                    btnGoToPay.setVisibility(View.VISIBLE);
                    layoutAcceptBid.setVisibility(View.VISIBLE);
                }

                //商品已出售
                if (list.get(0).getStatusCode() == 3) {
                    btnBid.setVisibility(View.GONE);
//                    layoutBidWaittingForSeller.setVisibility(View.VISIBLE);
                }
            } else { //如果都沒有人留過言，MsgBoard是Null的時候
                btnBid.setVisibility(View.VISIBLE);
            }


            btnExit.setVisibility(View.GONE);
            btnAcceptBit.setVisibility(View.GONE);
            btnRejectBit.setVisibility(View.GONE);

        }


    }

    @Override
    public void onLoaderReset(Loader<List<PrivateMessageBean>> arg0) {
        mTalkAdapter.setData(null);

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

                case R.id.img_bottom_menu_send:
                    sendMsg(edittextMsgInput.getText().toString());
                    break;

                case R.id.btn_bid:
//                    ViewUtility.showToast(getActivity(), "我要出價(尚未開放，敬請期待)");

                    //跳到出價頁面
                    Intent postBidIntent = new Intent(getActivity(),
                            PostBidActivity.class);

                    // 送出夾帶參數
                    postBidIntent.putExtra("productName", productName);
                    postBidIntent.putExtra("productPrice", productPrice);
                    postBidIntent.putExtra("productImageUrl", productImageUrl);
                    postBidIntent.putExtra("auth", SharedPreferenceUtility.getAuth(getActivity()));
                    postBidIntent.putExtra("lastPrivateMsgId", lastPrivateMsgId);
                    postBidIntent.putExtra("productItemNo", itemNo);
                    postBidIntent.putExtra("ownerUid", ownerUid);
                    postBidIntent.putExtra("ownerName", ownerName);

                    startActivity(postBidIntent);
                    break;

                case R.id.btn_exit:
                    getActivity().finish();
                    break;

                case R.id.btn_accept_bid:
                    refreshList();
                    new DoAcceptBidTask().execute();

                    break;

                case R.id.btn_reject_bid:
                    refreshList();
                    new DoRejectBidTask().execute();

                    break;

                case R.id.btn_go_pay:
//                    ViewUtility.showToast(getActivity(), "前往付款(尚未開放，敬請期待)");
                    //跳到結帳頁面
                    Intent billIntent = new Intent(getActivity(),
                            BillActivity.class);

                    // 送出夾帶參數
                    billIntent.putExtra("productItemNo", itemNo);
                    startActivity(billIntent);
                    break;

                case R.id.img_bottom_take_picture:
                    ViewUtility.showToast(getActivity(), "拍照留言(尚未開放，敬請期待)");
                    break;

            }

        }
    };

    /**
     * 傳送私訊
     */
    private void sendMsg(String msg) {

        if (!TextUtils.isEmpty(msg)) { // 如果有輸入訊息
            new DoSendMsgTask(msg).execute();

            edittextMsgInput.setText("");

        }

    }

    private class DoSendMsgTask extends AsyncTask<Void, Void, Void> {
        String apiUrl = Constant.API_URL + "PostPrivateMsg.ashx";
        String encrytJsonString = ""; // 從Server抓取到的加密Json
        String decryptJsonString = "";// 解密後的Json
        String sendPrice;
        String sendMsg;

        public DoSendMsgTask(String price) {
            this.sendPrice = price;
        }

        protected Void doInBackground(Void... msg) {

			/* 利用map的對照來產生JSONObject */
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("Itemno", itemNo);
            params.put("Msg", sendPrice);
            params.put("Ts", CommonUtility.getTimeStamp());

            if (isOwner) {
                params.put("PrivateMsgBoardId", privateMsgBoardId);
            }

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
                makePrivateMsgBunlde();

                // 畫面刷新
                refreshList();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class DoAcceptBidTask extends AsyncTask<Void, Void, Void> {

        String apiUrl = Constant.API_URL + "PostPrivateReplyMsg.ashx";
        String encrytJsonString = ""; // 從Server抓取到的加密Json
        String decryptJsonString = "";// 解密後的Json

        protected Void doInBackground(Void... msg) {

			/* 利用map的對照來產生JSONObject */
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("PrivateMsgBoardId", privateMsgBoardId);
            params.put("Msg", "接受出價");
            params.put("IsAccept", 1);
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
                makePrivateMsgBunlde();

                // 畫面刷新
                refreshList();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class DoRejectBidTask extends AsyncTask<Void, Void, Void> {

        String apiUrl = Constant.API_URL + "PostPrivateReplyMsg.ashx";
        String encrytJsonString = ""; // 從Server抓取到的加密Json
        String decryptJsonString = "";// 解密後的Json

        protected Void doInBackground(Void... msg) {

			/* 利用map的對照來產生JSONObject */
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("PrivateMsgBoardId", privateMsgBoardId);
            params.put("Msg", "拒絕出價");
            params.put("IsAccept", 0);
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
                makePrivateMsgBunlde();

                // 畫面刷新
                refreshList();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    /**
     * 送出留言刷新清單
     */
    private void refreshList() {
        getLoaderManager().restartLoader(0, null, this);
        mTalkAdapter.notifyDataSetChanged();
    }

    /*
     *製作取得私訊的Loader用的bundle
     */
    private void makePrivateMsgBunlde() {
        mForLoaderBundle.putString("productName", productName);
        mForLoaderBundle.putInt("productPrice", productPrice);
        mForLoaderBundle.putString("productImageUrl", productImageUrl);
        mForLoaderBundle.putInt("productItemNo", itemNo);
        mForLoaderBundle.putLong("msgBoardId", privateMsgBoardId);
    }

}
