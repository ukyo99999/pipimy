package com.macrowell.pipimy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.macrowell.pipimy.product.ProductCategoryActivity;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.HttpUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;

import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class LoginFragment extends Fragment {

    private WebView webViewLogin;
    private int redirectUrlTimes = 0;
    private String url = Constant.SERVER_URL + "LoginInit.aspx"; // 登入網頁
    private String redirectUrl = ""; // 轉址網頁
    private String resultCode = ""; // 登入結果代碼
    private String token = "";
    private String uid = "";
    private String deviceId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.token = SharedPreferenceUtility.getAuth(getActivity());
        this.uid = SharedPreferenceUtility.getUid(getActivity());
        this.deviceId = SharedPreferenceUtility.getDeviceId(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, null);
        webViewLogin = (WebView) view.findViewById(R.id.webLogin);
        webViewLogin.setWebViewClient(new WebViewClient());

        WebSettings websettings = webViewLogin.getSettings();
        websettings.setSupportZoom(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);
        websettings.setDefaultTextEncodingName("utf-8");

        webViewLogin.setWebChromeClient(new WebChromeClient() {
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

        webViewLogin.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:

                redirectUrlTimes++;
                redirectUrl = url;

                webLogin();
                return false; // false:繼續頁面轉址

            }
        });

        String postData = "Auth=" + this.token + "&DeviceID=" + this.deviceId + "&OS=2";
        webViewLogin.postUrl(url, EncodingUtils.getBytes(postData, "base64"));

        return view;
    }

    /**
     * 執行取得網頁header資訊
     */
    private void webLogin() {

        try {
            Map<String, String> splitUrlQuery = CommonUtility
                    .splitUrl(redirectUrl);

            // urlSchema = splitUrlQuery.get("UrlSchema");
            resultCode = String.valueOf(splitUrlQuery.get("login?code"));

            if (resultCode != null && resultCode.equals("100")) { // 如果成功
                // 存入Auth
                SharedPreferenceUtility.setAuth(getActivity(),
                        splitUrlQuery.get("auth"));

                // 存入User id
                SharedPreferenceUtility.setUid(getActivity(),
                        splitUrlQuery.get("userid"));

                // 跳轉到產品大分類頁
                Intent intent = new Intent(getActivity(),
                        ProductCategoryActivity.class);
                startActivity(intent);

                // 清除登入頁面
                getActivity().finish();


            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除token
     */
    private void delToken() {
        SharedPreferenceUtility.setAuth(getActivity(), "");
    }

    /**
     * 只用來測試用程式post回傳資訊
     */
    private void testLogin() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("Auth", "testKevin");

        try {
            HttpUtility
                    .post("http://wwwtest.pipimy.com.tw/LoginProcess.aspx?Auth=testKevin",
                            params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
