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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class FlattenToSetTest{
    // ==================== normal scenarios ====================

    /**
     * Test merging multiple non-empty sets with distinct elements.
     */
    @Test
    public void testFlattenToSetNormal(){
        Set<Integer> set1 = new LinkedHashSet<>(Arrays.asList(1, null, 2, 3));
        Set<Integer> set2 = new LinkedHashSet<>(Arrays.asList(4, 5, 6));
        Set<Integer> result = CollectionsUtil.flattenToSet(Arrays.asList(set1, set2));

        assertThat(result, contains(1, null, 2, 3, 4, 5, 6));
    }

    /**
     * Test merging sets with overlapping elements (duplicates are removed).
     */
    @Test
    public void testFlattenToSetWithDuplicates(){
        Set<Integer> set1 = new LinkedHashSet<>(Arrays.asList(1, 2, 3));
        Set<Integer> set2 = new LinkedHashSet<>(Arrays.asList(3, 4, 5));
        Set<Integer> result = CollectionsUtil.flattenToSet(Arrays.asList(set1, set2));
        assertThat(result, contains(1, 2, 3, 4, 5));
    }

    /**
     * Test that the order of first occurrence is preserved.
     */
    @Test
    public void testFlattenToSetPreservesOrder(){
        Set<Integer> set1 = new LinkedHashSet<>(Arrays.asList(3, 1, 2));
        Set<Integer> set2 = new LinkedHashSet<>(Arrays.asList(2, 4, 1));
        Set<Integer> result = CollectionsUtil.flattenToSet(Arrays.asList(set1, set2));
        // First occurrence order: 3, 1, 2, 4
        assertThat(result, contains(3, 1, 2, 4));
    }

    /**
     * Test merging a single set.
     */
    @Test
    public void testFlattenToSetSingleSet(){
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> result = CollectionsUtil.flattenToSet(Collections.singletonList(set));
        assertThat(result, contains("a", "b", "c"));
    }

    // ==================== null / empty input ====================

    /**
     * Test that null input returns an empty set.
     */
    @Test
    public void testFlattenToSetNullInput(){
        Set<Integer> result = CollectionsUtil.flattenToSet(null);
        assertThat(result, empty());
    }

    /**
     * Test that empty collection input returns an empty set.
     */
    @Test
    public void testFlattenToSetEmptyCollection(){
        Set<Integer> result = CollectionsUtil.flattenToSet(Collections.emptyList());
        assertThat(result, empty());
    }

    // ==================== null elements in collection ====================

    /**
     * Test that null elements in the collection are safely ignored.
     */
    @Test
    public void testFlattenToSetWithNullElement(){
        Set<Integer> set1 = new LinkedHashSet<>(Arrays.asList(1, 2));
        Set<Integer> set2 = null;
        Set<Integer> set3 = new LinkedHashSet<>(Arrays.asList(3, 4));
        Set<Integer> result = CollectionsUtil.flattenToSet(Arrays.asList(set1, set2, set3));
        assertThat(result, contains(1, 2, 3, 4));
    }

    /**
     * Test that all elements are null (should return empty set).
     */
    @Test
    public void testFlattenToSetAllNullElements(){
        Set<Integer> result = CollectionsUtil.flattenToSet(Arrays.asList(null, null));
        assertThat(result, empty());
    }

    // ==================== edge cases ====================

    /**
     * Test merging empty sets.
     */
    @Test
    public void testFlattenToSetWithEmptySets(){
        Set<Integer> empty1 = Collections.emptySet();
        Set<Integer> empty2 = Collections.emptySet();
        Set<Integer> result = CollectionsUtil.flattenToSet(Arrays.asList(empty1, empty2));
        assertThat(result, empty());
    }

    /**
     * Test merging mix of empty and non-empty sets.
     */
    @Test
    public void testFlattenToSetMixEmptyAndNonEmpty(){
        Set<Integer> empty = Collections.emptySet();
        Set<Integer> nonEmpty = new LinkedHashSet<>(Arrays.asList(1, 2));
        Set<Integer> result = CollectionsUtil.flattenToSet(Arrays.asList(empty, nonEmpty));
        assertThat(result, contains(1, 2));
    }

    /**
     * Test that the returned set is a new object (not shared reference).
     */
    @Test
    public void testFlattenToSetReturnsNewSet(){
        Set<Integer> set = new LinkedHashSet<>(Arrays.asList(1, 2));
        Set<Integer> result = CollectionsUtil.flattenToSet(Collections.singletonList(set));
        assertThat(result, not(sameInstance(set)));
    }

    /**
     * Test with generic type String.
     */
    @Test
    public void testFlattenToSetWithString(){
        Set<String> set1 = new LinkedHashSet<>(Arrays.asList("a", "b"));
        Set<String> set2 = new LinkedHashSet<>(Arrays.asList("b", "c"));
        Set<String> result = CollectionsUtil.flattenToSet(Arrays.asList(set1, set2));
        assertThat(result, contains("a", "b", "c"));
    }

    /**
     * Test with custom object type.
     */
    @Test
    public void testFlattenToSetWithCustomObject(){
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

        Set<Item> set1 = new LinkedHashSet<>(Arrays.asList(new Item(1), new Item(2)));
        Set<Item> set2 = new LinkedHashSet<>(Arrays.asList(new Item(2), new Item(3)));
        Set<Item> result = CollectionsUtil.flattenToSet(Arrays.asList(set1, set2));
        assertThat(result.size(), is(3));
    }
}