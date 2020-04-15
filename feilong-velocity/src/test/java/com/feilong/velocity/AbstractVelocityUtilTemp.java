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
package com.feilong.velocity;

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

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
abstract class AbstractVelocityUtilTemp{

    /** The feilong string velocity. */
    private static final String LOGTAG = "feilongStringVelocity";

    //---------------------------------------------------------------

    /**
     * 解析vm文件内容字符串.
     * 
     * <p>
     * 性能差
     * </p>
     *
     * @param vmContent
     *            vm字符串
     * @param contextKeyValues
     *            vm参数
     * @return the string if vmContent isNullOrEmpty,返回 empty
     * @see org.apache.velocity.runtime.resource.ResourceManager#RESOURCE_TEMPLATE
     * @see org.apache.velocity.runtime.resource.ResourceManager#RESOURCE_CONTENT
     * @since 1.2.1
     * @deprecated 性能差, 建议使用 {@link #parseString(String, String, Map)}
     */
    @Deprecated
    public String parseString(String vmContent,Map<String, ?> contextKeyValues){
        if (isNullOrEmpty(vmContent)){
            return EMPTY;
        }

        Context context = buildContext(contextKeyValues);
        //---------------------------------------------------------------
        Writer writer = new StringWriter();
        //true if successful, false otherwise. If false, see Velocity runtime log
        AbstractVelocityUtil.STRING_VELOCITY_ENGINE.evaluate(context, writer, LOGTAG, vmContent);
        return writer.toString();
    }

    /**
     * @param contextKeyValues
     * @return
     * @since 1.12.5
     */
    private Context buildContext(Map<String, ?> contextKeyValues){
        Map<String, Object> map = new LinkedHashMap<>(contextKeyValues);
        return new VelocityContext(map);
    }

}
