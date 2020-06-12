/*
 * Copyright (C) 2006, 2007, 2010 XStream Committers.
 * All rights reserved.
 *
 * Created on 12.10.2010 by Joerg Schaible, extracted from TreeMapConverter.
 */
package com.feilong.lib.xstream.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author J&ouml;rg Schaible
 */
public class PresortedMap implements SortedMap{

    private static class ArraySet extends ArrayList implements Set{
    }

    private final PresortedMap.ArraySet set;

    private final Comparator            comparator;

    public PresortedMap(){
        this(null, new ArraySet());
    }

    public PresortedMap(Comparator comparator){
        this(comparator, new ArraySet());
    }

    private PresortedMap(Comparator comparator, PresortedMap.ArraySet set){
        this.comparator = comparator != null ? comparator : new ArraySetComparator(set);
        this.set = set;
    }

    @Override
    public Comparator comparator(){
        return comparator;
    }

    @Override
    public Set entrySet(){
        return set;
    }

    @Override
    public Object firstKey(){
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap headMap(Object toKey){
        throw new UnsupportedOperationException();
    }

    @Override
    public Set keySet(){
        Set keySet = new ArraySet();
        for (final Iterator iterator = set.iterator(); iterator.hasNext();){
            final Entry entry = (Entry) iterator.next();
            keySet.add(entry.getKey());
        }
        return keySet;
    }

    @Override
    public Object lastKey(){
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap subMap(Object fromKey,Object toKey){
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap tailMap(Object fromKey){
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection values(){
        Set values = new ArraySet();
        for (final Iterator iterator = set.iterator(); iterator.hasNext();){
            final Entry entry = (Entry) iterator.next();
            values.add(entry.getValue());
        }
        return values;
    }

    @Override
    public void clear(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key){
        return false;
    }

    @Override
    public boolean containsValue(Object value){
        throw new UnsupportedOperationException();
    }

    @Override
    public Object get(Object key){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty(){
        return set.isEmpty();
    }

    @Override
    public Object put(final Object key,final Object value){
        set.add(new Entry(){

            @Override
            public Object getKey(){
                return key;
            }

            @Override
            public Object getValue(){
                return value;
            }

            @Override
            public Object setValue(Object value){
                throw new UnsupportedOperationException();
            }
        });
        return null;
    }

    @Override
    public void putAll(Map m){
        for (final Iterator iter = m.entrySet().iterator(); iter.hasNext();){
            set.add(iter.next());
        }
    }

    @Override
    public Object remove(Object key){
        throw new UnsupportedOperationException();
    }

    @Override
    public int size(){
        return set.size();
    }

    private static class ArraySetComparator implements Comparator{

        private final ArrayList list;

        private Map.Entry[]     array;

        ArraySetComparator(ArrayList list){
            this.list = list;
        }

        @Override
        public int compare(Object object1,Object object2){
            if (array == null || list.size() != array.length){
                Map.Entry[] a = new Map.Entry[list.size()];
                if (array != null){
                    System.arraycopy(array, 0, a, 0, array.length);
                }
                for (int i = array == null ? 0 : array.length; i < list.size(); ++i){
                    a[i] = (Map.Entry) list.get(i);
                }
                array = a;
            }
            int idx1 = Integer.MAX_VALUE, idx2 = Integer.MAX_VALUE;
            for (int i = 0; i < array.length && !(idx1 < Integer.MAX_VALUE && idx2 < Integer.MAX_VALUE); ++i){
                if (idx1 == Integer.MAX_VALUE && object1 == array[i].getKey()){
                    idx1 = i;
                }
                if (idx2 == Integer.MAX_VALUE && object2 == array[i].getKey()){
                    idx2 = i;
                }
            }
            return idx1 - idx2;
        }
    }
}