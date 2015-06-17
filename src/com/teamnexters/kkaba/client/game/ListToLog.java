package com.teamnexters.kkaba.client.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.common.data.GameListData;
import com.teamnexters.kkaba.common.data.GameResultLog;
import com.teamnexters.kkaba.common.game.GameResultCode;
import com.teamnexters.kkaba.common.game.RPSUtil;

public class ListToLog {
	
	private static ListToLog instance;
	int turn;
	boolean first = true;
	GameResultLog gameResultLog;
	List<GameResultLog> tempGameResultLogs = new ArrayList<GameResultLog>();
	List<GameListData> listGameListDatas = new ArrayList<GameListData>();
	List<GameListData> resultListGameListDatas = new ArrayList<GameListData>();
	int position = -1;
	
	public static ListToLog getInstance(){
		if(instance == null){
			instance = new ListToLog();
		}
		return instance;
	}

	public void setList(List<GameListData> listGameListDatas) {
		this.listGameListDatas = listGameListDatas;
	}
	
	public void clear(){
		first = true;
		position = -1;
		tempGameResultLogs.clear();
		listGameListDatas.clear();
		resultListGameListDatas.clear();
	}

	public List<GameListData> convert() {
		for (GameListData currentGameListData : listGameListDatas) {
			position++;
			if (first) {
				first = false;
			} else {
				currentGameListData.setTurn(turn);
			}

			gameResultLog = new GameResultLog();
			gameResultLog.setMyRps(currentGameListData.getMyRps());
			gameResultLog.setFoeRps(currentGameListData.getFoeRps());
//			if (currentGameListData.getMyRps() != 0) {
				tempGameResultLogs.add(gameResultLog);
//			}

			if (position == listGameListDatas.size() - 1) {
				List<GameResultLog> log = new ArrayList<GameResultLog>();
				log.addAll(tempGameResultLogs);
				currentGameListData.setLog(log);
				resultListGameListDatas.add(currentGameListData);
				tempGameResultLogs.clear();
				break;
			} else {

				switch (RPSUtil.getRPSResult(currentGameListData.getMyRps(),
						currentGameListData.getFoeRps())) {
				case GameResultCode.WIN:
					break;
				case GameResultCode.DRAW:
					resultListGameListDatas.add(currentGameListData);
					continue;
				case GameResultCode.LOSE:
					break;
				}
				List<GameResultLog> log = new ArrayList<GameResultLog>();
				log.addAll(tempGameResultLogs);

				switch (RPSUtil.getRPSResult(gameResultLog.getMyRps(),
						gameResultLog.getFoeRps())) {
				case GameResultCode.WIN:
					turn = 1;
					break;
				case GameResultCode.DRAW:
					break;
				case GameResultCode.LOSE:
					turn = -1;
					break;
				}

				currentGameListData.setLog(log);
				resultListGameListDatas.add(currentGameListData);
				tempGameResultLogs.clear();
			}
		}
		return resultListGameListDatas;

	}

}
