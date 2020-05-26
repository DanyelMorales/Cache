package xyz.softwaredeveloveper.impl;

import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Stores a collection of id's sorted by timestamp.
 * Every value within the collection is associated to a timestamp.
 */
public class TimeStampLog<T> {
    private final SortedMap<Long, T> log = new TreeMap<>();

    /**
     * Add a new log value (AKA id)
     *
     * @param id identifier to be stored and ordered by timestamp
     * @return Long timestamp generated as the key for this value
     */
    public Long add(T id) {
        Long timeStamp = getCurrentTimeStamp();
        log.put(timeStamp, id);
        return timeStamp;
    }

    /**
     * Update an id associated with a timestamp
     *
     * @param timeStamp associated with an existing value
     * @param id        new value to be replaced with
     * @return true if the operation was successful otherwise return false
     */
    public boolean updateValueByTimeStamp(Long timeStamp, T id) {
        if (log.containsKey(timeStamp)) {
            log.put(timeStamp, id);
            return true;
        }
        return false;
    }

    /**
     * Create a new timestamp and reassign this new timestamp to an existing value.
     *
     * @param id value used to find the timeStamp to be replaced
     * @return an optional of the new timeStamp generated
     */
    public Optional<Long> regenerateTimeStamp(T id) {
        Optional<Long> optional = this.findTimeStamp(id);
        if (optional.isPresent()) {
            remove(optional.get());
            return Optional.of(add(id));
        }
        return Optional.empty();
    }

    /**
     * Create a new timestamp and reassign this new timestamp to an existing value.
     *
     * @param timeStamp to be replaced for a new generated timestamp
     * @return an optional of the new timeStamp generated
     */
    public Long regenerateTimeStamp(Long timeStamp) {
        T id = getId(timeStamp);
        log.remove(timeStamp);
        return add(id);
    }

    /**
     * Given an id it'll return the timestamp associated
     *
     * @param id value associated to a timestamp
     * @return Optional of timestamp
     */
    public Optional<Long> findTimeStamp(T id) {
        Set<Long> keySet = log.keySet();
        for (Long key : keySet) {
            if (log.get(key).equals(id)) {
                return Optional.of(key);
            }
        }
        return Optional.empty();
    }

    /**
     * Get the oldest entry in the collection
     *
     * @return if the value is found is returned otherwise null
     */
    public T getTheOldestEntry() {
        Long timeStamp = this.log.firstKey();
        return log.get(timeStamp);
    }

    /**
     * Evicts the oldest value
     *
     * @return the evicted value
     */
    public T evictTheOldestRegistry() {
        Long timeStamp = log.firstKey();
        return remove(timeStamp);
    }

    /**
     * It's possible that sometimes a time stamp could collide
     * because of access speed.
     */
    private long getCurrentTimeStamp() {
        long timeStamp;
        do {
            timeStamp = System.currentTimeMillis();
        } while (log.containsKey(timeStamp));
        return timeStamp;
    }

    /**
     * Get the value associated to a timestamp
     *
     * @param timeStamp associated to a value
     * @return the value associated to a timeStamp otherwise null
     */
    public T getId(Long timeStamp) {
        return log.get(timeStamp);
    }

    /**
     * Removes an entry associated to a timestamp
     *
     * @param timeStamp associated to a value
     * @return the value removed
     */
    public T remove(Long timeStamp) {
        return log.remove(timeStamp);
    }

    /**
     * Removes all values stored in the log.
     */
    public void clear() {
        log.clear();
    }

    /**
     * @return the numbers of elements stored in the log
     */
    public int size() {
        return log.size();
    }

}
