package xyz.softwaredeveloveper.CustomCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xyz.softwaredeveloveper.impl.Cache;
import xyz.softwaredeveloveper.impl.KeyAlreadyExistsException;

import java.util.Optional;

import static org.junit.Assert.*;

public class CacheTest {
    private Cache<Integer, Integer> cache = new Cache<>(3);

    @Before
    public void before() {
    }

    @After
    public void after() {
        this.cache.clear();
    }

    @Test
    public void shouldAddANewCachedValue() throws Exception {
        this.cache.put(1, 1);
        this.cache.put(2, 2);
        this.cache.put(3, 3);
        assertEquals(3, this.cache.size());
    }

    @Test(expected = KeyAlreadyExistsException.class)
    public void shouldNoReplaceAnExistingCachedValue() throws Exception {
        this.cache.put(1, 1);
        this.cache.put(2, 2);
        this.cache.put(3, 3);
        this.cache.put(3, 4);
    }

    @Test
    public void shouldRemoveAnExistingKey() throws Exception {
        this.cache.put(1, 1);
        this.cache.put(2, 2);
        this.cache.put(3, 3);
        this.cache.remove(3);
        assertEquals(2, this.cache.size());
    }

    @Test
    public void shouldEvictTheOldestKey() throws Exception {
        this.cache.put(1, 1);
        this.cache.put(2, 2);
        this.cache.put(3, 3);
        this.cache.put(4, 4);
        assertEquals(3, this.cache.size());
    }

    @Test
    public void shouldEvictKeyWhenExceedsLength() throws Exception {
        this.cache.put(1, 1);
        this.cache.put(2, 2);
        this.cache.update(1, 666);
        this.cache.put(4, 4);
        this.cache.put(5, 5);
        // should evict 2
        assertEquals(3, this.cache.size());
        assertEquals(new Integer(666), this.cache.get(1));
        assertFalse(this.cache.optionalGet(2).isPresent());
    }

    @Test
    public void shouldUpdateTimestampOnOperations() throws Exception {
        this.cache.put(1, 1);
        this.cache.put(2, 2);
        this.cache.put(4, 4);

        Optional<Long> t1 = this.cache.getTimeStamp(1);
        Optional<Long> t2 = this.cache.getTimeStamp(2);
        Optional<Long> t3 = this.cache.getTimeStamp(4);

        this.cache.get(1);
        this.cache.update(2, 434334);

        Optional<Long> t1_update = this.cache.getTimeStamp(1);
        Optional<Long> t2_update = this.cache.getTimeStamp(2);
        Optional<Long> t3_update = this.cache.getTimeStamp(2);

        // should evict 2
        assertNotEquals(t1.get(), t1_update.get());
        assertNotEquals(t2.get(), t2_update.get());
        assertNotEquals(t3.get(), t3_update.get());
    }

    @Test
    public void shouldRemoveAnAssociatedValue() throws Exception {
        this.cache.put(1, 1);
        this.cache.put(2, 2);
        this.cache.put(4, 4);
        this.cache.remove(2);
        assertEquals(2, this.cache.size());
    }

    @Test
    public void shouldNotReturnANotExistingValue() throws Exception {
        assertFalse(this.cache.optionalGet(232323).isPresent());
    }

}
