package xyz.softwaredeveloveper.spec;

import xyz.softwaredeveloveper.impl.KeyAlreadyExistsException;
import xyz.softwaredeveloveper.impl.KeyNotExistsException;

import java.util.Optional;

/**
 * Cache interface
 */
public interface ICache<K, T> {
    void put(K key, T value) throws KeyAlreadyExistsException;

    T get(K key) throws KeyNotExistsException;

    Optional<T> optionalGet(K key);

    Optional<T> remove(K key);

    void clear();

    int size();
    void update(K key, T value);

    Optional<Long> getTimeStamp(K key);
}
