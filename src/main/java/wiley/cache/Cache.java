package wiley.cache;

import wiley.storages.LFUStorage;
import wiley.storages.LRUStorage;

// Strategy Context Class
public class Cache<K,V> {
    public enum EvictionStrategy {LRU, LFU};
    public static String MAX_SIZE_ERROR = "maxSize should be more than 0";
    public static String EVICTION_STRATEGY_ERROR = "Eviction strategy was not found";

    private Storage<K,V> storage;

    public Cache(EvictionStrategy evictionStrategy, Integer maxSize) {
        if(maxSize <= 0) {
            throw new IllegalArgumentException(MAX_SIZE_ERROR);
        }
        switch (evictionStrategy) {
            case LFU:
                storage = new LFUStorage<>(maxSize);
                break;
            case LRU:
                storage = new LRUStorage<>(maxSize);
                break;
            default:
                throw new IllegalArgumentException(EVICTION_STRATEGY_ERROR);
        }
    }

    public V get(K key) {
        return storage.get(key);
    }

    public V put(K key, V value) {
        return storage.put(key,value);
    }

    public int size() {
        return storage.size();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
