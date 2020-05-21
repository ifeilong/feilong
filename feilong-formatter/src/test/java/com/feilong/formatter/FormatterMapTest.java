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
package com.feilong.formatter;

import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.lib.lang3.tuple.Pair;
import com.feilong.test.AbstractTest;

public class FormatterMapTest extends AbstractTest{

    @Test
    public final void testFormatToSimpleTable(){
        Map<String, String> map = toMapUseEntrys(//
                        Pair.of(
                                        "Loading entityengine.xml from",
                                        "file:/opt/atlassian/jira/atlassian-jira/WEB-INF/classes/entityengine.xml"),
                        Pair.of("Entity model field type name", "postgres72"),
                        Pair.of("Entity model schema name", "public"),
                        Pair.of("Database Version", "PostgreSQL - 9.2.8"),
                        Pair.of("Database Driver", "PostgreSQL Native Driver - PostgreSQL 9.0 JDBC4 (build 801)"),
                        Pair.of("Database Version", "PostgreSQL - 9.2.8"),
                        Pair.of((String) null, "PostgreSQL - 9.2.8"),
                        Pair.of("Database URL", "jdbc:postgresql://127.0.0.1:5432/db_feilong_jira"),
                        Pair.of("Database JDBC config", "postgres72 jdbc:postgresql://127.0.0.1:5432/db_feilong_jira"));

        LOGGER.debug(formatToSimpleTable(map));
    }

    //---------------------------------------------------------------

    @Test
    public final void testFormatToSimpleTableNull(){
        assertEquals(EMPTY, formatToSimpleTable((Map<String, String>) null));
    }

    @Test
    public final void testFormatToSimpleTableEmpty(){
        assertEquals(EMPTY, formatToSimpleTable(emptyMap()));
    }
}
