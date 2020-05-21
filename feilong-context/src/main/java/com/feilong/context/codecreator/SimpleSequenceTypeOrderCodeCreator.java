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
package com.feilong.context.codecreator;

import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.date.DateUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.core.util.RandomUtil;
import com.feilong.core.Validate;

/**
 * 默认的code 生成器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class SimpleSequenceTypeOrderCodeCreator implements SequenceTypeOrderCodeCreator{

    /** The Constant LOGGER. */
    private static final Logger LOGGER  = LoggerFactory.getLogger(SimpleSequenceTypeOrderCodeCreator.class);

    /** The is debug. */
    private boolean             isDebug = false;

    //---------------------------------------------------------------

    /**
     * Instantiates a new simple sequence type order code creator.
     */
    public SimpleSequenceTypeOrderCodeCreator(){
        super();
    }

    /**
     * Instantiates a new simple sequence type order code creator.
     *
     * @param isDebug
     *            the is debug
     */
    public SimpleSequenceTypeOrderCodeCreator(boolean isDebug){
        super();
        this.isDebug = isDebug;
    }

    //---------------------------------------------------------------
    /**
     * 创建.
     *
     * @param sequence
     *            the sequence
     * @param maxLength
     *            the max length
     * @return the string
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.framework.code.CodeCreator#create(java.lang.Number, java.lang.Integer)
     */
    @Override
    public String create(long sequence,int maxLength){
        Validate.isTrue(sequence > 0, "sequence: [%s] must > 0", sequence);
        Validate.isTrue(maxLength > 0, "maxLength: [%s] must > 0", maxLength);

        int sequenceLength = String.valueOf(sequence).length();
        //---------------------------------------------------------------
        if (sequenceLength > maxLength){
            return String.valueOf(sequence);
        }
        //---------------------------------------------------------------
        //FIXME
        Validate.isTrue(maxLength == 11, "maxLength == 11");

        //---------------------------------------------------------------
        Date now = now();

        // 时间戳
        String yyString = DateUtil.toString(now, "yy");

        String hourOfYear = StringUtil.format("%04d", DateUtil.getHourOfYear(now));

        //两位随机数
        String randomString = "" + RandomUtil.createRandomWithLength(2);

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();
        if (11 == maxLength){
            sb.append(create11Code(sequence, sequenceLength, yyString, hourOfYear));
        }
        sb.append(CodeCreatorHelper.debugSeparator(isDebug) + randomString);// randomNumberLength

        //FIXME 风险 服务器时间不同步
        //---------------------------------------------------------------
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("{}", sb.toString() + CodeCreatorHelper.debugLength(sb, isDebug));
        }
        return sb.toString().replace(SPACE, EMPTY);
    }

    //---------------------------------------------------------------

    /**
     * 创建 11 code.
     *
     * @param sequence
     *            the sequence
     * @param sequenceLength
     *            the sequence length
     * @param yy
     *            the yy
     * @param hourOfYear
     *            the hour of year
     * @return the string builder
     * @throws NumberFormatException
     *             the number format exception
     * @since 1.11.0
     */
    private StringBuilder create11Code(long sequence,int sequenceLength,String yy,String hourOfYear){
        StringBuilder sb = new StringBuilder();
        if (sequenceLength <= 3){
            sb.append(CodeCreatorHelper.debugSeparator(isDebug) + yy);// 2
            sb.append(CodeCreatorHelper.debugSeparator(isDebug) + hourOfYear);// 4
            sb.append(CodeCreatorHelper.debugSeparator(isDebug) + StringUtil.format("%03d", sequence));// 4
        }else{
            int vString = Integer.parseInt(yy + hourOfYear) * 1000;

            sb.append(CodeCreatorHelper.debugSeparator(isDebug) + (vString + sequence));// 4
        }
        return sb;
    }

    //---------------------------------------------------------------

    /**
     * Sets the checks if is debug.
     *
     * @param isDebug
     *            the isDebug to set
     */
    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;
    }
}
