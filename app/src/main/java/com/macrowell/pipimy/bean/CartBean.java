package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class CartBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int groupNo; // 臨時的分組編號
    private int itemno; // 商品編號
    private String itemName; // 商品名稱
    private String itemPicUrl; // 商品圖片路徑
    private int buyerPrice; // 議價後決定的價格
    private int ownerUid; // 賣家會員編號
    private String ownerName; // 賣家會員名稱
    private String ownerPicUrl; // 賣家會員顯示圖路徑

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public int getGroupNo() {
        return groupNo;
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

    public void setItemPicUrl(String itemPicUrl) {
        this.itemPicUrl = itemPicUrl;
    }

    public String getItemPicUrl() {
        return itemPicUrl;
    }

    public void setBuyerPrice(int buyerPrice) {
        this.buyerPrice = buyerPrice;
    }

    public int getBuyerPrice() {
        return buyerPrice;
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
}
