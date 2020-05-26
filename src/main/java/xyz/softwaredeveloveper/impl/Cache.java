package xyz.softwaredeveloveper.impl;

import xyz.softwaredeveloveper.spec.ICache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Cache implementation
 * <p>
 * Every operation over a cached value will update the age of the entry. Keep it in mind
 * so you can understand which value is considered as "the oldest".
 *
 * @param <K> Key type
 * @param <T> Value type
 */
public class Cache<K, T> implements ICache<K, T> {
    private final long maxPermSize;
    private Map<K, CacheData<T>> cachedValues = new HashMap<>();
    private TimeStampLog<K> timeStampLog = new TimeStampLog();

    /**
     * @param size storage limit for the current cache instance, if this limit is exceeded
     *             old keys will be evicted. Get, Put and update operations updates the timestamp
     *             of the requested value.
     */
    public Cache(long size) {
        this.maxPermSize = size;
    }

    /**
     * Add a new value to the cache container.
     * If the cache container is already full then old keys will be evicted.
     *
     * @param key   identifier for the value
     * @param value
     * @throws KeyAlreadyExistsException if the key is already in the container
     */
    @Override
    public void put(K key, T value) throws KeyAlreadyExistsException {
        if (!this.cachedValues.isEmpty() && this.cachedValues.containsKey(key)) {
            throw new KeyAlreadyExistsException("Value already exists, try to update");
        } else {
            // verify size limits
            if (this.cachedValues.size() >= this.maxPermSize) {
                this.evictTheOldestRegistry();
            }
            // add new value and timeStamp
            Long timeStamp = this.timeStampLog.add(key);
            CacheData<T> metadata = new CacheData(timeStamp, value);
            this.cachedValues.put(key, metadata);
        }
    }

    /**
     * Returns a value associated to a key.
     *
     * @param key associated to a value
     * @return the value
     * @throws KeyNotExistsException if there is no any key in the container
     */
    @Override
    public T get(K key) throws KeyNotExistsException {
        Optional<T> optional = this.optionalGet(key);
        if (!optional.isPresent()) {
            throw new KeyNotExistsException("KEY_NOT_FOUND" + key);
        }
        return optional.get();
    }

    /**
     * The same operation as the get operation.
     * This method returns an Optional object with the requested value though.
     *
     * @param key associated to a value
     * @return Optional of requested value otherwise an empty value
     * @see this#get(Object)
     */
    @Override
    public Optional<T> optionalGet(K key) {
        if (this.cachedValues.isEmpty() || !this.cachedValues.containsKey(key)) {
            return Optional.empty();
        }
        this.updateTimeStamp(key);
        return Optional.of(this.cachedValues.get(key).getValue());
    }

    /**
     * Removes the key and its association
     *
     * @return an optional value of the removed element.
     * Otherwise if the key doesn't exist then an empty value is returned.
     */
    @Override
    public Optional<T> remove(K key) {
        if (this.cachedValues.isEmpty() || !this.cachedValues.containsKey(key)) {
            return Optional.empty();
        }
        CacheData<T> value = this.cachedValues.remove(key);
        this.timeStampLog.remove(value.getTimestamp());
        return Optional.of(value.getValue());
    }

    /**
     * Remove all the values and key onto the cache container
     */
    @Override
    public void clear() {
        this.cachedValues.clear();
        this.timeStampLog.clear();
    }

    /**
     * @return the number of elements in the cache container
     */
    @Override
    public int size() {
        return this.timeStampLog.size();
    }

    /**
     * Updates the value associated to a key.
     *
     * @param key
     * @param value
     */
    @Override
    public void update(K key, T value) {
        if (!this.cachedValues.isEmpty() && this.cachedValues.containsKey(key)) {
            // update timeStamp
            // update value
            this.updateCachedValue(key, value);
        }
    }

    /**
     * @return an optional object of the timestamp associated to a key,
     * this timestamp is an internal value used to sort the cache container.
     */
    @Override
    public Optional<Long> getTimeStamp(K key) {
        return this.timeStampLog.findTimeStamp(key);
    }

    /**
     * TODO document
     */
    private K evictTheOldestRegistry() {
        K key = this.timeStampLog.evictTheOldestRegistry();
        this.cachedValues.remove(key);
        return key;
    }

    /**
     * TODO document
     */
    private void updateTimeStamp(K key) {
        if (this.cachedValues.isEmpty() || !this.cachedValues.containsKey(key)) {
            return;
        }
        CacheData<T> metadata = this.cachedValues.get(key);
        Long newTimeStamp = this.timeStampLog.regenerateTimeStamp(metadata.getTimestamp());
        metadata.setTimestamp(newTimeStamp);
    }

    /**
     * TODO document
     */
    private void updateCachedValue(K key, T value) {
        if (this.cachedValues.isEmpty() || !this.cachedValues.containsKey(key)) {
            return;
        }
        CacheData<T> metadata = this.cachedValues.get(key);
        Long newTimeStamp = timeStampLog.regenerateTimeStamp(metadata.getTimestamp());
        metadata.setTimestamp(newTimeStamp).setValue(value);
    }

}
