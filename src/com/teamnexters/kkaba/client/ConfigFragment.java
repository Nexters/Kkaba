package com.teamnexters.kkaba.client;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.teamnexters.kkaba.client.config.ConfigUtil;
import com.teamnexters.kkaba.client.sound.SoundManager;
import com.teamnexters.kkaba.client.ui.UIHandlingActivity;

public class ConfigFragment extends Fragment implements OnClickListener ,OnCheckedChangeListener{

	private SoundManager soundmanager;
	private View logoutView, withdrawView;
	private CheckBox pushCheckbox;
	private ConfigUtil configUtil;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		configUtil = new ConfigUtil(getActivity());
		final SoundManager soundmanager = new SoundManager(getActivity());
		View rootView = inflater.inflate(R.layout.fragment_three, container,
				false);
		logoutView = rootView.findViewById(R.id.settingLogout);
		logoutView.setOnClickListener(this);
		withdrawView = rootView.findViewById(R.id.settingWithdraw);
		withdrawView.setOnClickListener(this);
		pushCheckbox = (CheckBox) rootView.findViewById(R.id.configPush);
		pushCheckbox.setChecked(configUtil.getChecked());
		pushCheckbox.setOnCheckedChangeListener(this);
		super.onCreate(savedInstanceState);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settingLogout:
			((MainActivity)getActivity()).requestWithdraw();
			break;

		case R.id.settingWithdraw:
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("È¸¿øÅ»Åð");
			builder.setMessage("Á¤¸»·Î Å»ÅðÇÏ½Ã°Ú½À´Ï±î?");
			builder.setPositiveButton("Å»Åð", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((MainActivity)getActivity()).requestWithdraw();
				}
			});
			builder.setNegativeButton("Ãë¼Ò", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			builder.create().show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.configPush:
			((MainActivity)getActivity()).requestPushId(isChecked);
			configUtil.setConfig(isChecked);
			break;
		}
	}

}
