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
package com.feilong.core.util.collectionsutil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class FlattenToListTest{
    // ==================== normal scenarios ====================

    /**
     * Test merging multiple non-empty lists with distinct elements.
     */
    @Test
    public void testFlattenToListNormal(){
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        List<Integer> result = CollectionsUtil.flattenToList(Arrays.asList(list1, list2));
        assertThat(result, contains(1, 2, 3, 4, 5, 6));
    }

    /**
     * Test merging lists with overlapping elements (duplicates are preserved).
     */
    @Test
    public void testFlattenToListWithDuplicates(){
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 4, 5);
        List<Integer> result = CollectionsUtil.flattenToList(Arrays.asList(list1, list2));
        assertThat(result, contains(1, 2, 3, 3, 4, 5));
    }

    /**
     * Test that the order of elements is preserved (outer list order, then inner list order).
     */
    @Test
    public void testFlattenToListPreservesOrder(){
        List<Integer> list1 = Arrays.asList(3, 1, 2);
        List<Integer> list2 = Arrays.asList(2, 4, 1);
        List<Integer> result = CollectionsUtil.flattenToList(Arrays.asList(list1, list2));
        assertThat(result, contains(3, 1, 2, 2, 4, 1));
    }

    /**
     * Test merging a single list.
     */
    @Test
    public void testFlattenToListSingleList(){
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = CollectionsUtil.flattenToList(Collections.singletonList(list));
        assertThat(result, contains("a", "b", "c"));
    }

    // ==================== null / empty input ====================

    /**
     * Test that null input returns an empty list.
     */
    @Test
    public void testFlattenToListNullInput(){
        List<Integer> result = CollectionsUtil.flattenToList(null);
        assertThat(result, empty());
    }

    /**
     * Test that empty collection input returns an empty list.
     */
    @Test
    public void testFlattenToListEmptyCollection(){
        List<Integer> result = CollectionsUtil.flattenToList(Collections.emptyList());
        assertThat(result, empty());
    }

    // ==================== null elements in collection ====================

    /**
     * Test that null elements in the collection are safely ignored.
     */
    @Test
    public void testFlattenToListWithNullElement(){
        List<Integer> list1 = Arrays.asList(1, 2);
        List<Integer> list2 = null;
        List<Integer> list3 = Arrays.asList(3, 4);
        List<Integer> result = CollectionsUtil.flattenToList(Arrays.asList(list1, list2, list3));
        assertThat(result, contains(1, 2, 3, 4));
    }

    /**
     * Test that all elements are null (should return empty list).
     */
    @Test
    public void testFlattenToListAllNullElements(){
        List<Integer> result = CollectionsUtil.flattenToList(Arrays.asList(null, null));
        assertThat(result, empty());
    }

    // ==================== edge cases ====================

    /**
     * Test merging empty lists (they contribute nothing but do not cause errors).
     */
    @Test
    public void testFlattenToListWithEmptyLists(){
        List<Integer> empty1 = Collections.emptyList();
        List<Integer> empty2 = Collections.emptyList();
        List<Integer> result = CollectionsUtil.flattenToList(Arrays.asList(empty1, empty2));
        assertThat(result, empty());
    }

    /**
     * Test merging mix of empty and non-empty lists.
     */
    @Test
    public void testFlattenToListMixEmptyAndNonEmpty(){
        List<Integer> empty = Collections.emptyList();
        List<Integer> nonEmpty = Arrays.asList(1, 2);
        List<Integer> result = CollectionsUtil.flattenToList(Arrays.asList(empty, nonEmpty));
        assertThat(result, contains(1, 2));
    }

    /**
     * Test that the returned list is a new object (not shared reference).
     */
    @Test
    public void testFlattenToListReturnsNewList(){
        List<Integer> list = Arrays.asList(1, 2);
        List<Integer> result = CollectionsUtil.flattenToList(Collections.singletonList(list));
        assertThat(result, not(sameInstance(list)));
    }

    /**
     * Test with generic type String.
     */
    @Test
    public void testFlattenToListWithString(){
        List<String> list1 = Arrays.asList("a", "b");
        List<String> list2 = Arrays.asList("b", "c");
        List<String> result = CollectionsUtil.flattenToList(Arrays.asList(list1, list2));
        assertThat(result, contains("a", "b", "b", "c"));
    }

    /**
     * Test with custom object type.
     */
    @Test
    public void testFlattenToListWithCustomObject(){
        class Item{

            final int id;

            Item(int id){
                this.id = id;
            }

            @Override
            public String toString(){
                return "Item(" + id + ")";
            }
        }

        List<Item> list1 = Arrays.asList(new Item(1), new Item(2));
        List<Item> list2 = Arrays.asList(new Item(2), new Item(3));
        List<Item> result = CollectionsUtil.flattenToList(Arrays.asList(list1, list2));
        assertThat(result.size(), is(4));
        assertThat(result.get(0).id, is(1));
        assertThat(result.get(1).id, is(2));
        assertThat(result.get(2).id, is(2));
        assertThat(result.get(3).id, is(3));
    }
}