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
import static com.feilong.core.date.DateUtil.formatElapsedTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.lib.lang3.tuple.Pair;

/**
 * {@link CollectionsUtil#removeDuplicate(java.util.Collection, String...)} 万级数据下的性能以及去重语义的测试.
 *
 * <p>
 * 原实现使用 List 双层循环判断是否重复(时间复杂度 O(n^2)),万级数据时性能很差,参见 <a
 * href="https://github.com/ifeilong/feilong/issues/327">issues/327</a>.
 * </p>
 */
@lombok.extern.slf4j.Slf4j
public class RemoveDuplicatePropertyNamesPerformanceTest{

    /** 数据量,参考 issue 里面的万级数据. */
    private static final int SIZE  = 10000;

    /** 重复周期:(albumId,multiBookTitle) 组合值每 CYCLE 条数据重复一次. */
    private static final int CYCLE = 1000;

    //---------------------------------------------------------------

    /**
     * 测试万级数据去重性能,同时校验结果的顺序以及只保留第一次出现的元素.
     */
    @Test
    public void testRemoveDuplicateLargeData(){
        List<Item> list = buildList();

        long beginTimeMillis = System.currentTimeMillis();

        List<Item> removeDuplicate = CollectionsUtil.removeDuplicate(list, "albumId", "multiBookTitle");

        if (log.isInfoEnabled()){
            log.info("removeDuplicate size:[{}],useTime: [{}]", removeDuplicate.size(), formatElapsedTime(beginTimeMillis));
        }

        //(albumId,multiBookTitle) 组合值每 CYCLE 条重复一次,所以去重之后剩下 CYCLE 条
        assertEquals(CYCLE, removeDuplicate.size());

        //保留第一次出现的元素,并且顺序和原集合一致
        for (int i = 0; i < removeDuplicate.size(); ++i){
            assertSame(list.get(i), removeDuplicate.get(i));
        }
    }

    /**
     * 数据以及属性和 {@link #testRemoveDuplicateLargeData()} 一样,校验 {@link CollectionsUtil#splitDuplicate(java.util.Collection, String...)}
     * 的划分结果.
     */
    @Test
    public void testSplitDuplicateLargeData(){
        List<Item> list = buildList();

        Pair<List<Item>, List<Item>> splitDuplicate = CollectionsUtil.splitDuplicate(list, "albumId", "multiBookTitle");

        List<Item> newList = splitDuplicate.getLeft();
        List<Item> duplicateList = splitDuplicate.getRight();

        assertEquals(CYCLE, newList.size());
        assertEquals(SIZE - CYCLE, duplicateList.size());

        assertSame(list.get(0), newList.get(0));
        //第 CYCLE 条数据和第 0 条数据的 (albumId,multiBookTitle) 组合值相同,属于重复数据
        assertSame(list.get(CYCLE), duplicateList.get(0));
    }

    /**
     * 属性值是 null 时,也能正常去重.
     */
    @Test
    public void testRemoveDuplicateNullPropertyValue(){
        Item item1 = new Item(null, "multiBookTitle1");
        Item item2 = new Item(null, "multiBookTitle1");
        Item item3 = new Item(null, "multiBookTitle2");

        List<Item> removeDuplicate = CollectionsUtil.removeDuplicate(toList(item1, item2, item3), "albumId", "multiBookTitle");

        assertEquals(2, removeDuplicate.size());
        assertSame(item1, removeDuplicate.get(0));
        assertSame(item3, removeDuplicate.get(1));
    }

    //---------------------------------------------------------------

    /**
     * 构造 SIZE 条数据, (albumId,multiBookTitle) 组合值每 CYCLE 条数据重复一次.
     *
     * @return the list
     */
    private static List<Item> buildList(){
        List<Item> list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; ++i){
            list.add(new Item("albumId" + (i % 10), "multiBookTitle" + (i % CYCLE)));
        }
        return list;
    }

    //---------------------------------------------------------------

    /**
     * 测试用的 bean.
     */
    public static class Item{

        /** The album id. */
        private final String albumId;

        /** The multi book title. */
        private final String multiBookTitle;

        /**
         * The constructor.
         *
         * @param albumId
         *            the album id
         * @param multiBookTitle
         *            the multi book title
         */
        public Item(String albumId, String multiBookTitle){
            this.albumId = albumId;
            this.multiBookTitle = multiBookTitle;
        }

        /**
         * Gets the album id.
         *
         * @return the album id
         */
        public String getAlbumId(){
            return albumId;
        }

        /**
         * Gets the multi book title.
         *
         * @return the multi book title
         */
        public String getMultiBookTitle(){
            return multiBookTitle;
        }
    }
}
