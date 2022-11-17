package task03.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCacheLinkedHashMap <K, V> extends LinkedHashMap<K, V> {
    private static final int FIXED_SIZE = 5;

    public LruCacheLinkedHashMap() {
        super(FIXED_SIZE, 0.75f, true);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > FIXED_SIZE;
    }
}
