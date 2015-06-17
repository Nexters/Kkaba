package com.teamnexters.kkaba.client.game;

public interface RPSRequester {
	public void initializeRPS(int rps, String message);
	public void replyRPS(long gameId, int rps);
	public void requestRPS(long gameId, int rps, String message);
	public void requestDrawRPS(long gameId, int rps);
}
