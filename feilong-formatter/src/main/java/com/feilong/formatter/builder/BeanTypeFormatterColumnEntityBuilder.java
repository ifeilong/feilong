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

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.lang.reflect.Field;

import com.feilong.formatter.FormatterColumn;
import com.feilong.formatter.entity.FormatterColumnEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * java Bean类型格式化列的创造器.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BeanTypeFormatterColumnEntityBuilder{

    /**
     * Builds the formatter column entity.
     *
     * @param field
     *            the field
     * @return the formatter column entity
     */
    static FormatterColumnEntity buildFormatterColumnEntity(Field field){
        FormatterColumn formatterColumn = field.getAnnotation(FormatterColumn.class);
        return new FormatterColumnEntity(getName(field, formatterColumn), field.getName(), getOrder(formatterColumn));
    }

    //---------------------------------------------------------------

    /**
     * 获得 order.
     *
     * @param formatterColumn
     *            the formatter column
     * @return the order
     */
    private static int getOrder(FormatterColumn formatterColumn){
        return null != formatterColumn ? formatterColumn.order() : Integer.MAX_VALUE;//设值的排在前面
    }

    /**
     * 获得 name.
     *
     * @param field
     *            the field
     * @param formatterColumn
     *            the formatter column
     * @return the name
     */
    private static String getName(Field field,FormatterColumn formatterColumn){
        if (null != formatterColumn && isNotNullOrEmpty(formatterColumn.name())){
            return formatterColumn.name().trim();
        }
        return field.getName();
    }
}
