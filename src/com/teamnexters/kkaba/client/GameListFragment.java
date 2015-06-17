package com.teamnexters.kkaba.client;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.game.GameActivity;
import com.teamnexters.kkaba.client.user.LoginUserData;
import com.teamnexters.kkaba.client.user.UserDataCacheStore;
import com.teamnexters.kkaba.common.data.FoeListData;
import com.teamnexters.kkaba.common.game.GameState;
import com.teamnexters.kkaba.db.AccessFoeListDB;

public class GameListFragment extends Fragment{

	private ListView lv_main;
	private GameListAdapter myAdapter;
	private List<FoeListData> gameList;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		gameList = new ArrayList<FoeListData>();
		gameList = AccessFoeListDB.getInstance().getAllFoeList(
				LoginUserData.get().getId().toString());
		for(FoeListData data : gameList){
			boolean myGame = false;
			if (data.getInitPlayer() == LoginUserData.get().getId()) {
				myGame = true;
			}
			boolean stateOn = false;
			switch (data.getStatus()) {
			case GameState.PLAYER1_REQUESTED:
				if (myGame) {
				} else {
					stateOn = true;
				}
				break;
			case GameState.PLAYER2_REQUESTED:
				if (myGame) {
					stateOn = true;
				} else {
				}
				break;
			case GameState.PROCESSING:
				break;
			case GameState.WATING_PLAYER1_DRAW_REQUEST:
				if (myGame) {
					stateOn = true;
				} else {
				}
				break;
			case GameState.WATING_PLAYER1_REQUEST:
				if (myGame) {
					stateOn = true;
				} else {
				}
				break;
			case GameState.WATING_PLAYER2_DRAW_REQUEST:
				if (myGame) {
				} else {
					stateOn = true;
				}
				break;
			case GameState.WATING_PLAYER2_REQUEST:
				if (myGame) {
				} else {
					stateOn = true;
				}
				break;
			default:
				break;
			}
			if (stateOn) {
				data.setStateOn(true);
			} else {
				data.setStateOn(false);
			}
		}
		

		View rootView = inflater.inflate(R.layout.fragment_two, container,
				false);
		super.onCreate(savedInstanceState);
		myAdapter = new GameListAdapter(getActivity(), gameList);
		lv_main = (ListView) rootView.findViewById(R.id.list);
		lv_main.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();
		((MainActivity) getActivity()).requestGameList();
		return rootView;
	}

	public void update(List<FoeListData> roomList) {
		this.gameList = roomList;
		for(FoeListData data : gameList){
			boolean myGame = false;
			if (data.getInitPlayer() == LoginUserData.get().getId()) {
				myGame = true;
			}
			boolean stateOn = false;
			switch (data.getStatus()) {
			case GameState.PLAYER1_REQUESTED:
				if (myGame) {
				} else {
					stateOn = true;
				}
				break;
			case GameState.PLAYER2_REQUESTED:
				if (myGame) {
					stateOn = true;
				} else {
				}
				break;
			case GameState.PROCESSING:
				break;
			case GameState.WATING_PLAYER1_DRAW_REQUEST:
				if (myGame) {
					stateOn = true;
				} else {
				}
				break;
			case GameState.WATING_PLAYER1_REQUEST:
				if (myGame) {
					stateOn = true;
				} else {
				}
				break;
			case GameState.WATING_PLAYER2_DRAW_REQUEST:
				if (myGame) {
				} else {
					stateOn = true;
				}
				break;
			case GameState.WATING_PLAYER2_REQUEST:
				if (myGame) {
				} else {
					stateOn = true;
				}
				break;
			default:
				break;
			}
			if (stateOn) {
				data.setStateOn(true);
			} else {
				data.setStateOn(false);
			}
		}
		myAdapter.update(roomList);
	}


}

class GameListAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private List<FoeListData> gameList;

	public GameListAdapter(Context context, List<FoeListData> roomList) {
		this.context = context;
		this.gameList = roomList;
	}

	@Override
	public int getCount() {
		return gameList.size();
	}

	@Override
	public Object getItem(int position) {
		return gameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Context context;

		View v = convertView;
		context = parent.getContext();
		FoeListData currentGame = gameList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		TextView nick = (TextView) v.findViewById(R.id.list_nick);
		TextView log = (TextView) v.findViewById(R.id.list_log);
		TextView time = (TextView) v.findViewById(R.id.list_time);
		ImageView badge = (ImageView) v.findViewById(R.id.list_badge);
		ImageView state = (ImageView) v.findViewById(R.id.list_state);
		UserDataCacheStore.getInstance()
		.fillView(currentGame.getUserId(), nick);
		UserDataCacheStore.getInstance()
		.fillView(currentGame.getUserId(), badge);
		log.setText(currentGame.getGameLog());
		time.setText(new SimpleDateFormat().format(currentGame.getFinalTime()));
		
		if (currentGame.isStateOn()) {
			state.setVisibility(View.VISIBLE);
		} else {
			state.setVisibility(View.INVISIBLE);
		}
		v.setTag(currentGame);
		v.setOnClickListener(this);
		((Activity)context).registerForContextMenu(v);
		return v;
	}

	public void update(List<FoeListData> roomList) {
		this.gameList = roomList;
		notifyDataSetInvalidated();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent((MainActivity) context,
				GameActivity.class);
		intent.putExtra("game", (Serializable) v.getTag());
		context.startActivity(intent);
	}

}
