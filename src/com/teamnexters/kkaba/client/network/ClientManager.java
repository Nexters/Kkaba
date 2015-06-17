package com.teamnexters.kkaba.client.network;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class ClientManager {
	static private Object mLock = new Object();
	static private CookieStore mCookie = null;
	public HttpClient getHttpClient() {
		
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        synchronized (mLock) {
            if (mCookie == null) {
                mCookie = httpClient.getCookieStore();
            } else {
                httpClient.setCookieStore(mCookie);
            }
        }
	    return httpClient;
	}
}
