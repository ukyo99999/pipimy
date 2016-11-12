package com.macrowell.pipimy.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.macrowell.pipimy.bean.Zipcode;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tw.com.pipimy.app.android.R;

public class CommonUtility {

    /**
     * 取得Unix時間
     *
     * @return String unix 時間
     */
    public static String getTimeStamp() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return String.valueOf(unixTime);
    }

    /**
     * 取得目前時間
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String currentTime = sdFormat.format(date);
        return currentTime;
    }

    /**
     * URL拆解(用於客制化的url schema)
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> splitUrl(String url)
            throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] temp = url.split("://");
        String urlSchema = temp[0] + "://";
        query_pairs.put("UrlSchema", urlSchema);

        String query = temp[1];
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }

        return query_pairs;
    }

    /**
     * URL拆解(只能用於非客制化的url schema)
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> splitUrlQuery(URL url)
            throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    /**
     * 物件轉Map
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> convertObjectToMap(Object obj)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        @SuppressWarnings("unused")
        Class<?> pomclass = obj.getClass();
        pomclass = obj.getClass();
        Method[] methods = obj.getClass().getMethods();

        Map<String, Object> map = new HashMap<String, Object>();
        for (Method m : methods) {
            if (m.getName().startsWith("get")
                    && !m.getName().startsWith("getClass")) {
                Object value = (Object) m.invoke(obj);
                map.put(m.getName().substring(3), (Object) value);
            }
        }
        return map;
    }

    /**
     * 取得選取照片後的照片路徑
     *
     * @return String filePath
     */
    public static String getImageUri(Context context, Intent intent) {
        String filePath = "";

        Uri selectedImage = intent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }

    /**
     * 圖片轉為byte[]
     *
     * @param filePath
     * @return
     */
    @SuppressWarnings("resource")
    public static byte[] imageTransToByte(String filePath) {
        // File myFile = new
        // File("/sdcard/DCIM/Camera/IMG_20141127_144348.jpg");
        // filePath = this.filePath;
        File imageFile = new File(filePath);

        FileInputStream fin = null;

        // create FileInputStream object
        try {
            fin = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int bytesRead;
        try {
            while ((bytesRead = fin.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] fileContent = bos.toByteArray();

        return fileContent;

    }

    /**
     * Base64還原為相片檔
     *
     * @throws IOException
     */
    @SuppressLint("SdCardPath")
    @SuppressWarnings("unused")
    private void base64toFile(String base64String) throws IOException {
        byte[] data = Base64.decode(base64String, Base64.DEFAULT);
        FileOutputStream fos = new FileOutputStream(new File(
                "/sdcard/abc123.jpg"));
        fos.write(data);
        fos.close();
    }

    // public static File imageScale(File originImageFile){
    // try{
    // image2 = ImageIO.read(new File("/tmp/aaa.jpg"));
    // BufferedImage bufferedImage = new
    // BufferedImage(image2.getWidth()/2,image2.getHeight()/2,image2.getType());
    // for(int x=0;x<image2.getWidth();x+=2)
    // for(int y=0;y<image2.getHeight();y+=2) {
    // bufferedImage.setRGB(x/2, y/2,image2.getRGB(x, y));
    // }
    // ImageIO.write(bufferedImage, "jpg", new File("/tmp/bbb.jpg"));
    // }
    // catch(Exception e) { System.out.println("error");}
    // }
    // }

    /**
     * 呼叫內建圖片裁切工具
     *
     * @param data
     * @return
     */
    public static Intent getCropImageIntent(Bitmap data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");// crop=true 有這句才能叫出裁剪頁面.
        intent.putExtra("aspectX", 1);// 这兩項為裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", 300);// 回傳照片比例X
        intent.putExtra("outputY", 300);// 回傳照片比例Y
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 載入影像檔並縮放縮影像檔大小與壓縮影像品質
     *
     * @param uri
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap setupImageSizeCompress(Context context, Uri uri,
                                                int maxWidth, int maxHeight, int compress) {
        Bitmap tempBitmap = null;
        Bitmap bitmap = null;

        try {
            tempBitmap = BitmapFactory.decodeStream(context
                    .getContentResolver().openInputStream(uri));

            // 縮圖片
            bitmap = resizeBitmap(tempBitmap, maxWidth, maxHeight);

            FileOutputStream out = new FileOutputStream(uri.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, compress, out);
            out.close();
        } catch (Exception e) {
        }

        return bitmap;
    }

    /**
     * 維持長寬比例縮放Bitmap
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }

        int width = originWidth;
        int height = originHeight;

        if (originWidth > maxWidth) {
            width = maxWidth;

            double i = originWidth * 1.0 / maxWidth;
            height = (int) Math.floor(originHeight / i);

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }

        if (height > maxHeight) {
            height = maxHeight;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        }

        return bitmap;
    }

    /**
     * Uri轉String
     *
     * @param uri
     * @return
     */
    public static String uriToString(Uri uri) {
        return uri.toString();
    }

    /**
     * String轉Uri
     *
     * @param str
     * @return
     */
    public static Uri stringToUri(String str) {
        return Uri.parse(str);
    }

    /**
     * 刪除資料夾
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!deleteDir(files[i]))
                    return false;
            }
        }
        //目錄現在清空了可以刪除
        return dir.delete();
    }

    /**
     * 取得郵遞區號
     *
     * @param context
     * @return
     */
    public static Zipcode obtainZipcode(Context context) {
        BufferedReader in = null;
        Zipcode zipcode = new Zipcode();

        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            context.getResources().openRawResource(R.raw.zipcode)));

            String line = null;
            String[] row = null;
            String city = null;
            List<Zipcode.Data> dataList = null;

            while ((line = in.readLine()) != null) {
                row = line.split(",");
                if (row.length >= 4) {
                    Zipcode.Data data = new Zipcode.Data();
                    data.setId(row[0]);
                    data.setCity(row[1]);
                    data.setArea(row[2]);
                    data.setZip(row[3]);

                    if (data.getCity().equals(city) == false) {
                        city = data.getCity();
                        zipcode.addCity(city);

                        dataList = new ArrayList<Zipcode.Data>();
                        zipcode.addDataList(dataList);
                    }

                    dataList.add(data);

                    zipcode.putData(data.getId(), data);
                }

                zipcode.setUnmodifiable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return zipcode;
    }

    /**
     * 刪除上傳照片時的暫存照片資料夾
     */
    public static void delUploadImagesFolder() {

        File delImagesFolder = new File(
                Environment.getExternalStorageDirectory(), "PipimyImages");
        CommonUtility.deleteDir(delImagesFolder);
    }

    /**
     * 刪除ArrayList裡的特定元素(字串)
     *
     * @param elementName
     * @return
     */
    public static ArrayList<String> delArrayListElement(ArrayList<String> list, String elementName) {
        ArrayList<String> arrayList = list;
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            if (elementName.equals(str)) {
                iterator.remove();
            }
        }
        return arrayList;
    }

}
