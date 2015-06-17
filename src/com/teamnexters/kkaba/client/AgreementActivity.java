package com.teamnexters.kkaba.client;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AgreementActivity extends ActionBarActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80a52a2a")));
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_agree);
		final ToggleButton cb1 = (ToggleButton)findViewById(R.id.option1);
		final ToggleButton cb2 = (ToggleButton)findViewById(R.id.option2);
		Button btn=(Button)findViewById(R.id.OK);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cb1.isChecked()&&cb2.isChecked()){
				Intent intent = new Intent(AgreementActivity.this,JoinActivity.class);
				startActivity(intent);
				finish();
				} 
				else {
					Toast.makeText(AgreementActivity.this,"동의하세요", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
}
