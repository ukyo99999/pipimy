package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class InboxBean implements Serializable {
    private static final long serialVersionUID = 1L;

    long privateMsgBoardId; // 私訊留言板編號
    int itemno; // 商品編號
    int buyerUid; // 買方會員編號
    String buyerPicUrl; // 買方顯示圖路徑
    int buyerPrice; // 買方最新出價紀錄(-1:從未出價)
    int statusCode; // 留言板狀態(0:待買方出價,1:待賣方接受,2:已接受出價,3:商品已出售)
    int ownerUid; // 賣家編號
    String ownerPicUrl; // 賣家顯示圖路徑
    String itemPicUrl_1; // 商品圖片路徑
    String itemPicName_1; // 商品圖片名稱
    String itemName; // 商品名稱
    int originalPrice; // 賣方商品定價

    String lastMsg; //最後一則留言內容
    int typeCode; //留言板類型(1:買東西,2:賣東西)

    String buyerName; //買家名稱
    String ownerName; //賣家名稱

    //	int uid; // 留言者會員編號
//	String msg; // Msg
    String crtime; // 張貼時間


    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setPrivateMsgBoardId(long privateMsgBoardId) {
        this.privateMsgBoardId = privateMsgBoardId;
    }

    public long getPrivateMsgBoardId() {
        return privateMsgBoardId;
    }

    public void setItemno(int itemno) {
        this.itemno = itemno;
    }

    public int getItemno() {
        return itemno;
    }

    public void setBuyerUid(int buyerUid) {
        this.buyerUid = buyerUid;
    }

    public int getBuyerUid() {
        return buyerUid;
    }

    public void setBuyerPicUrl(String buyerPicUrl) {
        this.buyerPicUrl = buyerPicUrl;
    }

    public String getBuyerPicUrl() {
        return buyerPicUrl;
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

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setBuyerPrice(int buyerPrice) {
        this.buyerPrice = buyerPrice;
    }

    public int getBuyerPrice() {
        return buyerPrice;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setOwnerUid(int ownerUid) {
        this.ownerUid = ownerUid;
    }

    public int getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerPicUrl(String ownerPicUrl) {
        this.ownerPicUrl = ownerPicUrl;
    }

    public String getOwnerPicUrl() {
        return ownerPicUrl;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

//	public void setUid(int uid) {
//		this.uid = uid;
//	}
//
//	public int getUid() {
//		return uid;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//
//	public String getMsg() {
//		return msg;
//	}

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setCrtime(String crtime) {
        this.crtime = crtime;
    }

    public String getCrtime() {
        return crtime;
    }

}
