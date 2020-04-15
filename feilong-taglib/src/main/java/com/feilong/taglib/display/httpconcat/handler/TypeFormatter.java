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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.taglib.display.httpconcat.builder.TemplateFactory.TYPE_CSS;
import static com.feilong.taglib.display.httpconcat.builder.TemplateFactory.TYPE_JS;

import java.util.List;

import org.apache.commons.lang3.Validate;

/**
 * type 值格式化.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public final class TypeFormatter{

    /** Don't let anyone instantiate this class. */
    private TypeFormatter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the type.
     *
     * @param type
     *            the type
     * @param itemSrcList
     *            the item src list
     * @return 如果 type 有值,那么直接返回<br>
     *         如果 type 没有值,那么:<br>
     *         如果 <code>itemSrcList</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>itemSrcList</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     */
    public static String format(String type,List<String> itemSrcList){
        if (isNotNullOrEmpty(type)){
            return type;
        }

        Validate.notEmpty(itemSrcList, "itemSrcList can't be null/empty!");

        //---------------------------------------------------------------

        String itemSrc = itemSrcList.get(0);
        return itemSrc.toLowerCase().endsWith(".js") ? TYPE_JS : TYPE_CSS;
    }
}
