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
package com.feilong.taglib.display.pager;

import static com.feilong.core.bean.ConvertUtil.toInteger;

import javax.servlet.http.HttpServletRequest;

import com.feilong.servlet.http.RequestUtil;
import com.feilong.taglib.display.pager.command.PagerConstants;

/**
 * 分页helper.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.4.0
 */
public final class PagerHelper{

    /** Don't let anyone instantiate this class. */
    private PagerHelper(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得当前分页数字,不带这个参数或者转换异常 返回1.
     * 
     * @param request
     *            当前请求
     * @param pageParamName
     *            分页参数名称 see {@link PagerConstants#DEFAULT_PAGE_PARAM_NAME}
     * @return
     *         <ul>
     *         <li>请求参数中,分页参数值 Integer 类型</li>
     *         <li>如果参数中不带这个分页参数,或者转换异常 返回1</li>
     *         </ul>
     * @see PagerConstants#DEFAULT_PAGE_PARAM_NAME
     */
    public static Integer getCurrentPageNo(HttpServletRequest request,String pageParamName){
        String pageNoString = RequestUtil.getParameter(request, pageParamName);
        return toInteger(pageNoString, 1);
    }

}
