package com.macrowell.pipimy.bill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.utility.LogUtility;

import org.apache.http.util.EncodingUtils;

import tw.com.pipimy.app.android.R;

public class PayFragment extends Fragment {
    private LogUtility log = LogUtility.getInstance(PayFragment.class);

    private WebView webPay;
    private String url = Constant.SERVER_URL + "Order/PaymentProcess.aspx"; // 付款網址

    private String orderNo; //訂單編號
    private String itemName; //商品名稱(以逗號分隔)
    private String paymentMethod; //付款方式：ALL(全部)/Credit(信用卡)/CVS(超商代碼繳款)
    private String totalAmount; //交易金額
    private String buyerName; //購買人姓名
    private String buyerCellPhone; //購買人手機
    private String buyerCity; //購買人縣市
    private String buyerTown; //購買人鄉鎮
    private String buyerAddress; //購買人地址
    private String receiverName; //收件人姓名
    private String receiverCellPhone; //收件人手機
    private String receiverCity; //收件人縣市
    private String receiverTown; //收件人鄉鎮
    private String receiverAddress; //收件人地址
    private String remark; //備註
    private String transportNo; //物流單號
    private String buyerUid; //買方會員編號
    private String ownerUid; //賣方會員編號

    private Bundle mBundle; // 來自結帳頁的bundle


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBundle = this.getArguments();

        this.orderNo = mBundle.getString("OrderNo");
        this.itemName = mBundle.getString("ItemName");
        this.paymentMethod = mBundle.getString("PaymentMethod");
        this.totalAmount = mBundle.getString("TotalAmount");
        this.buyerName = mBundle.getString("BuyerName");
        this.buyerCellPhone = mBundle.getString("BuyerCellPhone");
        this.buyerCity = mBundle.getString("BuyerCity");
        this.buyerTown = mBundle.getString("BuyerTown");
        this.buyerAddress = mBundle.getString("BuyerAddress");
        this.receiverName = mBundle.getString("ReceiverName");
        this.receiverCellPhone = mBundle.getString("ReceiverCellPhone");
        this.receiverCity = mBundle.getString("ReceiverCity");
        this.receiverTown = mBundle.getString("ReceiverTown");
        this.receiverAddress = mBundle.getString("ReceiverAddress");
        this.remark = mBundle.getString("Remark");
        this.transportNo = mBundle.getString("TransportNo");
        this.buyerUid = mBundle.getString("BuyerUid");
        this.ownerUid = mBundle.getString("OwnerUid");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pay, null);
        webPay = (WebView) view.findViewById(R.id.webPay);
        webPay.setWebViewClient(new WebViewClient());

        WebSettings websettings = webPay.getSettings();
        websettings.setSupportZoom(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);
        websettings.setDefaultTextEncodingName("utf-8");

        webPay.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final android.webkit.JsResult result) {
                new AlertDialog.Builder(getActivity())
                        // .setTitle("JavaScript Dialog")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                }).setCancelable(false).create().show();
                return true;
            }

            ;
        });

        webPay.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:

                return false; // false:繼續頁面轉址

            }
        });

        String postData = "OrderNo=" + this.orderNo +
                "&ItemName=" + this.itemName +
                "&PaymentMethod=" + this.paymentMethod +
                "&TotalAmount=" + this.totalAmount +
                "&BuyerName=" + this.buyerName +
                "&BuyerCellPhone=" + this.buyerCellPhone +
                "&BuyerCity=" + this.buyerCity +
                "&BuyerTown=" + this.buyerTown +
                "&BuyerAddress=" + this.buyerAddress +
                "&ReceiverName=" + this.receiverName +
                "&ReceiverCellPhone=" + this.receiverCellPhone +
                "&ReceiverCity=" + this.receiverCity +
                "&ReceiverTown=" + this.receiverTown +
                "&ReceiverAddress=" + this.receiverAddress +
                "&Remark=" + this.remark +
                "&TransportNo=" + this.transportNo +
                "&BuyerUid=" + this.buyerUid +
                "&OwnerUid=" + this.ownerUid;

        webPay.postUrl(url, EncodingUtils.getBytes(postData, "base64"));

        return view;
    }


}
