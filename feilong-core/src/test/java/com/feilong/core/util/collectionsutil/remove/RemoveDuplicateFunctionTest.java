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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class RemoveDuplicateFunctionTest{
    // ==================== helper classes ====================

    static class Person{

        final String name;

        final int    age;

        Person(String name, int age){
            this.name = name;
            this.age = age;
        }

        public String getName(){
            return name;
        }

        @Override
        public String toString(){
            return "Person{name='" + name + "', age=" + age + '}';
        }
    }

    // ==================== normal scenarios ====================

    /**
     * Test deduplicating a list of Person by name using method reference.
     */
    @Test
    public void testRemoveDuplicateByProperty(){
        List<Person> persons = Arrays.asList(
                        new Person("Alice", 25),
                        new Person("Bob", 30),
                        new Person("Alice", 22), // duplicate name, keep first
                        new Person("Charlie", 40));

        List<Person> result = CollectionsUtil.removeDuplicate(persons, Person::getName);

        assertThat(result.size(), is(3));
        assertThat(result.get(0).age, is(25)); // Alice first occurrence
        assertThat(result.get(1).name, is("Bob"));
        assertThat(result.get(2).name, is("Charlie"));
    }

    /**
     * Test deduplicating a list of String by identity function.
     */
    @Test
    public void testRemoveDuplicateByIdentity(){
        List<String> strings = Arrays.asList("a", "b", "a", "c", "b");

        List<String> result = CollectionsUtil.removeDuplicate(strings, Function.identity());

        assertThat(result, contains("a", "b", "c"));
    }

    // ==================== null keyExtractor ====================

    /**
     * Test that null keyExtractor falls back to {@link CollectionsUtil#removeDuplicate(Collection)}.
     */
    @Test
    public void testRemoveDuplicateWithNullKeyExtractor(){
        List<String> strings = Arrays.asList("x", "y", "x", "z");

        List<String> result = CollectionsUtil.removeDuplicate(strings, (Function) null);

        assertThat(result, contains("x", "y", "z"));
    }

    // ==================== empty / null collection ====================

    /**
     * Test that an empty input collection returns an empty list.
     */
    @Test
    public void testRemoveDuplicateWithEmptyCollection(){
        List<String> result = CollectionsUtil.removeDuplicate(Collections.emptyList(), Function.identity());

        assertThat(result, empty());
    }

    /**
     * Test that a null input collection returns an empty list.
     */
    @Test
    public void testRemoveDuplicateWithNullCollection(){
        List<String> result = CollectionsUtil.removeDuplicate(null, Function.identity());

        assertThat(result, empty());
    }

    // ==================== edge cases ====================

    /**
     * Test that only the first occurrence of each key is kept.
     */
    @Test
    public void testRemoveDuplicateKeepsFirstOccurrence(){
        List<Integer> numbers = Arrays.asList(1, 2, 3, 2, 1, 4);

        List<Integer> result = CollectionsUtil.removeDuplicate(numbers, n -> n % 2); // key: parity

        // first odd (1), first even (2) => [1, 2]
        assertThat(result, contains(1, 2));
    }

    /**
     * Test that the returned list preserves the insertion order of first occurrences.
     */
    @Test
    public void testRemoveDuplicatePreservesOrder(){
        List<String> strings = Arrays.asList("z", "a", "b", "a", "z", "c");

        List<String> result = CollectionsUtil.removeDuplicate(strings, Function.identity());

        assertThat(result, contains("z", "a", "b", "c"));
    }

    /**
     * Test that the original collection is not modified.
     */
    @Test
    public void testOriginalCollectionNotModified(){
        List<String> original = new ArrayList<>(Arrays.asList("a", "b", "a"));
        List<String> snapshot = new ArrayList<>(original);

        CollectionsUtil.removeDuplicate(original, Function.identity());

        assertThat(original, is(snapshot));
    }

    /**
     * Test that the returned list is a new object (not the same reference as input).
     */
    @Test
    public void testReturnsNewList(){
        List<String> input = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> result = CollectionsUtil.removeDuplicate(input, Function.identity());

        assertThat(result, not(sameInstance(input)));
    }

    /**
     * Test with a key extractor that returns null for some elements.
     * The implementation uses LinkedHashMap which supports null keys.
     */
    @Test
    public void testRemoveDuplicateWithNullKey(){
        List<String> strings = Arrays.asList("a", null, "b", null, "a");

        List<String> result = CollectionsUtil.removeDuplicate(strings, Function.identity());

        // null is a valid key, first null kept
        assertThat(result, contains("a", null, "b"));
    }

    /**
     * Test with custom object where key extractor extracts an integer field.
     */
    @Test
    public void testRemoveDuplicateByIntKey(){
        class Item{

            final int    id;

            final String label;

            Item(int id, String label){
                this.id = id;
                this.label = label;
            }
        }

        List<Item> items = Arrays.asList(new Item(1, "first"), new Item(2, "second"), new Item(1, "duplicate id"));

        List<Item> result = CollectionsUtil.removeDuplicate(items, item -> item.id);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).label, is("first")); // first occurrence kept
        assertThat(result.get(1).label, is("second"));
    }

    /**
     * Test that all unique elements remain unchanged.
     */
    @Test
    public void testRemoveDuplicateAllUnique(){
        List<Integer> unique = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> result = CollectionsUtil.removeDuplicate(unique, Function.identity());

        assertThat(result, contains(1, 2, 3, 4, 5));
    }

    // ==================== additional edge cases ====================

    /**
     * Test when the collection contains only null elements.
     */
    @Test
    public void testRemoveDuplicateAllNullElements(){
        List<String> strings = Arrays.asList(null, null, null);

        List<String> result = CollectionsUtil.removeDuplicate(strings, Function.identity());

        // only first null kept
        assertThat(result, contains((String) null));
        assertThat(result.size(), is(1));
    }

    /**
     * Test when the collection contains a single element.
     */
    @Test
    public void testRemoveDuplicateSingleElement(){
        List<String> strings = Collections.singletonList("only");

        List<String> result = CollectionsUtil.removeDuplicate(strings, Function.identity());

        assertThat(result, contains("only"));
    }

    /**
     * Test with a key extractor that always returns the same constant (all keys equal).
     */
    @Test
    public void testRemoveDuplicateConstantKey(){
        List<String> strings = Arrays.asList("a", "b", "c");

        List<String> result = CollectionsUtil.removeDuplicate(strings, s -> "constant");

        // all keys are same, only first element kept
        assertThat(result, contains("a"));
    }

}
