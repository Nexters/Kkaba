package com.teamnexters.kkaba.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.teamnexters.kkaba.client.IntroActivity;
import com.teamnexters.kkaba.common.data.FoeListData;
import com.teamnexters.kkaba.common.data.GameListData;

public class AccessGameListDB {
	private SQLiteDatabase sqliteDB = null;
	private ArrayList<GameListData> gameLists;
	private static AccessGameListDB instance;
	
	private static final String TABLE_GAMELIST = "gameList";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_MY_RPS = "myRps";
	private static final String KEY_FOE_RPS = "foeRps";
	private static final String KEY_MY_STAGE = "myStage";
	private static final String KEY_FOE_STAGE = "foeStage";
	private static final String KEY_FIRST_TIME = "firstTime";
	private static final String KEY_LAST_TIME = "lastTime";
	private static final String KEY_GAME_INDEX = "gameIndex";
	private static final String KEY_GAME_ID = "gameId";
	public static int finalIndex = 0;
	
	public static AccessGameListDB getInstance() {
		if(instance==null){
			instance = new AccessGameListDB();
		}
		return instance;
	}
	
	private AccessGameListDB() {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);
	}
	
	public ArrayList<GameListData> getAllGameList(Long id) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		String query = "SELECT message,myRps,foeRps,myStage,foeStage,firstTime,lastTime,gameIndex,gameId FROM gameList WHERE gameId ="
				+ id +  " ORDER BY gameIndex ASC";
		Cursor cursor = sqliteDB.rawQuery(query, null);

		gameLists = new ArrayList<GameListData>();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				String message = cursor.getString(0);
				Integer myRps = cursor.getInt(1);
				Integer foeRps = cursor.getInt(2);
				Integer myStage = cursor.getInt(3);
				Integer foeStage = cursor.getInt(4);
				Long firstTime = cursor.getLong(5);
				Long lastTime = cursor.getLong(6);
				int gameIndex = cursor.getInt(7);
				Long gameId = cursor.getLong(8);

				GameListData data = new GameListData();
				data.setMessage(message);
				data.setMyRps(myRps);
				data.setFoeRps(foeRps);
				data.setMyStage(myStage);
				data.setFoeStage(foeStage);
				data.setFirstTime(firstTime);
				data.setLastTime(lastTime);
				data.setGameId(gameId);

				gameLists.add(data);
				cursor.moveToNext();
			}
			cursor.close();
			sqliteDB.close();
		}

		return gameLists;
	}
	
	public void addGameList(GameListData data) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, data.getMessage());
		values.put(KEY_MY_RPS, data.getMyRps());
		values.put(KEY_FOE_RPS, data.getFoeRps());
		values.put(KEY_MY_STAGE, data.getMyStage());
		values.put(KEY_FOE_STAGE, data.getFoeStage());
		values.put(KEY_FIRST_TIME, data.getFirstTime());
		values.put(KEY_LAST_TIME, data.getLastTime());
		values.put(KEY_GAME_ID, data.getGameId());

		sqliteDB.insert(TABLE_GAMELIST, null, values);
		sqliteDB.close();

	}

	public void updateOrInsertGameList(GameListData data) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, data.getMessage());
		values.put(KEY_MY_RPS, data.getMyRps());
		values.put(KEY_FOE_RPS, data.getFoeRps());
		values.put(KEY_MY_STAGE, data.getMyStage());
		values.put(KEY_FOE_STAGE, data.getFoeStage());
		values.put(KEY_FIRST_TIME, data.getFirstTime());
		values.put(KEY_LAST_TIME, data.getLastTime());
		values.put(KEY_GAME_ID, data.getGameId());
		
		if(sqliteDB.update(TABLE_GAMELIST, values, KEY_GAME_ID + " = ?",new String[] { String.valueOf(data.getGameId()) })==0){
			sqliteDB.insert(TABLE_GAMELIST, null, values);
		}
		sqliteDB.close();
		return ;
	}

	public void deleteGameList(GameListData data) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		sqliteDB.delete(TABLE_GAMELIST, KEY_GAME_ID + " = ?",
				new String[] { String.valueOf(data.getGameId()) });
		sqliteDB.close();
	}
	
	public void updateLatestData(GameListData data){
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);
		
		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, data.getMessage());
		values.put(KEY_MY_RPS, data.getMyRps());
		values.put(KEY_FOE_RPS, data.getFoeRps());
		values.put(KEY_MY_STAGE, data.getMyStage());
		values.put(KEY_FOE_STAGE, data.getFoeStage());
		values.put(KEY_FIRST_TIME, data.getFirstTime());
		values.put(KEY_LAST_TIME, data.getLastTime());
		values.put(KEY_GAME_ID, data.getGameId());
		
//		sqliteDB.update(TABLE_GAMELIST, values, "'index'=(SELECT 'index' FROM gameList ORDER BY 'index' DESC limit 1)", null);
		sqliteDB.update(TABLE_GAMELIST, values, "gameIndex=? AND gameId=?", new String[] { String.valueOf(data.getGameIndex()),String.valueOf(data.getGameId()) });
		sqliteDB.close();
		
	}
	
	public GameListData getLatestData(){
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);
		
		String query = "SELECT * FROM gameList ORDER BY gameIndex DESC limit 1";
		Cursor cursor = sqliteDB.rawQuery(query, null);
		
		  if (cursor.getCount() > 0) {
		         cursor.moveToFirst();
		  }
		
		String message = cursor.getString(0);
		Integer myRps = cursor.getInt(1);
		Integer foeRps = cursor.getInt(2);
		Integer myStage = cursor.getInt(3);
		Integer foeStage = cursor.getInt(4);
		Long firstTime = cursor.getLong(5);
		Long lastTime = cursor.getLong(6);
		int gameIndex = cursor.getInt(7);
		Long gameId = cursor.getLong(8);
		

		GameListData data = new GameListData();
		data.setMessage(message);
		data.setMyRps(myRps);
		data.setFoeRps(foeRps);
		data.setMyStage(myStage);
		data.setFoeStage(foeStage);
		data.setFirstTime(firstTime);
		data.setLastTime(lastTime);
		data.setGameIndex(gameIndex);
		data.setGameId(gameId);
		
		return data;
	}
	

	public void deleteAllGameHistory(long gameId) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		sqliteDB.delete(TABLE_GAMELIST, KEY_GAME_ID + " = ?",
				new String[] { String.valueOf(gameId) });
		sqliteDB.close();
	}
	

	public void deleteAllUserHistory(long userId) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		sqliteDB.delete(TABLE_GAMELIST, "gameId in (SELECT gameId FROM foeList WHERE selfId = ?)",
				new String[] { String.valueOf(userId) });
		sqliteDB.close();
	}
	
}
