package com.teamnexters.kkaba.client.info;

import java.io.InputStream;
import java.lang.ref.Reference;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.client.image.CollectionImageManager;
import com.teamnexters.kkaba.common.data.InfoData;
import com.teamnexters.kkaba.common.facebook.InformationStore;
import com.teamnexters.kkaba.common.game.Collection;

public class InfoActivity extends Activity implements OnClickListener {

	private List<InfoData> infoList;
	private Collection targetCollection;
	private String targetName;
	private ListView infoListView;
	private ImageView infoCollectionView;
	private TextView infoNameView, infoCurrentObtainView, infoMaxObtainView;
	private Bitmap[] profilePictureBitmapRef;
	private InfoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		infoList = (List<InfoData>) getIntent().getSerializableExtra(
				"info_list");
		targetName = getIntent().getStringExtra("name");
		targetCollection = (Collection) getIntent().getSerializableExtra(
				"collection");
		infoListView = (ListView) findViewById(R.id.infoListView);
		profilePictureBitmapRef = new Bitmap[1];
		adapter = new InfoAdapter(this, infoList,profilePictureBitmapRef);
		infoListView.setAdapter(adapter);
		infoCollectionView = (ImageView) findViewById(R.id.infoCollectionView);
		infoCollectionView.setImageResource(CollectionImageManager
				.getInstance().getCollectionImageId(targetCollection));
		infoNameView = (TextView) findViewById(R.id.infoNameView);
		infoNameView.setText(targetName);
		infoCurrentObtainView = (TextView) findViewById(R.id.infoCurrentObtainCountView);
		infoCurrentObtainView.setText(String.valueOf(infoList.size()));
		infoMaxObtainView = (TextView) findViewById(R.id.infoMaxObtainCountView);
		infoMaxObtainView.setText(String.valueOf(InformationStore
				.getEmptyStore().getDataList().size()));
	}

	@Override
	public void onClick(View v) {
		Intent cafeIntent = new Intent();
		cafeIntent.setAction(Intent.ACTION_VIEW);
		cafeIntent.setData(Uri.parse("https://www.facebook.com/app_scoped_user_id/"+v.getTag()));
		startActivity(cafeIntent);
	}
	

}


