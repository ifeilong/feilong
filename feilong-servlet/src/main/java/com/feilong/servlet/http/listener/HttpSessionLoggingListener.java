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
package com.feilong.servlet.http.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.feilong.json.JsonUtil;
import com.feilong.servlet.http.SessionUtil;

/**
 * session创建和销毁的监听器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see "org.springframework.web.util.HttpSessionMutexListener"
 * @since 1.10.4
 */
@lombok.extern.slf4j.Slf4j
public class HttpSessionLoggingListener implements HttpSessionListener{

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent){
        if (log.isDebugEnabled()){
            HttpSession session = httpSessionEvent.getSession();
            log.debug(
                            "session [created],source:[{}],info:{}",
                            httpSessionEvent.getSource(),
                            JsonUtil.toString(SessionUtil.getSessionInfoMapForLog(session)));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent){
        if (log.isDebugEnabled()){
            HttpSession session = httpSessionEvent.getSession();
            log.debug(
                            "session [destroyed],source:[{}],info:{}",
                            httpSessionEvent.getSource(),
                            JsonUtil.toString(SessionUtil.getSessionInfoMapForLog(session)));
        }
    }
}
