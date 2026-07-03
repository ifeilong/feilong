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
package com.feilong.core.util.collectionsutil.add;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class AddAllIgnoreNullTest{

    @Test(expected = NullPointerException.class)
    public void testAddAllIgnoreNullNullObjectCollection(){
        CollectionsUtil.addAllIgnoreNull(null, null);
    }

    /**
     * Test add all ignore null.
     */
    @Test
    public void testAddAllIgnoreNullNullIterable(){
        List<String> list = toList("xinge", "feilong1");
        assertEquals(false, CollectionsUtil.addAllIgnoreNull(list, null));
        assertThat(list, contains("xinge", "feilong1"));
    }

    /**
     * Test add all ignore null2.
     */
    @Test
    public void testAddAllIgnoreNull2(){
        List<String> list = toList("xinge", "feilong1");
        boolean addAllIgnoreNull = CollectionsUtil.addAllIgnoreNull(list, toList("xinge", "feilong1"));
        assertEquals(true, addAllIgnoreNull);
        assertThat(list, contains("xinge", "feilong1", "xinge", "feilong1"));
    }

    // ==================== normal scenarios ====================

    /**
     * Test adding multiple non-null iterables successfully.
     */
    @Test
    public void testAddAllNonNullIterables(){
        List<String> target = new ArrayList<>(Arrays.asList("a"));
        List<String> iterable1 = Arrays.asList("b", "c");
        List<String> iterable2 = Arrays.asList("d");

        boolean result = CollectionsUtil.addAllIgnoreNull(target, iterable1, iterable2);

        assertThat(result, is(true));
        assertThat(target, contains("a", "b", "c", "d"));
    }

    /**
     * Test adding a single non-null iterable.
     */
    @Test
    public void testAddSingleIterable(){
        List<Integer> target = new ArrayList<>(Arrays.asList(1, 2));
        List<Integer> iterable = Arrays.asList(3, 4);

        boolean result = CollectionsUtil.addAllIgnoreNull(target, iterable);

        assertThat(result, is(true));
        assertThat(target, contains(1, 2, 3, 4));
    }

    // ==================== null iterable scenarios ====================

    /**
     * Test when some iterables are null, they are skipped but others are still added. The result should be false.
     */
    @Test
    public void testAddWithNullIterables(){
        List<String> target = new ArrayList<>(Arrays.asList("x"));
        List<String> validIterable = Arrays.asList("y", "z");
        List<String> nullIterable = null;

        boolean result = CollectionsUtil.addAllIgnoreNull(target, validIterable, nullIterable);

        assertThat(result, is(false));
        assertThat(target, contains("x", "y", "z"));
    }

    /**
     * Test when all iterables are null, nothing is added and result is false.
     */
    @Test
    public void testAddAllNullIterables(){
        List<String> target = new ArrayList<>(Arrays.asList("a"));

        boolean result = CollectionsUtil.addAllIgnoreNull(target, null, null);

        assertThat(result, is(false));
        assertThat(target, contains("a"));
    }

    // ==================== empty varargs / null varargs ====================

    /**
     * Test when varargs is empty array, nothing is added and result is false.
     */
    @Test
    public void testAddWithEmptyVarargs(){
        List<String> target = new ArrayList<>(Arrays.asList("a"));

        boolean result = CollectionsUtil.addAllIgnoreNull(target);

        assertThat(result, is(false));
        assertThat(target, contains("a"));
    }

    /**
     * Test when varargs is null, nothing is added and result is false.
     */
    @Test
    public void testAddWithNullVarargs(){
        List<String> target = new ArrayList<>(Arrays.asList("a"));

        boolean result = CollectionsUtil.addAllIgnoreNull(target, (Iterable<String>[]) null);

        assertThat(result, is(false));
        assertThat(target, contains("a"));
    }

    // ==================== duplicate elements ====================

    /**
     * Test when iterables contain elements already present in the target collection (which is a List allowing duplicates).
     * Since List allows duplicates, addAll should succeed and result should be true.
     */
    @Test
    public void testAddDuplicateElementsToList(){
        List<String> target = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> iterable = Arrays.asList("b", "c");

        boolean result = CollectionsUtil.addAllIgnoreNull(target, iterable);

        assertThat(result, is(true));
        assertThat(target, contains("a", "b", "b", "c"));
    }

    /**
     * Test when target is a Set (no duplicates), duplicate elements are ignored but addAll still returns true because at least one element
     * was added.
     */
    @Test
    public void testAddDuplicateElementsToSet(){
        java.util.Set<String> target = new java.util.LinkedHashSet<>(Arrays.asList("a", "b"));
        List<String> iterable = Arrays.asList("b", "c");

        boolean result = CollectionsUtil.addAllIgnoreNull(target, iterable);

        assertThat(result, is(true));
        assertThat(target, contains("a", "b", "c"));
    }

    // ==================== empty iterable ====================

    /**
     * Test when an iterable is empty, addAll returns false (no change), causing overall result to be false.
     */
    @Test
    public void testAddEmptyIterable(){
        List<String> target = new ArrayList<>(Arrays.asList("a"));
        List<String> emptyIterable = Collections.emptyList();
        List<String> validIterable = Arrays.asList("b");

        boolean result = CollectionsUtil.addAllIgnoreNull(target, emptyIterable, validIterable);

        // emptyIterable causes addAll to return false, so overall result is false
        assertThat(result, is(false));
        // But validIterable was still processed
        assertThat(target, contains("a", "b"));
    }

    // ==================== null target ====================

    /**
     * Test that passing a null target collection throws NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testNullTargetThrowsException(){
        CollectionsUtil.addAllIgnoreNull(null, Arrays.asList("a"));
    }

    // ==================== mixed scenario ====================

    /**
     * Test mixing null, empty, and valid iterables. All non-null iterables are processed, result is false due to null/empty.
     */
    @Test
    public void testMixedIterables(){
        List<Integer> target = new ArrayList<>(Arrays.asList(1));
        List<Integer> valid = Arrays.asList(2, 3);
        List<Integer> empty = Collections.emptyList();
        List<Integer> nullIterable = null;
        List<Integer> anotherValid = Arrays.asList(4);

        boolean result = CollectionsUtil.addAllIgnoreNull(target, valid, empty, nullIterable, anotherValid);

        assertThat(result, is(false));
        assertThat(target, contains(1, 2, 3, 4));
    }

    // ==================== generic type with custom objects ====================

    /**
     * Test with custom object type that has proper equals/hashCode.
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

            @Override
            public String toString(){
                return "Item(" + id + ")";
            }
        }

        List<Item> target = new ArrayList<>(Arrays.asList(new Item(1)));
        List<Item> iterable = Arrays.asList(new Item(2), new Item(3));

        boolean result = CollectionsUtil.addAllIgnoreNull(target, iterable);

        assertThat(result, is(true));
        assertThat(target.size(), is(3));
        assertThat(target.get(0).id, is(1));
        assertThat(target.get(1).id, is(2));
        assertThat(target.get(2).id, is(3));
    }
}
