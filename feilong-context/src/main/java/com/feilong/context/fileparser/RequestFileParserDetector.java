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
package com.feilong.context.fileparser;

import javax.servlet.http.HttpServletRequest;

/**
 * 基于request 请求来探测使用的{@link FileParser}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public interface RequestFileParserDetector{

    /**
     * Detect.
     *
     * @param request
     *            the request
     * @return the request file creator
     */
    FileParser detect(HttpServletRequest request);
}
