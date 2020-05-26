package xyz.softwaredeveloveper.impl;

/**
 *
 */
public class CacheData<T> {
    private Long timestamp;
    private T value;

    public CacheData(Long timestamp, T value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public CacheData<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
