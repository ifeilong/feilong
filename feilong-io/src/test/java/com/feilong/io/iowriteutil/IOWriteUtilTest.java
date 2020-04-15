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
package com.feilong.io.iowriteutil;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.date.DateUtil.now;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.io.FileUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.io.SpecialFolder;

public class IOWriteUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IOWriteUtilTest.class);

    @Test
    public void unescapeHtml2() throws Exception{
        String a = "第572章 三十年后(大结局) *局";
        String result = (String) MethodUtils.invokeExactStaticMethod(IOWriteUtil.class, "getFormatFilePath", a);
        LOGGER.debug(result);
    }

    @Test
    public void write1(){
        Date beginDate = now();

        InputStream inputStream = FileUtil.getFileInputStream("C:\\Users\\feilong\\feilong\\1993 超级战警 马可·布拉姆贝拉 史泰龙.rmvb");
        OutputStream outputStream = FileUtil.getFileOutputStream("C:\\Users\\feilong\\feilong\\1993 超级战警 马可·布拉姆贝拉 史泰龙1.rmvb");
        IOWriteUtil.write(inputStream, outputStream);

        LOGGER.debug("time:{}", formatDuration(beginDate));
    }

    @Test
    public void write(){
        String url = "F:\\test.txt";
        String directoryName = SpecialFolder.getDesktop();
        IOWriteUtil.writeStringToFile(url, directoryName, UTF8);
    }

}
