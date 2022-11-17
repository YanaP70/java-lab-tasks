package task03.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private static final int FIXED_SIZE = 5;

    private final CacheLinkedHashMap<K, V> cacheMap = new CacheLinkedHashMap<>();

    private static class CacheLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        public CacheLinkedHashMap() {
            super(FIXED_SIZE, 0.75f, true);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > FIXED_SIZE;
        }
    }

    public V get(K key) {
        return cacheMap.get(key);
    }

    public void put(K key, V value) {
        cacheMap.put(key, value);
    }

    @Override
    public String toString() {
        return cacheMap.toString();
    }
}
