/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.lib.collection4;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.Predicate;

/**
 * Provides utility methods and decorators for {@link List} instances.
 *
 * @since 1.0
 */
public class ListUtils{

    /**
     * <code>ListUtils</code> should not normally be instantiated.
     */
    private ListUtils(){
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a List containing all the elements in <code>collection</code>
     * that are also in <code>retain</code>. The cardinality of an element <code>e</code>
     * in the returned list is the same as the cardinality of <code>e</code>
     * in <code>collection</code> unless <code>retain</code> does not contain <code>e</code>, in which
     * case the cardinality is zero. This method is useful if you do not wish to modify
     * the collection <code>c</code> and thus cannot call <code>collection.retainAll(retain);</code>.
     * <p>
     * This implementation iterates over <code>collection</code>, checking each element in
     * turn to see if it's contained in <code>retain</code>. If it's contained, it's added
     * to the returned list. As a consequence, it is advised to use a collection type for
     * <code>retain</code> that provides a fast (e.g. O(1)) implementation of
     * {@link Collection#contains(Object)}.
     *
     * @param <E>
     *            the element type
     * @param collection
     *            the collection whose contents are the target of the #retailAll operation
     * @param retain
     *            the collection containing the elements to be retained in the returned collection
     * @return a <code>List</code> containing all the elements of <code>c</code>
     *         that occur at least once in <code>retain</code>.
     * @throws NullPointerException
     *             if either parameter is null
     * @since 3.2
     */
    public static <E> List<E> retainAll(final Collection<E> collection,final Collection<?> retain){
        final List<E> list = new ArrayList<>(Math.min(collection.size(), retain.size()));

        for (final E obj : collection){
            if (retain.contains(obj)){
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * Removes the elements in <code>remove</code> from <code>collection</code>. That is, this
     * method returns a list containing all the elements in <code>collection</code>
     * that are not in <code>remove</code>. The cardinality of an element <code>e</code>
     * in the returned collection is the same as the cardinality of <code>e</code>
     * in <code>collection</code> unless <code>remove</code> contains <code>e</code>, in which
     * case the cardinality is zero. This method is useful if you do not wish to modify
     * <code>collection</code> and thus cannot call <code>collection.removeAll(remove);</code>.
     * <p>
     * This implementation iterates over <code>collection</code>, checking each element in
     * turn to see if it's contained in <code>remove</code>. If it's not contained, it's added
     * to the returned list. As a consequence, it is advised to use a collection type for
     * <code>remove</code> that provides a fast (e.g. O(1)) implementation of
     * {@link Collection#contains(Object)}.
     *
     * @param <E>
     *            the element type
     * @param collection
     *            the collection from which items are removed (in the returned collection)
     * @param remove
     *            the items to be removed from the returned <code>collection</code>
     * @return a <code>List</code> containing all the elements of <code>c</code> except
     *         any elements that also occur in <code>remove</code>.
     * @throws NullPointerException
     *             if either parameter is null
     * @since 3.2
     */
    public static <E> List<E> removeAll(final Collection<E> collection,final Collection<?> remove){
        final List<E> list = new ArrayList<>();
        for (final E obj : collection){
            if (!remove.contains(obj)){
                list.add(obj);
            }
        }
        return list;
    }

    //-----------------------------------------------------------------------
    /**
     * Finds the first index in the given List which matches the given predicate.
     * <p>
     * If the input List or predicate is null, or no element of the List
     * matches the predicate, -1 is returned.
     *
     * @param <E>
     *            the element type
     * @param list
     *            the List to search, may be null
     * @param predicate
     *            the predicate to use, may be null
     * @return the first index of an Object in the List which matches the predicate or -1 if none could be found
     */
    public static <E> int indexOf(final List<E> list,final Predicate<E> predicate){
        if (list != null && predicate != null){
            for (int i = 0; i < list.size(); i++){
                final E item = list.get(i);
                if (predicate.evaluate(item)){
                    return i;
                }
            }
        }
        return -1;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns consecutive {@link List#subList(int, int) sublists} of a
     * list, each of the same size (the final list may be smaller). For example,
     * partitioning a list containing {@code [a, b, c, d, e]} with a partition
     * size of 3 yields {@code [[a, b, c], [d, e]]} -- an outer list containing
     * two inner lists of three and two elements, all in the original order.
     * <p>
     * The outer list is unmodifiable, but reflects the latest state of the
     * source list. The inner lists are sublist views of the original list,
     * produced on demand using {@link List#subList(int, int)}, and are subject
     * to all the usual caveats about modification as explained in that API.
     * <p>
     * Adapted from http://code.google.com/p/guava-libraries/
     *
     * @param <T>
     *            the element type
     * @param list
     *            the list to return consecutive sublists of
     * @param size
     *            the desired size of each sublist (the last may be smaller)
     * @return a list of consecutive sublists
     * @throws NullPointerException
     *             if list is null
     * @throws IllegalArgumentException
     *             if size is not strictly positive
     * @since 4.0
     */
    public static <T> List<List<T>> partition(final List<T> list,final int size){
        if (list == null){
            throw new NullPointerException("List must not be null");
        }
        if (size <= 0){
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        return new Partition<>(list, size);
    }

    /**
     * Provides a partition view on a {@link List}.
     * 
     * @since 4.0
     */
    private static class Partition<T> extends AbstractList<List<T>>{

        private final List<T> list;

        private final int     size;

        private Partition(final List<T> list, final int size){
            this.list = list;
            this.size = size;
        }

        @Override
        public List<T> get(final int index){
            final int listSize = size();
            if (index < 0){
                throw new IndexOutOfBoundsException("Index " + index + " must not be negative");
            }
            if (index >= listSize){
                throw new IndexOutOfBoundsException("Index " + index + " must be less than size " + listSize);
            }
            final int start = index * size;
            final int end = Math.min(start + size, list.size());
            return list.subList(start, end);
        }

        @Override
        public int size(){
            return (int) Math.ceil((double) list.size() / (double) size);
        }

        @Override
        public boolean isEmpty(){
            return list.isEmpty();
        }
    }
}
