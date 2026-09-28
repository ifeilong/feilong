package com.feilong.context.batch;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.bean.ConvertUtil.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.feilong.context.BatchProcessorUtil;

/**
 * {@link BatchProcessorUtil#executePartitionsToMap(Collection, int, int, Function)} 的单元测试。
 *
 * @author feilong
 * @since 4.5.5
 */
public class ExecutePartitionsToMapTest{

    //=======================================================================
    // 一、正常场景
    //=======================================================================

    @Test
    public void testBasicPartition(){
        List<Integer> data = toList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(
                        data,
                        3, // 每批3个 → 4批: [1,2,3] [4,5,6] [7,8,9] [10]
                        0,
                        batch -> {
                            Map<Integer, String> map = new LinkedHashMap<>();
                            for (Integer i : batch){
                                map.put(i, "value-" + i);
                            }
                            return map;
                        });

        assertNotNull(result);
        assertEquals(10, result.size());
        assertEquals("value-1", result.get(1));
        assertEquals("value-5", result.get(5));
        assertEquals("value-10", result.get(10));
    }

    @Test
    public void testExactDivision(){
        // 9个元素，每批3个，正好3批，无余数
        List<Integer> data = toList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Map<Integer, Integer> result = BatchProcessorUtil.executePartitionsToMap(data, 3, 0, batch -> {
            Map<Integer, Integer> map = new HashMap<>();
            for (Integer i : batch){
                map.put(i, i * 10);
            }
            return map;
        });

        assertEquals(9, result.size());
        assertEquals(Integer.valueOf(10), result.get(1));
        assertEquals(Integer.valueOf(50), result.get(5));
        assertEquals(Integer.valueOf(90), result.get(9));
    }

    @Test
    public void testLastBatchSmaller(){
        // 7个元素，每批3个 → [1,2,3] [4,5,6] [7]
        List<Integer> data = toList(1, 2, 3, 4, 5, 6, 7);

        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(data, 3, 0, batch -> {
            Map<Integer, String> map = new LinkedHashMap<>();
            for (Integer i : batch){
                map.put(i, "item-" + i);
            }
            return map;
        });

        assertEquals(7, result.size());
        assertEquals("item-1", result.get(1));
        assertEquals("item-7", result.get(7));
    }

    @Test
    public void testSingleBatch(){
        // 数据量 <= 分片大小，只有1批，不应触发休眠逻辑
        List<String> data = toList("a", "b", "c");

        Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(
                        data,
                        10, // 大于数据总量
                        100, // 即使设置了sleep，单批也不该sleep
                        batch -> {
                            Map<String, Integer> map = new HashMap<>();
                            for (String s : batch){
                                map.put(s, s.length());
                            }
                            return map;
                        });

        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(1), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(1), result.get("c"));
    }

    @Test
    public void testVerifyInsertionOrder(){
        // 使用 LinkedHashMap，验证结果顺序与输入一致
        List<Integer> data = toList(100, 200, 300, 400, 500);

        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(data, 2, 0, batch -> {
            Map<Integer, String> map = new LinkedHashMap<>();
            for (Integer i : batch){
                map.put(i, "v-" + i);
            }
            return map;
        });

        List<Integer> keys = new ArrayList<>(result.keySet());
        assertEquals(toList(100, 200, 300, 400, 500), keys);
    }

    @Test
    public void testWithSleepMilliseconds(){
        // 验证带休眠时间的场景不会抛异常（实际休眠时间不做精确断言，避免测试变慢）
        List<Integer> data = toList(1, 2, 3, 4, 5);

        long start = System.currentTimeMillis();
        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(
                        data,
                        2, // → 3批，前2批会sleep
                        10, // 每批后休眠10ms
                        batch -> {
                            Map<Integer, String> map = new LinkedHashMap<>();
                            for (Integer i : batch){
                                map.put(i, "v-" + i);
                            }
                            return map;
                        });
        long elapsed = System.currentTimeMillis() - start;

        assertEquals(5, result.size());
        // 至少休眠了2次（第1批和第2批后），约20ms，给个宽松下界
        assertTrue("休眠时间不足，elapsed=" + elapsed, elapsed >= 15);
    }

    //=======================================================================
    // 二、空值 / Null 输入
    //=======================================================================

    @Test
    public void testNullInput(){
        Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(null, 5, 0, batch -> {
            Map<String, Integer> map = new HashMap<>();
            map.put("shouldNotReach", 1);
            return map;
        });

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptyInput(){
        Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(toList(), 5, 0, batch -> {
            Map<String, Integer> map = new HashMap<>();
            map.put("shouldNotReach", 1);
            return map;
        });

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAllNullElements(){
        // 集合中全是 null 元素，去 null 后为空，function 不应被调用
        List<String> data = new ArrayList<>();
        data.add(null);
        data.add(null);
        data.add(null);

        Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(data, 2, 0, batch -> {
            // 如果走到这里就错了
            Map<String, Integer> map = new HashMap<>();
            map.put("shouldNotReach", 1);
            return map;
        });

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPartialNullElements(){
        // 部分 null，部分有效 → 只处理非 null 元素
        List<String> data = new ArrayList<>();
        data.add("a");
        data.add(null);
        data.add("b");
        data.add(null);
        data.add("c");

        Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(data, 2, 0, batch -> {
            Map<String, Integer> map = new HashMap<>();
            for (String s : batch){
                map.put(s, s.hashCode());
            }
            return map;
        });

        assertEquals(3, result.size());
        assertTrue(result.containsKey("a"));
        assertTrue(result.containsKey("b"));
        assertTrue(result.containsKey("c"));
    }

    //=======================================================================
    // 三、结果合并相关
    //=======================================================================

    @Test
    public void testKeyOverwrite(){
        // 不同分片返回相同 key，后处理的覆盖先处理的
        List<Integer> data = toList(1, 2, 3, 4, 5, 6);

        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(
                        data,
                        3, // → [1,2,3] [4,5,6]
                        0,
                        batch -> {
                            Map<Integer, String> map = new HashMap<>();
                            // 每个分片都返回同一个 key
                            map.put(999, "from-batch-containing-" + batch.get(0));
                            return map;
                        });

        // 最终 key=999 的值来自最后一批（第二批，起始元素4）
        assertEquals(1, result.size());
        assertEquals("from-batch-containing-4", result.get(999));
    }

    @Test
    public void testNullResultFromBatch(){
        // 某个分片返回 null → 跳过该分片，其他分片正常合并
        List<Integer> data = toList(1, 2, 3, 4, 5, 6);

        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(
                        data,
                        3, // → [1,2,3] [4,5,6]
                        0,
                        batch -> {
                            if (batch.contains(1)){
                                return null; // 第一批返回 null
                            }
                            Map<Integer, String> map = new LinkedHashMap<>();
                            for (Integer i : batch){
                                map.put(i, "v-" + i);
                            }
                            return map;
                        });

        assertEquals(3, result.size());
        assertEquals("v-4", result.get(4));
        assertEquals("v-5", result.get(5));
        assertEquals("v-6", result.get(6));
    }

    @Test
    public void testEmptyMapFromBatch(){
        // 某个分片返回空 Map → 该分片无贡献
        List<Integer> data = toList(1, 2, 3, 4);

        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(
                        data,
                        2, // → [1,2] [3,4]
                        0,
                        batch -> {
                            if (batch.contains(1)){
                                return new HashMap<>(); // 空 Map
                            }
                            Map<Integer, String> map = new LinkedHashMap<>();
                            for (Integer i : batch){
                                map.put(i, "v-" + i);
                            }
                            return map;
                        });

        assertEquals(2, result.size());
        assertEquals("v-3", result.get(3));
        assertEquals("v-4", result.get(4));
    }

    @Test
    public void testAllBatchesReturnNull(){
        // 所有分片都返回 null → 最终返回空 Map
        List<Integer> data = toList(1, 2, 3, 4, 5, 6);

        Map<Integer, String> result = BatchProcessorUtil.executePartitionsToMap(data, 2, 0, batch -> null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //=======================================================================
    // 四、参数校验（异常场景）
    //=======================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testPerPartitionSizeZero(){
        List<Integer> data = toList(1, 2, 3);
        BatchProcessorUtil.executePartitionsToMap(
                        data,
                        0, // 非法：必须 > 0
                        0,
                        batch -> toMap(1, "a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerPartitionSizeNegative(){
        List<Integer> data = toList(1, 2, 3);
        BatchProcessorUtil.executePartitionsToMap(
                        data,
                        -1, // 非法
                        0,
                        batch -> toMap(1, "a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSleepMillisecondsNegative(){
        List<Integer> data = toList(1, 2, 3);
        BatchProcessorUtil.executePartitionsToMap(
                        data,
                        2,
                        -100, // 非法：必须 >= 0
                        batch -> toMap(1, "a"));
    }

    @Test(expected = NullPointerException.class)
    public void testFunctionNull(){
        List<Integer> data = toList(1, 2, 3);
        BatchProcessorUtil.executePartitionsToMap(data, 2, 0, null);
    }

    //=======================================================================
    // 五、大数据量场景
    //=======================================================================

    @Test
    public void testLargeDataset(){
        // 1000 个元素，每批 100 → 10 批
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 1000; i++){
            data.add(i);
        }

        Map<Integer, Integer> result = BatchProcessorUtil.executePartitionsToMap(data, 100, 0, batch -> {
            Map<Integer, Integer> map = new HashMap<>(batch.size());
            for (Integer i : batch){
                map.put(i, i * 2);
            }
            return map;
        });

        assertEquals(1000, result.size());
        assertEquals(Integer.valueOf(2), result.get(1));
        assertEquals(Integer.valueOf(1000), result.get(500));
        assertEquals(Integer.valueOf(2000), result.get(1000));
    }

    //=======================================================================
    // 六、不同集合类型输入
    //=======================================================================

    @Test
    public void testSetInput(){
        // 输入是 Set（无序），验证结果完整性
        Set<String> data = toSet("x", "y", "z", "w", "v");

        Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(data, 2, 0, batch -> {
            Map<String, Integer> map = new HashMap<>();
            for (String s : batch){
                map.put(s, s.length());
            }
            return map;
        });

        assertEquals(5, result.size());
        assertTrue(result.containsKey("x"));
        assertTrue(result.containsKey("y"));
        assertTrue(result.containsKey("z"));
        assertTrue(result.containsKey("w"));
        assertTrue(result.containsKey("v"));
    }

    //=======================================================================
    // 七、异常传播：function 内部抛异常应向上传播
    //=======================================================================

    @Test(expected = RuntimeException.class)
    public void testFunctionThrowsException(){
        List<Integer> data = toList(1, 2, 3, 4, 5, 6);

        BatchProcessorUtil.executePartitionsToMap(data, 3, 0, batch -> {
            if (batch.contains(4)){
                throw new RuntimeException("模拟业务异常");
            }
            Map<Integer, String> map = new LinkedHashMap<>();
            for (Integer i : batch){
                map.put(i, "v-" + i);
            }
            return map;
        });
    }

    @Test
    public void testExceptionStopsProcessing(){
        // 验证异常抛出后，后续分片不会被执行
        List<Integer> data = toList(1, 2, 3, 4, 5, 6);

        final boolean[] secondBatchExecuted = { false };

        try{
            BatchProcessorUtil.executePartitionsToMap(
                            data,
                            3, // → [1,2,3] [4,5,6]
                            0,
                            batch -> {
                                if (batch.contains(1)){
                                    throw new RuntimeException("第一批就炸");
                                }
                                secondBatchExecuted[0] = true;
                                return toMap(1, "a");
                            });
        }catch (RuntimeException e){
            // 预期异常
        }

        // 异常在第一批就抛出，第二批不应被执行
        assertTrue("异常后第二批不应执行", !secondBatchExecuted[0]);
    }

}
