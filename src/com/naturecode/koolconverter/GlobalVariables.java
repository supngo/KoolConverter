package com.naturecode.koolconverter;

import android.app.Application;

public class GlobalVariables extends Application {
    private String currency_from;
    private String currency_to;
    private String isCurrency_from_to;
    private String currency_from_text;
    private int screen_width;
    private int screen_height;
    
    @Override
    public void onCreate() {
//    	reinitialize variable
    }
    
	public int getScreen_width() {
		return screen_width;
	}

	public void setScreen_width(int screen_width) {
		this.screen_width = screen_width;
	}

	public int getScreen_height() {
		return screen_height;
	}

	public void setScreen_height(int screen_height) {
		this.screen_height = screen_height;
	}

	public String getCurrency_from_text() {
		return currency_from_text;
	}

	public void setCurrency_from_text(String currency_from_text) {
		this.currency_from_text = currency_from_text;
	}

	public String getIsCurrency_from_to() {
		return isCurrency_from_to;
	}
	public void setIsCurrency_from_to(String isCurrency_from_to) {
		this.isCurrency_from_to = isCurrency_from_to;
	}
	public String getCurrency_from() {
		return currency_from;
	}
	public void setCurrency_from(String currency_from) {
		this.currency_from = currency_from;
	}
	public String getCurrency_to() {
		return currency_to;
	}
	public void setCurrency_to(String currency_to) {
		this.currency_to = currency_to;
	}
}