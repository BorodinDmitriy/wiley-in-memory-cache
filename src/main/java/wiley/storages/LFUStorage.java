package wiley.storages;

import wiley.cache.Storage;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFUStorage<K,V> implements Storage<K,V> {

    private final int MAX_SIZE;
    private HashMap<K,V> storage;
    private HashMap<K, Integer> keyFrequencies; //K and frequency of the key
    private HashMap<Integer, LinkedHashSet<K>> lists; // lists for sets of keys having current frequency
    private Integer currentMinimalFrequency = -1; // current minimal frequency in 'lists'

    public LFUStorage(int maxSize)
    {
        MAX_SIZE = maxSize;
        storage = new HashMap<>();
        keyFrequencies = new HashMap<>();
        lists = new HashMap<>();
        lists.put(1,new LinkedHashSet<K>());
    }

    private void increaseFrequency(K key) {
        Integer currentFrequency = keyFrequencies.get(key);
        Integer newFrequency = currentFrequency + 1;
        keyFrequencies.put(key,newFrequency);
        lists.get(currentFrequency).remove(key);

        // if currentMinimalFrequency element in lists does not have any data,
        // next one should be the currentMinimalFrequency
        if ((currentFrequency == currentMinimalFrequency) && (lists.get(currentFrequency).size() == 0)) {
            currentMinimalFrequency++;
        }
        // if there is no list for newCurrentFrequency add it
        if (!lists.containsKey(newFrequency)) {
            lists.put(newFrequency,new LinkedHashSet<K>());
        }
        lists.get(newFrequency).add(key);

    }
    @Override
    public V get(K key) {
        V value = storage.get(key);
        if (value != null) {
            increaseFrequency(key);
        }
        return value;
    }

    @Override
    public V put(K key, V value) {
        // if key exists hit the cache, change value and return
        if (storage.containsKey(key)) {
            V oldValue = storage.put(key, value);
            get(key);
            return oldValue;
        }
        if (storage.size() >= MAX_SIZE) {
            K evitableData = lists.get(currentMinimalFrequency).iterator().next();
            lists.get(currentMinimalFrequency).remove(evitableData);
            keyFrequencies.remove(evitableData);
            storage.remove(evitableData);
        }

        // if the key is new insert new data and set currentMinimalFrequency to 1
        V oldValue = storage.put(key, value);
        keyFrequencies.put(key,1);
        currentMinimalFrequency = 1;
        lists.get(currentMinimalFrequency).add(key);

        return oldValue;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
