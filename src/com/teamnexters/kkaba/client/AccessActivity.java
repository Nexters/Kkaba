package com.teamnexters.kkaba.client;

import java.security.MessageDigest;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.parse.ParseInstallation;
import com.teamnexters.kkaba.client.config.ConfigUtil;
import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.client.ui.UIHandlingActivity;
import com.teamnexters.kkaba.client.user.LoginUserData;
import com.teamnexters.kkaba.common.request.AuthRequestPacket;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.response.CommonResponsePacket;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;
import com.teamnexters.kkaba.common.response.LoginResponsePacket;

public class AccessActivity extends UIHandlingActivity implements
		OnClickListener {

	private Button loginButton;
	private static Session fbSession;
	private final static String appId = "1516520791959861";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_intro);

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.teamnexters.rps", PackageManager.GET_SIGNATURES);

			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} 
		catch (Exception e) {
		}
		boolean isLogouted = getIntent().getBooleanExtra("is_logout", false);
		fbSession = new Session.Builder(this).setApplicationId(appId).build();
		Session.setActiveSession(fbSession);
		Session.restoreSession(this, null, null, savedInstanceState);
		if(isLogouted){
			fbSession.closeAndClearTokenInformation();
			Session.setActiveSession(null);
			fbSession = new Session.Builder(this).setApplicationId(appId).build();
		}
		if (fbSession.getAccessToken().length() > 0) {
			requestAuth();
		} else {
			setContentView(R.layout.activity_access);
			loginButton = (Button) findViewById(R.id.loginButton);
			loginButton.setOnClickListener(this);
		}
	}

	@Override
	public void onHandleUI(KkabaResponsePacket response) {
		Intent intent;
		unlockUI();
		switch (response.getResponseCode()) 
		{
		case KkabaResponsePacket.RESPONSE_REGISTRATION_SUCCESS:
			Toast.makeText(this, "가입성공.", Toast.LENGTH_SHORT).show();
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case KkabaResponsePacket.RESPONSE_REQUIRE_REGISTERATION:
			intent = new Intent(this, AgreementActivity.class);
			startActivity(intent);
			finish();
			break;
		case KkabaResponsePacket.RESPONSE_REQUIRE_LOGIN:
			requestAuth();
			break;
		case KkabaResponsePacket.RESPONSE_SERVER_ERROR:
			CommonResponsePacket responsePacket1 = (CommonResponsePacket) response;
			Toast.makeText(this, "서버와의 통신에 실패했습니다. 다시 시도해 주세요",
					Toast.LENGTH_SHORT).show();

			// ...서버에러처리로직
			break;
		case KkabaResponsePacket.RESPONSE_LOGIN_SUCCESS:
			LoginUserData.set(((LoginResponsePacket)response).getLoginData());
			intent = new Intent(this, MainActivity.class);
			intent.putExtra("push_game_id",getIntent().getLongExtra("push_game_id",-1));
			startActivity(intent);
			Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
			finish();
			// ...로그인성공 로직
			break;
		default:
			finish();
		}
	}	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		fbSession.onActivityResult(this, requestCode, resultCode, data);
		if (resultCode == 0) {

			finish();
		} else {

			requestAuth();
		}
	}

	private void requestAuth() {
		lockUI();
		HttpRequestThread requestThread = HttpRequestThread.getInstance();
		AuthRequestPacket request = new AuthRequestPacket();
		request.setRequestCode(KkabaRequestCode.REQUEST_FACEBOOK_AUTH);
		request.setAccessToken(fbSession.getAccessToken());
		if(new ConfigUtil(this).getChecked()){
			request.setPushInstallationKey(ParseInstallation.getCurrentInstallation().getInstallationId());
		}else{
			request.setPushInstallationKey("");			
		}
		requestThread.addRequest(request);
	}

	@Override
	public void onClick(View v) {
		Session.OpenRequest or = new Session.OpenRequest(this);
		or.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		or.setPermissions("user_friends", "user_about_me ","user_birthday","user_location","user_relationships","user_relationship_details","user_about_me","user_birthday","user_likes","user_likes","user_hometown","user_likes","user_likes","user_likes","user_likes","user_religion_politics","user_likes","user_likes","user_groups","","user_status","user_tagged_places","user_education_history","user_religion_politics","user_education_history","user_work_history","user_about_me ","user_photos");
		fbSession.openForRead(or);
	}
}