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
package com.feilong.xml.xstream;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.feilong.lib.xstream.XStream;
import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.io.HierarchicalStreamDriver;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.io.naming.NoNameCoder;
import com.feilong.lib.xstream.io.xml.CompactWriter;
import com.feilong.lib.xstream.io.xml.DomDriver;
import com.feilong.lib.xstream.security.AnyTypePermission;

/**
 * 基于 {@link XStreamConfig} 构造 {@link XStream}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.7
 */
public final class XStreamBuilder{

    /** xstream 默认用的是 xppDriver 需要依赖 xmlpull:xmlpull jar. */
    private static final HierarchicalStreamDriver DEFAULT_NONAMECODER               = new DomDriver(UTF8, new NoNameCoder());

    /** xstream 默认用的是 xppDriver 需要依赖 xmlpull:xmlpull jar. */
    private static final HierarchicalStreamDriver DEFAULT_NONAMECODER_COMPACTWRITER = new DomDriver(UTF8, new NoNameCoder()){

                                                                                        @Override
                                                                                        public HierarchicalStreamWriter createWriter(
                                                                                                        Writer out){
                                                                                            return new CompactWriter(out, getNameCoder());
                                                                                        }
                                                                                    };
    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private XStreamBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 基于 {@link XStreamConfig} 构造 {@link XStream}.
     *
     * @param xStreamConfig
     *            the xstream config
     * @return the x stream
     */
    public static XStream build(XStreamConfig xStreamConfig){
        XStream xstream = buildDefault(xStreamConfig);
        if (isNullOrEmpty(xStreamConfig)){
            return xstream;
        }

        //---------------------------------------------------------------
        doAlias(xStreamConfig, xstream);

        doAddImplicitCollection(xStreamConfig, xstream);

        doWithConverter(xStreamConfig, xstream);

        doProcessAnnotationsTypes(xStreamConfig, xstream);

        doDefaultImplementationMap(xStreamConfig, xstream);

        // 属性重命名
        // xstream.aliasField(alias, definedIn, fieldName);
        // 包重命名
        // xstream.aliasPackage(name, pkgName);
        return xstream;
    }

    //---------------------------------------------------------------

    /**
     * Do default implementation map.
     *
     * @param xStreamConfig
     *            the x stream config
     * @param xstream
     *            the xstream
     * @since 1.10.7
     */
    private static void doDefaultImplementationMap(XStreamConfig xStreamConfig,XStream xstream){
        Map<Class<?>, Class<?>> defaultImplementationMap = xStreamConfig.getDefaultImplementationMap();
        if (isNotNullOrEmpty(defaultImplementationMap)){

            for (Map.Entry<Class<?>, Class<?>> entry : defaultImplementationMap.entrySet()){
                Class<?> defaultImplementation = entry.getKey();
                Class<?> ofType = entry.getValue();
                //Associate a default implementation of a class with an object.
                //Whenever XStream encounters an instance of this type, it will use the default implementation instead.
                //For example, java.util.ArrayList is the default implementation of java.util.List.
                xstream.addDefaultImplementation(defaultImplementation, ofType);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Do with converter.
     *
     * @param xStreamConfig
     *            the x stream config
     * @param xstream
     *            the xstream
     */
    private static void doWithConverter(XStreamConfig xStreamConfig,XStream xstream){
        List<? extends Converter> converterList = xStreamConfig.getConverterList();
        if (isNotNullOrEmpty(converterList)){
            for (Converter converter : converterList){
                xstream.registerConverter(converter);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the default.
     *
     * @param xStreamConfig
     *            the x stream config
     * @return the x stream
     */
    private static XStream buildDefault(XStreamConfig xStreamConfig){
        XStream xstream = new XStream(buildHierarchicalStreamDriver(xStreamConfig));
        //since 1.4.7
        xstream.addPermission(AnyTypePermission.ANY);
        //自动探测注解
        xstream.autodetectAnnotations(true);

        //忽略未知元素 Ignore all unknown elements.
        xstream.ignoreUnknownElements();

        //避免出现以下警告:Security framework of XStream not initialized, XStream is probably vulnerable
        //since 1.14.3
        //XStream.setupDefaultSecurity(xstream)
        return xstream;
    }

    /**
     * Do process annotations types.
     *
     * @param xStreamConfig
     *            the x stream config
     * @param xstream
     *            the xstream
     */
    private static void doProcessAnnotationsTypes(XStreamConfig xStreamConfig,XStream xstream){
        Class<?>[] processAnnotationsTypes = xStreamConfig.getProcessAnnotationsTypes();

        if (isNotNullOrEmpty(processAnnotationsTypes)){
            xstream.processAnnotations(processAnnotationsTypes);
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the hierarchical stream driver.
     *
     * @param xStreamConfig
     *            the x stream config
     * @return the hierarchical stream driver
     */
    private static HierarchicalStreamDriver buildHierarchicalStreamDriver(XStreamConfig xStreamConfig){
        return defaultDriver(xStreamConfig);
    }

    /**
     * Default driver.
     *
     * @param xStreamConfig
     *            the x stream config
     * @return the xpp driver
     */
    private static HierarchicalStreamDriver defaultDriver(XStreamConfig xStreamConfig){
        if (null == xStreamConfig){
            return DEFAULT_NONAMECODER;
        }
        return xStreamConfig.getIsPrettyPrint() ? DEFAULT_NONAMECODER : DEFAULT_NONAMECODER_COMPACTWRITER;
    }

    //---------------------------------------------------------------

    /**
     * 设置 add implicit collection.
     *
     * @param xStreamConfig
     *            the x stream config
     * @param xstream
     *            the xstream
     */
    private static void doAddImplicitCollection(XStreamConfig xStreamConfig,XStream xstream){
        Map<String, Class<?>> implicitCollectionMap = xStreamConfig.getImplicitCollectionMap();
        if (isNullOrEmpty(implicitCollectionMap)){
            return;
        }

        //---------------------------------------------------------------
        for (Map.Entry<String, Class<?>> entry : implicitCollectionMap.entrySet()){
            String key = entry.getKey();
            Class<?> value = entry.getValue();
            xstream.addImplicitCollection(value, key);
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 alias.
     *
     * @param xStreamConfig
     *            the x stream config
     * @param xstream
     *            the xstream
     */
    private static void doAlias(XStreamConfig xStreamConfig,XStream xstream){
        Map<String, Class<?>> aliasMap = xStreamConfig.getAliasMap();
        if (isNullOrEmpty(aliasMap)){
            return;
        }

        //---------------------------------------------------------------
        for (Map.Entry<String, Class<?>> entry : aliasMap.entrySet()){
            String key = entry.getKey();
            Class<?> value = entry.getValue();
            // 类重命名
            xstream.alias(key, value);
        }
    }
}
