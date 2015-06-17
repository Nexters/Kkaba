package com.teamnexters.kkaba.client.config;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ConfigAdapter extends BaseAdapter implements OnCheckedChangeListener{

	private Context context;
	private ConfigUtil configutile;

	public ConfigAdapter(Context context) {
		this.context = context;
		configutile = new ConfigUtil(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = new CheckBox(context);
			
		}
		((CheckBox)convertView).setOnCheckedChangeListener(this);
		((CheckBox)convertView).setChecked(configutile.getChecked());

		return convertView;
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		configutile.setConfig(isChecked);
	}
	

}
