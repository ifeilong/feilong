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
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.feilong.json.JsonUtil;
import com.feilong.servlet.http.SessionUtil;

/**
 * session属性的创建,删除,更新的监听器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
@lombok.extern.slf4j.Slf4j
public class HttpSessionAttributeLoggingListener implements HttpSessionAttributeListener{

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent){
        if (log.isDebugEnabled()){
            HttpSession session = httpSessionBindingEvent.getSession();

            log.debug(
                            "name:[{}],value:[{}] added to [session],now session info:[{}] ",
                            httpSessionBindingEvent.getName(),
                            httpSessionBindingEvent.getValue(),
                            JsonUtil.toString(SessionUtil.getSessionInfoMapForLog(session)));

        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent){
        if (log.isDebugEnabled()){
            log.debug("name:[{}],value:[{}] removed from [session]", httpSessionBindingEvent.getName(), httpSessionBindingEvent.getValue());

        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent){
        if (log.isDebugEnabled()){
            HttpSession session = httpSessionBindingEvent.getSession();

            log.debug(
                            "name:[{}],value:[{}] replaced to [session],now session info:[{}] ",
                            httpSessionBindingEvent.getName(),
                            httpSessionBindingEvent.getValue(),
                            JsonUtil.toString(SessionUtil.getSessionInfoMapForLog(session)));

        }
    }
}
