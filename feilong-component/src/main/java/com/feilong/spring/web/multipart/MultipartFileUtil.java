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
package com.feilong.spring.web.multipart;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.feilong.io.FileUtil;

/**
 * {@link MultipartFile} 工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.9
 */
public final class MultipartFileUtil{

    /** Don't let anyone instantiate this class. */
    private MultipartFileUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 multipart file info map for log map.
     *
     * @param importFile
     *            the import file
     * @return the multipart file info map for log map
     * @since 1.0.9
     */
    public static Map<String, Object> getMultipartFileInfoMapForLogMap(MultipartFile importFile){
        Map<String, Object> map = newLinkedHashMap(5);
        map.put("importFile.getContentType()", importFile.getContentType());
        map.put("importFile.getName()", importFile.getName());
        map.put("importFile.getOriginalFilename()", importFile.getOriginalFilename());
        map.put("importFile.getSize()", FileUtil.formatSize(importFile.getSize()));
        map.put("importFile.isEmpty()", importFile.isEmpty());
        return map;
    }
}
