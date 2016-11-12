package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class ProductListBean implements Serializable {
	private static final long serialVersionUID = 1L;

	int itemno; // 商品編號
	String itemName; // 商品名稱
	String itemPicUrl_1; // 商品圖片路徑
	String itemPicName_1; // 商品圖片名稱
	String originalPrice; // 商品價格
	String ownerUid; // 賣家編號
	String ownerName; // 賣家名稱
	String ownerPicUrl; // 賣家顯示圖路徑
	String crtimeTs; // 張貼時間(UnixTimeStamp格式)
	int isTracked; // 是否已按讚(0:未按讚, 1:已按讚)
	int trackingCount; // 按讚數量
	String statusCode; // 商品狀態碼(0:正常,1:交易完成)

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

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOwnerUid(String ownerUid) {
		this.ownerUid = ownerUid;
	}

	public String getOwnerUid() {
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

	public void setCrtimeTs(String crtimeTs) {
		this.crtimeTs = crtimeTs;
	}

	public String getCrtimeTs() {
		return crtimeTs;
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

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

}
