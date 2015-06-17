package com.teamnexters.kkaba.client.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;

public abstract class UIHandlingActivity extends ActionBarActivity {
	public abstract void onHandleUI(KkabaResponsePacket response);

	private UILocker uiLocker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		uiLocker = new UILocker(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		HttpRequestThread.getInstance().setHandler(this);
	}

	protected void lockUI() {
		uiLocker.lock();
	}

	protected void unlockUI() {
		uiLocker.unlock();
	}
}
