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
package com.feilong.csv;

import static com.feilong.core.CharsetType.GBK;
import static com.feilong.core.DatePattern.COMMON_DATE;
import static com.feilong.core.date.DateUtil.addDay;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;

import org.junit.Test;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.date.DateUtil;
import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.csv.entity.CsvConfig;
import com.feilong.lib.lang3.SystemUtils;
import com.feilong.test.AbstractTest;

/**
 * The Class CSVUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class CsvArrayWriteTest extends AbstractTest{

    private final CsvWrite csvWrite = new DefaultCsvWrite();

    //---------------------------------------------------------------

    @Test
    public void testWrite(){
        String path = SystemUtils.USER_HOME + "/Downloads/feilong/${date}/feilongid_pix_demand.csv";
        path = path.replace("${date}", DateUtil.toString(addDay(now(), -1), COMMON_DATE));
        LOGGER.debug(path);

        //---------------------------------------------------------------

        String[] columnTitles = { "a", "b" };
        List<Object[]> list = newArrayList();
        for (int i = 0; i < 5; i++){
            list.add(ConvertUtil.toArray(i + "金,鑫", i + "jin'\"xin"));
        }

        csvWrite.write(path, columnTitles, list, new CsvConfig(GBK));
        DesktopUtil.open(path);
    }
}
