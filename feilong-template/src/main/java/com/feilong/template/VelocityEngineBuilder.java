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

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toProperties;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JsonUtil;
import com.feilong.core.Validate;

/**
 * {@link VelocityEngine} 构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.5
 */
class VelocityEngineBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityEngineBuilder.class);

    /** Don't let anyone instantiate this class. */
    private VelocityEngineBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    // 分离实例 避免影响其他的 项目

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param configFileInClassPath
     *            the config class path file
     * @return 如果 <code>configFileInClassPath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>configFileInClassPath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    static VelocityEngine build(String configFileInClassPath){
        Validate.notBlank(configFileInClassPath, "configFileInClassPath can't be blank!");

        Properties properties = toProperties(getResourceBundle(configFileInClassPath));
        //---------------------------------------------------------------

        String message = "can't load [%s],this properties is use for init velocityEngine,Please make sure that the location of the file path";
        Validate.notEmpty(properties, message, configFileInClassPath);

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will use [{}] init velocity, properties:{}", configFileInClassPath, JsonUtil.format(toMap(properties)));
        }
        //---------------------------------------------------------------
        // 单列模式 Velocity.init(properties); RuntimeSingleton.isInitialized()

        // 分离实例 避免影响其他的 项目
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(properties);
        return velocityEngine;
    }
}
