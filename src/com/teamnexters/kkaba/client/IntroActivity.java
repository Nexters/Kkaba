package com.teamnexters.kkaba.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseInstallation;

public class IntroActivity extends Activity {
	private SQLiteDatabase sqliteDB = null;
	public static String packageName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		String iid = ParseInstallation.getCurrentInstallation().getInstallationId();
		Log.i(iid, iid);
		String data;
		final long pushGameId ;
		long tmpPushGameId ;
		try {
			tmpPushGameId= new JSONObject(getIntent().getStringExtra("com.parse.Data")).getLong("game_id");
		} catch (Exception e) {
			tmpPushGameId = -1;
		}
		pushGameId = tmpPushGameId;
		packageName = this.getPackageName();
		if (!new File("/data/data/" + this.getPackageName() + "/KKABA.sqlite")
				.exists()) {
			createDb();
		}
		// } else {
		// selectDb();
		// }

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {

				Intent intent = new Intent(IntroActivity.this,
						AccessActivity.class);
				intent.putExtra("push_game_id",pushGameId);
				startActivity(intent);
				finish();
			}
		}, 1500);

	}

	private void createDb() {
		try {
			FileOutputStream out = new FileOutputStream("/data/data/"
					+ this.getPackageName() + "/KKABA.sqlite");
			InputStream in = getAssets().open("KKABA");
			byte[] buffer = new byte[2048];
			int readBytes = 0;

			while ((readBytes = in.read(buffer)) != -1)
				out.write(buffer, 0, readBytes);

			in.close();
			out.close();

		} catch (IOException e) {
			e.getMessage();
		}

	}

}
