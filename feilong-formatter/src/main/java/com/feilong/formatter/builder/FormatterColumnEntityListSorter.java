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
package com.feilong.formatter.builder;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.SortUtil.sortList;

import java.util.Comparator;
import java.util.List;

import com.feilong.core.Validate;
import com.feilong.core.util.comparator.BeanComparatorUtil;
import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.formatter.entity.FormatterColumnEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 用来给 {@code List<FormatterColumnEntity> formatterColumnEntityList} 排序的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class FormatterColumnEntityListSorter{

    /** 先按照order进行排序,如果相同按照属性名字进行排序. */
    private static final Comparator<FormatterColumnEntity> CHAINED_COMPARATOR = BeanComparatorUtil
                    .chainedComparator("order", "propertyName");

    //---------------------------------------------------------------

    /**
     * Sort formatter column entity list.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果没有指定排序属性,那么默认按照 order值和属性名字排序</li>
     * <li>否则按照属性名字的顺序排序</li>
     * </ol>
     * </blockquote>
     * 
     * @param formatterColumnEntityList
     *            the formatter column entity list
     * @param beanFormatterConfig
     *            the bean formatter config
     * @return 如果 <code>beanFormatterConfig</code> 是null,抛出 {@link NullPointerException}<br>
     */
    static List<FormatterColumnEntity> sortFormatterColumnEntityList(
                    List<FormatterColumnEntity> formatterColumnEntityList,
                    BeanFormatterConfig beanFormatterConfig){
        Validate.notNull(beanFormatterConfig, "beanFormatterConfig can't be null!");

        //-----------------------------------------------------------------------------------

        String[] sorts = beanFormatterConfig.getSorts();

        //如果没有指定排序属性,那么默认按照 order值和属性名字排序
        if (isNullOrEmpty(sorts)){
            return sortList(formatterColumnEntityList, CHAINED_COMPARATOR);
        }

        //-----------------------------------------------------------------------------------
        //否则按照属性名字的顺序排序
        Comparator<FormatterColumnEntity> propertyComparator = BeanComparatorUtil.propertyComparator("propertyName", sorts);
        return sortList(formatterColumnEntityList, propertyComparator, CHAINED_COMPARATOR);
    }
}
