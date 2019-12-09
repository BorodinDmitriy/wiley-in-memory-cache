package wiley;

import org.junit.Before;
import wiley.cache.Storage;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import static wiley.cache.Cache.EvictionStrategy.LRU;

import org.junit.Test;
import wiley.storages.LRUStorage;


public class LRUStorageTest {
    private int maxSize;
    private Storage<Integer, Integer> storage;

    @Before
    public void initializeTestsForLRU() {
        maxSize = 3;
        storage = new LRUStorage<>(maxSize);
        for(int i = 0; i < maxSize - 2; i++) {
            storage.put(i, i);
        }
    }
    @Test
    public void setNewValueInCacheByKey() {
        assertEquals(storage.size(),1);
        assertEquals("{0=0}",storage.toString());
        Integer oldValue = storage.put(0,2);
        assertEquals(oldValue,Integer.valueOf(0));
        assertEquals("{0=2}",storage.toString());
    }

    @Test
    public void putInNotFilledCache() {
        assertEquals(storage.size(),1);
        Integer oldValue = storage.put(2,2);
        assertEquals(oldValue,null);
        assertEquals("{0=0, 2=2}",storage.toString());
    }
    @Test
    public void getCurrentSizeOfStorage() {
        assertEquals(storage.size(),1);
        Integer oldValue = storage.put(3,3);
        assertEquals(storage.size(),2);
    }
    @Test
    public void putInFilledCache() {
        assertEquals(storage.size(),1);
        assertEquals("{0=0}",storage.toString());
        for(int i = 4; i < 4 + maxSize; i++) {
            storage.put(i, i);
        }
        assertEquals("{4=4, 5=5, 6=6}",storage.toString());
    }
    @Test
    public void checkLRUForTheLastElement() {
        for(int i = 1; i < maxSize; i++) {
            storage.put(i, i);
        }
        assertEquals("{0=0, 1=1, 2=2}",storage.toString());
        Integer keyToBeRemoved = 2;
        for(int i = 1; i < maxSize; i++) {
            if (i != keyToBeRemoved) {
                storage.get(i);
            }
        }
        storage.put(maxSize + 1, maxSize + 1);
        assertEquals("{2=2, 1=1, 4=4}",storage.toString());
    }
}
