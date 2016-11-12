package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class MeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String username; // 使用者名稱
	private String nickName; //暱稱
	private String introduction; //個人簡介
	private String myCity; //我的城市
	private String crtime; //加入時間
	private String email; //Email
	
	private String mobile; //手機號碼
	private int sex; //性別 男:1,女:0
	private String birthday; //生日 (yyyy/MM/dd)
	private String city; //縣市(address1)
	private String town; //鄉鎮市區(address2)
	private String address; //地址(address3)
	private String webURL; //網站
	
	private int fans;  //粉絲:被追蹤人數
	private int tracking; //追蹤使用者人數
	private int tradedCount; //成交次數
	private int unpaidCount; //棄標次數
	private String myPicUrl; //個人頭像URL
	private String myPicName; //個人頭像檔名
	private int rtnCode; //狀態碼:成功100,查無此會員 200,其他:
	private String rtnMsg; //狀態訊息:成功 or查無此會員
	
	private int itemno; //拍賣物品編號
	private String itemName; //拍賣物品名
	private int originalPrice; //拍賣物品價格
	private String itemPicName_1; //拍賣物品名
	private String itemPicUrl_1; //拍賣物品圖片網址
	private int trackingCount; //喜愛次數
	private int isTracked; //目前登入的使用者 0:未按過讚 1:按過讚
	
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setMyCity(String myCity) {
		this.myCity = myCity;
	}

	public String getMyCity() {
		return myCity;
	}

	public void setCrtime(String crtime) {
		this.crtime = crtime;
	}

	public String getCrtime() {
		return crtime;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return sex;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthday() {
		return birthday;
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

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public int getFans() {
		return fans;
	}

	public void setTracking(int tracking) {
		this.tracking = tracking;
	}

	public int getTracking() {
		return tracking;
	}

	public void setTradedCount(int tradedCount) {
		this.tradedCount = tradedCount;
	}

	public int getTradedCount() {
		return tradedCount;
	}

	public void setUnpaidCount(int unpaidCount) {
		this.unpaidCount = unpaidCount;
	}

	public int getUnpaidCount() {
		return unpaidCount;
	}

	public void setMyPicName(String myPicName) {
		this.myPicName = myPicName;
	}

	public String getMyPicName() {
		return myPicName;
	}

	public void setMyPicUrl(String myPicUrl) {
		this.myPicUrl = myPicUrl;
	}

	public String getMyPicUrl() {
		return myPicUrl;
	}

	public void setRtnCode(int rtnCode) {
		this.rtnCode = rtnCode;
	}

	public int getRtnCode() {
		return rtnCode;
	}

	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

	public String getRtnMsg() {
		return rtnMsg;
	}

	public void setItemno(int itemno) {
		this.itemno = itemno;
	}

	public int getItemno() {
		return itemno;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getOriginalPrice() {
		return originalPrice;
	}

	public void setItemPicName_1(String itemPicName_1) {
		this.itemPicName_1 = itemPicName_1;
	}

	public String getItemPicName_1() {
		return itemPicName_1;
	}

	public void setItemPicUrl_1(String itemPicUrl_1) {
		this.itemPicUrl_1 = itemPicUrl_1;
	}

	public String getItemPicUrl_1() {
		return itemPicUrl_1;
	}

	public void setTrackingCount(int trackingCount) {
		this.trackingCount = trackingCount;
	}

	public int getTrackingCount() {
		return trackingCount;
	}

	public void setIsTracked(int isTracked) {
		this.isTracked = isTracked;
	}

	public int getIsTracked() {
		return isTracked;
	}


}
