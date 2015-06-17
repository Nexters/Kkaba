package com.teamnexters.kkaba.client.image;

import java.util.HashMap;
import java.util.Map;

import com.teamnexters.kkaba.client.R;
import com.teamnexters.kkaba.common.game.Collection;
import com.teamnexters.kkaba.common.game.CollectionCode;

public class CollectionImageManager implements CollectionCode
{

    private static CollectionImageManager instance = null;
    Map<Long, Integer> collectionImages;

    private CollectionImageManager()
    {
    	collectionImages = new HashMap<Long, Integer>();
    	collectionImages.put(COLLECTION_CODE_TEST, R.drawable.badge_ghost_310);
    	collectionImages.put(COLLECTION_CODE_PAPER, R.drawable.badge_paper);
    	collectionImages.put(COLLECTION_CODE_ROCK, R.drawable.badge_rock);
    	collectionImages.put(COLLECTION_CODE_SCISSOR, R.drawable.badge_scissor);
    	collectionImages.put(COLLECTION_CODE_1000, R.drawable.badge_1000plays_310);
    	collectionImages.put(COLLECTION_CODE_PROFILE, R.drawable.badge_newprofile25_310);
    	collectionImages.put(COLLECTION_CODE_TIGER, R.drawable.badge_tigermouth_310);
    	collectionImages.put(COLLECTION_CODE_WIN, R.drawable.badge_new10wins_310);
    }

    public static synchronized CollectionImageManager getInstance()
    {
        if (instance == null)
        {
            instance = new CollectionImageManager();
        }
        return instance;
    }

    public int getCollectionImageId(Collection collection)
    {
    	Integer returnId = collectionImages.get(collection.toLongValue());
    	if(returnId==null){
    		returnId = R.drawable.badge_defualt_large;
    	}
        return returnId;
    }

}
