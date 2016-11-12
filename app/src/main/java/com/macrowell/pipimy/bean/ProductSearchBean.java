package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class ProductSearchBean implements Serializable {
	private static final long serialVersionUID = 1L;

	String categoryId; // 主分類編號
	String subCategoryId; // 副分類編號
	String keyword; // 關鍵字
	String priceCap; // 價格上限
	String priceFloor; // 價格下限
	String orderBy; // 排序代碼(1:熱門度,2:最新上架,3:價低先,4:價高先)預設1
	String ts; // 驗證時間

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setPriceCap(String priceCap) {
		this.priceCap = priceCap;
	}

	public String getPriceCap() {
		return priceCap;
	}

	public void setPriceFloor(String priceFloor) {
		this.priceFloor = priceFloor;
	}

	public String getPriceFloor() {
		return priceFloor;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setts(String ts) {
		this.ts = ts;
	}

	public String getts() {
		return ts;
	}

}
