package wiley.cache;

public interface Storage<K,V> {
    V get(K key);
    V put(K key, V value);
    int size();
}
