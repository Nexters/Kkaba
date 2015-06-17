package com.teamnexters.kkaba.client.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.common.data.GameListData;
import com.teamnexters.kkaba.common.data.GameResultData;
import com.teamnexters.kkaba.common.game.GameResultCode;
import com.teamnexters.kkaba.common.game.GameType;
import com.teamnexters.kkaba.common.game.RPSUtil;

public class GameListAdapter extends BaseAdapter implements OnClickListener {
	Context context;
	private int status = 0;
	private String myNick;
	private String foeNick;
	private long gameId;
	private List<GameListData> listGameListData = new ArrayList<GameListData>();
	boolean draw = false;
	RPSRequester requester;
	GameResultData updatedData = null;

	public void setNick(String fn, String mn) {
		myNick = mn;
		foeNick = fn;
	}

	public void setGameId(long a) {
		gameId = a;
	}

	public void setStatus(int a) {
		status = a;
	}

	public GameListAdapter(Context context, List<GameListData> gameList,
			RPSRequester requester) {
		this.context = context;
		this.listGameListData = gameList;
		this.requester = requester;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listGameListData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listGameListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		context = parent.getContext();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.list_item_game, parent, false);

		GameListData currentGameListData = listGameListData.get(position);

		Log.d("bgbg", listGameListData.size() + "!ASD");

		GameLogAdapter gameLogAdapter = new GameLogAdapter(context);

		if (currentGameListData.getMyRps() != null) {
			if (currentGameListData.getMyRps() != 0
					&& currentGameListData.getFoeRps() != 0) {
				switch (RPSUtil.getRPSResult(currentGameListData.getMyRps(),
						currentGameListData.getFoeRps())) {
				case GameResultCode.WIN:
					break;
				case GameResultCode.DRAW:
					if (position == listGameListData.size() - 1) {
						break;
					} else {
						return v = inflater.inflate(R.layout.list_null_item,
								null);
					}
				case GameResultCode.LOSE:
					break;
				}
				LinearLayout stageLayout = (LinearLayout) v
						.findViewById(R.id.layout_game_stage);

				TextView message = (TextView) v
						.findViewById(R.id.text_game_message);
				if (currentGameListData.getMessage() != null) {
					message.setText(currentGameListData.getMessage().toString());
				}
				TextView myStage = (TextView) v
						.findViewById(R.id.text_game_myStage);
				myStage.setText(currentGameListData.getMyStage().toString());
				TextView foeStage = (TextView) v
						.findViewById(R.id.text_game_foeStage);
				foeStage.setText(currentGameListData.getFoeStage().toString());

				try {
					if (currentGameListData.getTurn() == 1) {
						stageLayout
								.setBackgroundResource(R.drawable.unactive_top_white_right);
					}
				} catch (NullPointerException e) {
				}

				ListView gameLogList = (ListView) v
						.findViewById(R.id.listview_game_log);

				DisplayMetrics displayMetrics = new DisplayMetrics();
				WindowManager wm = (WindowManager) context
						.getSystemService(Context.WINDOW_SERVICE);
				wm.getDefaultDisplay().getMetrics(displayMetrics);
				int screenHeight = displayMetrics.heightPixels;
				int height = currentGameListData.getLog().size();
				LayoutParams layoutParams = new LayoutParams(0,
						(int) (screenHeight / 6.6) * height, 84);

				TextView textMyNick = (TextView) v
						.findViewById(R.id.text_game_myNick);
				textMyNick.setText(myNick);
				TextView textFoeNick = (TextView) v
						.findViewById(R.id.text_game_foeNick);
				textFoeNick.setText(foeNick);

				if (currentGameListData.getLog() != null) {
					gameLogAdapter.updateNick(foeNick, myNick);
					gameLogAdapter.update(currentGameListData.getLog());
					gameLogList.setLayoutParams(layoutParams);
					gameLogList.setAdapter(gameLogAdapter);
					gameLogAdapter.notifyDataSetInvalidated();
				} else {
					v.findViewById(R.id.layout_game_listview).setVisibility(
							View.GONE);
				}
			}
		}

		if (position == listGameListData.size() - 1) {
			TextView message = (TextView) v
					.findViewById(R.id.text_game_message);
			if (currentGameListData.getMessage() != null) {
				message.setText(currentGameListData.getMessage().toString());
			}
			TextView myStage = (TextView) v
					.findViewById(R.id.text_game_myStage);
			myStage.setText(currentGameListData.getMyStage().toString());
			TextView foeStage = (TextView) v
					.findViewById(R.id.text_game_foeStage);
			foeStage.setText(currentGameListData.getFoeStage().toString());
			DisplayMetrics displayMetrics = new DisplayMetrics();
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(displayMetrics);
			int screenHeight = displayMetrics.heightPixels;
			int height = 0;

			ListView gameLogList = (ListView) v
					.findViewById(R.id.listview_game_log);

			 if (currentGameListData.getLog() != null) {
				 height = currentGameListData.getLog().size();
			 } else{
				 height = 1;
			 }
			LayoutParams layoutParams = new LayoutParams(0,
					(int) (screenHeight / 6.6) * height, 84);
			gameLogAdapter.updateNick(foeNick, myNick);
			gameLogAdapter.update(currentGameListData.getLog());
			gameLogList.setLayoutParams(layoutParams);
			gameLogList.setAdapter(gameLogAdapter);
			gameLogAdapter.notifyDataSetInvalidated();

			LinearLayout stateLayout;
			if (listGameListData.size() >= 2) {
				if (listGameListData.get(listGameListData.size() - 1).getTurn() == 1) {
					v.findViewById(R.id.layout_game_stage)
							.setBackgroundResource(
									R.drawable.active_top_white_right);
				} else {
					v.findViewById(R.id.layout_game_stage)
							.setBackgroundResource(R.drawable.active_top_white);
				}
			} else {
				if (listGameListData.get(0).getTurn() == 1) {
					v.findViewById(R.id.layout_game_stage)
							.setBackgroundResource(
									R.drawable.active_top_white_right);
				} else {
					v.findViewById(R.id.layout_game_stage)
							.setBackgroundResource(R.drawable.active_top_white);
				}
			}
			TextView textMyNick = (TextView) v
					.findViewById(R.id.text_game_myNick);
			textMyNick.setText(myNick);
			TextView textFoeNick = (TextView) v
					.findViewById(R.id.text_game_foeNick);
			textFoeNick.setText(foeNick);
			v.findViewById(R.id.layout_game_message).setBackgroundResource(
					R.drawable.active_middle_gray);
			v.findViewById(R.id.layout_game_around_listview)
					.setBackgroundResource(R.drawable.active_middle_white);
			v.findViewById(R.id.layout_game_transparent).setVisibility(
					View.GONE);
			v.findViewById(R.id.layout_game_default).setVisibility(View.GONE);
			
			switch (status) {
			case GameListState.WAIT:
				stateLayout = (LinearLayout) v
						.findViewById(R.id.layout_game_wait);
				stateLayout.setVisibility(View.VISIBLE);
				stateLayout
						.setBackgroundResource(R.drawable.active_bottom_pink);
				break;
			case GameListState.REPLY:
				stateLayout = (LinearLayout) v
						.findViewById(R.id.layout_game_accept);
				stateLayout.setVisibility(View.VISIBLE);
				stateLayout.setOnClickListener(this);
				stateLayout
						.setBackgroundResource(R.drawable.active_bottom_pink);
				break;
			case GameListState.REQUEST:
				stateLayout = (LinearLayout) v
						.findViewById(R.id.layout_game_request);
				stateLayout.setVisibility(View.VISIBLE);
				stateLayout.setOnClickListener(this);
				stateLayout
						.setBackgroundResource(R.drawable.active_bottom_pink);
				break;
			case GameListState.DRAW_REQUEST:
				stateLayout = (LinearLayout) v
						.findViewById(R.id.layout_game_draw_request);
				stateLayout.setVisibility(View.VISIBLE);
				stateLayout.setOnClickListener(this);
				stateLayout
						.setBackgroundResource(R.drawable.active_bottom_pink);
				break;
			}

		}

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (status) {
		case GameListState.REPLY:
			new RPSDialog(context, requester, gameId, GameType.REPLY).show();
			break;
		case GameListState.REQUEST:
			new RPSDialog(context, requester, gameId, GameType.REQUEST).show();
			break;
		case GameListState.DRAW_REQUEST:
			new RPSDialog(context, requester, gameId, GameType.DRAW_REQUEST)
					.show();
			break;
		}

	}

}