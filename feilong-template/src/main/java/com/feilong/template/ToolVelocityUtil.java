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
package com.feilong.template;

import java.util.Map;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.ConfigurationUtils;
import org.apache.velocity.tools.config.FactoryConfiguration;

/**
 * Tool Velocity 工具类.
 * 
 * <h3>maven dependency:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * {@code 
 * <dependency>
 *    <groupId>org.apache.velocity</groupId>
 *    <artifactId>velocity-tools</artifactId>
 *    <version>2.0</version>
 * </dependency>
 * }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.2.1
 */
public class ToolVelocityUtil extends AbstractTemplateUtil{

    /**
     * Static instance.
     * 
     * @since 1.12.5
     */
    // the static instance works for all types
    public static final ToolVelocityUtil INSTANCE = new ToolVelocityUtil();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.velocity.BaseVelocityUtil#constructContext(java.util.Map)
     */
    @Override
    protected Context buildContext(Map<String, ?> contextKeyValues){
        ToolManager toolManager = buildToolManager();

        //---------------------------------------------------------------
        Map<String, Object> map = toMap(contextKeyValues);

        ToolContext toolContext = toolManager.createContext(map);
        toolContext.putAll(map);
        return toolContext;
    }

    //---------------------------------------------------------------

    /**
     * Builds the tool manager.
     *
     * @return the tool manager
     * @since 1.12.5
     */
    private static ToolManager buildToolManager(){
        FactoryConfiguration factoryConfiguration = ConfigurationUtils.getGenericTools();

        //See org.apache.velocity.tools.config.ConfigurationUtils#read(URL)
        ToolManager toolManager = new ToolManager();
        toolManager.configure(factoryConfiguration);
        return toolManager;
    }
}
