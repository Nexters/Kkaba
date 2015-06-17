package com.teamnexters.kkaba.client.game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.common.game.GameType;
import com.teamnexters.kkaba.common.game.RPSCode;

public class RPSDialog extends Dialog implements android.view.View.OnClickListener{
	private int gameType;
	private long gameId;

	private RPSRequester requester;
	private TextView messageReadTextView, decisionView;
	private EditText messageWriteEditText;
	private ToggleButton rockButton, paperButton, scissorsButton;
	private ImageView blank;
	public RPSDialog(Context context, RPSRequester requester,long gameId, int gameType) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		this.gameType=gameType;
		this.requester = requester;
		this.gameId=gameId;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_rps);
		rockButton = (ToggleButton) findViewById(R.id.rpsDialogRockButton);
		rockButton.setOnClickListener(this);
		paperButton = (ToggleButton) findViewById(R.id.rpsDialogPaperButton);
		paperButton.setOnClickListener(this);
		scissorsButton = (ToggleButton) findViewById(R.id.rpsDialogScissorsButton);
		scissorsButton.setOnClickListener(this);
		decisionView = (TextView) findViewById(R.id.decision);
		decisionView.setOnClickListener(this);
		messageReadTextView = (TextView) findViewById(R.id.rpsDialogTextView);
		messageWriteEditText= (EditText) findViewById(R.id.rpsDialogEditText);
		blank = (ImageView) findViewById(R.id.blank);
		
		switch (gameType) {
		case GameType.DRAW_REQUEST:
//			messageReadTextView.setVisibility(View.GONE);
			messageWriteEditText.setVisibility(View.GONE);
			blank.setVisibility(View.VISIBLE);
			break;
		case GameType.REQUEST:
		case GameType.INITIALIZE:
			messageReadTextView.setVisibility(View.GONE);
			break;
		case GameType.REPLY:
			messageWriteEditText.setVisibility(View.GONE);
			blank.setVisibility(View.VISIBLE);
			break;
		}
		
		
	}


	
	@Override
	public void onClick(View arg0) {
		int selectedRps = RPSCode.RPS_NONE;
		switch (arg0.getId()) {
		case R.id.rpsDialogRockButton:
			paperButton.setChecked(false);
			scissorsButton.setChecked(false);
			break;
		case R.id.rpsDialogScissorsButton:
			rockButton.setChecked(false);
			paperButton.setChecked(false);
			break;
		case R.id.rpsDialogPaperButton:
			rockButton.setChecked(false);
			scissorsButton.setChecked(false);
			break;
		case R.id.decision:
			if(scissorsButton.isChecked()){
				selectedRps = RPSCode.RPS_SCISSORS;
			}
			else if(rockButton.isChecked()){
				selectedRps = RPSCode.RPS_ROCK;
			}
			else if(paperButton.isChecked()){
				selectedRps = RPSCode.RPS_PAPER;
			}
			else {
				Toast.makeText(getContext(), "가위바위보를 선택해주세요!", Toast.LENGTH_SHORT).show();
				break;
			}
			switch (gameType) {
			case GameType.DRAW_REQUEST:
				GameActivity.setDrawRPS(selectedRps);
				requester.requestDrawRPS(gameId, selectedRps);
				break;
			case GameType.REQUEST:
				GameActivity.setDrawRPS(selectedRps);
				GameActivity.setMessage(messageWriteEditText.getText().toString());
				requester.requestRPS(gameId, selectedRps,messageWriteEditText.getText().toString());
				break;
			case GameType.REPLY:
				GameActivity.setDrawRPS(selectedRps);
				requester.replyRPS(gameId, selectedRps);
				break;
			case GameType.INITIALIZE:
				requester.initializeRPS(selectedRps,messageWriteEditText.getText().toString());
				break;
			}
			dismiss();
		}
		
	}
}
