package com.demo.beans;

import java.sql.Date;

public class Borrower{
	private int userid;
	private int assetid;
	private Date dateofissue;
	private Date dateofreturn;
	private boolean status;
	private String category;
	public Borrower(int userid, int assetid, Date dateofissue, Date dateofreturn, boolean status,String category) {
		super();
		this.userid = userid;
		this.assetid = assetid;
		this.dateofissue = dateofissue;
		this.dateofreturn = dateofreturn;
		this.status = status;
		this.category=category;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getAssetid() {
		return assetid;
	}
	public void setAssetid(int assetid) {
		this.assetid = assetid;
	}
	public Date getDateofissue() {
		return dateofissue;
	}
	public void setDateofissue(Date dateofissue) {
		this.dateofissue = dateofissue;
	}
	public Date getDateofreturn() {
		return dateofreturn;
	}
	public void setDateofreturn(Date dateofreturn) {
		this.dateofreturn = dateofreturn;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Borrower [userid=" + userid + ", assetid=" + assetid + ", dateofissue=" + dateofissue
				+ ", dateofreturn=" + dateofreturn + ", status=" + status + ", category=" + category + "]";
	}
		
}