package com.teamnexters.kkaba.client.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.common.data.GameResultLog;
import com.teamnexters.kkaba.common.game.GameResultCode;
import com.teamnexters.kkaba.common.game.RPSCode;
import com.teamnexters.kkaba.common.game.RPSUtil;

public class GameLogAdapter extends BaseAdapter {
	private List<GameResultLog> listGameResultLogs = new ArrayList<GameResultLog>();
	Context context;
	String myNick;
	String foeNick;
	String unselected = "_unselected";
	String myRpsString = "";
	String foeRpsString = "";
	String WDL = "";

	public void updateNick(String f, String m) {
		foeNick = f;
		myNick = m;
	}

	public GameLogAdapter(Context context) {
		this.context = context;
	}

	public void update(List<GameResultLog> tempGameResultLogs) {
		this.listGameResultLogs.clear();
		for (GameResultLog item : tempGameResultLogs) {
			this.listGameResultLogs.add(item);
		}
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listGameResultLogs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listGameResultLogs.get(position);
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
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item_game_log, parent, false);
		}

		GameResultLog currentGameResultLog = listGameResultLogs.get(position);
		if (currentGameResultLog.getFoeRps() == null) {
			currentGameResultLog.setFoeRps(RPSCode.RPS_NONE);
		}
		if (currentGameResultLog.getMyRps() == null) {
			currentGameResultLog.setMyRps(RPSCode.RPS_NONE);
		}
		switch (currentGameResultLog.getMyRps()) {
		case RPSCode.RPS_ROCK:
			myRpsString = "rock";
			break;
		case RPSCode.RPS_SCISSORS:
			myRpsString = "scissor";
			break;
		case RPSCode.RPS_PAPER:
			myRpsString = "paper";
			break;
		case RPSCode.RPS_NONE:
			myRpsString = "badge_defualt_small";
			WDL = "응답 대기";
			break;
		}

		switch (currentGameResultLog.getFoeRps()) {
		case RPSCode.RPS_ROCK:
			foeRpsString = "rock";
			break;
		case RPSCode.RPS_SCISSORS:
			foeRpsString = "scissor";
			break;
		case RPSCode.RPS_PAPER:
			foeRpsString = "paper";
			break;
		case RPSCode.RPS_NONE:
			foeRpsString = "badge_defualt_small";
			WDL = "응답 대기";
			break;
		}

		if (currentGameResultLog.getMyRps() != 0
				&& currentGameResultLog.getFoeRps() != 0) {
			switch (RPSUtil.getRPSResult(currentGameResultLog.getMyRps(),
					currentGameResultLog.getFoeRps())) {
			case GameResultCode.WIN:
				WDL = myNick + "님이 이겼어요!";
				foeRpsString += unselected;
				break;
			case GameResultCode.DRAW:
				WDL = "무승부입니다.";
				myRpsString += unselected;
				foeRpsString += unselected;
				break;
			case GameResultCode.LOSE:
				WDL = foeNick + "님이 이겼어요!";
				myRpsString += unselected;
				break;
			default:
				WDL = "몰라";
				break;
			}
		}

		int myRpsId = context.getResources().getIdentifier(myRpsString,
				"drawable", context.getPackageName());
		int foeRpsId = context.getResources().getIdentifier(foeRpsString,
				"drawable", context.getPackageName());

		TextView textResultLog = (TextView) v
				.findViewById(R.id.text_game_result);
		textResultLog.setText(WDL);
		ImageView myRpsImage = (ImageView) v
				.findViewById(R.id.image_game_myRps);
		myRpsImage
				.setImageDrawable(context.getResources().getDrawable(myRpsId));
		ImageView foeRpsImage = (ImageView) v
				.findViewById(R.id.image_game_foeRps);
		foeRpsImage.setImageDrawable(context.getResources().getDrawable(
				foeRpsId));

		return v;
	}

}
