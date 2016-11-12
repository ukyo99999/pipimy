package com.macrowell.pipimy.buy;

import java.util.List;

import tw.com.pipimy.app.android.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.macrowell.pipimy.Constant;
import com.macrowell.pipimy.bean.OwnerPrivateMessageListBean;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;

public class OwnerPrivateMsgListFragment extends Fragment implements
        LoaderCallbacks<List<OwnerPrivateMessageListBean>> {

    //	private String KEY_AUTH = "auth"; // api key
//	private String KEY_HASH = "hash"; // api key
//	private String authValue = ""; // api auth的值
//	private String hashValue = ""; // api hash的值
    private String productName; // 放在商品資訊檔頭的產品名稱
    private int productPrice; // 放在商品資訊檔頭的產品價格
    private String productImageUrl; // 放在商品資訊檔頭的產品照片網址

    private AQuery mAq;
    private OwnerPrivateMsgListAdapter mOwnerPrivateMsgListAdapter;
    private ListView mListView;
    private ImageView btnBack;
    private ImageView imgProduct; // 商品小圖
    private TextView textProductName; // 商品名稱
    private TextView textProductPrice; // 商品定價
    private TextView textNoMsg; //沒有人出價與聊天時顯示資訊
    private Bundle mBundle; // 來自商品詳細頁的bundle
    private List<OwnerPrivateMessageListBean> list; //存放每筆私訊item的list


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAq = new AQuery(getActivity());
        mBundle = this.getArguments();
        mOwnerPrivateMsgListAdapter = new OwnerPrivateMsgListAdapter(getActivity());
        getLoaderManager().initLoader(0, null, this);

        this.productName = mBundle.getString("productName");
        this.productPrice = mBundle.getInt("productPrice");
        this.productImageUrl = mBundle.getString("productImageUrl");

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_private_message_list, null);

		/* find views */
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        imgProduct = (ImageView) view.findViewById(R.id.img_product);
        textProductName = (TextView) view.findViewById(R.id.text_product_name);
        textProductPrice = (TextView) view.findViewById(R.id.text_product_price);
        textNoMsg = (TextView) view.findViewById(R.id.text_no_msg);
        mListView = (ListView) view.findViewById(R.id.list_talk);

		/* set data */
        mListView.setAdapter(mOwnerPrivateMsgListAdapter);
        textProductName.setText(productName);
        textProductPrice.setText("NT$" + String.valueOf(productPrice));
        mAq.id(imgProduct).image(Constant.SERVER_URL + productImageUrl, true,
                true, 0, R.drawable.ic_launcher);

		/* set listener */
        btnBack.setOnClickListener(listener);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // 跳轉到私訊購買頁面
                Intent intent = new Intent(getActivity(),
                        TalkActivity.class);

                // 送出夾帶參數
                intent.putExtra("productName", productName);
                intent.putExtra("productPrice", productPrice);
                intent.putExtra("productImageUrl", productImageUrl);
                intent.putExtra("auth", SharedPreferenceUtility.getAuth(getActivity()));
                intent.putExtra("productItemNo", list.get(position).getItemno());
                intent.putExtra("msgBoardId", list.get(position).getPrivateMsgBoardId());
                intent.putExtra("isOwner", true); //送出是賣家參數
                intent.putExtra("talkerName", String.valueOf(list.get(position).getBuyerUid())); //送出對話者id

                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public Loader<List<OwnerPrivateMessageListBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new OwnerPrivateMsgListLoader(getActivity(), mBundle);
    }

    @Override
    public void onLoadFinished(Loader<List<OwnerPrivateMessageListBean>> arg0,
                               List<OwnerPrivateMessageListBean> arg1) {
        mOwnerPrivateMsgListAdapter.setData(arg1);
        list = arg1; // 存入公用的List
        if (list.size() == 0) {
            textNoMsg.setText("目前無人出價");
            textNoMsg.setVisibility(View.VISIBLE);
        } else {
            textNoMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<OwnerPrivateMessageListBean>> arg0) {
        mOwnerPrivateMsgListAdapter.setData(null);

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

            }

        }
    };


}
