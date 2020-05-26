package xyz.softwaredeveloveper.CustomCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xyz.softwaredeveloveper.impl.TimeStampLog;

import java.util.Optional;

import static org.junit.Assert.*;

public class TimeStampLogTest {
    private TimeStampLog<Integer> log = new TimeStampLog<>();

    @Before
    public void before() {
    }

    @After
    public void after() {
        log.clear();
    }

    @Test
    public void shouldAddNewEntryAndReturnItAsTheOldest() {
        Integer key = 1;
        Long timeStamp = log.add(key);
        assertNotNull(timeStamp);
        assertEquals(1, log.size());
        assertEquals(key, log.getTheOldestEntry());
    }

    @Test
    public void shouldTimeStampBeDifferentOnEveryInsertion() {
        Long t1 = log.add(1);
        Long t2 = log.add(2);
        Long t3 = log.add(3);
        Long t4 = log.add(4);

        assertNotEquals(t1, t2);
        assertNotEquals(t3, t4);
        assertNotEquals(t2, t3);
        assertNotEquals(t2, t4);
        assertEquals(4, log.size());
    }

    @Test
    public void shouldGetTheOldestEntrySimpleCase() {
        Integer expectedOldest = 1;
        log.add(1);
        log.add(2);
        log.add(3);
        log.add(4);
        assertEquals(expectedOldest, log.getTheOldestEntry());
    }

    @Test
    public void shouldGetTheOldestEntry() {
        Integer expectedOldest = 3;
        Long timeStamp = log.add(1);
        Long timeStamp2 = log.add(2);
        log.add(3);
        log.add(4);
        log.remove(timeStamp);
        log.remove(timeStamp2);
        assertEquals(expectedOldest, log.getTheOldestEntry());
    }

    @Test
    public void shouldGetTheOldestEntryByAlteringTimeStamp() {
        Integer expectedOldest = 3;
        Long timeStamp = log.add(1);
        Long timeStamp2 = log.add(2);
        log.add(3);
        log.add(4);
        log.remove(timeStamp);
        log.remove(timeStamp2);
        assertEquals(expectedOldest, log.getTheOldestEntry());
    }

    @Test
    public void shouldNotFindTimeStampById() {
        log.add(1);
        log.add(2);
        Long timeStamp = log.add(3);
        log.add(4);

        log.remove(timeStamp);

        Optional<Long> timeStampResult = log.findTimeStamp(3);
        assertFalse(timeStampResult.isPresent());
    }

    @Test
    public void shouldFindTimeStampById() {
        log.add(1);
        log.add(2);
        Long timeStamp = log.add(3);
        log.add(4);
        Optional<Long> timeStampResult = log.findTimeStamp(3);
        assertTrue(timeStampResult.isPresent());
        assertEquals(timeStamp, timeStampResult.get());
    }

    @Test
    public void shouldRegenerateTimeStampByTimeStamp() {
        log.add(1);
        log.add(2);
        Long timeStamp = log.add(3);
        log.add(4);
        Long newTimeStamp = log.regenerateTimeStamp(timeStamp);

        assertNotEquals(timeStamp, newTimeStamp);
        assertTrue(newTimeStamp > timeStamp);
        assertEquals(newTimeStamp, log.findTimeStamp(3).get());
        assertEquals(4, log.size());
    }

    @Test
    public void shouldRegenerateTimeStampById() {
        log.add(1);
        log.add(2);
        Long timeStamp = log.add(3);
        log.add(4);
        Optional<Long> newTimeStamp = log.regenerateTimeStamp(3);

        assertTrue(newTimeStamp.isPresent());
        assertNotEquals(timeStamp, newTimeStamp.get());
        assertTrue(newTimeStamp.get() > timeStamp);
        assertEquals(newTimeStamp.get(), log.findTimeStamp(3).get());
        assertEquals(4, log.size());
    }

    @Test
    public void shouldNotRegenerateTimeStamp() {
        log.add(1);
        log.add(2);
        Long timeStamp = log.add(3);
        log.add(4);
        Optional<Long> newTimeStamp = log.regenerateTimeStamp(50);
        assertFalse(newTimeStamp.isPresent());
    }

    @Test
    public void shouldUpdateValueByTimeStamp() {
        log.add(1);
        log.add(2);
        Long timeStamp = log.add(3);
        log.add(4);
        boolean isUpdated = log.updateValueByTimeStamp(timeStamp, 10);
        assertTrue(isUpdated);
        assertEquals(4, log.size());
        Integer actualValue = log.getId(timeStamp);
        assertEquals(new Integer(10), actualValue);
    }

    @Test
    public void testEvictTheOldestRegistry() {
        log.add(23434);
        log.add(12111);
        Long timeStamp = log.add(555523);
        log.add(44444);
        assertEquals(new Integer(23434), log.evictTheOldestRegistry());
    }

    @Test
    public void testEvictTheOldestRegistryByRemovingEntry() {
        Long timeStamp = log.add(23434);
        log.add(12111);
        Long timeStamp2 = log.add(555523);
        log.add(44444);

        log.remove(timeStamp);
        Integer evicted = log.evictTheOldestRegistry();

        assertEquals(2, log.size());
        assertEquals(new Integer(12111), evicted);
        assertNull(log.getId(timeStamp));
        assertNotNull(log.getId(timeStamp2));
    }
}
