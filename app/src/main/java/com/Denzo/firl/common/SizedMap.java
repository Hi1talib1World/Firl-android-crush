package com.Denzo.firl.common;

public class SizedMap<K,V> extends LinkedHashMap<K,V> {

    private int maxSize ;
    private final int DEFAULT_SIZE = 64;

    public SizedMap() {
        super();
        this.maxSize = DEFAULT_SIZE;
    }

    public SizedMap(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

}