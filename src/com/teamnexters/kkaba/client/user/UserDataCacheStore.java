package com.teamnexters.kkaba.client.user;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ImageView;
import android.widget.TextView;

import com.teamnexters.kkaba.client.image.CollectionImageManager;
import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.common.data.SimpleUserData;
import com.teamnexters.kkaba.common.game.Collection;
import com.teamnexters.kkaba.common.game.CollectionCode;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.request.LongRequestPacket;

public class UserDataCacheStore {
	private Map<Long, SimpleUserData> innerStore;
	private Map<Long, UserDataCallback> callbackStore;
	private static UserDataCacheStore instance = null;
	
	private UserDataCacheStore(){
		innerStore = new HashMap<Long, SimpleUserData>();
		callbackStore = new HashMap<Long, UserDataCallback>();
	}
	
	public static UserDataCacheStore getInstance(){
		if(instance == null){
			instance = new UserDataCacheStore();
		}
		return instance;
	}

	public synchronized SimpleUserData fillView(long id, TextView nameView){
		SimpleUserData returnData = innerStore.get(id);
		if(returnData==null){
			UserDataCallback curCallback;
			if((curCallback=callbackStore.get(id))==null){
				curCallback = new UserDataCallback();
				callbackStore.put(id, curCallback);
				LongRequestPacket request = new LongRequestPacket();
				request.setRequestCode(KkabaRequestCode.REQUEST_FRIEND_INFO);
				request.setValue(id);
				HttpRequestThread.getInstance().addRequest(request);
			}
			curCallback.addCallbackView(nameView);
			returnData = new SimpleUserData();
			returnData.setCollection(new Collection(CollectionCode.COLLECTION_CODE_ABNORMAL));
			returnData.setNickname("");
		}
		nameView.setText(returnData.getNickname());
		return returnData;
	}

	public synchronized SimpleUserData fillView(long id, ImageView collectionView){
		SimpleUserData returnData = innerStore.get(id);
		if(returnData==null){
			UserDataCallback curCallback;
			if((curCallback=callbackStore.get(id))==null){
				curCallback = new UserDataCallback();
				callbackStore.put(id, curCallback);
				LongRequestPacket request = new LongRequestPacket();
				request.setRequestCode(KkabaRequestCode.REQUEST_FRIEND_INFO);
				request.setValue(id);
				HttpRequestThread.getInstance().addRequest(request);
			}
			curCallback.addCallbackView(collectionView);
			returnData = new SimpleUserData();
			returnData.setCollection(new Collection(CollectionCode.COLLECTION_CODE_ABNORMAL));
			returnData.setNickname("");
		}
		collectionView.setImageResource(CollectionImageManager.getInstance().getCollectionImageId(returnData.getCollection()));
		return returnData;
	}
	
	public synchronized SimpleUserData getFriendData(long id){
		SimpleUserData returnData = innerStore.get(id);
		if(returnData==null){
			LongRequestPacket request = new LongRequestPacket();
			request.setRequestCode(KkabaRequestCode.REQUEST_FRIEND_INFO);
			request.setValue(id);
			HttpRequestThread.getInstance().addRequest(request);
			returnData = new SimpleUserData();
			returnData.setCollection(new Collection(CollectionCode.COLLECTION_CODE_ABNORMAL));
			returnData.setNickname("");
			innerStore.put(id, returnData);
		}
		return returnData;
	}
	
	public synchronized void updateFriendData(long id, SimpleUserData updatingData){
		SimpleUserData originalData ;
		if((originalData = innerStore.get(id))==null){
			innerStore.put(id, updatingData);
		}else{
			originalData.setCollection(updatingData.getCollection());
			originalData.setNickname(updatingData.getNickname());
		}
		UserDataCallback callback;
		if((callback = callbackStore.remove(id))!=null){
			callback.prepare(updatingData);
			HttpRequestThread.getInstance().runOnUi(callback);
		}
	}
	class UserDataCallback implements Runnable{

		private List<WeakReference<TextView>> nameViewList;
		private List<WeakReference<ImageView>> collectionViewList;
		private SimpleUserData userData;
		public UserDataCallback() {
			this.nameViewList = new ArrayList<WeakReference<TextView>>();
			this.collectionViewList = new ArrayList<WeakReference<ImageView>>();
		}
		
		public void addCallbackView(TextView nicknameView){
			nameViewList.add(new WeakReference<TextView>(nicknameView));
		}
		
		public void addCallbackView(ImageView collectionView){
			collectionViewList.add(new WeakReference<ImageView>(collectionView));
		}

		public void prepare(SimpleUserData userData){
			this.userData = userData;
		}
		@Override
		public void run() {
			try{
				for(WeakReference<TextView> curNameView : nameViewList){
					curNameView.get().setText(userData.getNickname());
					curNameView.get().invalidate();
				}
				for(WeakReference<ImageView> curCollectionView : collectionViewList){
					curCollectionView.get().setImageResource(CollectionImageManager.getInstance().getCollectionImageId(userData.getCollection()));
					curCollectionView.get().invalidate();					
				}
			}catch(Exception e){}
		}
		
	}
}
