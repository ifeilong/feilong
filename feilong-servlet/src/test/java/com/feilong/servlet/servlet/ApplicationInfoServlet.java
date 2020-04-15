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
package com.feilong.servlet.servlet;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.SystemUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.ServletContextUtil;

/**
 * 应用信息(用于监控查看).
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.8
 * @deprecated 想个更好的办法
 */
@Deprecated
public class ApplicationInfoServlet extends HttpServlet{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 672020928153455796L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(ApplicationInfoServlet.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException{
        ServletContext servletContext = getServletContext();

        Map<String, Object> map = newLinkedHashMap();
        map.put("Charset defaultCharset", Charset.defaultCharset().name());
        map.put("ServletContext Info", ServletContextUtil.getServletContextInfoMapForLog(servletContext));
        map.put("ServletContext initParameter Map", ServletContextUtil.getInitParameterMap(servletContext));
        //      map.put("ServletContext attributeNames", CollectionsUtil.toList(servletContext.getAttributeNames()));
        //map.put("ServletContext Attribute String Map", ServletContextUtil.getAttributeStringMapForLog(servletContext));
        map.put("System Env Map", SystemUtil.getEnvMap());
        map.put("System Properties Map", SystemUtil.getPropertiesMap());

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("ApplicationInfoServlet:{}", JsonUtil.format(map));
        }

        super.init();
    }
}
