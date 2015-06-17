package com.teamnexters.kkaba.client.game;

import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.client.ui.UIHandlingActivity;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.request.RPSRequestPacket;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;

public abstract class RPSRequestingActivity extends UIHandlingActivity implements RPSRequester{
	public abstract void onHandleUI(KkabaResponsePacket response);


	@Override
	public void initializeRPS(int rps, String message) {
		lockUI();
		RPSRequestPacket request = new RPSRequestPacket();
		request.setRps(rps);
		request.setMessage(message);
		request.setRequestCode(KkabaRequestCode.REQUEST_GAME_INITIALIZE);
		HttpRequestThread.getInstance().addRequest(request);
		
	}



	@Override
	public void replyRPS(long gameId, int rps) {
		lockUI();
		RPSRequestPacket request = new RPSRequestPacket();
		request.setRps(rps);
		request.setGameId(gameId);
		request.setMessage("");
		request.setRequestCode(KkabaRequestCode.REQUEST_GAME_REPLY);
		HttpRequestThread.getInstance().addRequest(request);
		
	}



	@Override
	public void requestRPS(long gameId, int rps, String message) {
		lockUI();
		RPSRequestPacket request = new RPSRequestPacket();
		request.setRps(rps);
		request.setGameId(gameId);
		request.setMessage(message);
		request.setRequestCode(KkabaRequestCode.REQUEST_GAME_REQUEST);
		HttpRequestThread.getInstance().addRequest(request);
		
	}



	@Override
	public void requestDrawRPS(long gameId, int rps) {
		lockUI();
		RPSRequestPacket request = new RPSRequestPacket();
		request.setRps(rps);
		request.setGameId(gameId);
		request.setRequestCode(KkabaRequestCode.REQUEST_GAME_DRAW_REQUEST);
		HttpRequestThread.getInstance().addRequest(request);
		
	}
}
