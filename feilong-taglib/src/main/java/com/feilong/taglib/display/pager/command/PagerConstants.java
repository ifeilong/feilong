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
package com.feilong.taglib.display.pager.command;

/**
 * 分页常量.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.5
 */
public final class PagerConstants{

    /** 默认分页每页显示数量 <code>{@value}</code>. */
    public static final int    DEFAULT_PAGESIZE                              = 20;

    /**
     * 默认分页页码显示数量 <code>{@value}</code>.
     * 
     * @since 1.9.2
     */
    public static final int    DEFAULT_NAVIGATION_PAGE_NUMBER                = 10;

    /** 默认的皮肤 <code>{@value}</code>. */
    public static final String DEFAULT_SKIN                                  = "digg";

    /** 默认分页页码参数名称 <code>{@value}</code>. */
    public static final String DEFAULT_PAGE_PARAM_NAME                       = "pageNo";

    /** 默认分页使用的vm 脚本 <code>{@value}</code>. */
    public static final String DEFAULT_TEMPLATE_IN_CLASSPATH                 = "velocity/feilong-default-pager.vm";

    //---------------------------------------------------------------
    /** 默认将解析出来的htm 的存放在 request 作用域里面的变量 <code>{@value}</code>. */
    public static final String DEFAULT_PAGE_ATTRIBUTE_PAGER_HTML_NAME        = "feilongPagerHtml";

    /**
     * 模板链接页码.
     * 
     * <p>
     * 通常用于vm渲染,ajax替换,一般url正常的url不会出现这个分页码<code>{@value}</code>.
     * </p>
     *
     * @since 1.0.5
     */
    // XXX 可修改为 可传递参数
    public static final int    DEFAULT_TEMPLATE_PAGE_NO                      = -88888888;

    /** 最多显示分页码 <code>{@value}</code>. */
    public static final int    DEFAULT_LIMITED_MAX_PAGENO                    = -1;

    /**
     * 动态显示导航页码数量配置 <code>{@value}</code>.
     * 
     * @since 1.9.2
     */
    public static final String DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG = "1000=6&100=8&1=10";

    // --------------特殊变量----------------------------------------------------------------------

    /** <code>(调试使用)</code> url 中特殊变量,如果带有这个变量将不解析vm,便于通过日志等来查找发生的问题 <code>{@value}</code> . */
    public static final String DEFAULT_PARAM_DEBUG_NOT_PARSEVM               = "debugNotParseVM";

    /** <code>(调试使用)</code> url 中特殊变量,如果带有这个变量将不解析vm,便于通过日志等来查找发生的问题 <code>{@value}</code> . */
    public static final String DEFAULT_PARAM_DEBUG_NOT_PARSEVM_VALUE         = "true";

    //---------------------------------------------------------------
    /**
     * vm里面 {@link PagerVMParam}对象的变量名称 <code>{@value}</code>.
     * 
     * <p>
     * 在vm里面,你可以使用 <code>${pagerVMParam.xxx}</code> 来获取数据
     * </p>
     */
    public static final String VM_KEY_PAGERVMPARAM                           = "pagerVMParam";

    /**
     * vm里面i18n变量名称 <code>{@value}</code>.
     * 
     * <p>
     * 在vm里面,你可以使用 <code>${i18nMap.xxx}</code> 来获取数据.
     * </p>
     */
    public static final String VM_KEY_I18NMAP                                = "i18nMap";

    /** 国际化配置文件<code>{@value}</code>. */
    public static final String I18N_FEILONG_PAGER                            = "messages/feilong-pager";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private PagerConstants(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
}
