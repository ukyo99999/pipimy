package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class CategoriesBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int categoryId; // 主分類編號
	private String categoryName; // 主分類名稱
	private int bidItemCount; // 上架商品數量
	private String picUrl; // 主分類圖片

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setBidItemCount(int bidItemCount) {
		this.bidItemCount = bidItemCount;
	}

	public int getBidItemCount() {
		return bidItemCount;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicUrl() {
		return picUrl;
	}

}
