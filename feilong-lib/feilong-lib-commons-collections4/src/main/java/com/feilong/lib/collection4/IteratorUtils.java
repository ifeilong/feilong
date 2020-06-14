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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.Transformer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.feilong.lib.collection4.functors.EqualPredicate;
import com.feilong.lib.collection4.iterators.ArrayIterator;
import com.feilong.lib.collection4.iterators.EmptyIterator;
import com.feilong.lib.collection4.iterators.EnumerationIterator;
import com.feilong.lib.collection4.iterators.FilterIterator;
import com.feilong.lib.collection4.iterators.IteratorChain;
import com.feilong.lib.collection4.iterators.IteratorEnumeration;
import com.feilong.lib.collection4.iterators.NodeListIterator;
import com.feilong.lib.collection4.iterators.ObjectArrayIterator;
import com.feilong.lib.collection4.iterators.SingletonIterator;
import com.feilong.lib.collection4.iterators.TransformIterator;

/**
 * Provides static utility methods and decorators for {@link Iterator}
 * instances. The implementations are provided in the iterators subpackage.
 *
 * @since 2.1
 */
public class IteratorUtils{
    // validation is done in this class in certain cases because the
    // public classes allow invalid states

    /**
     * Default prefix used while converting an Iterator to its String representation.
     */
    private static final String DEFAULT_TOSTRING_PREFIX    = "[";

    /**
     * Default suffix used while converting an Iterator to its String representation.
     */
    private static final String DEFAULT_TOSTRING_SUFFIX    = "]";

    /**
     * Default delimiter used to delimit elements while converting an Iterator
     * to its String representation.
     */
    private static final String DEFAULT_TOSTRING_DELIMITER = ", ";

    /**
     * IteratorUtils is not normally instantiated.
     */
    private IteratorUtils(){
    }

    // Empty
    //-----------------------------------------------------------------------
    /**
     * Gets an empty iterator.
     * <p>
     * This iterator is a valid iterator object that will iterate over nothing.
     *
     * @param <E>
     *            the element type
     * @return an iterator over nothing
     */
    public static <E> ResettableIterator<E> emptyIterator(){
        return EmptyIterator.<E> resettableEmptyIterator();
    }

    // Chained
    //-----------------------------------------------------------------------

    /**
     * Gets an iterator that iterates through two {@link Iterator}s
     * one after another.
     *
     * @param <E>
     *            the element type
     * @param iterator1
     *            the first iterator to use, not null
     * @param iterator2
     *            the second iterator to use, not null
     * @return a combination iterator over the iterators
     * @throws NullPointerException
     *             if either iterator is null
     */
    public static <E> Iterator<E> chainedIterator(final Iterator<? extends E> iterator1,final Iterator<? extends E> iterator2){
        // keep a version with two iterators to avoid the following warning in client code (Java 5 & 6)
        // "A generic array of E is created for a varargs parameter"
        return new IteratorChain<>(iterator1, iterator2);
    }

    /**
     * Gets an iterator that iterates through an array of {@link Iterator}s
     * one after another.
     *
     * @param <E>
     *            the element type
     * @param iterators
     *            the iterators to use, not null or empty or contain nulls
     * @return a combination iterator over the iterators
     * @throws NullPointerException
     *             if iterators array is null or contains a null
     */
    @SafeVarargs
    public static <E> Iterator<E> chainedIterator(final Iterator<? extends E>...iterators){
        return new IteratorChain<>(iterators);
    }

    /**
     * Gets an iterator that iterates through a collections of {@link Iterator}s
     * one after another.
     *
     * @param <E>
     *            the element type
     * @param iterators
     *            the iterators to use, not null or empty or contain nulls
     * @return a combination iterator over the iterators
     * @throws NullPointerException
     *             if iterators collection is null or contains a null
     * @throws ClassCastException
     *             if the iterators collection contains the wrong object type
     */
    public static <E> Iterator<E> chainedIterator(final Collection<Iterator<? extends E>> iterators){
        return new IteratorChain<>(iterators);
    }

    // Object Graph

    // Transformed
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that transforms the elements of another iterator.
     * <p>
     * The transformation occurs during the next() method and the underlying
     * iterator is unaffected by the transformation.
     *
     * @param <I>
     *            the input type
     * @param <O>
     *            the output type
     * @param iterator
     *            the iterator to use, not null
     * @param transform
     *            the transform to use, not null
     * @return a new transforming iterator
     * @throws NullPointerException
     *             if either parameter is null
     */
    public static <I, O> Iterator<O> transformedIterator(
                    final Iterator<? extends I> iterator,
                    final Transformer<? super I, ? extends O> transform){

        if (iterator == null){
            throw new NullPointerException("Iterator must not be null");
        }
        if (transform == null){
            throw new NullPointerException("Transformer must not be null");
        }
        return new TransformIterator<>(iterator, transform);
    }

    // Filtered
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that filters another iterator.
     * <p>
     * The returned iterator will only return objects that match the specified
     * filtering predicate.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to use, not null
     * @param predicate
     *            the predicate to use as a filter, not null
     * @return a new filtered iterator
     * @throws NullPointerException
     *             if either parameter is null
     */
    public static <E> Iterator<E> filteredIterator(final Iterator<? extends E> iterator,final Predicate<? super E> predicate){
        if (iterator == null){
            throw new NullPointerException("Iterator must not be null");
        }
        if (predicate == null){
            throw new NullPointerException("Predicate must not be null");
        }
        return new FilterIterator<>(iterator, predicate);
    }

    // org.w3c.dom.NodeList iterators
    //-----------------------------------------------------------------------
    /**
     * Gets an {@link Iterator} that wraps the specified {@link NodeList}.
     * The returned {@link Iterator} can be used for a single iteration.
     *
     * @param nodeList
     *            the node list to use, may not be null
     * @return a new, single use {@link Iterator}
     * @throws NullPointerException
     *             if nodeList is null
     * @since 4.0
     */
    public static NodeListIterator nodeListIterator(final NodeList nodeList){
        if (nodeList == null){
            throw new NullPointerException("NodeList must not be null");
        }
        return new NodeListIterator(nodeList);
    }

    /**
     * Gets an {@link Iterator} that wraps the specified node's childNodes.
     * The returned {@link Iterator} can be used for a single iteration.
     * <p>
     * Convenience method, allows easy iteration over NodeLists:
     * 
     * <pre>
     *   Iterator&lt;Node&gt; iterator = IteratorUtils.nodeListIterator(node);
     *   for(Node childNode : IteratorUtils.asIterable(iterator)) {
     *     ...
     *   }
     * </pre>
     *
     * @param node
     *            the node to use, may not be null
     * @return a new, single use {@link Iterator}
     * @throws NullPointerException
     *             if node is null
     * @since 4.0
     */
    public static NodeListIterator nodeListIterator(final Node node){
        if (node == null){
            throw new NullPointerException("Node must not be null");
        }
        return new NodeListIterator(node);
    }

    // Views
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that provides an iterator view of the given enumeration.
     *
     * @param <E>
     *            the element type
     * @param enumeration
     *            the enumeration to use, may not be null
     * @return a new iterator
     * @throws NullPointerException
     *             if enumeration is null
     */
    public static <E> Iterator<E> asIterator(final Enumeration<? extends E> enumeration){
        if (enumeration == null){
            throw new NullPointerException("Enumeration must not be null");
        }
        return new EnumerationIterator<>(enumeration);
    }

    /**
     * Gets an iterator that provides an iterator view of the given enumeration
     * that will remove elements from the specified collection.
     *
     * @param <E>
     *            the element type
     * @param enumeration
     *            the enumeration to use, may not be null
     * @param removeCollection
     *            the collection to remove elements from, may not be null
     * @return a new iterator
     * @throws NullPointerException
     *             if enumeration or removeCollection is null
     */
    public static <E> Iterator<E> asIterator(final Enumeration<? extends E> enumeration,final Collection<? super E> removeCollection){
        if (enumeration == null){
            throw new NullPointerException("Enumeration must not be null");
        }
        if (removeCollection == null){
            throw new NullPointerException("Collection must not be null");
        }
        return new EnumerationIterator<>(enumeration, removeCollection);
    }

    /**
     * Gets an enumeration that wraps an iterator.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to use, may not be null
     * @return a new enumeration
     * @throws NullPointerException
     *             if iterator is null
     */
    public static <E> Enumeration<E> asEnumeration(final Iterator<? extends E> iterator){
        if (iterator == null){
            throw new NullPointerException("Iterator must not be null");
        }
        return new IteratorEnumeration<>(iterator);
    }

    /**
     * Gets an array based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, this is converted to an array.
     *
     * @param iterator
     *            the iterator to use, not null
     * @return an array of the iterator contents
     * @throws NullPointerException
     *             if iterator parameter is null
     */
    public static Object[] toArray(final Iterator<?> iterator){
        if (iterator == null){
            throw new NullPointerException("Iterator must not be null");
        }
        final List<?> list = toList(iterator, 100);
        return list.toArray();
    }

    /**
     * Gets an array based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, this is converted to an array.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to use, not null
     * @param arrayClass
     *            the class of array to create
     * @return an array of the iterator contents
     * @throws NullPointerException
     *             if iterator parameter or arrayClass is null
     * @throws ArrayStoreException
     *             if the arrayClass is invalid
     */
    public static <E> E[] toArray(final Iterator<? extends E> iterator,final Class<E> arrayClass){
        if (iterator == null){
            throw new NullPointerException("Iterator must not be null");
        }
        if (arrayClass == null){
            throw new NullPointerException("Array class must not be null");
        }
        final List<E> list = toList(iterator, 100);
        @SuppressWarnings("unchecked")
        final E[] array = (E[]) Array.newInstance(arrayClass, list.size());
        return list.toArray(array);
    }

    /**
     * Gets a list based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, the list is returned.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to use, not null
     * @return a list of the iterator contents
     * @throws NullPointerException
     *             if iterator parameter is null
     */
    public static <E> List<E> toList(final Iterator<? extends E> iterator){
        return toList(iterator, 10);
    }

    /**
     * Gets a list based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, the list is returned.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to use, not null
     * @param estimatedSize
     *            the initial size of the ArrayList
     * @return a list of the iterator contents
     * @throws NullPointerException
     *             if iterator parameter is null
     * @throws IllegalArgumentException
     *             if the size is less than 1
     */
    public static <E> List<E> toList(final Iterator<? extends E> iterator,final int estimatedSize){
        if (iterator == null){
            throw new NullPointerException("Iterator must not be null");
        }
        if (estimatedSize < 1){
            throw new IllegalArgumentException("Estimated size must be greater than 0");
        }
        final List<E> list = new ArrayList<>(estimatedSize);
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * Gets a suitable Iterator for the given object.
     * <p>
     * This method can handle objects as follows
     * <ul>
     * <li>null - empty iterator
     * <li>Iterator - returned directly
     * <li>Enumeration - wrapped
     * <li>Collection - iterator from collection returned
     * <li>Map - values iterator returned
     * <li>Dictionary - values (elements) enumeration returned as iterator
     * <li>array - iterator over array returned
     * <li>object with iterator() public method accessed by reflection
     * <li>object - singleton iterator
     * <li>NodeList - iterator over the list
     * <li>Node - iterator over the child nodes
     * </ul>
     *
     * @param obj
     *            the object to convert to an iterator
     * @return a suitable iterator, never null
     */
    public static Iterator<?> getIterator(final Object obj){
        if (obj == null){
            return emptyIterator();
        }
        if (obj instanceof Iterator){
            return (Iterator<?>) obj;
        }
        if (obj instanceof Iterable){
            return ((Iterable<?>) obj).iterator();
        }
        if (obj instanceof Object[]){
            return new ObjectArrayIterator<>((Object[]) obj);
        }
        if (obj instanceof Enumeration){
            return new EnumerationIterator<>((Enumeration<?>) obj);
        }
        if (obj instanceof Map){
            return ((Map<?, ?>) obj).values().iterator();
        }
        if (obj instanceof NodeList){
            return new NodeListIterator((NodeList) obj);
        }
        if (obj instanceof Node){
            return new NodeListIterator((Node) obj);
        }
        if (obj instanceof Dictionary){
            return new EnumerationIterator<>(((Dictionary<?, ?>) obj).elements());
        }else if (obj.getClass().isArray()){
            return new ArrayIterator<>(obj);
        }
        try{
            final Method method = obj.getClass().getMethod("iterator", (Class[]) null);
            if (Iterator.class.isAssignableFrom(method.getReturnType())){
                final Iterator<?> it = (Iterator<?>) method.invoke(obj, (Object[]) null);
                if (it != null){
                    return it;
                }
            }
        }catch (final RuntimeException e){ // NOPMD
            // ignore
        }catch (final NoSuchMethodException e){ // NOPMD
            // ignore
        }catch (final IllegalAccessException e){ // NOPMD
            // ignore
        }catch (final InvocationTargetException e){ // NOPMD
            // ignore
        }
        return singletonIterator(obj);
    }

    // Singleton
    //-----------------------------------------------------------------------
    /**
     * Gets a singleton iterator.
     * <p>
     * This iterator is a valid iterator object that will iterate over
     * the specified object.
     *
     * @param <E>
     *            the element type
     * @param object
     *            the single object over which to iterate
     * @return a singleton iterator over the object
     */
    public static <E> ResettableIterator<E> singletonIterator(final E object){
        return new SingletonIterator<>(object);
    }

    // Utility methods
    //-----------------------------------------------------------------------

    /**
     * Applies the closure to each element of the provided iterator.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to use, may be null
     * @param closure
     *            the closure to apply to each element, may not be null
     * @throws NullPointerException
     *             if closure is null
     * @since 4.1
     */
    public static <E> void forEach(final Iterator<E> iterator,final Closure<? super E> closure){
        if (closure == null){
            throw new NullPointerException("Closure must not be null");
        }

        if (iterator != null){
            while (iterator.hasNext()){
                final E element = iterator.next();
                closure.execute(element);
            }
        }
    }

    /**
     * Executes the given closure on each but the last element in the iterator.
     * <p>
     * If the input iterator is null no change is made.
     *
     * @param <E>
     *            the type of object the {@link Iterator} contains
     * @param iterator
     *            the iterator to get the input from, may be null
     * @param closure
     *            the closure to perform, may not be null
     * @return the last element in the iterator, or null if iterator is null or empty
     * @throws NullPointerException
     *             if closure is null
     * @since 4.1
     */
    public static <E> E forEachButLast(final Iterator<E> iterator,final Closure<? super E> closure){
        if (closure == null){
            throw new NullPointerException("Closure must not be null.");
        }
        if (iterator != null){
            while (iterator.hasNext()){
                final E element = iterator.next();
                if (iterator.hasNext()){
                    closure.execute(element);
                }else{
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Finds the first element in the given iterator which matches the given predicate.
     * <p>
     * A <code>null</code> or empty iterator returns null.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to search, may be null
     * @param predicate
     *            the predicate to use, may not be null
     * @return the first element of the iterator which matches the predicate or null if none could be found
     * @throws NullPointerException
     *             if predicate is null
     * @since 4.1
     */
    public static <E> E find(final Iterator<E> iterator,final Predicate<? super E> predicate){
        if (predicate == null){
            throw new NullPointerException("Predicate must not be null");
        }

        if (iterator != null){
            while (iterator.hasNext()){
                final E element = iterator.next();
                if (predicate.evaluate(element)){
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Returns the index of the first element in the specified iterator that
     * matches the given predicate.
     * <p>
     * A <code>null</code> or empty iterator returns -1.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to search, may be null
     * @param predicate
     *            the predicate to use, may not be null
     * @return the index of the first element which matches the predicate or -1 if none matches
     * @throws NullPointerException
     *             if predicate is null
     * @since 4.1
     */
    public static <E> int indexOf(final Iterator<E> iterator,final Predicate<? super E> predicate){
        if (predicate == null){
            throw new NullPointerException("Predicate must not be null");
        }

        if (iterator != null){
            for (int index = 0; iterator.hasNext(); index++){
                final E element = iterator.next();
                if (predicate.evaluate(element)){
                    return index;
                }
            }
        }
        return -1;
    }

    /**
     * Answers true if a predicate is true for any element of the iterator.
     * <p>
     * A <code>null</code> or empty iterator returns false.
     *
     * @param <E>
     *            the type of object the {@link Iterator} contains
     * @param iterator
     *            the {@link Iterator} to use, may be null
     * @param predicate
     *            the predicate to use, may not be null
     * @return true if any element of the collection matches the predicate, false otherwise
     * @throws NullPointerException
     *             if predicate is null
     * @since 4.1
     */
    public static <E> boolean matchesAny(final Iterator<E> iterator,final Predicate<? super E> predicate){
        return indexOf(iterator, predicate) != -1;
    }

    /**
     * Answers true if a predicate is true for every element of an iterator.
     * <p>
     * A <code>null</code> or empty iterator returns true.
     *
     * @param <E>
     *            the type of object the {@link Iterator} contains
     * @param iterator
     *            the {@link Iterator} to use, may be null
     * @param predicate
     *            the predicate to use, may not be null
     * @return true if every element of the collection matches the predicate or if the
     *         collection is empty, false otherwise
     * @throws NullPointerException
     *             if predicate is null
     * @since 4.1
     */
    public static <E> boolean matchesAll(final Iterator<E> iterator,final Predicate<? super E> predicate){
        if (predicate == null){
            throw new NullPointerException("Predicate must not be null");
        }

        if (iterator != null){
            while (iterator.hasNext()){
                final E element = iterator.next();
                if (!predicate.evaluate(element)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given iterator is empty.
     * <p>
     * A <code>null</code> or empty iterator returns true.
     *
     * @param iterator
     *            the {@link Iterator} to use, may be null
     * @return true if the iterator is exhausted or null, false otherwise
     * @since 4.1
     */
    public static boolean isEmpty(final Iterator<?> iterator){
        return iterator == null || !iterator.hasNext();
    }

    /**
     * Checks if the object is contained in the given iterator.
     * <p>
     * A <code>null</code> or empty iterator returns false.
     *
     * @param <E>
     *            the type of object the {@link Iterator} contains
     * @param iterator
     *            the iterator to check, may be null
     * @param object
     *            the object to check
     * @return true if the object is contained in the iterator, false otherwise
     * @since 4.1
     */
    public static <E> boolean contains(final Iterator<E> iterator,final Object object){
        return matchesAny(iterator, EqualPredicate.equalPredicate(object));
    }

    /**
     * Returns the <code>index</code>-th value in {@link Iterator}, throwing
     * <code>IndexOutOfBoundsException</code> if there is no such element.
     * <p>
     * The Iterator is advanced to <code>index</code> (or to the end, if
     * <code>index</code> exceeds the number of entries) as a side effect of this method.
     *
     * @param <E>
     *            the type of object in the {@link Iterator}
     * @param iterator
     *            the iterator to get a value from
     * @param index
     *            the index to get
     * @return the object at the specified index
     * @throws IndexOutOfBoundsException
     *             if the index is invalid
     * @since 4.1
     */
    public static <E> E get(final Iterator<E> iterator,final int index){
        int i = index;
        CollectionUtils.checkIndexBounds(i);
        while (iterator.hasNext()){
            i--;
            if (i == -1){
                return iterator.next();
            }
            iterator.next();
        }
        throw new IndexOutOfBoundsException("Entry does not exist: " + i);
    }

    /**
     * Shortcut for {@code get(iterator, 0)}.
     * <p>
     * Returns the <code>first</code> value in {@link Iterator}, throwing
     * <code>IndexOutOfBoundsException</code> if there is no such element.
     * </p>
     * <p>
     * The Iterator is advanced to <code>0</code> (or to the end, if
     * <code>0</code> exceeds the number of entries) as a side effect of this method.
     * </p>
     * 
     * @param <E>
     *            the type of object in the {@link Iterator}
     * @param iterator
     *            the iterator to get a value from
     * @return the first object
     * @throws IndexOutOfBoundsException
     *             if the request is invalid
     * @since 4.2
     */
    public static <E> E first(final Iterator<E> iterator){
        return get(iterator, 0);
    }

    /**
     * Returns the number of elements contained in the given iterator.
     * <p>
     * A <code>null</code> or empty iterator returns {@code 0}.
     *
     * @param iterator
     *            the iterator to check, may be null
     * @return the number of elements contained in the iterator
     * @since 4.1
     */
    public static int size(final Iterator<?> iterator){
        int size = 0;
        if (iterator != null){
            while (iterator.hasNext()){
                iterator.next();
                size++;
            }
        }
        return size;
    }

    /**
     * Returns a string representation of the elements of the specified iterator.
     * <p>
     * The string representation consists of a list of the iterator's elements,
     * enclosed in square brackets ({@code "[]"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space). Elements are
     * converted to strings as by {@code String.valueOf(Object)}.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to convert to a string, may be null
     * @return a string representation of {@code iterator}
     * @since 4.1
     */
    public static <E> String toString(final Iterator<E> iterator){
        return toString(
                        iterator,
                        TransformerUtils.stringValueTransformer(),
                        DEFAULT_TOSTRING_DELIMITER,
                        DEFAULT_TOSTRING_PREFIX,
                        DEFAULT_TOSTRING_SUFFIX);
    }

    /**
     * Returns a string representation of the elements of the specified iterator.
     * <p>
     * The string representation consists of a list of the iterable's elements,
     * enclosed in square brackets ({@code "[]"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space). Elements are
     * converted to strings as by using the provided {@code transformer}.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to convert to a string, may be null
     * @param transformer
     *            the transformer used to get a string representation of an element
     * @return a string representation of {@code iterator}
     * @throws NullPointerException
     *             if {@code transformer} is null
     * @since 4.1
     */
    public static <E> String toString(final Iterator<E> iterator,final Transformer<? super E, String> transformer){
        return toString(iterator, transformer, DEFAULT_TOSTRING_DELIMITER, DEFAULT_TOSTRING_PREFIX, DEFAULT_TOSTRING_SUFFIX);
    }

    /**
     * Returns a string representation of the elements of the specified iterator.
     * <p>
     * The string representation consists of a list of the iterator's elements,
     * enclosed by the provided {@code prefix} and {@code suffix}. Adjacent elements
     * are separated by the provided {@code delimiter}. Elements are converted to
     * strings as by using the provided {@code transformer}.
     *
     * @param <E>
     *            the element type
     * @param iterator
     *            the iterator to convert to a string, may be null
     * @param transformer
     *            the transformer used to get a string representation of an element
     * @param delimiter
     *            the string to delimit elements
     * @param prefix
     *            the prefix, prepended to the string representation
     * @param suffix
     *            the suffix, appended to the string representation
     * @return a string representation of {@code iterator}
     * @throws NullPointerException
     *             if either transformer, delimiter, prefix or suffix is null
     * @since 4.1
     */
    public static <E> String toString(
                    final Iterator<E> iterator,
                    final Transformer<? super E, String> transformer,
                    final String delimiter,
                    final String prefix,
                    final String suffix){
        if (transformer == null){
            throw new NullPointerException("transformer may not be null");
        }
        if (delimiter == null){
            throw new NullPointerException("delimiter may not be null");
        }
        if (prefix == null){
            throw new NullPointerException("prefix may not be null");
        }
        if (suffix == null){
            throw new NullPointerException("suffix may not be null");
        }
        final StringBuilder stringBuilder = new StringBuilder(prefix);
        if (iterator != null){
            while (iterator.hasNext()){
                final E element = iterator.next();
                stringBuilder.append(transformer.transform(element));
                stringBuilder.append(delimiter);
            }
            if (stringBuilder.length() > prefix.length()){
                stringBuilder.setLength(stringBuilder.length() - delimiter.length());
            }
        }
        stringBuilder.append(suffix);
        return stringBuilder.toString();
    }

}
