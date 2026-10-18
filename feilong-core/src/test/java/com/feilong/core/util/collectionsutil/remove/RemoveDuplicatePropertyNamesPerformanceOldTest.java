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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.formatElapsedTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * {@link CollectionsUtil#removeDuplicate(Collection, String...)} 优化前(O(n^2))和优化后(O(n))的性能对比测试.
 *
 * <p>
 * 优化前的实现(4.5.6 之前)原样拷贝到了本类的 {@link #removeDuplicateOld(Collection, String...)}.<br>
 * 只有多属性分支有差异:多属性分支是 O(n^2) 的 List 双层循环;单属性/空属性分支 4.5.6 没有改动,所以直接委托给当前实现.
 * </p>
 *
 * <p>
 * 测试数据模拟 issue <a href="https://github.com/ifeilong/feilong/issues/327">issues/327</a> 的场景:<br>
 * 每个 multiBookTitle 组里面有 {@link #GROUP_SIZE} 条数据,其中 albumId 只有 {@link #ALBUM_ID_SIZE_PER_GROUP} 个不同的值;<br>
 * 也就是 (albumId,multiBookTitle) 组合值大量不同(接近 n)并且带少量重复,这正是原实现最慢的场景
 * (原实现的 allValueMapList 只放不重复的 key,耗时约等于 n * 去重后的条数).
 * </p>
 *
 * <p>
 * <b>注意:</b>本类只用于对比性能,确认之后可以直接删除,或者加 {@link org.junit.Ignore} 避免拖慢整个测试.
 * </p>
 */
@lombok.extern.slf4j.Slf4j
public class RemoveDuplicatePropertyNamesPerformanceOldTest{

    /** 数据量,参考 issue 里面的万级数据. */
    private static final int      SIZE                    = 10000;

    /** 每个 multiBookTitle 组里面的数据条数. */
    private static final int      GROUP_SIZE              = 10;

    /** 每个 multiBookTitle 组里面不同的 albumId 个数,同一个组里面超出的就是重复数据. */
    private static final int      ALBUM_ID_SIZE_PER_GROUP = 8;

    /** 去重之后的预期条数. */
    private static final int      EXPECTED_SIZE           = SIZE / GROUP_SIZE * ALBUM_ID_SIZE_PER_GROUP;

    /** 去重属性,和 issue 里面的 removeDuplicate(all, "albumId", "multiBookTitle") 一致. */
    private static final String[] PROPERTY_NAMES          = { "albumId", "multiBookTitle" };

    //---------------------------------------------------------------

    /**
     * 只跑优化前的实现,看看有多慢.
     */
    @Test
    public void testRemoveDuplicateOld(){
        List<Item> list = buildList(SIZE);

        long beginTimeMillis = System.currentTimeMillis();
        long beginNanoTime = System.nanoTime();

        List<Item> removeDuplicate = removeDuplicateOld(list, PROPERTY_NAMES);

        long useNanoTime = System.nanoTime() - beginNanoTime;
        log.info(
                        "[old] size:[{}],expectSize:[{}],resultSize:[{}],useTime:[{}],useNanoTime:[{}] ms",
                        SIZE,
                        EXPECTED_SIZE,
                        removeDuplicate.size(),
                        formatElapsedTime(beginTimeMillis),
                        useNanoTime / 1000000.0);

        assertEquals(EXPECTED_SIZE, removeDuplicate.size());
        assertResult(list, removeDuplicate);
    }

    /**
     * 只跑优化后的实现,和 {@link #testRemoveDuplicateOld()} 对比.
     */
    @Test
    public void testRemoveDuplicateNew(){
        List<Item> list = buildList(SIZE);

        long beginTimeMillis = System.currentTimeMillis();
        long beginNanoTime = System.nanoTime();

        List<Item> removeDuplicate = CollectionsUtil.removeDuplicate(list, PROPERTY_NAMES);

        long useNanoTime = System.nanoTime() - beginNanoTime;
        log.info(
                        "[new] size:[{}],expectSize:[{}],resultSize:[{}],useTime:[{}],useNanoTime:[{}] ms",
                        SIZE,
                        EXPECTED_SIZE,
                        removeDuplicate.size(),
                        formatElapsedTime(beginTimeMillis),
                        useNanoTime / 1000000.0);

        assertEquals(EXPECTED_SIZE, removeDuplicate.size());
        assertResult(list, removeDuplicate);
    }

    /**
     * 同一份数据,先小数据量预热,再分别跑优化前 / 优化后的实现,并且校验两者结果完全一致.
     */
    @Test
    public void testRemoveDuplicateOldVsNew(){
        //预热,避免第一次反射的开销影响对比
        removeDuplicateOld(buildList(1000), PROPERTY_NAMES);
        CollectionsUtil.removeDuplicate(buildList(1000), PROPERTY_NAMES);

        List<Item> list = buildList(SIZE);

        //---------------------------------------------------------------
        long oldBeginNanoTime = System.nanoTime();
        List<Item> oldResult = removeDuplicateOld(list, PROPERTY_NAMES);
        long oldUseNanoTime = System.nanoTime() - oldBeginNanoTime;

        //---------------------------------------------------------------
        long newBeginNanoTime = System.nanoTime();
        List<Item> newResult = CollectionsUtil.removeDuplicate(list, PROPERTY_NAMES);
        long newUseNanoTime = System.nanoTime() - newBeginNanoTime;

        //---------------------------------------------------------------
        log.info("propertyNames:[albumId,multiBookTitle],size:[{}],resultSize:[{}]", SIZE, newResult.size());
        log.info("old(4.5.6 之前的实现) useTime:[{}] ms", oldUseNanoTime / 1000000.0);
        log.info("new(LinkedHashSet 实现) useTime:[{}] ms", newUseNanoTime / 1000000.0);
        log.info("new 比 old 快约:[{}] 倍", oldUseNanoTime / Math.max(newUseNanoTime, 1));

        //两种实现的结果必须完全一致:条数一样,并且每个位置的元素都是同一个对象
        assertEquals(oldResult.size(), newResult.size());
        for (int i = 0; i < newResult.size(); ++i){
            assertSame(oldResult.get(i), newResult.get(i));
        }
    }

    /**
     * 数据量翻倍,看耗时增长曲线:优化前应该约 4 倍增长(O(n^2)),优化后约 2 倍增长(O(n)).
     */
    @Test
    public void testRemoveDuplicateScale(){
        log.info("scale test,propertyNames:[albumId,multiBookTitle]");

        for (int size : new int[] { 1000, 2000, 4000, 8000 }){
            List<Item> list = buildList(size);

            long oldBeginNanoTime = System.nanoTime();
            List<Item> oldResult = removeDuplicateOld(list, PROPERTY_NAMES);
            long oldUseNanoTime = System.nanoTime() - oldBeginNanoTime;

            long newBeginNanoTime = System.nanoTime();
            List<Item> newResult = CollectionsUtil.removeDuplicate(list, PROPERTY_NAMES);
            long newUseNanoTime = System.nanoTime() - newBeginNanoTime;

            assertEquals(oldResult.size(), newResult.size());

            log.info(
                            "size:[{}],resultSize:[{}],old:[{}] ms,new:[{}] ms",
                            size,
                            newResult.size(),
                            oldUseNanoTime / 1000000.0,
                            newUseNanoTime / 1000000.0);
        }
    }

    /**
     * 小数据量下,校验优化前后的语义完全一致(含属性值为 null、重复数据、顺序).
     */
    @Test
    public void testRemoveDuplicateOldVsNewSmallData(){
        Item item1 = new Item(null, "multiBookTitle1");
        Item item2 = new Item(null, "multiBookTitle1");
        Item item3 = new Item(null, "multiBookTitle2");
        Item item4 = new Item("albumId4", "multiBookTitle2");
        Item item5 = new Item("albumId4", "multiBookTitle2");

        List<Item> list = toList(item1, item2, item3, item4, item5);

        List<Item> oldResult = removeDuplicateOld(list, PROPERTY_NAMES);
        List<Item> newResult = CollectionsUtil.removeDuplicate(list, PROPERTY_NAMES);

        //属性值为 null 也能正常去重,并且保留第一次出现的元素,顺序和原集合一致
        assertEquals(3, oldResult.size());
        assertEquals(3, newResult.size());

        assertSame(item1, oldResult.get(0));
        assertSame(item3, oldResult.get(1));
        assertSame(item4, oldResult.get(2));

        for (int i = 0; i < newResult.size(); ++i){
            assertSame(oldResult.get(i), newResult.get(i));
        }
    }

    //---------------------------------------------------------------

    /**
     * 校验去重结果:条数正确,并且保留第一次出现的元素,顺序和原集合一致.
     *
     * @param list
     *            the list
     * @param removeDuplicate
     *            the remove duplicate
     */
    private static void assertResult(List<Item> list,List<Item> removeDuplicate){
        assertEquals(list.size() / GROUP_SIZE * ALBUM_ID_SIZE_PER_GROUP, removeDuplicate.size());

        //按照原集合顺序,逐个比对第一次出现的元素
        Set<List<String>> existKeySet = new LinkedHashSet<>();
        int resultIndex = 0;
        for (Item item : list){
            if (existKeySet.add(Arrays.asList(item.getAlbumId(), item.getMultiBookTitle()))){
                assertSame(item, removeDuplicate.get(resultIndex));
                ++resultIndex;
            }
        }
        assertEquals(removeDuplicate.size(), resultIndex);
    }

    /**
     * 构造测试数据.
     *
     * <p>
     * 每个 multiBookTitle 组里面有 {@link #GROUP_SIZE} 条数据,其中 albumId 只有 {@link #ALBUM_ID_SIZE_PER_GROUP} 个不同的值.
     * </p>
     *
     * @param size
     *            数据量
     * @return the list
     */
    private static List<Item> buildList(int size){
        List<Item> list = new ArrayList<>(size);
        for (int i = 0; i < size; ++i){
            int group = i / GROUP_SIZE;
            int indexInGroup = i % GROUP_SIZE;
            list.add(new Item("albumId" + group + "_" + indexInGroup % ALBUM_ID_SIZE_PER_GROUP, "multiBookTitle" + group));
        }
        return list;
    }

    //---------------------------------------------------------------
    // 以下为 4.5.6 之前的实现,原样拷贝,仅用于性能对比
    //---------------------------------------------------------------

    /**
     * 4.5.6 之前的实现,时间复杂度 O(n^2).
     *
     * <p>
     * 只有多属性分支和当前实现不一样;单属性/空属性分支 4.5.6 没有改动,所以直接委托给当前实现.
     * </p>
     *
     * @param <O>
     *            the generic type
     * @param objectCollection
     *            the object collection
     * @param propertyNames
     *            the property names
     * @return the list
     */
    private static <O> List<O> removeDuplicateOld(Collection<O> objectCollection,String...propertyNames){
        if (isNullOrEmpty(propertyNames)){
            //此分支 4.5.6 没有改动
            return CollectionsUtil.removeDuplicate(objectCollection);
        }
        if (isNullOrEmpty(objectCollection)){
            return Collections.<O> emptyList();
        }

        //此分支 4.5.6 没有改动
        if (propertyNames.length == 1){
            return CollectionsUtil.removeDuplicate(objectCollection, propertyNames[0]);
        }

        //---------------------------------------------------------------
        //用来识别是否重复
        List<Map<String, Object>> allValueMapList = new ArrayList<>();

        //用来存放返回list
        List<O> returnList = new ArrayList<>(objectCollection.size());

        //---------------------------------------------------------------
        for (O o : objectCollection){
            Map<String, Object> propertyNameAndValueMap = PropertyUtil.describe(o, propertyNames);
            boolean isNotExist = !isExistOld(allValueMapList, propertyNameAndValueMap, propertyNames);
            if (isNotExist){
                returnList.add(o);
                allValueMapList.add(propertyNameAndValueMap);
            }
        }
        return returnList;
    }

    /**
     * 判断 <code>allValueMapList</code> 中,是否含有指定 key-value 的map.(原实现)
     *
     * @param allValueMapList
     *            the map list
     * @param propertyNameAndValueMap
     *            the property name and value map
     * @param keys
     *            the keys
     * @return 存在,返回true
     */
    private static boolean isExistOld(List<Map<String, Object>> allValueMapList,Map<String, Object> propertyNameAndValueMap,String...keys){
        for (Map<String, Object> map : allValueMapList){
            if (eqaulsOld(map, propertyNameAndValueMap, keys)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个map ,提取每个属性 <code>keys</code> 值, 看看是否一致, 如果有不一致的返回false; 如果都一致那么返回true.(原实现)
     *
     * @param map
     *            the map
     * @param propertyNameAndValueMap
     *            the property name and value map
     * @param keys
     *            the property names
     * @return true, if successful
     */
    private static boolean eqaulsOld(Map<String, Object> map,Map<String, Object> propertyNameAndValueMap,String...keys){
        for (String propertyName : keys){
            if (!Objects.equals(map.get(propertyName), propertyNameAndValueMap.get(propertyName))){
                return false;
            }
        }
        return true;
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
