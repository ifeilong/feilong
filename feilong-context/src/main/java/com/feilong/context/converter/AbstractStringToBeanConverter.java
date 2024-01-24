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
package com.feilong.context.converter;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.formatPattern;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的将 字符串转成 bean 的转换器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.12.0
 */
public abstract class AbstractStringToBeanConverter<T> implements StringToBeanConverter<T>{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStringToBeanConverter.class);

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.bind.StringToBeanConverter#convert(java.lang.String, java.lang.Class)
     */
    @Override
    public T convert(String value){
        if (isNullOrEmpty(value)){
            return null;
        }
        //---------------------------------------------------------------
        preHandlerLog(value);

        //前
        preHandler(value);

        //中
        T t = handler(value);

        //后
        postHandler(t);
        return t;
    }
    //---------------------------------------------------------------

    /**
     * Pre handler log.
     *
     * @param value
     *            the value
     * @since 1.11.5
     */
    private void preHandlerLog(String value){
        if (LOGGER.isDebugEnabled()){
            try{
                LOGGER.debug("inputString:[{}],afterFormat: {}", value, formatValue(value));
            }catch (Exception e){
                String message = formatPattern("inputString:[{}] can't format,message:[{}]", value, e.getMessage());
                throw new IllegalArgumentException(message, e);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Format value.
     *
     * @param value
     *            the value
     * @return the string
     * @since 1.11.5
     */
    protected String formatValue(String value){
        return value;
    }

    /**
     * Handler.
     *
     * @param inputString
     *            the input string
     * @return the t
     */
    protected abstract T handler(String inputString);

    //---------------------------------------------------------------

    /**
     * Before.
     *
     * @param inputString
     *            the input string
     */
    protected void preHandler(@SuppressWarnings("unused") String inputString){
    }

    //---------------------------------------------------------------

    /**
     * Post handler.
     *
     * @param t
     *            the t
     * @since 1.11.2
     */
    protected void postHandler(T t){
        if (LOGGER.isDebugEnabled()){
            String message = "after [{}] convert,return [{}],info :{}";
            LOGGER.debug(message, this.getClass().getSimpleName(), t.getClass().getName(), formatToSimpleTable(t));
        }
    }

}
