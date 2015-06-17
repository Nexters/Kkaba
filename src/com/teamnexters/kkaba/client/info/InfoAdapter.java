package com.teamnexters.kkaba.client.info;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.common.data.InfoData;
import com.teamnexters.kkaba.common.facebook.InformationStore;

public class InfoAdapter extends BaseAdapter {
	private List<InfoData> infoList;
	private InformationStore informationStore;
	private LayoutInflater inflater;
	private Bitmap pictureBitmap = null;
	private InfoActivity infoActivity;

	public InfoAdapter(InfoActivity context, List<InfoData> infoList,
			Bitmap[] pictureBitmapRef) {
		this.infoActivity = context;
		inflater = LayoutInflater.from(context);
		this.infoList = infoList;
		informationStore = new InformationStore(infoList);
		try{
			new DownloadImageTask(this).execute(informationStore
					.getDataByKey("picture").getDataList().get(0));
		}catch(Exception e){}
	}

	@Override
	public int getCount() {
		return InformationStore.getEmptyStore().getDataList().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView infoRowIndexImage;
		ImageView infoRowPictureImage;
		TextView infoRowKey, infoRowValue, infoRowIndexText;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.info_list_row, parent,
					false);
		}
		infoRowIndexImage = (ImageView) convertView
				.findViewById(R.id.infoRowIndexImage);
		infoRowKey = (TextView) convertView.findViewById(R.id.infoRowInfoKey);
		infoRowValue = (TextView) convertView
				.findViewById(R.id.infoRowInfoValue);
		infoRowPictureImage = (ImageView) convertView
				.findViewById(R.id.infoRowPictureImage);
		infoRowIndexText = (TextView) convertView
				.findViewById(R.id.infoRowIndexText);
		if (infoList.size() > position) {
			InfoData data = infoList.get(position);
			StringBuilder sb = new StringBuilder();
			for (String rowData : informationStore.getData(position)
					.getDataList()) {
				sb.append(rowData + "\n");
			}
			if (data.getInfoKey().equals("picture")
					|| data.getInfoKey().equals("facebook")) {
				infoRowValue.setVisibility(View.GONE);
				infoRowPictureImage.setVisibility(View.VISIBLE);
				if (data.getInfoKey().equals("picture")) {
					if (pictureBitmap != null) {
						infoRowPictureImage.setImageBitmap(pictureBitmap);
					} else {
						infoRowPictureImage
								.setImageResource(R.drawable.kkaba_icon);
						infoRowPictureImage.setOnClickListener(null);
					}
				}else{
					infoRowPictureImage.setImageResource(R.drawable.icon_profile_facebook);
					infoRowPictureImage.setOnClickListener(infoActivity);
					infoRowPictureImage.setTag(data.getInfoValue());
				}
			} else {
				infoRowValue.setVisibility(View.VISIBLE);
				infoRowPictureImage.setVisibility(View.GONE);
				infoRowValue.setText(sb.toString());
			}
			infoRowIndexImage.setImageResource(R.drawable.bullet_profile_up);
			infoRowIndexText.setTextColor(Color.rgb(255, 255, 255));
		} else {
			infoRowValue.setVisibility(View.VISIBLE);
			infoRowPictureImage.setVisibility(View.GONE);
			infoRowValue.setText("");
			infoRowIndexImage.setImageResource(R.drawable.bullet_profile_down);
			infoRowIndexText.setTextColor(Color.rgb(204, 204, 204));
		}
		infoRowKey.setText(InformationStore.getEmptyStore().getData(position)
				.getDescription());
		infoRowIndexText.setText(String.valueOf(position + 1));
		return convertView;
	}

	public void updateProfilePicture(Bitmap pictureBitmap) {
		this.pictureBitmap = pictureBitmap;
		notifyDataSetChanged();

	}
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	private InfoAdapter infoAdapter;

	public DownloadImageTask(InfoAdapter infoAdapter) {
		this.infoAdapter = infoAdapter;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		infoAdapter.updateProfilePicture(result);
	}
}