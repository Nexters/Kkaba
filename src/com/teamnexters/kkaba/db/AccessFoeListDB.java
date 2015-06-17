package com.teamnexters.kkaba.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.teamnexters.kkaba.client.IntroActivity;
import com.teamnexters.kkaba.common.data.FoeListData;
	
public class AccessFoeListDB {
	private SQLiteDatabase sqliteDB = null;
	private ArrayList<FoeListData> foeLists;
	private static AccessFoeListDB instance;
	
	private static final String TABLE_FOElIST = "foeList";

	private static final String KEY_FINAL_TIME = "finalTime";
	private static final String KEY_GAME_LOG = "gameLog";
	private static final String KEY_GAME_ID = "gameId";
	private static final String KEY_USER_ID = "userId";
	private static final String KEY_STATUS = "status";
	private static final String KEY_INIT_PLAYER = "initPlayer";
	private static final String KEY_SELF_ID = "selfId";

	public static AccessFoeListDB getInstance(){
		if(instance==null){
			instance = new AccessFoeListDB();
		}
		return instance;
	}
	private AccessFoeListDB() {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);
	}

	public ArrayList<FoeListData> getAllFoeList(String selfId) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		String query = "SELECT finalTime,gameLog,gameId,userId,status,initPlayer,selfId FROM foeList WHERE selfId =" + selfId + " ORDER BY finalTime DESC";
		Cursor cursor = sqliteDB.rawQuery(query, null);

		foeLists = new ArrayList<FoeListData>();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				Long finalTime = cursor.getLong(0);
				String gameLog = cursor.getString(1);
				Long gameId = cursor.getLong(2);
				Long userId = cursor.getLong(3);
				Integer status = cursor.getInt(4);
				Long initPlayer = cursor.getLong(5);

				FoeListData data = new FoeListData();
				data.setFinalTime(finalTime);
				data.setGameId(gameId);
				data.setGameLog(gameLog);
				data.setInitPlayer(initPlayer);
				data.setStatus(status);
				data.setUserId(userId);

				foeLists.add(data);
				cursor.moveToNext();
			}
			cursor.close();
			sqliteDB.close();
		}

		return foeLists;
	}
	
	public FoeListData getFoeData(String selfId, long gameId) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		String query = "SELECT finalTime,gameLog,gameId,userId,status,initPlayer,selfId FROM foeList WHERE selfId =" + selfId + " AND gameId="+gameId+" ORDER BY finalTime DESC";
		Cursor cursor = sqliteDB.rawQuery(query, null);

		FoeListData data = null ;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			data = new FoeListData();
			Long finalTime = cursor.getLong(0);
			String gameLog = cursor.getString(1);
			gameId = cursor.getLong(2);
			Long userId = cursor.getLong(3);
			Integer status = cursor.getInt(4);
			Long initPlayer = cursor.getLong(5);

			data.setFinalTime(finalTime);
			data.setGameId(gameId);
			data.setGameLog(gameLog);
			data.setInitPlayer(initPlayer);
			data.setStatus(status);
			data.setUserId(userId);
			cursor.close();
			sqliteDB.close();
		}

		return data;
	}

	public void addFoeList(FoeListData data) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		ContentValues values = new ContentValues();
		values.put(KEY_FINAL_TIME, data.getFinalTime());
		values.put(KEY_GAME_LOG, data.getGameLog());
		values.put(KEY_GAME_ID, data.getGameId());
		values.put(KEY_USER_ID, data.getUserId());
		values.put(KEY_STATUS, data.getStatus());
		values.put(KEY_INIT_PLAYER, data.getInitPlayer());
		values.put(KEY_SELF_ID, data.getSelfId());

		sqliteDB.insert(TABLE_FOElIST, null, values);
		sqliteDB.close();

	}

	public void updateOrInsertFoeList(FoeListData data) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		ContentValues values = new ContentValues();
		values.put(KEY_FINAL_TIME, data.getFinalTime());
		values.put(KEY_GAME_LOG, data.getGameLog());
		values.put(KEY_GAME_ID, data.getGameId());
		values.put(KEY_USER_ID, data.getUserId());
		values.put(KEY_STATUS, data.getStatus());
		values.put(KEY_INIT_PLAYER, data.getInitPlayer());
		values.put(KEY_SELF_ID, data.getSelfId());
		
		if(sqliteDB.update(TABLE_FOElIST, values, KEY_GAME_ID + " = ?",
				new String[] { String.valueOf(data.getGameId()) })==0){
			sqliteDB.insert(TABLE_FOElIST, null, values);
		}
		sqliteDB.close();
		return ;
	}

	public void deleteFoeList(long gameId) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		sqliteDB.delete(TABLE_FOElIST, KEY_GAME_ID + " = ?",
				new String[] { String.valueOf(gameId) });
		sqliteDB.close();
	}
	

	public void deleteAllUserHistory(long userId) {
		sqliteDB = SQLiteDatabase.openOrCreateDatabase("/data/data/"
				+ IntroActivity.packageName + "/KKABA.sqlite", null);

		sqliteDB.delete(TABLE_FOElIST,  "selfId = ?",
				new String[] { String.valueOf(userId) });
		sqliteDB.close();
	}
}
