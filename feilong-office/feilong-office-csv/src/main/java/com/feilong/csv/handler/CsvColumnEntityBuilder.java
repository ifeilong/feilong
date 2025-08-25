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
package com.feilong.csv.handler;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.lang.reflect.Field;

import com.feilong.csv.entity.CsvColumn;
import com.feilong.csv.entity.CsvColumnEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class CsvColumnEntityBuilder.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.7
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvColumnEntityBuilder{

    /**
     * Builds the csv column entity.
     *
     * @param field
     *            the field
     * @return the csv column entity
     * @since 1.8.0
     */
    static CsvColumnEntity buildCsvColumnEntity(Field field){
        CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
        return new CsvColumnEntity(getName(field, csvColumn), field.getName(), getOrder(csvColumn));
    }

    //---------------------------------------------------------------

    /**
     * 获得 order.
     *
     * @param csvColumn
     *            the csv column
     * @return the order
     */
    private static int getOrder(CsvColumn csvColumn){
        return null != csvColumn ? csvColumn.order() : 0;
    }

    /**
     * 获得 name.
     *
     * @param field
     *            the field
     * @param csvColumn
     *            the csv column
     * @return the name
     */
    private static String getName(Field field,CsvColumn csvColumn){
        if (null != csvColumn && isNotNullOrEmpty(csvColumn.name())){
            return csvColumn.name().trim();
        }
        return field.getName();
    }
}
