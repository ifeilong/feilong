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
package com.feilong.office.excel;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * The Interface ExcelWriter.
 */
public interface ExcelWriter extends ExcelConfig{

    /**
     * Write.
     *
     * @param os
     *            the os
     * @param beans
     *            the beans
     * @return the write status
     */
    WriteStatus write(OutputStream os,Map<String, Object> beans);

    /**
     * Write per sheet.
     *
     * @param os
     *            the os
     * @param beansList
     *            the beans list
     * @return the write status
     */
    WriteStatus writePerSheet(OutputStream os,List<Map<String, Object>> beansList);

    /**
     * Write.
     *
     * @param template
     *            the template
     * @param os
     *            the os
     * @param beans
     *            the beans
     * @return the write status
     */
    WriteStatus write(String template,OutputStream os,Map<String, Object> beans);

    /**
     * Write.
     *
     * @param is
     *            the is
     * @param os
     *            the os
     * @param beans
     *            the beans
     * @return the write status
     */
    WriteStatus write(InputStream is,OutputStream os,Map<String, Object> beans);

    //---------------------------------------------------------------

    /**
     * Write per sheet.
     *
     * @param template
     *            the template
     * @param os
     *            the os
     * @param beansList
     *            the beans list
     * @return the write status
     */
    WriteStatus writePerSheet(String template,OutputStream os,List<Map<String, Object>> beansList);

    /**
     * Write per sheet.
     *
     * @param is
     *            the is
     * @param os
     *            the os
     * @param beansList
     *            the beans list
     * @return the write status
     */
    WriteStatus writePerSheet(InputStream is,OutputStream os,List<Map<String, Object>> beansList);

}
