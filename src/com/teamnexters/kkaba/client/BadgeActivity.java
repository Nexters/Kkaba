package com.teamnexters.kkaba.client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnexters.kkaba.client.image.Badge;
import com.teamnexters.kkaba.client.image.CollectionImageManager;
import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.client.ui.UIHandlingActivity;
import com.teamnexters.kkaba.client.user.LoginUserData;
import com.teamnexters.kkaba.common.game.Collection;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.request.LongRequestPacket;
import com.teamnexters.kkaba.common.response.KkabaResponseCode;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;

public class BadgeActivity extends UIHandlingActivity implements OnClickListener {
	GridView badgeGrid;
	BadgeAdapter badageAdapter;
	ArrayList<Badge> badges = new ArrayList<Badge>();
	long profile;
	long gainBadge;
	Badge badge;
	CollectionImageManager collectionImageManager;
	ImageView profileBadge;
	TextView badgeName, badgeContent;
	Collection havingCollection;
	Collection selectedCollection;
	private Button setupButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_badge);
		collectionImageManager = CollectionImageManager.getInstance();
		havingCollection = LoginUserData.get().getCollection();
		setupButton = (Button) findViewById(R.id.badgeSetUp);
		setupButton.setOnClickListener(this);
		profileBadge = (ImageView) findViewById(R.id.profileBadge);
		badgeName = (TextView) findViewById(R.id.badgeName);
		badgeContent = (TextView) findViewById(R.id.badageExplanation);
		profileBadge.setImageResource(collectionImageManager
				.getCollectionImageId(LoginUserData.get()
						.getCurrentCollection()));
		badgeName
				.setText(LoginUserData.get().getCurrentCollection().getTitle());
		badgeContent.setText(LoginUserData.get().getCurrentCollection()
				.getContent());
		profile = 2L;
		gainBadge = 3L;
		selectedCollection = null;

		badageAdapter = new BadgeAdapter(this, LoginUserData.get()
				.getCollection());
		badgeGrid = (GridView) findViewById(R.id.badgeGrid);
		badgeGrid.setAdapter(badageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class BadgeAdapter extends BaseAdapter {
		LayoutInflater inflater;
		Collection collection;
		Activity activity;

		public BadgeAdapter(Activity activity, Collection collection) {
			this.collection = collection;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return Collection.AVAILABLE_COLLECTION_COUNT;
		}

		@Override
		public Object getItem(int position) {
			return badges.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.grid_badge, parent,
						false);
			}

			ImageView badge = (ImageView) convertView.findViewById(R.id.badge);
			if (collection.haveCollection(1L << position)) {
				badge.setImageResource(collectionImageManager
						.getCollectionImageId(new Collection(1 << position)));
			} else {
				badge.setImageResource(R.drawable.badge_defualt_large);
			}
			badge.setOnClickListener(BadgeActivity.this);
			badge.setTag(1L << position);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.badgeSetUp) {
			if(selectedCollection == null){
				Toast.makeText(this, "착용할 뱃지를 선택해주세요.", Toast.LENGTH_SHORT).show();
			}else{
				lockUI();
				LongRequestPacket request = new LongRequestPacket();
				request.setValue(selectedCollection.toLongValue());
				request.setRequestCode(KkabaRequestCode.REQUEST_USE_COLLECTION);
				HttpRequestThread.getInstance().addRequest(request);
			}
		} else {
			Collection selectedCollection = new Collection((Long) v.getTag());
			if (havingCollection.haveCollection(selectedCollection
					.toLongValue())) {
				profileBadge.setImageResource(collectionImageManager
						.getCollectionImageId(selectedCollection));
				badgeName.setText(selectedCollection.getTitle());
				badgeContent.setText(selectedCollection.getContent());
				this.selectedCollection = selectedCollection;
			} else {
				this.selectedCollection = null;
				profileBadge.setImageResource(R.drawable.badge_defualt_large);
				badgeName.setText("미확인 배지");
				badgeContent.setText("조건을 달성하여 배지를 모아보세요!");
			}
		}
	}

	@Override
	public void onHandleUI(KkabaResponsePacket response) {
		switch (response.getResponseCode()) {
		case KkabaResponseCode.RESPONSE_USE_COLLECTION:
			unlockUI();
			Toast.makeText(this, "뱃지를 착용했습니다.", Toast.LENGTH_SHORT).show();
			LoginUserData.get().setCurrentCollection(selectedCollection);
			finish();
			break;
		}
	}
}
