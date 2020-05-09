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

import static com.feilong.core.Validator.isNullOrEmpty;
import static java.util.Collections.emptyMap;

import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JsonUtil;

/**
 * base Velocity 工具类.
 * 
 * <h3>分离实例&&单列模式:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * //Properties properties = new Properties();
 * //properties.put(Velocity.RESOURCE_LOADER, resource_loader_string);
 * //properties.put(resource_loader_string + ".resource.loader.class", StringResourceLoader.class.getName());
 * //
 * //properties.put(Velocity.INPUT_ENCODING, default_CharsetType);
 * //properties.put(Velocity.OUTPUT_ENCODING, default_CharsetType);
 * 
 * //分离实例
 * //VelocityEngine velocityEngine = new VelocityEngine();
 * //velocityEngine.init(properties);
 * 
 * //单列模式
 * //Velocity.init(properties);
 * //---------------------------------------------------------------
 * //String templateName = feilongStringVelocity;
 * //StringResourceRepository stringResourceRepository = StringResourceLoader.getRepository();
 * //stringResourceRepository.putStringResource(templateName, vmContent);
 * //String parseVMTemplateAfterInitVelocity = parseVMTemplateAfterInitVelocity(templateName, contextKeyValues);
 * 
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader
 * @see org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
 * @see org.apache.velocity.runtime.resource.loader.DataSourceResourceLoader
 * @see org.apache.velocity.runtime.resource.loader.StringResourceLoader
 * @see org.apache.velocity.runtime.RuntimeConstants
 * @see org.apache.velocity.runtime.resource.ResourceManagerImpl#assembleResourceLoaderInitializers()
 * @since 1.2.1
 */
//默认访问修饰符
abstract class AbstractTemplateUtil{

    /** The Constant log. */
    private static final Logger LOGGER                    = LoggerFactory.getLogger(AbstractTemplateUtil.class);

    //---------------------------------------------------------------

    /** The Constant CLASSPATH_VELOCITY_ENGINE. */
    static final VelocityEngine CLASSPATH_VELOCITY_ENGINE = VelocityEngineBuilder.build("config.feilong-velocity-ClasspathResourceLoader");

    /** The Constant STRING_VELOCITY_ENGINE. */
    static final VelocityEngine STRING_VELOCITY_ENGINE    = VelocityEngineBuilder.build("config.feilong-velocity-StringResourceLoader");

    //---------------------------------------------------------------

    /**
     * 解析vm模板文件.
     *
     * @param templateInClassPath
     *            基于classpath 下面的velocity模版文件路径,比如 \\loxia\\excel\\template\\trainReport.html
     * @return the string
     *         如果 <code>templateInClassPath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>templateInClassPath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see Template
     * @see org.apache.velocity.runtime.resource.ResourceManager#RESOURCE_TEMPLATE
     * @see org.apache.velocity.runtime.resource.ResourceManager#RESOURCE_CONTENT
     * @see VelocityEngine#getTemplate(String)
     * @see VelocityEngine#getTemplate(String, String)
     * @see Template#merge(org.apache.velocity.context.Context, Writer)
     * @since 2.1.0
     */
    public String parseTemplateWithClasspathResourceLoader(String templateInClassPath){
        return parseTemplateWithClasspathResourceLoader(templateInClassPath, null);
    }

    /**
     * 解析vm模板文件.
     *
     * @param templateInClassPath
     *            基于classpath 下面的velocity模版文件路径,比如 \\loxia\\excel\\template\\trainReport.html
     * @param contextKeyValues
     *            解析vm模板使用到的参数
     * @return the string
     *         如果 <code>templateInClassPath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>templateInClassPath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see Template
     * @see org.apache.velocity.runtime.resource.ResourceManager#RESOURCE_TEMPLATE
     * @see org.apache.velocity.runtime.resource.ResourceManager#RESOURCE_CONTENT
     * @see VelocityEngine#getTemplate(String)
     * @see VelocityEngine#getTemplate(String, String)
     * @see Template#merge(org.apache.velocity.context.Context, Writer)
     * @since 1.2.1
     */
    //XXX will Restructure
    public String parseTemplateWithClasspathResourceLoader(String templateInClassPath,Map<String, ?> contextKeyValues){
        Validate.notBlank(templateInClassPath, "templateInClassPath can't be null/empty!");

        Context context = buildContext(contextKeyValues);
        return VelocityEngineParser.parse(CLASSPATH_VELOCITY_ENGINE, templateInClassPath, context);
    }

    //---------------------------------------------------------------
    /**
     * Velocity with string template example.
     * 
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * Map{@code <String, Object>} map = newHashMap();
     * map.put("memberId", "5");
     * 
     * String vmContent = "${memberId},feilong";
     * 
     * assertEquals("5,feilong", VelocityUtil.INSTANCE.parseString(vmContent, map));
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <p>
     * 如果有参数解析不了, 将不解析该参数
     * </p>
     * 
     * <h4>示例:</h4>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * Map{@code <String, Object>} map = newHashMap();
     * map.put("memberId1", "5");
     * 
     * String vmContent = "${memberId},${memberId1},feilong";
     * 
     * assertEquals("${memberId},5,feilong", VelocityUtil.INSTANCE.parseString(vmContent, map));
     * </pre>
     * 
     * </blockquote>
     * </blockquote>
     * 
     * @param vmContent
     *            the vm content
     * @param contextKeyValues
     *            the context key values
     * @return 如果 <code>vmContent</code> 是null或者empty,返回 <code>vmContent</code> <br>
     * @see <a href=
     *      "https://stackoverflow.com/questions/6521871/apaches-velocity-gettemplate-how-to-pass-string-object-instead-of-vm-fil">how to
     *      pass string/object instead of .VM file
     *      </a>
     * @since 1.12.5
     */
    public String parseString(String vmContent,Map<String, ?> contextKeyValues){
        if (isNullOrEmpty(vmContent)){
            return vmContent;
        }

        //---------------------------------------------------------------
        String templateName = vmContent;

        // Initialize my template repository. 
        StringResourceRepository stringResourceRepository = (StringResourceRepository) STRING_VELOCITY_ENGINE
                        .getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);

        stringResourceRepository.putStringResource(templateName, vmContent);

        //---------------------------------------------------------------
        // Set parameters for my template.
        Context context = buildContext(contextKeyValues);
        return VelocityEngineParser.parse(STRING_VELOCITY_ENGINE, templateName, context);
    }

    //---------------------------------------------------------------

    /**
     * Construct context.
     *
     * @param contextKeyValues
     *            the context key values
     * @return the context
     * @since 1.10.5
     */
    protected abstract Context buildContext(Map<String, ?> contextKeyValues);

    /**
     * To map.
     *
     * @param contextKeyValues
     *            the context key values
     * @return the map
     * @since 1.12.5
     */
    protected static Map<String, Object> toMap(Map<String, ?> contextKeyValues){
        if (null == contextKeyValues){
            return emptyMap();
        }
        //---------------------------------------------------------------
        Map<String, Object> result = new LinkedHashMap<>(contextKeyValues);
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("will build [{}] use map:[{}]", Context.class.getName(), JsonUtil.formatSimpleMap(result));
        }
        return result;
    }
}
