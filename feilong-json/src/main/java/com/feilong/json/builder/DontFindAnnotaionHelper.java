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
package com.feilong.json.builder;

import static com.feilong.core.bean.ConvertUtil.toArray;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * 不要去查找 annotaion 的类型.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.1.1
 */
class DontFindAnnotaionHelper{

    /** 基本类型. */
    static final Class<?>[] BASE_CLASS_ARRAY   = toArray(
                    BigDecimal.class,
                    Byte.class,
                    Short.class,
                    Integer.class,
                    BigInteger.class,
                    Long.class,
                    Float.class,
                    Double.class,

                    Boolean.class,
                    Character.class,
                    String.class);

    /** 数组. */
    static final Class<?>[] ARRAY_CLASS_ARRAY  = toArray(
                    boolean[].class,
                    Boolean[].class,

                    byte[].class,
                    Byte[].class,

                    short[].class,
                    Short[].class,
                    int[].class,
                    Integer[].class,
                    long[].class,
                    Long[].class,
                    float[].class,
                    Float[].class,
                    double[].class,
                    Double[].class,

                    char[].class,
                    Character[].class,

                    String[].class);

    //---------------------------------------------------------------

    /** 常规类型. */
    static final Class<?>[] COMMON_CLASS_ARRAY = toArray(   //
                    Calendar.class,
                    Number.class,
                    Date.class,

                    CharSequence.class,

                    Iterable.class,
                    Iterator.class,
                    Map.class);

}
