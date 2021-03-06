<a href="https://www.buymeacoffee.com/DanielVeraM" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" style="height: 41px!important;width: 174px!important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" height="41px" width="174px" /></a>

# Custom Cache
This project is a coding test suggested by a java recruiter. The main goal is to create a Cache Manager with a fixed size.

> Warning: This code is not thread safe

## Usage
Every cache instance must have a size. This size is the number of elements inside the cache container. 
When the cache is full the oldest element will be evicted. When you add a new value to the registry, the cache manager creates a timestamp to handle the life of the registry.

Every operation over the cache manager should update the timestamp of the accessed object, therefore
those not recently accessed could be evicted.  

**Operations that updates the timestamp are:**

* put
* get
* update

So, if you try to add a new value using the "put" method, but the cache is full then automatically the cache manager will evict the oldest value.

### coding example
> **Beware of**  The code fence below. It shows a coding example with no exception handling.
```
          int cacheSize = 2;
          Cache<Integer, Integer> cache = new Cache<>(cacheSize);
          cache.put(1, 1);
          cache.put(2, 2); 
          cache.get(1); // returns 1
          cache.put(3, 3); // evicts key 2
          cache.get(2); // returns -1 (not found)
          cache.get(3); // returns 3.
          cache.put(4, 4); // evicts key 1.
          cache.get(1); // returns -1 (not found)
          cache.get(3); // returns 3
          cache.get(4); // returns 4
```

#### Create your own Cache Manager instance

1. Create a Cache Instance defining key type, and the Value type. Assume we want to store links from images, then we'd have the following code snippet:
```
          int cacheSize = 5;
          Cache<String, String> cache = new Cache<>(cacheSize);
```

2. Add new values
```
          cache.put("img1", "https://picsum.photos/200/300");
          cache.put("placeholder1", "https://picsum.photos/100/100");
          cache.put("placeholder3", "https://picsum.photos/500/200");
```

3. Play around with the operations by reading the javadoc.

## Background operation
### Data representation
Below is the information we need to represent using the cache manager.

| k | V | Timestamp |   |   |
|--------|----------|----------|---|---|
| image1 | img1.jpg | 121212    |   |   |
| image2 | img2.png | 232323    |   |   |
| image3 | img3.png | 121212    |   |   |

Where K is the resource identifier, V is the value represented by K and timestamp is the moment of the operation.

Basically, we need to create a relation among the Key, the value and the timestamp. The idea is simple: Store the key sorted by time and store the value related to key. 
The relation between K and V is handled by a Hashmap, and it's used a SortedTreeMap to handle the relation between K and timestamp .

### Classes

* __Cache.java__ :  cache manager that handles the key, and the value by using a CacheData instance which is a pojo.
* __CacheData.java__: Pojo to ease the data handling between cache manager and TimeStampLog instance
* __TimeStampLog.java__: Helper class, makes it easy to sort the K by its timestamp.
