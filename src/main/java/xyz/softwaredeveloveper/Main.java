package xyz.softwaredeveloveper;

import xyz.softwaredeveloveper.impl.Cache;
import xyz.softwaredeveloveper.impl.KeyAlreadyExistsException;
import xyz.softwaredeveloveper.impl.KeyNotExistsException;

/**
 * DEMO
 */
public class Main {

    public static void main(String[] args) throws KeyAlreadyExistsException {

        System.out.println("==============EXPECTED==============");
        System.out.println("1");
        System.out.println("-1");
        System.out.println("3");
        System.out.println("-1");
        System.out.println("3");
        System.out.println("4");
        System.out.println("============================");

        Cache<Integer, Integer> cache = new Cache<>(2);
        cache.put(1, 1);
        cache.put(2, 2);
        try {
            System.out.println(cache.get(1)); // returns 1
        } catch (KeyNotExistsException e) {
            e.printStackTrace();
        }
        cache.put(3, 3); // evicts key 2
        try {
            cache.get(2); // returns -1 (not found)
        } catch (KeyNotExistsException e) {
            System.out.println("-1");
        }
        try {
            System.out.println(cache.get(3)); // returns 3.
        } catch (KeyNotExistsException e) {
            e.printStackTrace();
        }
        cache.put(4, 4); // evicts key 1.
        try {
            System.out.println(cache.get(1)); // returns -1 (not found)
        } catch (KeyNotExistsException e) {
            System.out.println("-1");
        }
        try {
            System.out.println( cache.get(3)); // returns 3
        } catch (KeyNotExistsException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(cache.get(4));// returns 4
        } catch (KeyNotExistsException e) {
            e.printStackTrace();
        }
    }


}
