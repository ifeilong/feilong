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
package com.feilong.io;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import com.feilong.io.entity.MimeType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 获取文件Mime-Type.
 * 
 * <p>
 * MIME(Multi-Purpose Internet Mail Extensions)
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see com.feilong.io.entity.MimeType
 * @see "org.apache.catalina.startup.Tomcat#DEFAULT_MIME_MAPPINGS"
 * @see "org.apache.http.entity.ContentType"
 * @see <a href="http://stackoverflow.com/questions/4348810/java-library-to-find-the-mime-type-from-file-content/10140531#10140531">java
 *      library to find the mime type from file content</a>
 * @see <a href="http://stackoverflow.com/questions/51438/getting-a-files-mime-type-in-java">Getting A File Mime Type In Java</a>
 * @see <a href="http://en.wikipedia.org/wiki/MIME">MIME Wiki</a>
 * @see <a href="http://tika.apache.org/">也可以使用Apache Tika</a>
 * @since 1.0.8
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MimeTypeUtil{

    /** The Constant fileExtensionMap. */
    private static final Map<String, String> FILE_EXTENSION_MAP;

    //---------------------------------------------------------------

    static{
        FILE_EXTENSION_MAP = newHashMap();
        for (MimeType mimeType : MimeType.values()){
            FILE_EXTENSION_MAP.put(mimeType.getExtension(), mimeType.getMime());
        }
    }

    //---------------------------------------------------------------

    /**
     * 获得 content type by file name.
     * 
     * <p>
     * <b>Very incomplete function.</b><br>
     * As of Java 7, html, pdf and jpeg extensions 返回正确的 mime-type,但是js 和 css 返回 null!
     * </p>
     * 
     * <p>
     * I tried Apache Tika but it is huge with tons of dependencies, <br>
     * URLConnection doesn't use the bytes of the files, <br>
     * {@link javax.activation.MimetypesFileTypeMap} also just looks at files names,and I couldn't move to Java 7.
     * </p>
     * 
     * @param fileName
     *            the file name
     * @return the content type by file name
     * @see java.net.URLConnection#getFileNameMap()
     * @see javax.activation.MimetypesFileTypeMap#getContentType(java.io.File)
     * @see java.net.FileNameMap#getContentTypeFor(String)
     * @see com.feilong.lib.io.FilenameUtils#getExtension(String)
     * @see java.net.URLConnection#guessContentTypeFromName(String)
     * @see java.net.URLConnection#guessContentTypeFromStream(java.io.InputStream)
     * @see "java.nio.file.Files#probeContentType(java.nio.file.Path)"
     */
    public static String getContentTypeByFileName(String fileName){
        String extension = FilenameUtil.getExtension(fileName);
        if (isNullOrEmpty(extension)){
            return EMPTY;
        }

        //---------------------------------------------------------------

        // 1. first use java's build-in utils
        //从数据文件加载文件名映射(一个 mimetable).它首先尝试加载由 "content.types.user.table" 属性定义的特定于用户的表.如果加载失败,它会尝试加载位于 java 主目录下的 lib/content-types.properties 中的默认内置表.
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(fileName);

        // 2. nothing found -> lookup our in extension map to find types like ".doc" or ".docx"
        return isNullOrEmpty(contentType) ? FILE_EXTENSION_MAP.get(extension.toLowerCase()) : contentType;
    }
}