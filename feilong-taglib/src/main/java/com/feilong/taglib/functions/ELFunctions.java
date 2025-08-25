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
package com.feilong.taglib.functions;

import static com.feilong.core.lang.StringUtil.EMPTY;

import java.util.Iterator;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.json.JsonUtil;
import com.feilong.taglib.common.AbstractContainsSupport;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Static methods for JSP EL expression functions.
 * 
 * <h3>does-el-support-overloaded-methods?</h3>
 * 
 * <blockquote>
 * <p>
 * It'll always be the first method of the {@link Class#getMethods()} array whose name (and amount of arguments) matches the EL method call.
 * Whether it returns the same method everytime or not depends on the JVM make/version used.
 * </p>
 * 
 * <p>
 * Perhaps you made a Java SE upgrade in the meanwhile as well. The javadoc even says this:
 * </p>
 * 
 * <p style="color:green">
 * The elements in the array returned are not sorted and are not in any particular order.
 * </p>
 * 
 * <p>
 * You should not rely on unspecified behaviour. Give them a different name.
 * </p>
 * 
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see "org.apache.taglibs.standard.functions.Functions"
 * @see "org.owasp.esapi.tags.ELEncodeFunctions"
 * 
 * @see <a href="http://stackoverflow.com/questions/9763619/does-el-support-overloaded-methods">does-el-support-overloaded-methods</a>
 * @since 1.4.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ELFunctions{

    /**
     * 用于 自定义标签/ 自定义el.
     * 
     * <p style="color:red">
     * 注意,比较的是 {@link java.lang.Object#toString()}
     * </p>
     * 
     * @param collection
     *            一个集合,将会被转成Iterator,可以为逗号隔开的字符串,会被分隔成Iterator.
     * @param value
     *            任意类型的值,最终toString 判断比较.
     * @return true, if successful <br>
     *         如果iterator为null/empty,则返回false<br>
     *         否则迭代 <code>iterator</code>,将元素转成String,和传入参数 <code>value</code>的String值进行比较,如果相等直接返回true
     * @see ConvertUtil#toIterator(Object)
     * @see "org.apache.taglibs.standard.tag.common.core.ForEachSupport#supportedTypeForEachIterator(Object)"
     */
    public static boolean contains(Object collection,Object value){
        Iterator<?> iterator = ConvertUtil.toIterator(collection);
        return AbstractContainsSupport.containsByStringValue(iterator, value);
    }

    //---------------------------------------------------------------

    /**
     * 将对象format成json字符串(不会有pretty输出,会连在一起).
     * 
     * <p>
     * 使用 indentFactor=0, indent=0 来格式化
     * </p>
     *
     * @param obj
     *            the obj
     * @return the string
     * @see com.feilong.json.JsonUtil#toString(Object)
     */
    public static String toJsonString(Object obj){
        try{
            return JsonUtil.toString(obj);
        }catch (Exception e){
            log.error("json format:" + obj.toString(), e);
        }
        //此方法应用于jsp标签,如果抛出异常,可能页面不能持续渲染, 但是不能显示异常页面, 因此此处直接返回 ""
        return EMPTY;
    }
}
