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
package com.feilong.context.converter;

import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;

import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.context.converter.builder.AliasBeanBuilder;
import com.feilong.context.converter.builder.BeanBuilder;
import com.feilong.context.converter.builder.CommonBeanBuilder;
import com.feilong.context.converter.builder.NameAndValueMapBuilder;
import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.json.JsonUtil;

/**
 * map 构造器之后转成bean ,定义了两个标准方法,也是核心步骤:
 * 
 * <ol>
 * <li>{@link NameAndValueMapBuilder#build(String)} 将xml转成map,目前需要自定义来实现,后期可能会开发默认基本实现</li>
 * 
 * <li>
 * {@link BeanBuilder#build(Map, Object)} 将map转成javabean<br>
 * 
 * 目前有两个基本实现类:
 * 
 * <ul>
 * <li>{@link CommonBeanBuilder} 原javabean只需要符合标准javaBean规范即可,不需要修改,内部使用 {@link com.feilong.core.bean.BeanUtil} 来实现反射赋值</li>
 * <li>{@link AliasBeanBuilder},javabean需要在字段上添加 {@link com.feilong.core.bean.Alias} 标识,可以来处理 XML中字段可能全部是大写 <br>
 * 比如BANK,但是javabean 中的字段却是 bank,可以使用下面的代码来实现隐射
 * 
 * <code>
 * <pre class="code">
 * &#064;Alias(name = &quot;BANK&quot;,sampleValue = &quot;BRI&quot;)
 * private String bank;
 * </pre>
 * </code>
 * 
 * </li>
 * 
 * </ul>
 * </li>
 * 
 * </ol>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.8.3
 * @since 1.11.2 rename from AbstractParse
 * @since 1.11.3 rename
 */
public class MapBuilderStringToBeanConverter<T> extends AbstractBeanClassStringToBeanConverter<T>{

    /** The Constant LOGGER. */
    private static final Logger    LOGGER = LoggerFactory.getLogger(MapBuilderStringToBeanConverter.class);

    //---------------------------------------------------------------

    /** 名字和值的map 构造器. */
    private NameAndValueMapBuilder nameAndValueMapBuilder;

    /** 构造对象. */
    private BeanBuilder            beanBuilder;

    //---------------------------------------------------------------

    /**
     * Instantiates a new string to bean converter.
     */
    public MapBuilderStringToBeanConverter(){
        this(null, null);
    }

    /**
     * Instantiates a new string to bean converter.
     *
     * @param nameAndValueMapBuilder
     *            the name and value map builder
     * @param beanBuilder
     *            the bean builder
     */
    public MapBuilderStringToBeanConverter(NameAndValueMapBuilder nameAndValueMapBuilder, BeanBuilder beanBuilder){
        super();
        this.nameAndValueMapBuilder = nameAndValueMapBuilder;
        this.beanBuilder = beanBuilder;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.converter.AbstractStringToBeanConverter#handler(java.lang.String)
     */
    @Override
    protected T handler(String inputString){
        Validate.notNull(beanClass, "beanClass can't be null!");

        //---------------------------------------------------------------

        // 解析 xml,获得我们需要的 var name 和值.
        Map<String, String> nameAndValueMap = nameAndValueMapBuilder.build(inputString);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will build [{}], use data:{}", beanClass.getName(), JsonUtil.format(sortMapByKeyAsc(nameAndValueMap)));
        }
        //---------------------将nameAndValueMap转成对象.------------------------------------------
        return beanBuilder.build(nameAndValueMap, ConstructorUtil.newInstance(beanClass));
    }

    //---------------------------------------------------------------

    /**
     * 获得 名字和值的map 构造器.
     *
     * @return the nameAndValueMapBuilder
     */
    public NameAndValueMapBuilder getNameAndValueMapBuilder(){
        return nameAndValueMapBuilder;
    }

    /**
     * 设置 名字和值的map 构造器.
     *
     * @param nameAndValueMapBuilder
     *            the nameAndValueMapBuilder to set
     */
    public void setNameAndValueMapBuilder(NameAndValueMapBuilder nameAndValueMapBuilder){
        this.nameAndValueMapBuilder = nameAndValueMapBuilder;
    }

    /**
     * 获得 构造对象.
     *
     * @return the beanBuilder
     */
    public BeanBuilder getBeanBuilder(){
        return beanBuilder;
    }

    /**
     * 设置 构造对象.
     *
     * @param beanBuilder
     *            the beanBuilder to set
     */
    public void setBeanBuilder(BeanBuilder beanBuilder){
        this.beanBuilder = beanBuilder;
    }
}
