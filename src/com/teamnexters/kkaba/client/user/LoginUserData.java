package com.teamnexters.kkaba.client.user;

import com.teamnexters.kkaba.common.data.LoginData;

public class LoginUserData {
	private static LoginData data;

	public static LoginData get() {
		return data;
	}

	public static void set(LoginData data) {
		LoginUserData.data = data;
	}
	
	
}
