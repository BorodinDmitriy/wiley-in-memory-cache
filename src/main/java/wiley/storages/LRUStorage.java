package wiley.storages;

import wiley.cache.Storage;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUStorage<K,V> extends LinkedHashMap<K,V> implements Storage<K,V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final boolean ACCESS_ORDER = true;
    private final int MAX_SIZE;

    public LRUStorage(int maxSize){
        super(maxSize,LOAD_FACTOR,ACCESS_ORDER);
        MAX_SIZE = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > MAX_SIZE ;
    }
}
