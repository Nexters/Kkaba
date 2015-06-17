package com.teamnexters.kkaba.client.network;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Message;
import android.util.Log;

import com.teamnexters.kkaba.client.process.ProcessThread;
import com.teamnexters.kkaba.client.ui.UIHandlingActivity;
import com.teamnexters.kkaba.client.ui.UiHandler;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.request.KkabaRequestPacket;
import com.teamnexters.kkaba.common.request.RequestUriFactory;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;
import com.teamnexters.kkaba.common.response.ResponseJsonParser;

public class HttpRequestThread extends Thread implements KkabaRequestCode {

	private ProcessThread processThread;
	private Object waitingObject;
	private static HttpRequestThread instance = new HttpRequestThread();
	private UiHandler handler;
	private ClientManager clientManager;
	ConcurrentLinkedQueue<KkabaRequestPacket> requestQueue;

	static {
		instance.start();
	}

	public void addRequest(KkabaRequestPacket request) {
		requestQueue.add(request);
		synchronized (waitingObject) {
			waitingObject.notify();
		}
	}

	private HttpRequestThread() {
		setDaemon(true);
		processThread = new ProcessThread();
		waitingObject = new Object();
		clientManager = new ClientManager();
		requestQueue = new ConcurrentLinkedQueue<KkabaRequestPacket>();
	}

	public synchronized void setHandler(UIHandlingActivity activity) {
		handler = new UiHandler(activity);
	}

	public static HttpRequestThread getInstance() {
		return instance;
	}

	
	public void runOnUi(Runnable action){
		handler.runAction(action);
	}
	@Override
	public void run() {
		while (true) {
			while (!requestQueue.isEmpty()) {
				KkabaRequestPacket request = requestQueue.poll();
				String URL = NetworkConfiguration.getHost()
						+ RequestUriFactory.requestUri(request.getRequestCode());
				HttpPost httpPost = new HttpPost(URL);
				HttpClient client = clientManager.getHttpClient();
				HttpResponse httpResponse;
				try {
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							request.toHttpParams(), HTTP.UTF_8);
					httpPost.setEntity(entity);
					httpResponse = client.execute(httpPost);
					String str = EntityUtils.toString(httpResponse.getEntity(),
							HTTP.UTF_8);
					KkabaResponsePacket response = ResponseJsonParser.getInstance().parse(str);
					switch (response.getResponseType()) {
					case KkabaResponsePacket.RESPONSE_TYPE_PROCESS:
						processThread.addProcess(response);
						break;
					case KkabaResponsePacket.RESPONSE_TYPE_UI:
						synchronized (this) {
							if (handler != null) {
								Message msg = handler.obtainMessage();
								msg.obj = ResponseJsonParser.getInstance().parse(str);
								handler.sendMessage(msg);
							}
						}
						break;
					}

				} catch (Exception e) {
					Log.e("Response Error", "Response Error", e);
				}
			}
			synchronized (waitingObject) {
				try {
					waitingObject.wait();
				} catch (InterruptedException e) {}
			}
		}
	}
}
