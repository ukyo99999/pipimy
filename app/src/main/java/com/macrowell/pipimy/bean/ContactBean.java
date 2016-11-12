package com.macrowell.pipimy.bean;

import java.io.Serializable;

/**
 * Created by Chris on 15/1/28.
 */
public class ContactBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int autoID; //編號
    private String username; //聯絡人姓名
    private String mobile; //手機號碼
    private String city; //居住縣市
    private String town; //居住鄉鎮市區
    private String address; //居住地址

    public void setAutoID(int autoID) {
        this.autoID = autoID;
    }

    public int getAutoID() {
        return autoID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

}
