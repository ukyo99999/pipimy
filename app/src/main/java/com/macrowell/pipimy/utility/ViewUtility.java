package com.macrowell.pipimy.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;

public class ViewUtility {

    /**
     * 顯示清單式對話框
     *
     * @param context
     * @param title
     * @param items
     * @param listener
     * @return
     */
    public static AlertDialog.Builder simpleListDialog(Context context,
                                                       String title, String[] items,
                                                       DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setItems(items, listener);
        dialog.show();

        return dialog;

    }

    /**
     * 填入View顯示對話框
     *
     * @param context
     * @param title
     * @param view
     * @return
     */
    public static Dialog listItemDialog(Context context,
                                        String title, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Dialog dialog;
        builder.setTitle(title);
        builder.setView(view);
        dialog = builder.create();

        return dialog;

    }

    public static void closeDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Dialog dialog = builder.show();
        dialog.dismiss();
    }

    /**
     * 顯示Toast文字訊息
     *
     * @param context
     * @param showText
     */
    public static void showToast(Context context, String showText) {
        Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
    }

    /**
     * 測試照片裁切
     *
     * @param uri
     */
    private void cropImage(Uri uri) {
        Uri uri2 = null;

        // 裁剪圖片意圖
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        // 裁剪框的比例，4:3
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);

        // 裁剪後輸出圖片的尺寸大小

        // intent.putExtra("outputX", 1080);

        // intent.putExtra("outputY", 810); //像這個太大如果使用true,記憶體不夠就會出錯

        intent.putExtra("outputFormat", "JPEG");// 圖片格式

        // intent.putExtra("noFaceDetection", true);// 取消人臉識別

        intent.putExtra("return-data", false);

        // 開啟一個帶有返回值的Activity，請求碼為PHOTO_REQUEST_CUT

        String localTempImgDir = "Hello";

        String localTempImgFileName = System.currentTimeMillis() + ".jpg";

        File f = new File(Environment.getExternalStorageDirectory()

                + "/" + localTempImgDir + "/" + localTempImgFileName);

        uri2 = Uri.fromFile(f); // 用這個uri去做事

        // intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri2);

        // 把他連結到uri2,如此返回時便可使用此uri2做事

        // Toast.makeText( photoActivity.this, "targetUri ="+targetUri,
        // Toast.LENGTH_SHORT ).show();

        String selectedImagePath = f.getAbsolutePath();

        // Log.w("TAG", "selectedImagePath:" + selectedImagePath);

        // View1.setText("selectedImagePath:"+selectedImagePath);

        // mImageUri = Uri.fromFile(f);

        // strImage = f.getAbsolutePath();

        Log.w("TAG", "f.getAbsolutePath():" + f.getAbsolutePath());

        // startActivityForResult(intent, PHOTO_REQUEST_CUT);

    }

    /**
     * 取得螢幕解析度
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        return metrics;
    }

    /**
     * 將圖片轉為圓型  不標準的圖形從中心截取
     *
     * @param bitmap
     * @param outputType (1:圓型、2:圓角)
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int outputType) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        // 切圓的直徑
        int minLength = width > height ? height : width;
        final int color = 0xff424242;
        final Paint paint = new Paint();
        int x = (width - minLength) / 2;
        int y = (height - minLength) / 2;
        final Rect rect = new Rect(x, y, x + minLength, y + minLength);
        final RectF rectF = new RectF(rect);
        final float roundPx = minLength / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        if (outputType == 1) {
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        }
        if (outputType == 2) {
            canvas.drawRoundRect(rectF, 35, 35, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 隱藏鍵盤
     *
     * @param context
     * @param view
     */
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
