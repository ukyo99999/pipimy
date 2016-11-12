package com.macrowell.pipimy.bean;

import java.io.Serializable;

public class SubCategoriesBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	String subCategoryId; //副分類編號
	String subCategoryName; //副分類名稱
	String categoryId; //所屬的主分類編號
	String bidItemCount; //上架商品數量
	
	public void setCategoryId(String categoryId){
		this.subCategoryId = categoryId; 
	}
	
	public String getCategoryId(){
		return subCategoryId;
	}
	
	public void setCategoryName(String categoryName){
		this.subCategoryName = categoryName;
	}
	
	public String getCategoryName(){
		return subCategoryName;
	}
	
	public void setBidItemCount(String bidItemCount){
		this.bidItemCount = bidItemCount;
	}
	
	public String getBidItemCount(){
		return bidItemCount;
	}

}
