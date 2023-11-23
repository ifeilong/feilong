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
import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;
import static com.feilong.core.lang.NumberUtil.getMultiplyValue;
import static com.feilong.core.lang.NumberUtil.getSubtractValueWithScale;
import static com.feilong.core.lang.StringUtil.EMPTY;

import com.feilong.core.lang.NumberUtil;
import com.feilong.lib.lang3.BooleanUtils;
import com.feilong.lib.lang3.StringUtils;

/**
 * 日志辅助类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.0
 */
public class LogHelper{

    /**
     * 用来渲染进度日志的.
     * 
     * @param processLogParamEntity
     * @return 如果 <code>processLogParamEntity</code> 是null,返回 {@link StringUtils#EMPTY}<br>
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
        appendUseTIme(sbResult, needProccess, isFinish, processLogParamEntity);
        return sbResult.toString();
    }

    /**
     * 追加耗时时间
     * 
     * @param processLogParamEntity
     */
    private static void appendUseTIme(StringBuilder sb,Boolean needProccess,Boolean isFinish,ProcessLogParamEntity processLogParamEntity){
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
            appendItem(sb, " ,已经执行时间(elapsedTime): ", formatDurationUseBeginTimeMillis(allBeginTimeMillis), 12);
        }
        appendItem(sb, " ,预估剩余时间(estimatedRemainingTime): ", formatDuration(estimatedRemainingTime), 12);
    }

    //---------------------------------------------------------------

    /**
     * 追加name <code>itemName</code>和值 <code>itemValue</code>, 值使用<code>itemValueLenth</code>来left pad.
     * 
     * @param sb
     * @param itemName
     * @param itemValue
     * @param itemValueLenth
     * @since 4.0.0
     */
    private static void appendItem(StringBuilder sb,String itemName,String itemValue,int itemValueLenth){
        sb.append(itemName);
        appendItem(sb, itemValue, itemValueLenth);
    }

    /**
     * 追加值 <code>itemValue</code>, 值使用<code>itemValueLenth</code>来left pad.
     * 
     * @param sb
     * @param itemValue
     *            值
     * @param itemValueLenth
     *            left pad 长度
     * @since 4.0.0
     */
    private static void appendItem(StringBuilder sb,String itemValue,int itemValueLenth){
        sb.append(StringUtils.leftPad(itemValue, itemValueLenth));
    }
}
