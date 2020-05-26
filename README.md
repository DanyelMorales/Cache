# Custom Cache
This project is a coding test suggested by a java recruiter. The main goal is create a Cache with a fixed size.

> Warning: This code is not thread safe

<a href="https://www.buymeacoffee.com/DanielVeraM" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" style="height: 41px!important;width: 174px!important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

## Usage
Every cache instance must have a size. This size is the number of elements inside the cache container. 
When the cache is full the oldest element will be evicted. When you add a new value to the registry, the cache manager creates a timestamp to handle the life of the registry.

Every operation on the cache manager should update the timestamp of the accessed object, therefore
those not recently accessed could be evicted.  

**Operations that updates the timestamp are:**

* put
* get
* update

So, if you try to add a new value using the "put" method, but the cache is full then automatically the cache manager will evict the oldest value on the registry.

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
The relation between K and V is handled by a Hashmap and the relation between K and timestamp we use a SortedTreeMap.

### Classes

* __Cache.java__ :  cache manager, handle key and value using a CacheData instance which is a pojo.
* __CacheData.java__: Pojo to ease the data handling between cache manager and TimeStampLog instance
* __TimeStampLog.java__: Helper class, makes it easy to sort and handle K and timestamp sorted by time.

# Support me
 Have fun and enjoy improving this cache implementation. You can support me by <style>.bmc-button img{height: 34px !important;width: 35px !important;margin-bottom: 1px !important;box-shadow: none !important;border: none !important;vertical-align: middle !important;}.bmc-button{padding: 7px 15px 7px 10px !important;line-height: 35px !important;height:51px !important;text-decoration: none !important;display:inline-flex !important;color:#FFFFFF !important;background-color:#FF813F !important;border-radius: 5px !important;border: 1px solid transparent !important;padding: 7px 15px 7px 10px !important;font-size: 22px !important;letter-spacing: 0.6px !important;box-shadow: 0px 1px 2px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 1px 2px 2px rgba(190, 190, 190, 0.5) !important;margin: 0 auto !important;font-family:'Cookie', cursive !important;-webkit-box-sizing: border-box !important;box-sizing: border-box !important;}.bmc-button:hover, .bmc-button:active, .bmc-button:focus {-webkit-box-shadow: 0px 1px 2px 2px rgba(190, 190, 190, 0.5) !important;text-decoration: none !important;box-shadow: 0px 1px 2px 2px rgba(190, 190, 190, 0.5) !important;opacity: 0.85 !important;color:#FFFFFF !important;}</style><link href="https://fonts.googleapis.com/css?family=Cookie" rel="stylesheet"><a class="bmc-button" target="_blank" href="https://www.buymeacoffee.com/DanielVeraM"><img src="https://cdn.buymeacoffee.com/buttons/bmc-new-btn-logo.svg" alt="Buy me a coffee"><span style="margin-left:5px;font-size:28px !important;">Buying me a coffee</span></a> 