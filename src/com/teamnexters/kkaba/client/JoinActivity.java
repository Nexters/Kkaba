package com.teamnexters.kkaba.client;

import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.client.ui.UIHandlingActivity;
import com.teamnexters.kkaba.client.user.LoginUserData;
import com.teamnexters.kkaba.common.game.RPSCode;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.request.RegistrationRequestPacket;
import com.teamnexters.kkaba.common.response.KkabaResponseCode;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;
import com.teamnexters.kkaba.common.response.LoginResponsePacket;

public class JoinActivity extends UIHandlingActivity implements OnClickListener{

	private Button okButton;
	private EditText nicknameText;
	private int join_rps = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_join);
		okButton=(Button)findViewById(R.id.OK1);
		okButton.setOnClickListener(this);		
		nicknameText = (EditText) findViewById(R.id.joinNicknameText);
		nicknameText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					joinRequest();
					return true;
				}
				return false;
			}
		});
		
		final ToggleButton scissor = (ToggleButton)findViewById(R.id.button_join_scissor);
		final ToggleButton rock = (ToggleButton)findViewById(R.id.button_join_rock);
		final ToggleButton paper = (ToggleButton)findViewById(R.id.button_join_paper);

		scissor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				join_rps = 2;
				rock.setChecked(false);
				paper.setChecked(false);
			}
		});
		
		rock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				join_rps = 1;
				scissor.setChecked(false);
				paper.setChecked(false);
			}
		});
		
		paper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				join_rps = 3;
				rock.setChecked(false);
				scissor.setChecked(false);
			}
		});
		
		
	}

	@Override
	public void onHandleUI(KkabaResponsePacket response) {
		unlockUI();
		switch (response.getResponseCode()) {
		case KkabaResponseCode.RESPONSE_REGISTRATION_SUCCESS:
			LoginUserData.set(((LoginResponsePacket)response).getLoginData());
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		joinRequest();
		}
	
	public void joinRequest(){
		if(nicknameText.getText().toString().isEmpty()){
			Toast.makeText(getApplication(), "닉네임을 지정해주세요.", Toast.LENGTH_SHORT).show();
			return;
		}
		lockUI();
		RegistrationRequestPacket request = new RegistrationRequestPacket();
		request.setInitialRps(join_rps);
		request.setRequestCode(KkabaRequestCode.REQUEST_REGISTRATION);
		request.setNickname(nicknameText.getText().toString());
		HttpRequestThread.getInstance().addRequest(request);
	}

}
