package wiley;

import wiley.cache.Cache;
import wiley.cache.Storage;
import wiley.storages.LRUStorage;

import static wiley.cache.Cache.EvictionStrategy.LFU;
import static wiley.cache.Cache.EvictionStrategy.LRU;


public class App 
{
    public static void main( String[] args )
    {
        int maxSize = 3;
        Cache<Integer, Integer> storage = new Cache(LFU,maxSize);
        Integer[] testArray = {2,3,4,2,1,3,7,5,4,3};

        for (int i = 0; i < testArray.length; i++) {
            storage.put(testArray[i],testArray[i]);
        }

        System.out.println( storage );
    }
}
