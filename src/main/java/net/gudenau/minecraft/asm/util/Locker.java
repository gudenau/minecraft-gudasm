package net.gudenau.minecraft.asm.util;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;

public class Locker{
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    
    public <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<K, V> factory){
        V value = readLock(()->map.get(key));
        if(value == null){
            return writeLock(()->map.computeIfAbsent(key, factory));
        }else{
            return value;
        }
    }
    
    public <K, V> V putIfAbsent(Map<K, V> map, K key, V value){
        V existing = readLock(()->map.get(key));
        if(existing == null){
            return writeLock(()->map.putIfAbsent(key, value));
        }
        return existing;
    }
    
    public <T> T readLock(Supplier<T> action){
        readLock.lock();
        try{
            return action.get();
        }finally{
            readLock.unlock();
        }
    }
    
    public <T> T writeLock(Supplier<T> action){
        writeLock.lock();
        try{
            return action.get();
        }finally{
            writeLock.unlock();
        }
    }
    
    public void readLock(Runnable action){
        readLock.lock();
        try{
            action.run();
        }finally{
            readLock.unlock();
        }
    }
    
    public void writeLock(Runnable action){
        writeLock.lock();
        try{
            action.run();
        }finally{
            writeLock.unlock();
        }
    }
}
