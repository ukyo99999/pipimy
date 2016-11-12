package com.macrowell.pipimy.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.macrowell.pipimy.Constant;

public class SharedPreferenceUtility {
    private static final String PREF_NAME = "PYPYMY"; // SharedPreference name
    private static final String KEY_AUTH = "AUTH"; // for token
    private static final String KEY_UID = "UID"; //for user id
    private static final String KEY_DEVICE_ID = "DEVICE_ID";
    private static final String KEY_SEARCH_REC = "SEARCH_REC"; //for search record

    private static final String KEY_ME_NICKNAME = "ME_NICKNAME"; //for edit profile
    private static final String KEY_ME_MY_CITY = "ME_MY_CITY"; //for search record
    private static final String KEY_ME_WEB_URL = "ME_WEB_URL"; //for search record
    private static final String KEY_ME_INTRODUCTION = "ME_INTRODUCTION"; //for search record
    private static final String KEY_ME_MY_PIC_URL = "ME_MY_PIC_URL"; //for search record
    private static final String KEY_ME_MY_PIC_NAME = "ME_MY_PIC_NAME"; //for search record
    private static final String KEY_ME_USER_NAME = "ME_USER_NAME"; //for search record
    private static final String KEY_ME_MOBILE = "ME_MOBILE"; //for search record
    private static final String KEY_ME_SEX = "ME_SEX"; //for search record
    private static final String KEY_ME_BIRTHDAY = "ME_BIRTHDAY"; //for search record
    private static final String KEY_ME_EMAIL = "ME_EMAIL"; //for search record
    private static final String KEY_ME_CITY = "ME_CITY"; //for search record
    private static final String KEY_ME_TOWN = "ME_TOWN"; //for search record
    private static final String KEY_ME_ADDRESS = "ME_ME_ADDRESS"; //for search record

    public static void setDeviceId(Context context, String deviceId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_DEVICE_ID, deviceId);
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDeviceId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString(KEY_DEVICE_ID, "");
        try {
            if (!TextUtils.isEmpty(deviceId)) {
//				deviceId = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,deviceId);
                return deviceId;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return deviceId;
    }


    public static void setAuth(Context context, String auth) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_AUTH, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, auth));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAuth(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String auth = sharedPreferences.getString(KEY_AUTH, "");
        try {
            if (!TextUtils.isEmpty(auth)) {
                auth = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        auth);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return auth;
    }

    public static void setUid(Context context, String uid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_UID, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, uid));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUid(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(KEY_UID, "");
        try {
            if (!TextUtils.isEmpty(uid)) {
                uid = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        uid);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return uid;
    }

    public static void setSearchRec(Context context, String uid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_SEARCH_REC, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, uid));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSearchRec(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(KEY_SEARCH_REC, "");
        try {
            if (!TextUtils.isEmpty(uid)) {
                uid = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        uid);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return uid;
    }

    public static void setNickName(Context context, String nickName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_NICKNAME, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, nickName));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNickName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String nickName = sharedPreferences.getString(KEY_ME_NICKNAME, "");
        try {
            if (!TextUtils.isEmpty(nickName)) {
                nickName = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        nickName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return nickName;
    }

    public static void setMyCity(Context context, String myCity) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_MY_CITY, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, myCity));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMyCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String myCity = sharedPreferences.getString(KEY_ME_MY_CITY, "");
        try {
            if (!TextUtils.isEmpty(myCity)) {
                myCity = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        myCity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return myCity;
    }

    public static void setWebURL(Context context, String webURL) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_WEB_URL, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, webURL));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getWebURL(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String webURL = sharedPreferences.getString(KEY_ME_WEB_URL, "");
        try {
            if (!TextUtils.isEmpty(webURL)) {
                webURL = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        webURL);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return webURL;
    }

    public static void setIntroduction(Context context, String introduction) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_INTRODUCTION, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, introduction));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getIntroduction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String introduction = sharedPreferences.getString(KEY_ME_INTRODUCTION, "");
        try {
            if (!TextUtils.isEmpty(introduction)) {
                introduction = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        introduction);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return introduction;
    }

    public static void setMyPicUrl(Context context, String myPicUrl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_MY_PIC_URL, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, myPicUrl));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMyPicUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String myPicUrl = sharedPreferences.getString(KEY_ME_MY_PIC_URL, "");
        try {
            if (!TextUtils.isEmpty(myPicUrl)) {
                myPicUrl = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        myPicUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return myPicUrl;
    }

    public static void setMyPicName(Context context, String myPicName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_MY_PIC_NAME, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, myPicName));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMyPicName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String myPicName = sharedPreferences.getString(KEY_ME_MY_PIC_NAME, "");
        try {
            if (!TextUtils.isEmpty(myPicName)) {
                myPicName = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        myPicName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return myPicName;
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_USER_NAME, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, username));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_ME_USER_NAME, "");
        try {
            if (!TextUtils.isEmpty(username)) {
                username = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        username);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return username;
    }

    public static void setMobile(Context context, String mobile) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_MOBILE, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, mobile));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMobile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString(KEY_ME_MOBILE, "");
        try {
            if (!TextUtils.isEmpty(mobile)) {
                mobile = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        mobile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mobile;
    }

    public static void setSex(Context context, String sex) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_SEX, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, sex));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String sex = sharedPreferences.getString(KEY_ME_SEX, "");
        try {
            if (!TextUtils.isEmpty(sex)) {
                sex = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        sex);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sex;
    }

    public static void setBirthday(Context context, String birthday) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_BIRTHDAY, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, birthday));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBirthday(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String birthday = sharedPreferences.getString(KEY_ME_BIRTHDAY, "");
        try {
            if (!TextUtils.isEmpty(birthday)) {
                birthday = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        birthday);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return birthday;
    }

    public static void setEmail(Context context, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_EMAIL, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, email));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_ME_EMAIL, "");
        try {
            if (!TextUtils.isEmpty(email)) {
                email = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        email);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return email;
    }

    public static void setCity(Context context, String city) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_CITY, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, city));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String city = sharedPreferences.getString(KEY_ME_CITY, "");
        try {
            if (!TextUtils.isEmpty(city)) {
                city = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        city);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return city;
    }

    public static void setTown(Context context, String town) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_TOWN, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, town));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTown(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String town = sharedPreferences.getString(KEY_ME_TOWN, "");
        try {
            if (!TextUtils.isEmpty(town)) {
                town = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        town);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return town;
    }

    public static void setAddress(Context context, String address) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(KEY_ME_ADDRESS, AESUtility.encrypt(Constant.INIT_VECTOR,
                    Constant.KEY, address));
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String address = sharedPreferences.getString(KEY_ME_ADDRESS, "");
        try {
            if (!TextUtils.isEmpty(address)) {
                address = AESUtility.decrypt(Constant.INIT_VECTOR, Constant.KEY,
                        address);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return address;
    }

}
