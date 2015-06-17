package com.teamnexters.kkaba.client.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.client.image.CollectionImageManager;
import com.teamnexters.kkaba.client.info.InfoActivity;
import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.client.user.LoginUserData;
import com.teamnexters.kkaba.client.user.UserDataCacheStore;
import com.teamnexters.kkaba.common.data.FoeListData;
import com.teamnexters.kkaba.common.data.GameData;
import com.teamnexters.kkaba.common.data.GameListData;
import com.teamnexters.kkaba.common.data.GameResultData;
import com.teamnexters.kkaba.common.data.SimpleUserData;
import com.teamnexters.kkaba.common.game.GameResultCode;
import com.teamnexters.kkaba.common.game.GameState;
import com.teamnexters.kkaba.common.game.RPSUtil;
import com.teamnexters.kkaba.common.request.InfoRequestPacket;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.request.LongRequestPacket;
import com.teamnexters.kkaba.common.response.GameCheckResponsePacket;
import com.teamnexters.kkaba.common.response.GameResultResponsePacket;
import com.teamnexters.kkaba.common.response.InfoResponsePacket;
import com.teamnexters.kkaba.common.response.KkabaResponseCode;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;
import com.teamnexters.kkaba.db.AccessFoeListDB;
import com.teamnexters.kkaba.db.AccessGameListDB;

public class GameActivity extends RPSRequestingActivity implements
		OnClickListener {
	private FoeListData foeData;
	private List<GameListData> listGameListData = new ArrayList<GameListData>();
	ListView gameListView;
	List<GameListData> resultListGameListData = new ArrayList<GameListData>();
	GameListAdapter gameListAdapter;
	private boolean isMyInfoRequested;
	String foeNick;
	String myNick;
	int myStage;
	int foeStage;
	static int drawRPS;
	static String message;
	
	public static void setMessage(String msg){
		message = msg;
	}

	public static void setDrawRPS(int rps) {
		drawRPS = rps;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);


		foeData = (FoeListData) getIntent().getSerializableExtra("game");
		TextView topFoeNick = (TextView) findViewById(R.id.text_game_top_foeNick);
		ImageView foeBadge = (ImageView) findViewById(R.id.image_game_foeBadge);
		SimpleUserData currentFoeData = UserDataCacheStore.getInstance()
				.fillView(foeData.getUserId(),topFoeNick);
		currentFoeData = UserDataCacheStore.getInstance()
				.fillView(foeData.getUserId(),foeBadge);
		foeNick = currentFoeData.getNickname();
		myNick = LoginUserData.get().getNickname();
		topFoeNick.setText(foeNick);
		topFoeNick.setOnClickListener(this);
		TextView topMyNick = (TextView) findViewById(R.id.text_game_top_myNick);
		topMyNick.setText(myNick);
		topMyNick.setOnClickListener(this);
		foeBadge.setImageResource(CollectionImageManager.getInstance()
				.getCollectionImageId(currentFoeData.getCollection()));
		foeBadge.setOnClickListener(this);
		ImageView myBadge = (ImageView) findViewById(R.id.image_game_myBadge);
		myBadge.setImageResource(CollectionImageManager.getInstance()
				.getCollectionImageId(
						LoginUserData.get().getCurrentCollection()));
		myBadge.setOnClickListener(this);
		requestCheck();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onHandleUI(KkabaResponsePacket response) {

		int state;
		boolean myGame = true;
		if (foeData.getInitPlayer() == foeData.getUserId()) {
			myGame = false;
		}

		unlockUI();

//		Log.d("bgbg", AccessGameListDB.getInstance().getLatestData().toString());

		switch (response.getResponseCode()) {
		case KkabaResponseCode.RESPONSE_GAME_CHECK:
			listGameListData.clear();
			GameCheckResponsePacket checkResponsePacket = (GameCheckResponsePacket) response;
			GameData responseGameData = checkResponsePacket.getGameData();
			state = responseGameData.getStatus();
			foeData.setStatus(state);
			foeData.setSelfId(LoginUserData.get().getId().toString());
			AccessFoeListDB.getInstance().updateOrInsertFoeList(foeData);
			myStage = responseGameData.getMyStage();
			foeStage = responseGameData.getFoeStage();
			gameListView = (ListView) findViewById(R.id.listView_game);
			listGameListData = AccessGameListDB.getInstance().getAllGameList(
					foeData.getGameId());

			if (listGameListData.size() == 0) {

				GameListData initData = ((GameCheckResponsePacket) response)
						.getGameData().toGameListData();
				if (checkResponsePacket.getGameResultDataList().size() != 0) {
					for (GameResultData data : checkResponsePacket
							.getGameResultDataList()) {
						GameListData listData = new GameListData();
						listData = data.convertToGameListData();
						listData.setGameId(foeData.getGameId());
						listData.setLastTime(responseGameData.getFinalTime());
						listData.setFoeStage(responseGameData.getFoeStage());
						listData.setMessage(responseGameData.getGameLog());
						listData.setMyStage(responseGameData.getMyStage());
						listGameListData.add(listData);
						AccessGameListDB.getInstance().addGameList(listData);
					}
				} else {
					initData.setGameId(foeData.getGameId());
					resultListGameListData = new ArrayList<GameListData>();
					resultListGameListData.add(initData);
					if (foeData.getInitPlayer() == foeData.getUserId()) {
						resultListGameListData.get(0).setTurn(-1);

					} else {
						resultListGameListData.get(0).setTurn(1);
					}
					ListToLog.getInstance().clear();

					ListToLog.getInstance().setList(resultListGameListData);
					resultListGameListData = ListToLog.getInstance().convert();
				}
			}

			else {
				if (checkResponsePacket.getGameResultDataList() != null) {
					for (GameResultData data : checkResponsePacket
							.getGameResultDataList()) {
						GameListData listData = new GameListData();
						listData = data.convertToGameListData();
						listData.setGameId(foeData.getGameId());
						listData.setFoeRps(data.getFoeRps());
						listData.setLastTime(responseGameData.getFinalTime());
						listData.setFoeStage(responseGameData.getFoeStage());
						listData.setMessage(responseGameData.getGameLog());
						listData.setMyStage(responseGameData.getMyStage());
						listData.setGameIndex(AccessGameListDB.getInstance().getLatestData().getGameIndex());
						AccessGameListDB.getInstance().updateLatestData(listData);
						listGameListData = AccessGameListDB.getInstance().getAllGameList(foeData.getGameId());
					}
				}
				if (listGameListData.get(listGameListData.size() - 1)
						.getFoeRps() == 0) {
					if (state == GameState.WATING_PLAYER1_DRAW_REQUEST
							|| state == GameState.WATING_PLAYER2_DRAW_REQUEST) {
						updateDrawData(responseGameData);
						listGameListData.get(listGameListData.size()-1).setFoeRps(AccessGameListDB.getInstance().getLatestData().getFoeRps());
					}
				}

				Log.d("bgbg", listGameListData.size() + "!ASDSD");
				if (foeData.getInitPlayer() == foeData.getUserId()) {
					listGameListData.get(0).setTurn(-1);
				} else {
					listGameListData.get(0).setTurn(1);
				}
				
				switch (state) {
				case GameState.PLAYER1_REQUESTED:
					if (!myGame) {
						GameListData nData = new GameListData();
						nData = responseGameData.toGameListData();
						nData.setMyRps(0);
						nData.setFoeRps(0);
						listGameListData.add(nData);
					}
					break;
				case GameState.PLAYER2_REQUESTED:
					if (myGame) {
						GameListData nData = new GameListData();
						nData = responseGameData.toGameListData();
						nData.setMyRps(0);
						nData.setFoeRps(0);
						listGameListData.add(nData);
					} 
					break;
				}

				ListToLog.getInstance().clear();
				ListToLog.getInstance().setList(listGameListData);
				resultListGameListData = ListToLog.getInstance().convert();

			}

			gameListAdapter = new GameListAdapter(getApplication(),
					resultListGameListData, this);

			switch (state) {
			case GameState.PLAYER1_REQUESTED:
				if (myGame) {
					gameListAdapter.setStatus(GameListState.WAIT);
				} else {
					gameListAdapter.setStatus(GameListState.REPLY);
				}
				break;
			case GameState.PLAYER2_REQUESTED:
				if (myGame) {
					gameListAdapter.setStatus(GameListState.REPLY);
				} else {
					gameListAdapter.setStatus(GameListState.WAIT);
				}
				break;
			case GameState.PROCESSING:
				gameListAdapter.setStatus(GameListState.WAIT);
				break;
			case GameState.WATING_PLAYER1_DRAW_REQUEST:
				if (myGame) {
					gameListAdapter.setStatus(GameListState.DRAW_REQUEST);
				} else {
					gameListAdapter.setStatus(GameListState.WAIT);
				}
				break;
			case GameState.WATING_PLAYER1_REQUEST:
				if (myGame) {
					gameListAdapter.setStatus(GameListState.REQUEST);
				} else {
					gameListAdapter.setStatus(GameListState.WAIT);
				}
				break;
			case GameState.WATING_PLAYER2_DRAW_REQUEST:
				if (myGame) {
					gameListAdapter.setStatus(GameListState.WAIT);
				} else {
					gameListAdapter.setStatus(GameListState.DRAW_REQUEST);
				}
				break;
			case GameState.WATING_PLAYER2_REQUEST:
				if (myGame) {
					gameListAdapter.setStatus(GameListState.WAIT);
				} else {
					gameListAdapter.setStatus(GameListState.REQUEST);
				}
				break;
			default:
				break;
			}
			
			gameListView.setAdapter(gameListAdapter);
			gameListAdapter.setGameId(foeData.getGameId());
			gameListAdapter.setNick(foeNick, myNick);
			gameListView.setSelectionFromTop(20000, 0);
			gameListAdapter.notifyDataSetInvalidated();
			break;
		case KkabaResponseCode.RESPONSE_GAME_REQUESTED:
			GameListData qData = new GameListData();
			qData.setMyRps(drawRPS);
			qData.setMessage(message);
			qData.setGameId(foeData.getGameId());
			qData.setMyStage(myStage);
			qData.setFoeStage(foeStage);
			AccessGameListDB.getInstance().addGameList(qData);
			foeData.setFinalTime(System.currentTimeMillis());
			foeData.setGameLog(message);
			foeData.setSelfId(LoginUserData.get().getId().toString());
			AccessFoeListDB.getInstance().updateOrInsertFoeList(foeData);
			requestCheck();
			break;
		case KkabaResponseCode.RESPONSE_GAME_RESULT:
			GameResultData resultData = ((GameResultResponsePacket) response)
					.getGameResultData();
			GameListData rData = resultData.convertToGameListData();
			int res = RPSUtil.getRPSResult(rData.getMyRps(), rData.getFoeRps());
			switch (res) {
			case GameResultCode.WIN:
				new ResultPopupDialog(GameActivity.this, res).show();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				break;
			case GameResultCode.DRAW:
				new ResultPopupDialog(GameActivity.this, res).show();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				break;
			case GameResultCode.LOSE:
				new ResultPopupDialog(GameActivity.this, res).show();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				break;
			}
			rData.setGameId(foeData.getGameId());
			rData.setFoeStage(foeStage);
			rData.setMessage(foeData.getGameLog());
			rData.setMyStage(myStage);
			rData.setFirstTime(foeData.getFinalTime());
			AccessGameListDB.getInstance().addGameList(rData);
			foeData.setFinalTime(System.currentTimeMillis());
			foeData.setGameLog(message);
			foeData.setSelfId(LoginUserData.get().getId().toString());
			AccessFoeListDB.getInstance().updateOrInsertFoeList(foeData);
			requestCheck();
			break;
		case KkabaResponseCode.RESPONSE_GAME_FACEBOOK_INFO:
			Intent i = new Intent(this, InfoActivity.class);
			if (isMyInfoRequested) {
				i.putExtra("name", LoginUserData.get().getNickname());
				i.putExtra("collection", LoginUserData.get()
						.getCurrentCollection());
			} else {
				i.putExtra("name", UserDataCacheStore.getInstance()
						.getFriendData(foeData.getUserId()).getNickname());
				i.putExtra("collection", UserDataCacheStore.getInstance()
						.getFriendData(foeData.getUserId()).getCollection());
			}
			i.putExtra("info_list",
					(Serializable) ((InfoResponsePacket) response)
							.getInfoList());
			unlockUI();
			startActivity(i);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		lockUI();
		InfoRequestPacket request = new InfoRequestPacket();
		request.setGameId(foeData.getGameId());
		switch (v.getId()) {
		case R.id.text_game_top_foeNick:
		case R.id.image_game_foeBadge:
			isMyInfoRequested = false;
			break;
		case R.id.text_game_myNick:
		case R.id.image_game_myBadge:
			isMyInfoRequested = true;
			break;
		}
		request.setIsMine(isMyInfoRequested);
		request.setRequestCode(KkabaRequestCode.REQUEST_GAME_FACEBOOK_INFO);
		HttpRequestThread.getInstance().addRequest(request);
	}

	public void requestCheck() {
		LongRequestPacket request = new LongRequestPacket();
		request.setRequestCode(KkabaRequestCode.REQUEST_GAME_CHECK);
		request.setValue(foeData.getGameId());
		HttpRequestThread.getInstance().addRequest(request);
		lockUI();
	}

	public void updateDrawData(GameData gameData) {
		GameListData drawData = AccessGameListDB.getInstance().getLatestData();
		drawData.setLastTime(foeData.getFinalTime());
		drawData.setFoeStage(gameData.getFoeStage());
		drawData.setMessage(gameData.getGameLog());
		drawData.setMyStage(gameData.getMyStage());
		drawData.setFoeRps(drawData.getMyRps());
		AccessGameListDB.getInstance().updateLatestData(drawData);

	}
}
