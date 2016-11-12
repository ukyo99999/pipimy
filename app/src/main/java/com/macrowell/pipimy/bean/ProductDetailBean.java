package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class ProductDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int itemno; // 商品編號
	private String itemName; // 商品名稱
	private String itemDescription; // 商品描述
	private String itemPicUrl_1; // 商品圖片1路徑
	private String itemPicName_1; // 商品圖片1名稱
	private String itemPicUrl_2; // 商品圖片2路徑
	private String itemPicName_2; // 商品圖片2名稱
	private String itemPicUrl_3; // 商品圖片3路徑
	private String itemPicName_3; // 商品圖片3名稱
	private String itemPicUrl_4; // 商品圖片4路徑
	private String itemPicName_4; // 商品圖片4名稱
	private int originalPrice; // 商品價格
	private int categoryId; // 商品主分類編號
	private int subCategoryId; // 商品副分類編號
	private int ownerUid; // 賣家編號
	private String ownerName; // 賣家名稱
	private String ownerPicUrl; // 賣家顯示圖路徑
	private String crtime; // 張貼時間
	private int trackingCount; // 按讚數量
	private int isTracked; // 是否已追蹤(0:未追蹤, 1:已追蹤)
	private int statusCode; // 商品狀態碼(0:正常,1:交易完成)
	
	private long privateMsgBoardId; //私訊留言板編號，沒有會帶0
	private int bidCount; //出價中的私訊留言板數量(登入者是賣家時才使用)
	
	
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

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemPicUrl_1(String itemPicUrl_1) {
		this.itemPicUrl_1 = itemPicUrl_1;
	}

	public String getItemPicUrl_1() {
		return itemPicUrl_1;
	}

	public void setItemPicName_1(String itemPicName_1) {
		this.itemPicName_1 = itemPicName_1;
	}

	public String getItemPicName_1() {
		return itemPicName_1;
	}

	public void setItemPicUrl_2(String itemPicUrl_2) {
		this.itemPicUrl_2 = itemPicUrl_2;
	}

	public String getItemPicUrl_2() {
		return itemPicUrl_2;
	}

	public void setItemPicName_2(String itemPicName_2) {
		this.itemPicName_2 = itemPicName_2;
	}

	public String getItemPicName_2() {
		return itemPicName_2;
	}

	public void setItemPicUrl_3(String itemPicUrl_3) {
		this.itemPicUrl_3 = itemPicUrl_3;
	}

	public String getItemPicUrl_3() {
		return itemPicUrl_3;
	}

	public void setItemPicName_3(String itemPicName_3) {
		this.itemPicName_3 = itemPicName_3;
	}

	public String getItemPicName_3() {
		return itemPicName_3;
	}

	public void setItemPicUrl_4(String itemPicUrl_4) {
		this.itemPicUrl_4 = itemPicUrl_4;
	}

	public String getItemPicUrl_4() {
		return itemPicUrl_4;
	}

	public void setItemPicName_4(String itemPicName_4) {
		this.itemPicName_4 = itemPicName_4;
	}

	public String getItemPicName_4() {
		return itemPicName_4;
	}

	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getOriginalPrice() {
		return originalPrice;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setSubCategoryId(int subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public int getSubCategoryId() {
		return subCategoryId;
	}

	public void setOwnerUid(int ownerUid) {
		this.ownerUid = ownerUid;
	}

	public int getOwnerUid() {
		return ownerUid;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerPicUrl(String ownerPicUrl) {
		this.ownerPicUrl = ownerPicUrl;
	}

	public String getOwnerPicUrl() {
		return ownerPicUrl;
	}

	public void setCrtime(String crtime) {
		this.crtime = crtime;
	}

	public String getCrtime() {
		return crtime;
	}

	public void setIsTracked(int isTracked) {
		this.isTracked = isTracked;
	}

	public int getIsTracked() {
		return isTracked;
	}

	public void setTrackingCount(int trackingCount) {
		this.trackingCount = trackingCount;
	}

	public int getTrackingCount() {
		return trackingCount;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setPrivateMsgBoardId(long privateMsgBoardId) {
		this.privateMsgBoardId = privateMsgBoardId;
	}

	public long getPrivateMsgBoardId() {
		return privateMsgBoardId;
	}

	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}

	public int getBidCount() {
		return bidCount;
	}

}
