package com.teamnexters.kkaba.client;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamnexters.kkaba.client.game.RPSDialog;
import com.teamnexters.kkaba.client.game.RPSRequester;
import com.teamnexters.kkaba.client.image.CollectionImageManager;
import com.teamnexters.kkaba.client.user.LoginUserData;
import com.teamnexters.kkaba.common.game.GameType;
//?��?��?�� 주의?���?
 
public class MainFragment extends Fragment implements OnClickListener {

	private ImageView badgeView;
	private TextView nicknameView;
	private TextView rockTotalView,rockWinView,rockDrawView,rockLoseView;
	private TextView scissorTotalView,scissorWinView,scissorDrawView,scissorLoseView;
	private TextView paperTotalView,paperWinView,paperDrawView,paperLoseView;
	private Button gameStartButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
 
        badgeView = (ImageView) rootView.findViewById(R.id.badge);
        badgeView.setImageResource(CollectionImageManager.getInstance().getCollectionImageId(LoginUserData.get().getCurrentCollection()));
        badgeView.setOnClickListener(this);
        gameStartButton = (Button) rootView.findViewById(R.id.game_start);
        gameStartButton.setOnClickListener(this);
        LinearLayout gameStartLayout = (LinearLayout)rootView.findViewById(R.id.layout_game_start);
        gameStartLayout.bringToFront();
        gameStartLayout.invalidate();
        nicknameView = (TextView) rootView.findViewById(R.id.nickname_text);
        nicknameView.setText(LoginUserData.get().getNickname());
        nicknameView.setTag(nicknameView.getText());
        getActivity().registerForContextMenu(nicknameView);
        rockTotalView=(TextView) rootView.findViewById(R.id.total_rock);
        rockWinView = (TextView) rootView.findViewById(R.id.win_rock);
        rockDrawView = (TextView) rootView.findViewById(R.id.draw_rock);
        rockLoseView = (TextView) rootView.findViewById(R.id.lose_rock);
        scissorTotalView=(TextView) rootView.findViewById(R.id.total_scissor);
        scissorWinView = (TextView) rootView.findViewById(R.id.win_scissor);
        scissorDrawView = (TextView) rootView.findViewById(R.id.draw_scissor);
        scissorLoseView = (TextView) rootView.findViewById(R.id.lose_scissor);
        paperTotalView=(TextView) rootView.findViewById(R.id.total_paper);
        paperWinView = (TextView) rootView.findViewById(R.id.win_paper);
        paperDrawView = (TextView) rootView.findViewById(R.id.draw_paper);
        paperLoseView = (TextView) rootView.findViewById(R.id.lose_paper);
        updateUserInfo();

        return rootView;
    }
    
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.badge:
			Intent intent = new Intent(getActivity(),BadgeActivity.class);
			startActivity(intent);
			break;
		case R.id.game_start:
			new RPSDialog(getActivity(), (RPSRequester)getActivity(), 0, GameType.INITIALIZE).show();
			break;

		default:
			break;
		}
		
	}
	
	public void updateUserInfo(){
		badgeView.setImageResource(CollectionImageManager.getInstance().getCollectionImageId(LoginUserData.get().getCurrentCollection()));
		rockWinView.setText(String.valueOf(LoginUserData.get().getRockWinCount()));
        rockDrawView.setText(String.valueOf(LoginUserData.get().getRockDrawCount()));
        rockLoseView.setText(String.valueOf(LoginUserData.get().getRockLoseCount()));
        rockTotalView.setText(String.valueOf(LoginUserData.get().getRockWinCount()+LoginUserData.get().getRockDrawCount()+LoginUserData.get().getRockLoseCount()));
        scissorWinView.setText(String.valueOf(LoginUserData.get().getScissorsWinCount()));
        scissorDrawView.setText(String.valueOf(LoginUserData.get().getScissorsDrawCount()));
        scissorLoseView.setText(String.valueOf(LoginUserData.get().getScissorsLoseCount()));
        scissorTotalView.setText(String.valueOf(LoginUserData.get().getScissorsWinCount()+LoginUserData.get().getScissorsDrawCount()+LoginUserData.get().getScissorsLoseCount()));
        paperWinView.setText(String.valueOf(LoginUserData.get().getPaperWinCount()));
        paperDrawView.setText(String.valueOf(LoginUserData.get().getPaperDrawCount()));
        paperLoseView.setText(String.valueOf(LoginUserData.get().getPaperLoseCount()));
        paperTotalView.setText(String.valueOf(LoginUserData.get().getPaperWinCount()+LoginUserData.get().getPaperDrawCount()+LoginUserData.get().getPaperLoseCount()));
	}

}