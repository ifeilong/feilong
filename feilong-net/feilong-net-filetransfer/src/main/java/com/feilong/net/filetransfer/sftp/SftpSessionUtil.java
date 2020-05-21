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

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import com.feilong.core.Validate;
import com.feilong.tools.slf4j.Slf4jUtil;
import com.jcraft.jsch.Session;

/**
 * The Class SftpSessionUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
class SftpSessionUtil{

    /** Don't let anyone instantiate this class. */
    private SftpSessionUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the session pretty string.
     *
     * @param session
     *            the session
     * @return the string
     */
    static final String buildSessionPrettyString(Session session){
        Validate.notNull(session, "session can't be null!");

        return Slf4jUtil.format("{}@{}:{}", session.getUserName(), session.getHost(), session.getPort());
    }

    //---------------------------------------------------------------

    /**
     * Gets the map for log.
     *
     * @param session
     *            the session
     * @return the map for log
     */
    static final Map<String, Object> getMapForLog(Session session){
        Map<String, Object> map = newLinkedHashMap();

        map.put("clientVersion", session.getClientVersion());
        map.put("serverVersion", session.getServerVersion());
        map.put("serverAliveCountMax", session.getServerAliveCountMax());
        map.put("serverAliveInterval", session.getServerAliveInterval());
        map.put("timeout", session.getTimeout());

        //map.put("userInfo", session.getUserInfo());
        return map;
    }
}
