package com.macrowell.pipimy.message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.macrowell.pipimy.bean.InboxBean;
import com.macrowell.pipimy.buy.TalkActivity;
import com.macrowell.pipimy.imagecache.ImageCacheFragment;
import com.macrowell.pipimy.me.MeActivity;
import com.macrowell.pipimy.product.ProductCategoryActivity;
import com.macrowell.pipimy.sell.AddProductActivity;
import com.macrowell.pipimy.utility.CommonUtility;
import com.macrowell.pipimy.utility.LogUtility;
import com.macrowell.pipimy.utility.SharedPreferenceUtility;
import com.macrowell.pipimy.utility.ViewUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.List;

import tw.com.pipimy.app.android.R;

public class InboxFragment extends ImageCacheFragment implements
        LoaderCallbacks<List<InboxBean>> {

    private LogUtility log = LogUtility.getInstance(InboxFragment.class);

    private RelativeLayout menuBrowse;
    private RelativeLayout menu2;
    private RelativeLayout menuSell;
    private RelativeLayout menuInbox;
    private RelativeLayout menuMe;
    private ImageView btnBack;
    private Button btnAll; //全部按鈕
    private Button btnBuy; //買東西按鈕
    private Button btnSell; //賣東西按鈕

    private InboxAdapter inboxAdapter;
    private ListView listView;
    private ProgressBar progressLoadingImage;


    // private String KEY_AUTH = "auth"; // api key:auth
    // private String KEY_HASH = "hash"; // api key:hash
    private String authValue = ""; // api auth的值
    private String hashValue = ""; // api hash的值

    private final int TAKE_PHOTO = 99;
    private final int SELECT_PHOTO = 100;
    private final int PHOTO_CUT = 101;
    private Uri uriOringin; // 拍照後的檔案uri
    private Uri uriCrop; // 裁切後的檔案uri

    List<InboxBean> list; //用來存放Loader讀取完之後的list
    private DisplayImageOptions avatarImageOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        inboxAdapter = new InboxAdapter(getActivity());
        avatarImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.img_no_avatar)
                .showImageOnFail(R.drawable.img_no_avatar)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(getActivity()));


        getLoaderManager().initLoader(0, null, this);

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inbox, null);

		/*find views*/
        menuBrowse = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_browse);
        menu2 = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu2);
        menuSell = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_sell);
        menuInbox = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_inbox);
        menuMe = (RelativeLayout) view.findViewById(R.id.layout_bottom_menu_me);
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        progressLoadingImage = (ProgressBar) view.findViewById(R.id.progress_loading_image);
        btnAll = (Button) view.findViewById(R.id.btn_all);
        btnBuy = (Button) view.findViewById(R.id.btn_buy);
        btnSell = (Button) view.findViewById(R.id.btn_sell);

        listView = (ListView) view.findViewById(R.id.list_message_content);
        listView.setAdapter(inboxAdapter);


		/*set listener*/
        menuBrowse.setOnClickListener(listener);
        menu2.setOnClickListener(listener);
        menuSell.setOnClickListener(listener);
        menuInbox.setOnClickListener(listener);
        menuMe.setOnClickListener(listener);
        btnBack.setOnClickListener(listener);
        btnAll.setOnClickListener(listener);
        btnBuy.setOnClickListener(listener);
        btnSell.setOnClickListener(listener);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // 跳轉到私訊購買頁面
                Intent intent = new Intent(getActivity(), TalkActivity.class);

                // 送出夾帶參數
                intent.putExtra("productName", list.get(position).getItemName());
                intent.putExtra("productPrice", list.get(position).getOriginalPrice());
                intent.putExtra("productImageUrl", list.get(position).getItemPicUrl_1());
                intent.putExtra("auth", SharedPreferenceUtility.getAuth(getActivity()));
                intent.putExtra("msgBoardId", list.get(position).getPrivateMsgBoardId());
                intent.putExtra("productItemNo", list.get(position).getItemno());

                switch (list.get(position).getTypeCode()) {
                    case 1:
                        intent.putExtra("isOwner", false); //送出不是賣家參數
                        intent.putExtra("talkerName", list.get(position).getOwnerName());

                        break;
                    case 2:
                        intent.putExtra("isOwner", true); //送出是賣家參數
                        intent.putExtra("talkerName", list.get(position).getBuyerName());
                        break;
                }


                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public Loader<List<InboxBean>> onCreateLoader(int arg0, Bundle arg1) {
        return new InboxLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<InboxBean>> arg0, List<InboxBean> arg1) {

        list = arg1;

        Boolean isHasMsgData = null; //判斷收件匣裡有沒有訊息

		/*set view data*/
        if (list.size() > 0) {
            isHasMsgData = true;
        } else {
            isHasMsgData = false;

        }

        inboxAdapter.setData(arg1, isHasMsgData);

    }

    @Override
    public void onLoaderReset(Loader<List<InboxBean>> arg0) {
        inboxAdapter.setData(null, false);
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

                case R.id.layout_bottom_menu2:
                    ViewUtility.showToast(getActivity(), "購物車(尚未開放，敬請期待)");

                    break;


                case R.id.layout_bottom_menu_browse:
                    // 跳轉到瀏覽頁面
                    Intent browseIntent = new Intent(getActivity(),
                            ProductCategoryActivity.class);

                    startActivity(browseIntent);
                    break;


                case R.id.layout_bottom_menu_sell:
                    // 產生對話框的選項文字
                    String[] items = new String[]{"使用相機拍照", "從圖庫中選擇一張舊有的照片"};

                    // 產生對話框的選項監聽
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int itemId) {

                            switch (itemId) {
                                case 0:
                                    // 使用Intent呼叫其他服務幫忙拍照
                                    Intent cameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);

                                    // 建立拍照上傳的照片放置資料夾
                                    File imagesFolder = new File(
                                            Environment.getExternalStorageDirectory(),
                                            "PipimyImages");
                                    imagesFolder.mkdirs();

                                    // 設定照片路徑與檔名
                                    File image = new File(imagesFolder, "image_001_" + CommonUtility.getCurrentTime() + ".jpg");
                                    uriOringin = Uri.fromFile(image);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            uriOringin);

                                    startActivityForResult(cameraIntent, TAKE_PHOTO);

                                    break;
                                case 1:
                                    // 選取手機中的相片
                                    Intent photoPickerIntent = new Intent(
                                            Intent.ACTION_PICK);
                                    photoPickerIntent.setType("image/*");
                                    startActivityForResult(photoPickerIntent,
                                            SELECT_PHOTO);

                                    break;
                            }

                        }
                    };

                    // 產生對話框
                    ViewUtility.simpleListDialog(getActivity(), "你想要如何取得你的照片?",
                            items, listener);

                    break;

                case R.id.layout_bottom_menu_me:
                    // 跳轉到[我]首頁
                    Intent meIntent = new Intent(getActivity(),
                            MeActivity.class);

                    startActivity(meIntent);
                    break;
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = null;
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    File imageSelected = new File(CommonUtility.getImageUri(
                            getActivity(), data));
                    uriOringin = Uri.fromFile(imageSelected);

                    // 裁切
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(uriOringin, "image/*");
                    cropIntent.putExtra("crop", "true");

                    // 裁剪框的比例，正方型
                    cropIntent.putExtra("aspectX", 1);
                    cropIntent.putExtra("aspectY", 1);
                    cropIntent.putExtra("outputX", 640);// 回傳照片解析度X
                    cropIntent.putExtra("outputY", 640);// 回傳照片解析度Y
                    cropIntent.putExtra("outputFormat", "JPEG");// 圖片格式

                    // 建立拍照上傳的照片放置資料夾
                    File imagesFolder = new File(
                            Environment.getExternalStorageDirectory(),
                            "PipimyImages");
                    imagesFolder.mkdirs();

                    // 設定照片路徑與檔名
                    File imageOutput = new File(imagesFolder, "image_001_" + CommonUtility.getCurrentTime() + "_crop.jpg");
                    uriCrop = Uri.fromFile(imageOutput);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);

                    startActivityForResult(cropIntent, PHOTO_CUT);
                }

                break;

            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    // 裁切
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(uriOringin, "image/*");
                    cropIntent.putExtra("crop", "true");

                    // 裁剪框的比例，正方型
                    cropIntent.putExtra("aspectX", 1);
                    cropIntent.putExtra("aspectY", 1);
                    cropIntent.putExtra("outputX", 640);// 回傳照片解析度X
                    cropIntent.putExtra("outputY", 640);// 回傳照片解析度Y
                    cropIntent.putExtra("outputFormat", "JPEG");// 圖片格式

                    // 建立拍照上傳的照片放置資料夾
                    File imagesFolder = new File(
                            Environment.getExternalStorageDirectory(),
                            "PipimyImages");
                    imagesFolder.mkdirs();

                    // 設定照片路徑與檔名
                    File image = new File(imagesFolder, "image_001_" + CommonUtility.getCurrentTime() + "_crop.jpg");
                    uriCrop = Uri.fromFile(image);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);

                    startActivityForResult(cropIntent, PHOTO_CUT);

                }
                break;

            case PHOTO_CUT:
                if (resultCode == Activity.RESULT_OK) {

                    bundle = new Bundle();
                    bundle.putString("photoUri", CommonUtility.uriToString(uriCrop));

                    // 跳轉到新增商品清單頁面
                    Intent addProductIntent = new Intent(getActivity(),
                            AddProductActivity.class);

                    addProductIntent.putExtras(bundle);
                    startActivity(addProductIntent);

                }
                break;

        }

    }

    private SimpleImageLoadingListener loadingListener(
            final ProgressBar progressBar) {
        return new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                ((ImageView) view).setImageBitmap(ViewUtility.getRoundedCornerBitmap(loadedImage, 1));
            }
        };
    }

    ;

    private SimpleImageLoadingListener avatarLoadingListener(
            final ProgressBar progressBar) {
        return new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                ((ImageView) view).setImageBitmap(ViewUtility.getRoundedCornerBitmap(loadedImage, 1));
            }
        };
    }

    ;
}
