package task03.lru;

public interface LruCache <K, V>{
    V get(K key);
    void put(K key, V value);
    void clear();
}
