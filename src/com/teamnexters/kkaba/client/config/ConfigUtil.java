package com.teamnexters.kkaba.client.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ConfigUtil {
	
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private boolean isChecked;
	
	public ConfigUtil(Context context) {
		pref = context.getSharedPreferences("kkaba_pref", Activity.MODE_PRIVATE);
		isChecked=pref.getBoolean("config", true);
	}
	
	public void setConfig(boolean check){
		isChecked = check;
		editor = pref.edit();
		editor.putBoolean("config", check);
		editor.commit();
	}
	public boolean getChecked(){
		return isChecked;
	}

}
