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
package com.feilong.taglib.display.httpconcat.builder;

import static com.feilong.core.bean.ConvertUtil.convert;
import static com.feilong.core.bean.ConvertUtil.toInteger;
import static com.feilong.core.util.ResourceBundleUtil.getValue;

import java.util.ResourceBundle;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatGlobalConfig;

/**
 * {@link HttpConcatGlobalConfig} 构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.0
 * @since 1.8.8 change class Access Modifiers
 */
public final class HttpConcatGlobalConfigBuilder{

    /** The Constant log. */
    private static final Logger                LOGGER                    = LoggerFactory.getLogger(HttpConcatGlobalConfigBuilder.class);

    //---------------------------------------------------------------

    /** 配置文件 <code>{@value}</code>. */
    //XXX support different environment
    private static final String                CONFIG_FILE               = "config/httpconcat";

    /** The Constant HTTPCONCAT_RESOURCEBUNDLE. */
    private static final ResourceBundle        HTTPCONCAT_RESOURCEBUNDLE = ResourceBundleUtil.getResourceBundle(CONFIG_FILE);

    //---------------------------------------------------------------

    /** Static instance. */
    public static final HttpConcatGlobalConfig GLOBAL_CONFIG             = HttpConcatGlobalConfigBuilder.build();

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private HttpConcatGlobalConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @return the http concat global config
     * @since 1.11.1 rename
     */
    private static HttpConcatGlobalConfig build(){
        HttpConcatGlobalConfig httpConcatGlobalConfig = new HttpConcatGlobalConfig();

        //support
        httpConcatGlobalConfig.setHttpConcatSupport(getRequiredValue("httpconcat.support", Boolean.class));

        //template
        httpConcatGlobalConfig.setTemplateCss(getRequiredValue("httpconcat.template.css", String.class));
        httpConcatGlobalConfig.setTemplateJs(getRequiredValue("httpconcat.template.js", String.class));

        //cache
        httpConcatGlobalConfig
                        .setDefaultCacheEnable(BooleanUtils.toBoolean(getRequiredValue("httpconcat.defaultCacheEnable", Boolean.class)));
        httpConcatGlobalConfig.setDefaultCacheSizeLimit(getRequiredValue("httpconcat.defaultCacheSizeLimit", Integer.class));

        //verson
        httpConcatGlobalConfig.setVersionEncode(getValue(HTTPCONCAT_RESOURCEBUNDLE, "httpconcat.version.encode"));
        httpConcatGlobalConfig.setVersionNameInScope(getValue(HTTPCONCAT_RESOURCEBUNDLE, "httpconcat.version.nameInScope"));
        httpConcatGlobalConfig.setVersionSearchScope(getValue(HTTPCONCAT_RESOURCEBUNDLE, "httpconcat.version.search.scope"));
        httpConcatGlobalConfig.setVersionAutoRefreshValue(getValue(HTTPCONCAT_RESOURCEBUNDLE, "httpconcat.version.autoRefresh.value"));

        //domain
        httpConcatGlobalConfig.setDomain(getValue(HTTPCONCAT_RESOURCEBUNDLE, "httpconcat.domain"));

        //autoPartitionSize
        httpConcatGlobalConfig.setAutoPartitionSize(toInteger(getValue(HTTPCONCAT_RESOURCEBUNDLE, "httpconcat.autoPartitionSize")));

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("init http concat config:[{}]", JsonUtil.format(httpConcatGlobalConfig));
        }
        return httpConcatGlobalConfig;
    }

    //---------------------------------------------------------------

    /**
     * 获得 required value.
     * 
     * <p>
     * 如果 {@code isNullOrEmpty(Object)} ,抛出NullPointerException
     * </p>
     * 
     * @param <T>
     *            the generic type
     * @param keyName
     *            the key name
     * @param typeClass
     *            the type class
     * @return the value if not null or empty
     * @since 1.8.8
     */
    private static <T> T getRequiredValue(String keyName,Class<T> typeClass){
        String value = ResourceBundleUtil.getValue(HTTPCONCAT_RESOURCEBUNDLE, keyName);

        Validate.notBlank(value, "can't find key:[%s] in [%s],pls ensure you have put the correct configuration", keyName, CONFIG_FILE);
        return convert(value, typeClass);
    }
}
