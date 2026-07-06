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
package com.feilong.context;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.5
 */
@Slf4j
public class BatchProcessorUtilTest{

    // ==================== normal scenarios ====================

    /**
     * Test normal batch processing with exact division. Each partition has exactly perPartitionSize elements.
     */
    @Test
    public void testExecutePartitionsNormal(){
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<String> results = BatchProcessorUtil.executePartitions(
                        data,
                        2,
                        0, //
                        batch -> "processed " + batch.size() + " items");
        assertThat(results, contains("processed 2 items", "processed 2 items", "processed 2 items"));
        assertThat(results.size(), is(3));
    }

    /**
     * Test batch processing when the last partition is smaller than perPartitionSize.
     */
    @Test
    public void testExecutePartitionsWithRemainder(){
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        List<String> results = BatchProcessorUtil.executePartitions(
                        data,
                        2,
                        0, //
                        batch -> "size=" + batch.size());
        assertThat(results, contains("size=2", "size=2", "size=1"));
        assertThat(results.size(), is(3));
    }

    /**
     * Test that the function receives the correct sublists in order.
     */
    @Test
    public void testExecutePartitionsCorrectSublists(){
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50);
        List<List<Integer>> captured = new ArrayList<>();
        BatchProcessorUtil.executePartitions(data, 2, 0, batch -> {
            captured.add(new ArrayList<>(batch));
            return "ok";
        });
        assertThat(captured.size(), is(3));
        assertThat(captured.get(0), contains(10, 20));
        assertThat(captured.get(1), contains(30, 40));
        assertThat(captured.get(2), contains(50));
    }

    /**
     * Test that sleep is applied between partitions. Use a very short sleep to verify behavior without slowing tests.
     */
    @Test
    public void testExecutePartitionsWithSleep(){
        List<Integer> data = Arrays.asList(1, 2, 3, 4);
        long start = System.currentTimeMillis();
        BatchProcessorUtil.executePartitions(
                        data,
                        2,
                        10, // 10ms sleep between batches
                        batch -> "done");
        long elapsed = System.currentTimeMillis() - start;
        // There are 2 partitions, so 1 sleep interval (between them). Expect at least 10ms.
        assertThat(elapsed, greaterThanOrEqualTo(10L));
    }

    // ==================== null / empty data ====================

    /**
     * Test that null data returns an empty list.
     */
    @Test
    public void testExecutePartitionsNullData(){
        List<String> results = BatchProcessorUtil.executePartitions(null, 10, 0, batch -> "ignored");
        assertThat(results, empty());
    }

    /**
     * Test that empty data returns an empty list.
     */
    @Test
    public void testExecutePartitionsEmptyData(){
        List<String> results = BatchProcessorUtil.executePartitions(Collections.emptyList(), 10, 0, batch -> "ignored");
        assertThat(results, empty());
    }

    // ==================== invalid parameters ====================

    /**
     * Test that perPartitionSize = 0 throws IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExecutePartitionsInvalidPartitionSizeZero(){
        BatchProcessorUtil.executePartitions(Arrays.asList(1, 2), 0, 0, batch -> "fail");
    }

    /**
     * Test that perPartitionSize < 0 throws IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExecutePartitionsInvalidPartitionSizeNegative(){
        BatchProcessorUtil.executePartitions(Arrays.asList(1, 2), -1, 0, batch -> "fail");
    }

    /**
     * Test that perPartitionSleepMilliseconds < 0 throws IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExecutePartitionsInvalidSleepNegative(){
        BatchProcessorUtil.executePartitions(Arrays.asList(1, 2), 2, -1, batch -> "fail");
    }

    /**
     * Test that null function throws NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testExecutePartitionsNullFunction(){
        BatchProcessorUtil.executePartitions(Arrays.asList(1, 2), 2, 0, null);
    }

    // ==================== edge cases ====================

    /**
     * Test that the function can return null, and null values are preserved in the result list.
     */
    @Test
    public void testExecutePartitionsFunctionReturnsNull(){
        List<Integer> data = Arrays.asList(1, 2, 3, 4);
        List<String> results = BatchProcessorUtil.executePartitions(data, 2, 0, batch -> null);
        assertThat(results, contains(nullValue(), nullValue()));
        assertThat(results.size(), is(2));
    }

    /**
     * Test with a large dataset to ensure all elements are processed correctly.
     */
    @Test
    public void testExecutePartitionsLargeDataSet(){
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 997; i++){
            data.add(i);
        }
        List<Integer> results = BatchProcessorUtil.executePartitions(data, 100, 0, batch -> batch.size());
        // 997 / 100 = 9 full partitions + 1 remainder of 97
        assertThat(results.size(), is(10));
        for (int i = 0; i < 9; i++){
            assertThat(results.get(i), is(100));
        }
        assertThat(results.get(9), is(97));
    }

    /**
     * Test with perPartitionSize = 1 (each element is its own partition).
     */
    @Test
    public void testExecutePartitionsSingleElementPerPartition(){
        List<String> data = Arrays.asList("a", "b", "c");
        List<String> results = BatchProcessorUtil.executePartitions(data, 1, 0, batch -> batch.get(0));
        assertThat(results, contains("a", "b", "c"));
    }

    /**
     * Test that the original collection is not modified.
     */
    @Test
    public void testExecutePartitionsOriginalCollectionNotModified(){
        List<Integer> original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> snapshot = new ArrayList<>(original);
        BatchProcessorUtil.executePartitions(original, 2, 0, batch -> "ok");
        assertThat(original, is(snapshot));
    }
}