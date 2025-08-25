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
package com.feilong.component.upload;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.feilong.io.FileUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link MultipartFile} 工具类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.9
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class MultipartFileUtil{

    /**
     * 获得 multipart file info map for log map.
     *
     * @param multipartFile
     *            the import file
     * @return the multipart file info map for log map
     * @since 1.0.9
     */
    static Map<String, Object> getMultipartFileInfoMapForLogMap(MultipartFile multipartFile){
        Map<String, Object> map = newLinkedHashMap(5);
        map.put("multipartFile.getContentType()", multipartFile.getContentType());
        map.put("multipartFile.getName()", multipartFile.getName());
        map.put("multipartFile.getOriginalFilename()", multipartFile.getOriginalFilename());
        map.put("multipartFile.getSize()", FileUtil.formatSize(multipartFile.getSize()));
        return map;
    }
}
