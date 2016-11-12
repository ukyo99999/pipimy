package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class BillBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int itemno; // 商品編號
    private String itemName; // 商品名稱
    private String itemPicUrl; // 商品圖片路徑
    private int buyerPrice; // 議價後決定的價格

    private String infoUsername;
    private String infoMobile;
    private String infoCity;
    private String infoTown;
    private String infoAddress;

    private String contactUsername;
    private String contactMobile;
    private String contactCity;
    private String contactTown;
    private String contactAddress;

    private String billingTypeCode;
    private String billingTypeSubTypeCode;
    private String billingTypeName;
    private String billingTypeShoreName;

    private String logisticsTypeCode;
    private String logisticsTypeSubTypeCode;
    private String logisticsTypeName;
    private String logisticsTypeShoreName;
    private int logisticsTypeCharge;

    private int buyerUid; //買家會員編號
    private int ownerUid; // 賣家會員編號
    private String productPicUrl; // 商品縮圖路徑
    private int orderNo; //訂單編號
    private int amount; //購物金額總計
    private int transportNo; //物流單號


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

    public void setInfoUsername(String infoUsername) {
        this.infoUsername = infoUsername;
    }

    public String getInfoUsername() {
        return infoUsername;
    }

    public void setInfoMobile(String infoMobile) {
        this.infoMobile = infoMobile;
    }

    public String getInfoMobile() {
        return infoMobile;
    }

    public void setInfoCity(String infoCity) {
        this.infoCity = infoCity;
    }

    public String getInfoCity() {
        return infoCity;
    }

    public void setInfoTown(String infoTown) {
        this.infoTown = infoTown;
    }

    public String getInfoTown() {
        return infoTown;
    }

    public void setInfoAddress(String infoAddress) {
        this.infoAddress = infoAddress;
    }

    public String getInfoAddress() {
        return infoAddress;
    }

    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }

    public String getContactUsername() {
        return contactUsername;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactCity(String contactCity) {
        this.contactCity = contactCity;
    }

    public String getContactCity() {
        return contactCity;
    }

    public void setContactTown(String contactTown) {
        this.contactTown = contactTown;
    }

    public String getContactTown() {
        return contactTown;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setBillingTypeCode(String billingTypeCode) {
        this.billingTypeCode = billingTypeCode;
    }

    public String getBillingTypeCode() {
        return billingTypeCode;
    }

    public void setBillingTypeSubTypeCode(String billingTypeSubTypeCode) {
        this.billingTypeSubTypeCode = billingTypeSubTypeCode;
    }

    public String getBillingTypeSubTypeCode() {
        return billingTypeSubTypeCode;
    }

    public void setBillingTypeName(String billingTypeName) {
        this.billingTypeName = billingTypeName;
    }

    public String getBillingTypeName() {
        return billingTypeName;
    }

    public void setBillingTypeShoreName(String billingTypeShoreName) {
        this.billingTypeShoreName = billingTypeShoreName;
    }

    public String getBillingTypeShoreName() {
        return billingTypeShoreName;
    }

    public void setLogisticsTypeCode(String logisticsTypeCode) {
        this.logisticsTypeCode = logisticsTypeCode;
    }

    public String getLogisticsTypeCode() {
        return logisticsTypeCode;
    }

    public void setLogisticsTypeSubTypeCode(String logisticsTypeSubTypeCode) {
        this.logisticsTypeSubTypeCode = logisticsTypeSubTypeCode;
    }

    public String getLogisticsTypeSubTypeCode() {
        return logisticsTypeSubTypeCode;
    }

    public void setLogisticsTypeName(String logisticsTypeName) {
        this.logisticsTypeName = logisticsTypeName;
    }

    public String getLogisticsTypeName() {
        return logisticsTypeName;
    }

    public void setLogisticsTypeShoreName(String logisticsTypeShoreName) {
        this.logisticsTypeShoreName = logisticsTypeShoreName;
    }

    public String getLogisticsTypeShoreName() {
        return logisticsTypeShoreName;
    }

    public void setLogisticsTypeCharge(int logisticsTypeCharge) {
        this.logisticsTypeCharge = logisticsTypeCharge;
    }

    public int getLogisticsTypeCharge() {
        return logisticsTypeCharge;
    }

    public void setBuyerUid(int buyerUid) {
        this.buyerUid = buyerUid;
    }

    public int getBuyerUid() {
        return buyerUid;
    }

    public void setOwnerUid(int ownerUid) {
        this.ownerUid = ownerUid;
    }

    public int getOwnerUid() {
        return ownerUid;
    }


    public void setProductPicUrl(String productPicUrl) {
        this.productPicUrl = productPicUrl;
    }

    public String getProductPicUrl() {
        return productPicUrl;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setTransportNo(int transportNo){
        this.transportNo = transportNo;
    }
    public int getTransportNo(){
        return transportNo;
    }
}
