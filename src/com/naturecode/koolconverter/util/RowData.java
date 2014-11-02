package com.naturecode.koolconverter.util;

public class RowData {
	private int mId;
	private String mTitle;
	private String mDetail;
	public RowData(int id, String title, String detail){
		mId=id;
		mTitle = title;
		mDetail=detail;
	}
	@Override
	public String toString() {
		return getmId()+" "+getmTitle()+" "+getmDetail();
	}
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmDetail() {
		return mDetail;
	}
	public void setmDetail(String mDetail) {
		this.mDetail = mDetail;
	}
}