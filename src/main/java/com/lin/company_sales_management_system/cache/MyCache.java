package com.lin.company_sales_management_system.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyCache {

    private static final Map<String, TimeoutUser> cacheMap = new HashMap<>();
    private static final ReadWriteLock rw = new ReentrantReadWriteLock();

    public static TimeoutUser get(String key) {
        Lock lock = rw.readLock();
        lock.lock();
        TimeoutUser myCache = cacheMap.get(key);
        lock.unlock();
        return myCache;
    }

    public static void put(String key, TimeoutUser timeoutUser) {
        Lock lock = rw.writeLock();
        lock.lock();
        cacheMap.put(key, timeoutUser);
        lock.unlock();
    }

    public static void delete(String key) {
        Lock lock = rw.writeLock();
        lock.lock();
        cacheMap.remove(key);
        lock.unlock();
    }
}
