package task03.lru;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LruCacheImplTest {
    private final LruCacheImpl<Integer, String> lruCache = new LruCacheImpl<>();

    @Before
    public void setUp() {
        lruCache.put(1, "val_1");
        lruCache.put(2, "val_2");
        lruCache.put(3, "val_3");
        lruCache.put(4, "val_4");
        lruCache.put(5, "val_5");
    }

    @Test
    public void put() {
        Assert.assertEquals("{1=val_1, 2=val_2, 3=val_3, 4=val_4, 5=val_5}", lruCache.toString());
        lruCache.put(6, "val_6");
        lruCache.put(7, "val_7");
        Assert.assertEquals("{3=val_3, 4=val_4, 5=val_5, 6=val_6, 7=val_7}", lruCache.toString());
    }

    @Test
    public void get() {
        String val = lruCache.get(1);
        Assert.assertEquals("val_1", val);
        Assert.assertEquals("{2=val_2, 3=val_3, 4=val_4, 5=val_5, 1=val_1}", lruCache.toString());
    }
}