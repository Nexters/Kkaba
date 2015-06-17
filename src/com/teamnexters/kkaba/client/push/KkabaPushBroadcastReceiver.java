package com.teamnexters.kkaba.client.push;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

public class KkabaPushBroadcastReceiver extends ParsePushBroadcastReceiver{

	@Override
	protected void onPushOpen(Context context, Intent intent) {
		super.onPushOpen(context, intent);
	}
}
