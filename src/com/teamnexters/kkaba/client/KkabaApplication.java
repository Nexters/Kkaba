package com.teamnexters.kkaba.client;

import com.parse.Parse;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.PushService;

import android.app.Application;

public class KkabaApplication extends Application {
	@Override
	public void onCreate() {
		Parse.initialize(getApplicationContext(),
				"RYKjhMzy2SwwkbMs2Ts6wrDAVZKswPm7ZCwZ9HjT",
				"DK89PkE1rOrk8HxJDP0MiXfjFd6VTrwgcnqI4GzV");

		ParsePushBroadcastReceiver ppbr = new ParsePushBroadcastReceiver();
	}
}
