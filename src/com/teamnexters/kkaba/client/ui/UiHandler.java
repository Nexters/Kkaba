package com.teamnexters.kkaba.client.ui;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;

import com.teamnexters.kkaba.common.response.KkabaResponsePacket;


public class UiHandler extends Handler {
	final WeakReference<UIHandlingActivity> hActivity;

	public UiHandler(UIHandlingActivity activity) {
		this.hActivity = new WeakReference<UIHandlingActivity>(activity);
	}

	@Override
	public void handleMessage(android.os.Message msg) {
		KkabaResponsePacket response = (KkabaResponsePacket) msg.obj;
		UIHandlingActivity realActivity = hActivity.get();
		if (realActivity != null) {
			realActivity.onHandleUI(response);
		}
	};
	
	public void runAction(Runnable action) {
		Activity realActivity = hActivity.get();
		if (realActivity != null) {
			realActivity.runOnUiThread(action);
		}
	}
}
