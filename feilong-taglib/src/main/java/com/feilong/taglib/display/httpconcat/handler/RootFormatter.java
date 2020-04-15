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

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.StringUtils;

import com.feilong.core.lang.StringUtil;

/**
 * Root 格式化.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class RootFormatter{

    /** Don't let anyone instantiate this class. */
    private RootFormatter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 格式化 root 成 xxxx/xxx/ 形式,.
     *
     * @param root
     *            the root
     * @return 如果 <code>root</code> 是null或者empty,返回 {@link StringUtils#EMPTY}<br>
     *         如果不是以 单斜杆结尾,那么加上一个单斜杆 <br>
     *         如果是以 单斜杆开头,去掉开头的单斜杆 <br>
     */
    public static String format(String root){
        if (isNullOrEmpty(root)){
            return EMPTY;
        }

        //---------------------------------------------------------------

        // 如果不是以 单斜杆结尾,那么加上一个单斜杆 
        if (!root.endsWith("/")){
            root = root + "/";
        }

        //---------------------------------------------------------------

        //如果是以 单斜杆开头,去掉开头的单斜杆
        if (root.startsWith("/")){
            root = StringUtil.substring(root, 1);
        }
        return root;
    }
}
