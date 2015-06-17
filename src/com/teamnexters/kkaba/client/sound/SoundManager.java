package com.teamnexters.kkaba.client.sound;

import com.teamnexters.kkaba.client.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	SoundPool pool;
	int sound_id1 = -1;

	
	public SoundManager(Context context){
		if(pool == null){
			pool= new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		}
		
		if(sound_id1 == -1){
        sound_id1=pool.load(context,R.raw.effect, 1);
		}
	}
	public void PlaySound(){
		//if(pool != null && sound_id1 != -1){
			pool.play(sound_id1, 1, 1, 0, 0, 1);
		//}   
	}
}
