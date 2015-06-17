package com.teamnexters.kkaba.client.process;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.teamnexters.kkaba.client.user.UserDataCacheStore;
import com.teamnexters.kkaba.common.response.FriendDataResponsePacket;
import com.teamnexters.kkaba.common.response.KkabaResponseCode;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;

public class ProcessThread extends Thread{
	private Object waitingObject;
	public ProcessThread() {
		setDaemon(true);
		waitingObject = new Object();
		processList = new ConcurrentLinkedQueue<KkabaResponsePacket>();
		start();
	}
	private Queue<KkabaResponsePacket> processList;

	@Override
	public void run() {
		while (true) {
			while (!processList.isEmpty()) {
				KkabaResponsePacket process = processList.poll();
				switch (process.getResponseCode()) {
				case KkabaResponseCode.RESPONSE_FRIEND_INFO:
					UserDataCacheStore.getInstance().updateFriendData(((FriendDataResponsePacket)process).getFriendId(),((FriendDataResponsePacket)process).getFriendData());
					break;

				default:
					break;
				}
			}
			synchronized (waitingObject) {
				try {
					waitingObject.wait();
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public void addProcess(KkabaResponsePacket response) {
		processList.add(response);
		synchronized (waitingObject) {
			waitingObject.notify();
		}
	}

}
