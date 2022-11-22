package task03.lru;

public class LruCacheImpl<K, V> implements LruCache <K, V>{
    private final LruCacheLinkedHashMap<K, V> cacheMap = new LruCacheLinkedHashMap<>();

    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }

    @Override
    public void put(K key, V value) {
        cacheMap.put(key, value);
    }

    @Override
    public void clear() {
        cacheMap.clear();
    }

    @Override
    public String toString() {
        return cacheMap.toString();
    }
}
