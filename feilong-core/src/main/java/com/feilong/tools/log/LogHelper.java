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
package com.feilong.tools.log;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.date.DateUtil.formatElapsedTime;
import static com.feilong.core.lang.NumberUtil.getMultiplyValue;
import static com.feilong.core.lang.NumberUtil.getSubtractValueWithScale;
import static com.feilong.core.lang.StringUtil.EMPTY;

import com.feilong.core.lang.NumberUtil;
import com.feilong.lib.lang3.BooleanUtils;
import com.feilong.lib.lang3.StringUtils;

/**
 * 日志辅助类，提供格式化进度日志、耗时统计等工具方法。
 * <p>
 * 主要用于批量任务处理场景，帮助输出可读性强的进度信息和预估剩余时间。
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.0
 */
public class LogHelper{

    /**
     * 根据 {@link ProcessLogParamEntity} 渲染进度日志字符串。
     * <p>
     * 生成的日志格式示例：
     * <pre>{@code
     * 进度: [3/20]  15.00% ,perUseTimes: 00:01:23 ,已经执行时间(elapsedTime): 00:04:10 ,预估剩余时间(estimatedRemainingTime): 00:23:20
     * 进度: [20/20] 100.00%, done ,perUseTimes: 00:00:05 ,总耗时(totalUseTimes): 00:26:45
     * }</pre>
     * </p>
     *
     * @param processLogParamEntity
     *            进度日志参数实体，包含当前进度、总数、开始时间等信息；可以为 null
     * @return 格式化后的进度日志字符串；如果 {@code processLogParamEntity} 为 null，返回 {@link StringUtils#EMPTY}
     * @since 4.0.0
     */
    public static String getProcessLog(ProcessLogParamEntity processLogParamEntity){
        if (isNullOrEmpty(processLogParamEntity)){
            return EMPTY;
        }

        //---------------------------------------------------------------

        //是否需要进度
        Boolean needProccess = null;
        //是否完成
        Boolean isFinish = null;
        //---------------------------------------------------------------

        StringBuilder sbResult = new StringBuilder();
        //---------------------------------------------------------------
        Number current = processLogParamEntity.getCurrent();
        Number all = processLogParamEntity.getAll();
        if (null != current && null != all){
            sbResult.append("进度:");
            sbResult.append(" [");
            appendItem(sbResult, current + "/" + all, ("" + all).length() * 2 + 1);
            sbResult.append("]");
            appendItem(sbResult, NumberUtil.getProgress(current, all), 8);

            if (NumberUtil.isEquals(current, all)){
                sbResult.append(", done");

                isFinish = true;
            }
            needProccess = true;
        }

        //---------------------------------------------------------------
        //追加耗时时间
        appendUseTime(sbResult, needProccess, isFinish, processLogParamEntity);
        return sbResult.toString();
    }

    /**
     * 追加耗时相关信息到 {@link StringBuilder} 中。
     * <p>
     * 包括单次耗时、总耗时（如果已完成）、已执行时间和预估剩余时间（如果未完成）。
     * </p>
     *
     * @param sb
     *            目标 StringBuilder
     * @param needProccess
     *            是否需要渲染进度（如果为 null 或 false，则不追加任何内容）
     * @param isFinish
     *            是否已完成（如果为 true，则追加总耗时）
     * @param processLogParamEntity
     *            进度日志参数实体，从中获取时间信息
     */
    private static void appendUseTime(StringBuilder sb,Boolean needProccess,Boolean isFinish,ProcessLogParamEntity processLogParamEntity){
        Long currentBeginTimeMillis = processLogParamEntity.getCurrentBeginTimeMillis();
        if (null == currentBeginTimeMillis){
            return;
        }

        //---------------------------------------------------------------
        //如果有渲染进度
        if (!BooleanUtils.isTrue(needProccess)){
            return;
        }

        //---------------------------------------------------------------
        //单个耗时
        long useTime = System.currentTimeMillis() - currentBeginTimeMillis;
        appendItem(sb, " ,perUseTimes: ", formatDuration(useTime), 12);

        Long allBeginTimeMillis = processLogParamEntity.getAllBeginTimeMillis();
        //已经完成
        if (BooleanUtils.isTrue(isFinish)){
            if (null != allBeginTimeMillis){
                appendItem(sb, " ,总耗时(totalUseTimes): ", formatDuration(System.currentTimeMillis() - allBeginTimeMillis), 12);
            }
            return;
        }

        //---------------------------------------------------------------
        Number current = processLogParamEntity.getCurrent();
        Number all = processLogParamEntity.getAll();

        //预估剩余时间
        Long estimatedRemainingTime = getMultiplyValue(
                        useTime, //
                        getSubtractValueWithScale(all, current, 0), //相减剩余量
                        0).longValue();

        if (null != allBeginTimeMillis){
            //since 4.0.4
            appendItem(sb, " ,已经执行时间(elapsedTime): ", formatElapsedTime(allBeginTimeMillis), 12);
        }
        appendItem(sb, " ,预估剩余时间(estimatedRemainingTime): ", formatDuration(estimatedRemainingTime), 12);
    }

    //---------------------------------------------------------------

    /**
     * 追加一个带有名称和值的条目到 {@link StringBuilder} 中，值会按指定长度左对齐填充。
     *
     * @param sb
     *            目标 StringBuilder
     * @param itemName
     *            条目名称（如 "进度:", "perUseTimes:"）
     * @param itemValue
     *            条目值字符串
     * @param itemValueLenth
     *            值部分的填充长度（左对齐）
     * @since 4.0.0
     */
    private static void appendItem(StringBuilder sb,String itemName,String itemValue,int itemValueLenth){
        sb.append(itemName);
        appendItem(sb, itemValue, itemValueLenth);
    }

    /**
     * 追加一个值到 {@link StringBuilder} 中，值会按指定长度左对齐填充。
     *
     * @param sb
     *            目标 StringBuilder
     * @param itemValue
     *            值字符串
     * @param itemValueLenth
     *            填充长度（左对齐）
     * @since 4.0.0
     */
    private static void appendItem(StringBuilder sb,String itemValue,int itemValueLenth){
        sb.append(StringUtils.leftPad(itemValue, itemValueLenth));
    }
}
