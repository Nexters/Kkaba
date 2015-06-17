package com.teamnexters.kkaba.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.image.CollectionImageManager;
import com.teamnexters.kkaba.client.user.UserDataCacheStore;
import com.teamnexters.kkaba.common.data.FoeListData;
import com.teamnexters.kkaba.common.data.SimpleUserData;

public class PeepActivity extends Activity{

	private ListView lv_peep;
	private PeepListAdapter myAdapter;
	private List<FoeListData> gameList;

	protected View onCreate(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		gameList = new ArrayList<FoeListData>();

		View rootView = inflater.inflate(R.layout.activity_peep, container,
				false);
		super.onCreate(savedInstanceState);
		myAdapter = new PeepListAdapter(gameList);
		lv_peep = (ListView) rootView.findViewById(R.id.list_peep);
		lv_peep.setAdapter(myAdapter);
		
		ImageView myImage=(ImageView)findViewById(R.id.myImage);
		TextView myId=(TextView)findViewById(R.id.myId);
		TextView peepNum=(TextView)findViewById(R.id.peepNum);

		myAdapter.notifyDataSetChanged();
		return rootView;
	}

}

class PeepListAdapter extends BaseAdapter {

	private List<FoeListData> gameList;

	public PeepListAdapter(List<FoeListData> roomList) {
		this.gameList = roomList;
	}

	@Override
	public int getCount() {
		return gameList.size();
	}

	@Override
	public Object getItem(int position) {
		return gameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Context context;

		View v = convertView;
		context = parent.getContext();
		FoeListData currentGame = gameList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.peep_list_item, parent, false);

		}
		TextView info = (TextView) v.findViewById(R.id.info);
		TextView number = (TextView) v.findViewById(R.id.number);
		TextView myInfo = (TextView) v.findViewById(R.id.myInfo);
		info.setText("성별");
		number.setText("1");
		myInfo.setText("여자");
		v.setTag(currentGame);
		return v;
	}

	public void update(List<FoeListData> roomList) {
		this.gameList = roomList;
		notifyDataSetInvalidated();
	}

}
