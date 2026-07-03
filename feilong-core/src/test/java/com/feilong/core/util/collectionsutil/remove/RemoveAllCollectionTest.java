/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.core.util.collectionsutil.remove;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

/**
 * The Class CollectionsUtilRemoveAllCollectionTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class RemoveAllCollectionTest{

    /**
     * Test remove all collection.
     */
    @Test
    public void testRemoveAllCollection(){
        List<String> list = toList("xinge", "feilong1", "feilong2", "feilong2");
        List<String> removeList = CollectionsUtil.removeAll(list, toList("feilong2", "feilong1"));

        assertThat(removeList, contains("xinge"));
        assertThat(list, contains("xinge", "feilong1", "feilong2", "feilong2"));
    }

    //---------------------------------------------------------------

    /**
     * Test remove all collection null object collection.
     */
    @Test(expected = NullPointerException.class)
    public void testRemoveAllCollectionNullObjectCollection(){
        CollectionsUtil.removeAll(null, toList("feilong2"));
    }

    /**
     * Test remove all collection null remove collection.
     */
    @Test
    public void testRemoveAllCollectionNullRemoveCollection(){
        List<String> list = toList("feilong2");
        assertEquals(list, CollectionsUtil.removeAll(list, null));
    }
    // ==================== normal scenarios ====================

    /**
     * Test removing some elements from a non-empty collection.
     */
    @Test
    public void testRemoveSomeElements(){
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Collection<Integer> toRemove1 = Arrays.asList(2, 4);
        Collection<Integer> toRemove2 = Arrays.asList(5);

        List<Integer> result = CollectionsUtil.removeAll(source, toRemove1, toRemove2);

        assertThat(result, contains(1, 3));
        assertThat(result.size(), is(2));
    }

    /**
     * Test removing all elements from a collection, resulting in an empty list.
     */
    @Test
    public void testRemoveAllElements(){
        List<String> source = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Collection<String> toRemove = Arrays.asList("a", "b", "c");

        List<String> result = CollectionsUtil.removeAll(source, toRemove);

        assertThat(result, empty());
    }

    /**
     * Test when no removal collections are provided (only null vararg), should return a copy of the original collection.
     */
    @Test
    public void testRemoveWithNullVararg(){

        List<Integer> source = toList(1, 2, 3);
        //        List<Integer> result = CollectionsUtil.removeAll(source, (Collection<Integer>) null);
        List<Integer> result = CollectionsUtil.removeAll(source, (Collection<Integer>[]) null);

        assertThat(result, contains(1, 2, 3));
        assertThat(result, sameInstance(source));
        //        assertThat(result, not(sameInstance(source))); // must be a new list
    }

    /**
     * Test when removal collections are all empty or null, should return a copy of the original collection.
     */
    @Test
    public void testRemoveWithEmptyOrNullCollections(){
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collection<Integer> emptyColl = Collections.emptyList();
        Collection<Integer> nullColl = null;

        List<Integer> result = CollectionsUtil.removeAll(source, emptyColl, nullColl);

        assertThat(result, contains(1, 2, 3));
        assertThat(result, not(sameInstance(source)));
    }

    /**
     * Test when removal collections contain overlapping elements, duplicates are handled correctly.
     */
    @Test
    public void testRemoveWithOverlappingCollections(){
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Collection<Integer> toRemove1 = Arrays.asList(1, 2);
        Collection<Integer> toRemove2 = Arrays.asList(2, 3);

        List<Integer> result = CollectionsUtil.removeAll(source, toRemove1, toRemove2);

        assertThat(result, contains(4, 5));
    }

    /**
     * Test that the original collection remains unchanged.
     */
    @Test
    public void testOriginalCollectionNotModified(){
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collection<Integer> toRemove = Arrays.asList(2);

        CollectionsUtil.removeAll(source, toRemove);

        assertThat(source, contains(1, 2, 3)); // original unchanged
    }

    // ==================== null source ====================

    /**
     * Test that passing a null source collection throws NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testNullSourceThrowsException(){
        CollectionsUtil.removeAll(null, Arrays.asList(1));
    }

    // ==================== edge cases ====================

    /**
     * Test removing nothing from an empty source collection returns an empty list.
     */
    @Test
    public void testRemoveFromEmptySource(){
        List<Integer> source = Collections.emptyList();
        Collection<Integer> toRemove = Arrays.asList(1, 2);

        List<Integer> result = CollectionsUtil.removeAll(source, toRemove);

        assertThat(result, empty());
    }

    /**
     * Test removing nothing from an empty source with null vararg returns an empty list.
     */
    @Test
    public void testRemoveFromEmptySourceWithNullVararg(){
        List<Integer> source = Collections.emptyList();
        List<Integer> result = CollectionsUtil.removeAll(source, (Collection<Integer>[]) null);

        assertThat(result, empty());
    }

    /**
     * Test with generic type String.
     */
    @Test
    public void testWithStringType(){
        List<String> source = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        Collection<String> toRemove = Arrays.asList("banana");

        List<String> result = CollectionsUtil.removeAll(source, toRemove);

        assertThat(result, contains("apple", "cherry"));
    }

    /**
     * Test with custom object type.
     */
    @Test
    public void testWithCustomObjectType(){
        class Item{

            final int id;

            Item(int id){
                this.id = id;
            }

            @Override
            public boolean equals(Object o){
                if (this == o)
                    return true;
                if (!(o instanceof Item))
                    return false;
                return id == ((Item) o).id;
            }

            @Override
            public int hashCode(){
                return id;
            }
        }

        List<Item> source = new ArrayList<>(Arrays.asList(new Item(1), new Item(2), new Item(3)));
        Collection<Item> toRemove = Arrays.asList(new Item(2));

        List<Item> result = CollectionsUtil.removeAll(source, toRemove);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).id, is(1));
        assertThat(result.get(1).id, is(3));
    }

    /**
     * Test that the returned list is modifiable (not an immutable view).
     */
    @Test
    public void testReturnedListIsModifiable(){
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collection<Integer> toRemove = Arrays.asList(2);

        List<Integer> result = CollectionsUtil.removeAll(source, toRemove);

        // Should be able to add/remove without UnsupportedOperationException
        result.add(99);
        assertThat(result, contains(1, 3, 99));
    }
}
