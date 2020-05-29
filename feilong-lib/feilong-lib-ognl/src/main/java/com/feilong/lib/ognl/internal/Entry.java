package com.feilong.lib.ognl.internal;

/**
 * Used by {@link ClassCacheImpl} to store entries in the cache.
 */
class Entry{

    Entry  next;

    Class  key;

    Object value;

    public Entry(Class key, Object value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString(){
        return "Entry[" + "next=" + next + '\n' + ", key=" + key + '\n' + ", value=" + value + '\n' + ']';
    }
}
