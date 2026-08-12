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

import static com.feilong.context.log.AutoLog.autoLog;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.limit;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.partition;
import static com.feilong.core.util.CollectionsUtil.removeAllNull;
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.feilong.core.Validate;
import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.tools.log.LogHelper;
import com.feilong.tools.log.ProcessLogParamEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 批量数据处理器工具类，用于将大数据集按指定大小分批处理，支持批次间休眠和进度日志输出。
 * <p>
 * 此类为静态工具类，无需实例化，直接通过 {@code BatchProcessor.executePartitions(...)} 调用。
 * 典型使用场景包括：
 * <ul>
 * <li>调用第三方 API 时需要控制并发量（如每次只处理 100 条，间隔 200ms）。</li>
 * <li>批量更新数据库记录时避免锁竞争或连接池耗尽。</li>
 * <li>需要实时观察处理进度和耗时的离线任务。</li>
 * </ul>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * // 假设有 1000 个用户 ID，每次处理 50 个，每批间隔 100ms，返回处理结果
 * List<String> results = BatchProcessorUtil.executePartitions(
 *     userIds,
 *     50,
 *     100,
 *     batch -> remoteApi.batchProcess(batch)
 * );
 *
 * System.out.println(results.size()); // 20 批，每批一个结果
 * }</pre>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.5
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchProcessorUtil{

    /**
     * 将数据集按指定大小分批处理，每批执行一个函数，批次间可选择休眠，并输出处理进度日志。
     * <p>
     * 该方法会将 {@code allData} 分割成若干个大小为 {@code perPartitionSize} 的子列表（最后一组可能不足），
     * 然后依次对每个子列表应用 {@code function}，并将每次的结果收集到返回列表中。
     * 每批处理完成后，如果 {@code perPartitionSleepMilliseconds > 0}，则休眠指定毫秒数。
     * 同时，每批处理完毕后会输出一条 INFO 日志，包含该批数据的前 10 个元素、函数返回值和当前进度信息。
     * </p>
     *
     * <h3>日志输出示例</h3>
     * <pre>{@code
     * subList(前10):[1,2,3,4,5,6,7,8,9,10] 进度: [3/20]  15.00% ,perUseTimes: 2秒 ,已经执行时间(elapsedTime): 12秒 ,预估剩余时间(estimatedRemainingTime): 10秒
     * subList(前10):[1,2,3,4,5,6,7,8,9,10] 进度: [20/20] 100.00%, done ,perUseTimes: 2秒 ,总耗时(totalUseTimes): 25秒
     * }</pre>
     *
     * <h3>注意事项</h3>
     * <ul>
     * <li>如果 {@code allData} 为 null 或空，直接返回空列表。</li>
     * <li>如果 {@code perPartitionSize <= 0}，抛出 {@link IllegalArgumentException}。</li>
     * <li>如果 {@code perPartitionSleepMilliseconds < 0}，抛出 {@link IllegalArgumentException}。</li>
     * <li>如果 {@code function} 为 null，抛出 {@link NullPointerException}。</li>
     * <li>返回的列表包含每批 {@code function.apply(subList)} 的结果，不剔除 null 值，调用方可根据需要自行过滤。</li>
     * <li>日志中仅显示每批数据的前 10 个元素（通过 {@link com.feilong.core.util.CollectionsUtil#limit(List, int)}），避免日志过大。</li>
     * </ul>
     *
     * @param <T>
     *            单条数据类型
     * @param <R>
     *            每批处理结果的类型
     * @param allData
     *            待处理的全部数据集合，可以为 null 或空
     * @param perPartitionSize
     *            每批处理的数据量，必须大于 0
     * @param perPartitionSleepMilliseconds
     *            每批处理完成后的休眠时间（毫秒），必须大于等于 0
     * @param function
     *            每批数据的处理函数，接收一个子列表，返回一个结果，不能为 null
     * @return 每批处理结果组成的列表，顺序与分区顺序一致；如果输入为空则返回空列表
     * @throws IllegalArgumentException
     *             如果 {@code perPartitionSize <= 0} 或 {@code perPartitionSleepMilliseconds < 0}
     * @throws NullPointerException
     *             如果 {@code function} 为 null
     */
    public static <T, R> List<R> executePartitions(
                    Collection<T> allData,
                    int perPartitionSize,
                    int perPartitionSleepMilliseconds,
                    Function<List<T>, R> function){
        long allTime = System.currentTimeMillis();

        //---------------------------------------------------------------
        Validate.isTrue(perPartitionSize > 0, "perPartitionSize > 0, perPartitionSize:%s", perPartitionSize);
        Validate.isTrue(
                        perPartitionSleepMilliseconds >= 0,
                        "perPartitionSleepMilliseconds >= 0, perPartitionSleepMilliseconds:%s",
                        perPartitionSleepMilliseconds);
        Validate.notNull(function, "function can't be null!");
        if (isNullOrEmpty(allData)){
            return emptyList();
        }
        //去掉null元素
        allData = removeAllNull(allData);
        if (isNullOrEmpty(allData)){
            return emptyList();
        }

        //---------------------------------------------------------------
        List<R> returnList = newArrayList();
        execute(allData, perPartitionSize, perPartitionSleepMilliseconds, allTime, subList -> {
            R result = function.apply(subList);
            returnList.add(result);// 不判断结果null,自行拿到结果视需求做过滤吧
            return result;
        });
        return returnList;
    }

    /**
     * 将数据集按指定大小分批处理，每批执行一个函数，将各批返回的 Map 合并为一个 Map，批次间可选择休眠，并输出处理进度日志。
     * <p>
     * 与 {@link #executePartitions(Collection, int, int, Function)} 的区别在于：
     * 本方法接收的 {@code function} 返回 {@code Map<K, R>}，并将所有分片的 Map 合并后返回；
     * 同时，本方法会在处理前自动去除 {@code allData} 中的 null 元素。
     * </p>
     *
     * <h3>日志输出示例</h3>
     * <pre>{@code
     * subList(前10):[1,2,3,4,5,6,7,8,9,10] {key1=value1, key2=value2} 进度: [3/20]  15.00% ,perUseTimes: 2秒 ...
     * }</pre>
     *
     * <h3>注意事项</h3>
     * <ul>
     * <li>如果 {@code allData} 为 null 或空，直接返回空 Map。</li>
     * <li>如果 {@code allData} 中所有元素均为 null，也返回空 Map。</li>
     * <li>如果 {@code perPartitionSize <= 0}，抛出 {@link IllegalArgumentException}。</li>
     * <li>如果 {@code perPartitionSleepMilliseconds < 0}，抛出 {@link IllegalArgumentException}。</li>
     * <li>如果 {@code function} 为 null，抛出 {@link NullPointerException}。</li>
     * <li>合并 Map 时，如果某分片返回 null 或空 Map，则该分片不会贡献任何键值对。</li>
     * <li>如果不同分片返回的 Map 中存在相同的 key，后处理的会覆盖先处理的（保留最后一次的值）。</li>
     * <li>日志中仅显示每批数据的前 10 个元素，避免日志过大。</li>
     * </ul>
     *
     * @param <T>
     *            单条数据类型
     * @param <R>
     *            每批处理结果 Map 的 value 类型
     * @param <K>
     *            每批处理结果 Map 的 key 类型
     * @param allData
     *            待处理的全部数据集合，可以为 null 或空
     * @param perPartitionSize
     *            每批处理的数据量，必须大于 0
     * @param perPartitionSleepMilliseconds
     *            每批处理完成后的休眠时间（毫秒），必须大于等于 0
     * @param function
     *            每批数据的处理函数，接收一个子列表，返回一个 Map，不能为 null
     * @return 所有分片结果合并后的 Map；如果输入为空则返回空 Map
     * @throws IllegalArgumentException
     *             如果 {@code perPartitionSize <= 0} 或 {@code perPartitionSleepMilliseconds < 0}
     * @throws NullPointerException
     *             如果 {@code function} 为 null
     * @since 4.5.6
     */
    public static <T, R, K> Map<K, R> executePartitionsToMap(
                    Collection<T> allData,
                    int perPartitionSize,
                    int perPartitionSleepMilliseconds,
                    Function<List<T>, Map<K, R>> function){

        long allTime = System.currentTimeMillis();

        Validate.isTrue(perPartitionSize > 0, "perPartitionSize > 0, perPartitionSize:%s", perPartitionSize);
        Validate.isTrue(
                        perPartitionSleepMilliseconds >= 0,
                        "perPartitionSleepMilliseconds >= 0, perPartitionSleepMilliseconds:%s",
                        perPartitionSleepMilliseconds);
        Validate.notNull(function, "function can't be null!");

        //---------------------------------------------------------------

        if (isNullOrEmpty(allData)){
            return emptyMap();
        }
        //去掉null元素
        allData = removeAllNull(allData);
        if (isNullOrEmpty(allData)){
            return emptyMap();
        }

        Map<K, R> returnMap = newLinkedHashMap(size(allData));

        //---------------------------------------------------------------
        execute(allData, perPartitionSize, perPartitionSleepMilliseconds, allTime, subList -> {
            Map<K, R> result = function.apply(subList);
            MapUtil.putAllIfNotNull(returnMap, result);
            return result;
        });
        return returnMap;
    }

    //---------------------------------------------------------------
    /**
     * 执行分片遍历、调用函数、记录日志及休眠的核心方法。
     * <p>
     * 将 {@code allData} 按 {@code perPartitionSize} 分成若干子列表，依次对每个子列表应用 {@code function}，
     * 并记录每批的处理日志（包含前 10 个元素、结果和进度信息）。
     * 如果 {@code perPartitionSleepMilliseconds > 0} 且存在多个分片且当前分片不是最后一个，
     * 则在处理完当前分片后休眠指定毫秒数。
     * </p>
     *
     * <h3>日志行为</h3>
     * <ul>
     * <li>如果 {@code result} 为 null 或空，输出 WARN 级别日志，提示结果为空。</li>
     * <li>否则输出 INFO 级别日志，包含子列表前 10 个元素、结果和进度信息。</li>
     * </ul>
     *
     * <h3>异常处理</h3>
     * <ul>
     * <li>如果 {@code function.apply(subList)} 抛出异常，该异常会直接向上传播，整个处理过程立即中止。</li>
     * <li>调用者应在 {@code function} 内部自行捕获并处理预期内的异常，或容忍该方法因异常而终止。</li>
     * </ul>
     *
     * @param <T>
     *            单条数据类型
     * @param <R>
     *            每批处理结果的类型
     * @param allData
     *            待处理的全部数据集合（已去除 null 元素，且不为空）
     * @param perPartitionSize
     *            每批处理的数据量，必须大于 0
     * @param perPartitionSleepMilliseconds
     *            每批处理完成后的休眠时间（毫秒），必须大于等于 0
     * @param allTime
     *            整个处理过程的起始时间戳（毫秒），用于计算进度和耗时
     * @param function
     *            每批数据的处理函数，接收一个子列表，返回一个结果，不能为 null
     * @throws IllegalArgumentException
     *             如果 {@code perPartitionSize <= 0} 或 {@code perPartitionSleepMilliseconds < 0}
     * @throws NullPointerException
     *             如果 {@code function} 为 null（由调用方保证，此处不重复校验）
     */
    private static <T, R> void execute(
                    Collection<T> allData,
                    int perPartitionSize,
                    int perPartitionSleepMilliseconds,
                    long allTime,
                    Function<List<T>, R> function){
        List<List<T>> partition = partition(toList(allData), perPartitionSize);

        //分片出来的list大小
        int allsize = size(partition);
        //---------------------------------------------------------------
        int current = 1;
        for (List<T> subList : partition){
            long perStartTime = System.currentTimeMillis();

            R result = function.apply(subList);

            //---------------------------------------------------------------
            ProcessLogParamEntity processLogParamEntity = new ProcessLogParamEntity(current, allsize, perStartTime, allTime);
            if (isNullOrEmpty(result)){
                log.warn(autoLog("subList(前10):{} resultIsNull {}", limit(subList, 10), LogHelper.getProcessLog(processLogParamEntity)));
            }else{
                log.info(autoLog("subList(前10):{} {} {}", limit(subList, 10), result, LogHelper.getProcessLog(processLogParamEntity)));
            }
            //---------------------------------------------------------------
            //休眠
            if (perPartitionSleepMilliseconds > 0//
                            && allsize > 1 //有多个分片
                            && current != allsize //不是最后一个
            ){
                ThreadUtil.sleep(perPartitionSleepMilliseconds);
            }

            current++;
        }
    }

}
