package com.teamnexters.kkaba.client.game;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.client.R.id;
import com.teamnexters.kkaba.client.R.layout;
import com.teamnexters.kkaba.common.game.GameResultCode;

public class ResultPopupDialog extends Dialog implements AnimationListener{

	int resultCode;
	
	public ResultPopupDialog(Context context, int resultCode) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		this.resultCode = resultCode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		Animation a;
//		a.setAnimationListener(this)
		setContentView(R.layout.dialog_result_popup);

//		findViewById(R.id.match_win).start;
		switch(resultCode){
		case GameResultCode.WIN: 
			findViewById(R.id.match_win).setVisibility(View.VISIBLE); break;
		case GameResultCode.DRAW:
			findViewById(R.id.match_draw).setVisibility(View.VISIBLE); break;
		case GameResultCode.LOSE:
			findViewById(R.id.match_lose).setVisibility(View.VISIBLE); break;
		}
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				dismiss();
			}
		}, 2000);
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		 dismiss();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
