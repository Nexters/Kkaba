package com.teamnexters.kkaba.client;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseInstallation;
import com.teamnexters.kkaba.client.game.GameActivity;
import com.teamnexters.kkaba.client.game.RPSRequester;
import com.teamnexters.kkaba.client.game.RPSRequestingActivity;
import com.teamnexters.kkaba.client.network.HttpRequestThread;
import com.teamnexters.kkaba.client.user.LoginUserData;
import com.teamnexters.kkaba.client.user.UserDataCacheStore;
import com.teamnexters.kkaba.common.data.FoeListData;
import com.teamnexters.kkaba.common.data.GameData;
import com.teamnexters.kkaba.common.request.CommonRequestPacket;
import com.teamnexters.kkaba.common.request.KkabaRequestCode;
import com.teamnexters.kkaba.common.request.LongRequestPacket;
import com.teamnexters.kkaba.common.request.StringRequestPacket;
import com.teamnexters.kkaba.common.response.GameListResponsePacket;
import com.teamnexters.kkaba.common.response.GameResponsePacket;
import com.teamnexters.kkaba.common.response.KkabaResponseCode;
import com.teamnexters.kkaba.common.response.KkabaResponsePacket;
import com.teamnexters.kkaba.common.response.LoginResponsePacket;
import com.teamnexters.kkaba.common.response.LongResponsePacket;
import com.teamnexters.kkaba.db.AccessFoeListDB;
import com.teamnexters.kkaba.db.AccessGameListDB;

public class MainActivity extends RPSRequestingActivity implements ActionBar.TabListener, RPSRequester{
	private ViewPager viewPager;
	private MainFragment mainFragment;
	private GameListFragment gameListFragment;
	private ConfigFragment configFragment;
	private BackPressCloseHandler backPressCloseHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		backPressCloseHandler = new BackPressCloseHandler(this);
		
		final ActionBar actionBar = getSupportActionBar();

		viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
        
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
                switch (i) {
				case 0:
					requestUserInfo();
				case 1:
	       			requestGameList(); 
				case 2:
					
					break;

				default:
					break;
        		}
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
       
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        
        ActionBar.Tab tab1 = actionBar.newTab().setTabListener(this).setIcon(R.drawable.actionbar_tab_indicator);
     
        ActionBar.Tab tab2 = actionBar.newTab().setTabListener(this).setIcon(R.drawable.actionbar_tab2_indicator);

        ActionBar.Tab tab3 = actionBar.newTab().setTabListener(this).setIcon(R.drawable.actionbar_tab3_indicator);


        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
		long pushGameId = getIntent().getLongExtra("push_game_id",-1);
		FoeListData pushedData = AccessFoeListDB.getInstance().getFoeData(String.valueOf(LoginUserData.get().getId()), pushGameId);
		if(pushedData!=null){
			Intent intent = new Intent(this,
					GameActivity.class);
			intent.putExtra("game", pushedData);
			startActivity(intent);
		}
    }

   class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i){
                case 0: fragment = (mainFragment!=null)?mainFragment:(mainFragment=new MainFragment()); break;
                case 1: fragment = (gameListFragment!=null)?gameListFragment:(gameListFragment=new GameListFragment()); break;
                case 2: fragment = (configFragment!=null)?configFragment:(configFragment=new ConfigFragment()); break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
	

	@Override
	public void onHandleUI(KkabaResponsePacket response) {
		switch (response.getResponseCode()) {
		case KkabaResponseCode.RESPONSE_GAME_LIST:
			for(GameData gameData : ((GameListResponsePacket)response).getGameListData()){
				FoeListData updateFoeListData = gameData.toFoeListData();
				updateFoeListData.setSelfId(LoginUserData.get().getId().toString());
				AccessFoeListDB.getInstance().updateOrInsertFoeList(updateFoeListData);
			}
			gameListFragment.update(AccessFoeListDB.getInstance().getAllFoeList(LoginUserData.get().getId().toString()));
			unlockUI();
			break;
		case KkabaResponseCode.RESPONSE_GAME_INITIALIZED:
			GameData initializedGame= ((GameResponsePacket)response).getGameData();
			FoeListData initFoeListData = initializedGame.toFoeListData();
			initFoeListData.setSelfId(LoginUserData.get().getId().toString());
			AccessFoeListDB.getInstance().addFoeList(initFoeListData);
			requestGameList();
			break;
		case KkabaResponseCode.RESPONSE_USER_INFO:
			LoginUserData.set(((LoginResponsePacket)response).getLoginData());
			mainFragment.updateUserInfo();
			break;
		case KkabaResponseCode.RESPONSE_LOGOUT_SUCCESS:
			Intent i = new Intent(this, AccessActivity.class);
			i.putExtra("is_logout", true);
			startActivity(i);
			unlockUI();
			finish();
			break;
		case KkabaResponseCode.RESPONSE_GAME_DELETE:
			long deletedGameId = ((LongResponsePacket)response).getValue();
			AccessGameListDB.getInstance().deleteAllGameHistory(deletedGameId);
			AccessFoeListDB.getInstance().deleteFoeList(deletedGameId);
			gameListFragment.update(AccessFoeListDB.getInstance().getAllFoeList(LoginUserData.get().getId().toString()));
			
			unlockUI();
			break;
		case KkabaResponseCode.RESPONSE_SET_PUSH_ID:
			unlockUI();
			break;
		}
	}



	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}



	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		requestUserInfo();
		requestGameList();
	}
	public void requestGameList(){
		CommonRequestPacket request = new CommonRequestPacket();
		request.setRequestCode(KkabaRequestCode.REQUEST_GAME_LIST);
		HttpRequestThread.getInstance().addRequest(request);
	}
	public void requestLogout(){
		lockUI();
		StringRequestPacket request = new StringRequestPacket();
		request.setValue("");
		request.setRequestCode(KkabaRequestCode.REQUEST_SET_PUSH_TOKEN);
		HttpRequestThread.getInstance().addRequest(request);
		CommonRequestPacket logoutRequest = new CommonRequestPacket();
		logoutRequest.setRequestCode(KkabaRequestCode.REQUEST_LOGOUT);
		HttpRequestThread.getInstance().addRequest(logoutRequest);
	}
	

	public void requestWithdraw(){
		lockUI();
		CommonRequestPacket logoutRequest = new CommonRequestPacket();
		logoutRequest.setRequestCode(KkabaRequestCode.REQUEST_WITHDRAW);
		HttpRequestThread.getInstance().addRequest(logoutRequest);
	}
	
	public void requestPushId(boolean isUsePushId){
		StringRequestPacket request = new StringRequestPacket();
		if(isUsePushId){
			request.setValue(ParseInstallation.getCurrentInstallation().getInstallationId());
		}else{
			request.setValue("");			
		}
		request.setRequestCode(KkabaRequestCode.REQUEST_SET_PUSH_TOKEN);
		HttpRequestThread.getInstance().addRequest(request);
	}
	
	public void requestUserInfo(){
		CommonRequestPacket request = new CommonRequestPacket();
		request.setRequestCode(KkabaRequestCode.REQUEST_USER_INFO);
		HttpRequestThread.getInstance().addRequest(request);
	}
	
	@Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(UserDataCacheStore.getInstance().getFriendData(((FoeListData)v.getTag()).getUserId()).getNickname());
		menu.add(0,0,0,"게임 종료");
		Intent tmpIntent = new Intent();
		tmpIntent.putExtra("gameId", ((FoeListData)v.getTag()).getGameId());
		menu.findItem(0).setIntent(tmpIntent);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		long gameId = item.getIntent().getLongExtra("gameId", 0);
		lockUI();
		LongRequestPacket deleteRequest = new LongRequestPacket();
		deleteRequest.setValue(gameId);
		deleteRequest.setRequestCode(KkabaRequestCode.REQUEST_GAME_DELETE);
		HttpRequestThread.getInstance().addRequest(deleteRequest);
		return super.onContextItemSelected(item);
	}
//	public static int myRps;
//	
//	public static void setMyRps(int a){
//		myRps = a;
//	}
	
}
