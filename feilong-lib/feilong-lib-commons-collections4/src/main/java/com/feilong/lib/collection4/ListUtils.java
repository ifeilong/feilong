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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
     * Returns an immutable empty list if the argument is <code>null</code>,
     * or the argument itself otherwise.
     *
     * @param <T>
     *            the element type
     * @param list
     *            the list, possibly <code>null</code>
     * @return an empty list if the argument is <code>null</code>
     */
    public static <T> List<T> emptyIfNull(final List<T> list){
        return list == null ? Collections.<T> emptyList() : list;
    }

    /**
     * Returns a new list containing all elements that are contained in
     * both given lists.
     *
     * @param <E>
     *            the element type
     * @param list1
     *            the first list
     * @param list2
     *            the second list
     * @return the intersection of those two lists
     * @throws NullPointerException
     *             if either list is null
     */
    public static <E> List<E> intersection(final List<? extends E> list1,final List<? extends E> list2){
        final List<E> result = new ArrayList<>();

        List<? extends E> smaller = list1;
        List<? extends E> larger = list2;
        if (list1.size() > list2.size()){
            smaller = list2;
            larger = list1;
        }

        final HashSet<E> hashSet = new HashSet<>(smaller);

        for (final E e : larger){
            if (hashSet.contains(e)){
                result.add(e);
                hashSet.remove(e);
            }
        }
        return result;
    }

    /**
     * Returns a new list containing the second list appended to the
     * first list. The {@link List#addAll(Collection)} operation is
     * used to append the two given lists into a new list.
     *
     * @param <E>
     *            the element type
     * @param list1
     *            the first list
     * @param list2
     *            the second list
     * @return a new list containing the union of those lists
     * @throws NullPointerException
     *             if either list is null
     */
    public static <E> List<E> union(final List<? extends E> list1,final List<? extends E> list2){
        final ArrayList<E> result = new ArrayList<>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * Selects all elements from input collection which match the given
     * predicate into an output list.
     * <p>
     * A <code>null</code> predicate matches no elements.
     *
     * @param <E>
     *            the element type
     * @param inputCollection
     *            the collection to get the input from, may not be null
     * @param predicate
     *            the predicate to use, may be null
     * @return the elements matching the predicate (new list)
     * @throws NullPointerException
     *             if the input list is null
     *
     * @since 4.0
     * @see CollectionUtils#select(Iterable, Predicate)
     */
    public static <E> List<E> select(final Collection<? extends E> inputCollection,final Predicate<? super E> predicate){
        return CollectionUtils.select(inputCollection, predicate, new ArrayList<E>(inputCollection.size()));
    }

    /**
     * Selects all elements from inputCollection which don't match the given
     * predicate into an output collection.
     * <p>
     * If the input predicate is <code>null</code>, the result is an empty list.
     *
     * @param <E>
     *            the element type
     * @param inputCollection
     *            the collection to get the input from, may not be null
     * @param predicate
     *            the predicate to use, may be null
     * @return the elements <b>not</b> matching the predicate (new list)
     * @throws NullPointerException
     *             if the input collection is null
     *
     * @since 4.0
     * @see CollectionUtils#selectRejected(Iterable, Predicate)
     */
    public static <E> List<E> selectRejected(final Collection<? extends E> inputCollection,final Predicate<? super E> predicate){
        return CollectionUtils.selectRejected(inputCollection, predicate, new ArrayList<E>(inputCollection.size()));
    }

    /**
     * Tests two lists for value-equality as per the equality contract in
     * {@link java.util.List#equals(java.lang.Object)}.
     * <p>
     * This method is useful for implementing <code>List</code> when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     * <p>
     * The relevant text (slightly paraphrased as this is a static method) is:
     * <blockquote>
     * Compares the two list objects for equality. Returns
     * {@code true} if and only if both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <i>equal</i>. (Two elements {@code e1} and
     * {@code e2} are <i>equal</i> if <code>(e1==null ? e2==null :
     * e1.equals(e2))</code>.) In other words, two lists are defined to be
     * equal if they contain the same elements in the same order. This
     * definition ensures that the equals method works properly across
     * different implementations of the {@code List} interface.
     * </blockquote>
     *
     * <b>Note:</b> The behaviour of this method is undefined if the lists are
     * modified during the equals comparison.
     *
     * @see java.util.List
     * @param list1
     *            the first list, may be null
     * @param list2
     *            the second list, may be null
     * @return whether the lists are equal by value comparison
     */
    public static boolean isEqualList(final Collection<?> list1,final Collection<?> list2){
        if (list1 == list2){
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()){
            return false;
        }

        final Iterator<?> it1 = list1.iterator();
        final Iterator<?> it2 = list2.iterator();
        Object obj1 = null;
        Object obj2 = null;

        while (it1.hasNext() && it2.hasNext()){
            obj1 = it1.next();
            obj2 = it2.next();

            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))){
                return false;
            }
        }

        return !(it1.hasNext() || it2.hasNext());
    }

    /**
     * Generates a hash code using the algorithm specified in
     * {@link java.util.List#hashCode()}.
     * <p>
     * This method is useful for implementing <code>List</code> when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     *
     * @see java.util.List#hashCode()
     * @param list
     *            the list to generate the hashCode for, may be null
     * @return the hash code
     */
    public static int hashCodeForList(final Collection<?> list){
        if (list == null){
            return 0;
        }
        int hashCode = 1;
        final Iterator<?> it = list.iterator();

        while (it.hasNext()){
            final Object obj = it.next();
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
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
     * Returns a synchronized list backed by the given list.
     * <p>
     * You must manually synchronize on the returned list's iterator to
     * avoid non-deterministic behavior:
     *
     * <pre>
     * List list = ListUtils.synchronizedList(myList);
     * synchronized (list){
     *     Iterator i = list.iterator();
     *     while (i.hasNext()){
     *         process(i.next());
     *     }
     * }
     * </pre>
     *
     * This method is just a wrapper for {@link Collections#synchronizedList(List)}.
     *
     * @param <E>
     *            the element type
     * @param list
     *            the list to synchronize, must not be null
     * @return a synchronized list backed by the given list
     * @throws NullPointerException
     *             if the list is null
     */
    public static <E> List<E> synchronizedList(final List<E> list){
        return Collections.synchronizedList(list);
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
