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
package com.feilong.net.filetransfer.sftp;

import static com.feilong.core.lang.StringUtil.formatPattern;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import com.feilong.core.Validate;
import com.jcraft.jsch.Session;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Sftp Session 工具.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SftpSessionUtil{

    /**
     * Builds the session pretty string.
     *
     * @param session
     *            the session
     *
     * @return the string
     */
    static String buildSessionPrettyString(Session session){
        Validate.notNull(session, "session can't be null!");

        return formatPattern("{}@{}:{}", session.getUserName(), session.getHost(), session.getPort());
    }

    //---------------------------------------------------------------

    /**
     * Gets the map for log.
     *
     * @param session
     *            the session
     *
     * @return the map for log
     */
    static Map<String, Object> getMapForLog(Session session){
        Map<String, Object> map = newLinkedHashMap();
        map.put("serverVersion", session.getServerVersion());
        map.put("clientVersion", session.getClientVersion());
        map.put("serverAliveCountMax", session.getServerAliveCountMax());
        map.put("serverAliveInterval", session.getServerAliveInterval());
        map.put("timeout", session.getTimeout());
        return map;
    }
}
