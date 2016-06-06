package com.example.zhbeijing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

	private SharedPreferences sp;
	private Editor edit;

	public SharedPreferencesUtil(Context context){
		sp = context.getSharedPreferences("config",context.MODE_PRIVATE);
		edit = sp.edit();
	}
	
	public boolean getBoolean(String key,boolean defValue){
		return sp.getBoolean(key, defValue);
	}
	
	public void putBoolean(String key,boolean value){
		edit.putBoolean(key, value).commit();
	}
	
	public String getString(String key,String defValue){
		return sp.getString(key, defValue);
	}
	
	public void putString(String key,String value){
		edit.putString(key, value).commit();
	}
	
	public int getInt(String key,int defValue){
		return sp.getInt(key, defValue);
	}
	
	public void putInt(String key,int value){
		edit.putInt(key, value).commit();
	}
}
