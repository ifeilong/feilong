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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_PAGESIZE;
import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_PAGE_ATTRIBUTE_PAGER_HTML_NAME;
import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_PARAM_DEBUG_NOT_PARSEVM;
import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_PARAM_DEBUG_NOT_PARSEVM_VALUE;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.feilong.core.CharsetType;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.taglib.AbstractStartWriteContentTag;
import com.feilong.taglib.LocaleSupport;
import com.feilong.taglib.display.LocaleSupportUtil;
import com.feilong.taglib.display.pager.command.PagerConstants;
import com.feilong.taglib.display.pager.command.PagerParams;

/**
 * 分页标签.
 * 
 * <p>
 * 你可以访问 wiki 查看更多 <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager">feilongDisplay-pager</a>
 * </p>
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <ol>
 * <li>支持皮肤切换</li>
 * <li>支持velocity模版,支持自定义velocity模版</li>
 * <li>自动识别是否是forwoad页面分页连接</li>
 * <li>分页页码,当前页码永远居中</li>
 * <li>分页页码支持根据页码数字自动显示分页码个数,见参数说明里面的{@link #dynamicNavigationPageNumberConfig}参数</li>
 * <li>经过大型项目检验,通用安全扫描</li>
 * <li>支持国际化(1.0.5 new feature)</li>
 * <li>内置文本框页码输入快速跳转(1.0.5 new feature)</li>
 * <li>支持类似于淘宝最大分页码100 这样的控制 ,见参数 {@link #maxShowPageNo} (1.0.5 new feature)</li>
 * <li>支持Ajax 分页 ,见参数 {@link PagerParams#pagerType} (1.4.0 new feature)</li>
 * </ol>
 * 
 * </blockquote>
 * 
 * <h3>使用方式:</h3>
 * 
 * <blockquote>
 * 
 * <dl>
 * <dt>步骤1.JSP引用自定义标签</dt>
 * <dd>
 * {@code
 *  <%@ taglib prefix="feilongDisplay" uri="http://java.feilong.com/tags-display"%>
 * }
 * </dd>
 * 
 * <dt>步骤2.使用自定义标签</dt>
 * <dd>
 * 精简写法:
 * 
 * {@code
 *  <feilongDisplay:pager count="1000"/>
 * }
 * 
 * 此时其余参数缺省,均使用默认值
 * 
 * </dd>
 * 
 * <dd>所有参数都赋值的写法:
 * 
 * <pre class="code">
 * {@code  
 *          <feilongDisplay:pager count="1000" 
 *              charsetType="utf-8" 
 *              dynamicNavigationPageNumberConfig="1000=6&100=8&1=10"
 *              pageParamName="page" 
 *              pageSize="10"
 *              locale="zh_CN" 
 *              maxShowPageNo="-1"
 *              vmPath="velocity/feilong-default-pager.vm" 
 *              skin="scott"
 *              pagerHtmlAttributeName="feilongPagerHtml1" />
 * }
 * 
 * </pre>
 * 
 * 每个参数的含义,请参见 <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager#params">分页参数</a>
 * </dd>
 * </dl>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager">feilongDisplay-pager</a>
 * @since 1.0.0
 */
public class PagerTag extends AbstractStartWriteContentTag implements LocaleSupport{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID                  = -3523064037264688170L;

    /** 数据总数. */
    private Integer           count;

    //---------------------------------------------------------------

    /** 每页显示多少行,默认20. */
    private Integer           pageSize                          = PagerConstants.DEFAULT_PAGESIZE;

    //---------------------------------------------------------------

    /** url页码参数,默认 pageNo. */
    private String            pageParamName                     = PagerConstants.DEFAULT_PAGE_PARAM_NAME;

    /** 基于classpath 下面的velocity模版文件路径. */
    private String            vmPath                            = PagerConstants.DEFAULT_TEMPLATE_IN_CLASSPATH;

    /** 皮肤,默认digg. */
    private String            skin                              = PagerConstants.DEFAULT_SKIN;

    //---------------------------------------------------------------
    /**
     * 显示最大的页码数,(-1或者不设置,默认显示所有页数).
     * 
     * <p>
     * 比如淘宝,不管搜索东西多少,最多显示100页
     * </p>
     * 
     * <p>
     * 这是一种折中的处理方式,<b>空间换时间</b>.<br>
     * 数据查询越往后翻,对服务器的压力越大,速度越低,而且从业务上来讲商品质量也越差,所以就没有必要给太多了.<br>
     * 新浪微博的时间轴也只给出了10页,同样的折中处理.
     * </p>
     * 
     * @since 1.0.5
     */
    private Integer           maxShowPageNo                     = PagerConstants.DEFAULT_LIMITED_MAX_PAGENO;

    /**
     * 设置{@link Locale} 环境, 支持 {@link java.util.Locale} 或 {@link String} 类型的实例.
     * 
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#value
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#parseLocale(String, String)
     * @see org.apache.taglibs.standard.tag.common.fmt.ParseDateSupport#parseLocale
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseNumberTag#setParseLocale(Object)
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseDateTag#setParseLocale(Object)
     * @see com.feilong.taglib.display.LocaleSupportUtil#toLocal(Object, HttpServletRequest)
     * @see com.feilong.core.bean.ConvertUtil#toLocale(Object)
     * @since 1.0.5
     * @since 1.7.2 change Object type
     */
    private Object            locale;

    /**
     * url编码(默认是 {@link CharsetType#UTF8}).
     * 
     * @since 1.0.5
     */
    private String            charsetType                       = UTF8;

    /**
     * vm被解析出来的文本,会被存在在这个变量中,作用域为pageContext,以便重复使用.
     * 
     * <p>
     * 比如某些页面,上面下面都要显示同样的分页,方便用户操作.
     * </p>
     * 
     * <p>
     * 此外,此变量名称允许变更,以便实现同一页页面不同功能的的分页.
     * </p>
     * 
     * @since 1.0.5
     */
    private String            pagerHtmlAttributeName;

    //---------------------------------------------------------------

    /**
     * 动态显示导航页码数量(默认是 {@link PagerConstants#DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG}).
     * 
     * <h3>背景:</h3>
     * <blockquote>
     * <p>
     * 一般的分页标签是固定的分页页码数量,一般是10个,如果当前页码页码大于1000的时候,还是10条页码的显示(如1001,1002,1003,1004,1005,1006,1007,1008,1009,1010),那么页面分页会很长
     * ,可能打乱页面布局.因此使用动态显示导航页码数量
     * </p>
     * </blockquote>
     * 
     * <h3>规则:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 示例:1000=6&100=8&1=10
     * </p>
     * 
     * <ol>
     * <li>含义:当当前页码{@code >=}1000的时候,显示6个页码;当当前页码{@code >=}100的时候显示8个页码;当当前页码{@code >=}1的时候,显示10个页码</li>
     * <li>设置规则类似于url的参数规则</li>
     * <li>分隔之后,key是当前页码参考值,value是显示页码数量,如果当前页码大于等于key的时候,那么页码的数量会显示值的数量
     * <p>
     * 比如上例中,如果当前页码是1001,那么分页页码会显示成6个, 而如果当前页码是5,那么会显示10个页码
     * </p>
     * </li>
     * <li>顺序不限制,不需要值大的写前面,程序会自动排序,比如你可以写成 1=10&100=8&1000=6</li>
     * <li>如果参数里面有相同名字的key,那么转换的时候取第一个值,比如<span style="color:red">1000</span>=6&<span style="color:red">1000</span>
     * =7&100=8&1=10,有效数据为1000=6&100=8&1=10</li>
     * <li>默认是 1000=6&100=8&1=10,如果设置为empty或者blank, 那么表示不使用动态显示的功能,永远显示10个页码</li>
     * <li>当然如果你需要不管什么时候都显示10个,除了将此值设置为empty或者blank外,你还可以设置为 <code>1000=10&100=10&1=10</code>,值设置为相同</li>
     * </ol>
     * </blockquote>
     * 
     * @since 1.9.2
     */
    private String            dynamicNavigationPageNumberConfig = PagerConstants.DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG;

    //-------------------------end--------------------------------------

    /**
     * Write content.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Override
    public String buildContent(HttpServletRequest request){
        PagerParams pagerParams = buildPagerParams(request);
        String htmlContent = PagerBuilder.buildContent(pagerParams);

        String name = defaultIfNullOrEmpty(pagerHtmlAttributeName, DEFAULT_PAGE_ATTRIBUTE_PAGER_HTML_NAME);
        pageContext.setAttribute(name, htmlContent);// 解析之后的变量设置在 pageContext作用域中

        return htmlContent;
    }

    //---------------------------------------------------------------

    /**
     * Builds the pager params.
     *
     * @param request
     *            the request
     * @return the pager params
     * @since 1.7.2
     */
    private PagerParams buildPagerParams(HttpServletRequest request){
        // 当前全路径
        String pageUrl = RequestUtil.getRequestFullURL(request, charsetType);

        //---------------------------------------------------------------
        PagerParams pagerParams = new PagerParams(count, pageUrl);

        pagerParams.setCurrentPageNo(PagerHelper.getCurrentPageNo(request, pageParamName)); // 当前页码
        pagerParams.setPageSize(pageSize <= 0 ? DEFAULT_PAGESIZE : pageSize);//如果传过来的是empty,如果不处理会报错,参见 https://github.com/venusdrogon/feilong-taglib/issues/7
        pagerParams.setPageParamName(pageParamName);
        pagerParams.setVmPath(vmPath);
        pagerParams.setCharsetType(charsetType);
        pagerParams.setLocale(LocaleSupportUtil.toLocal(locale, request));
        pagerParams.setMaxShowPageNo(maxShowPageNo);

        pagerParams.setSkin(skin);
        pagerParams.setDynamicNavigationPageNumberConfig(dynamicNavigationPageNumberConfig);
        pagerParams.setDebugIsNotParseVM(getDebugIsNotParseVM(request));
        return pagerParams;
    }

    /**
     * debugNotParseVM=true参数可以来控制 是否解析vm模板,以便测试.
     *
     * @param request
     *            the request
     * @return the debug is not parse vm
     * @since 1.7.2
     */
    private static boolean getDebugIsNotParseVM(HttpServletRequest request){
        // debugNotParseVM=true参数可以来控制 是否解析vm模板,以便测试
        String parameter = request.getParameter(DEFAULT_PARAM_DEBUG_NOT_PARSEVM);
        return DEFAULT_PARAM_DEBUG_NOT_PARSEVM_VALUE.equals(parameter);
    }

    //---------------------------------------------------------------

    /**
     * Sets the 数据总数.
     * 
     * @param count
     *            the count to set
     */
    public void setCount(Integer count){
        this.count = count;
    }

    /**
     * Sets the 每页显示多少行,默认20.
     * 
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }

    /**
     * Sets the url页码参数,默认 pageNo.
     * 
     * @param pageParamName
     *            the pageParamName to set
     */
    public void setPageParamName(String pageParamName){
        this.pageParamName = pageParamName;
    }

    /**
     * 基于classpath 下面的velocity模版文件路径.
     * 
     * @param vmPath
     *            the vmPath to set
     */
    public void setVmPath(String vmPath){
        this.vmPath = vmPath;
    }

    /**
     * Sets the 皮肤 默认digg.
     * 
     * @param skin
     *            the skin to set
     */
    public void setSkin(String skin){
        this.skin = skin;
    }

    /**
     * 显示最大的页码数,(-1或者不设置,默认显示所有页数).
     * 
     * <p>
     * 比如淘宝,不管搜索东西多少,最多显示100页
     * </p>
     * 
     * <p>
     * 这是一种折中的处理方式,<b>空间换时间</b>.<br>
     * 数据查询越往后翻,对服务器的压力越大,速度越低,而且从业务上来讲商品质量也越差,所以就没有必要给太多了.<br>
     * 新浪微博的时间轴也只给出了10页,同样的折中处理.
     * </p>
     * 
     * @param maxShowPageNo
     *            the maxShowPageNo to set
     */
    public void setMaxShowPageNo(Integer maxShowPageNo){
        this.maxShowPageNo = maxShowPageNo;
    }

    /**
     * Sets the url编码.
     * 
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     */
    public void setCharsetType(String charsetType){
        this.charsetType = charsetType;
    }

    /**
     * vm被解析出来的文本,会被存在在这个变量中,作用域为pageContext,以便重复使用.
     * 
     * <p>
     * 比如某些页面,上面下面都要显示同样的分页,方便用户操作.
     * </p>
     * 
     * <p>
     * 此外,此变量名称允许变更,以便实现同一页页面不同功能的的分页.
     * </p>
     * 
     * @param pagerHtmlAttributeName
     *            the pagerHtmlAttributeName to set
     */
    public void setPagerHtmlAttributeName(String pagerHtmlAttributeName){
        this.pagerHtmlAttributeName = pagerHtmlAttributeName;
    }

    /**
     * 设置{@link Locale} 环境, 支持 {@link java.util.Locale} 或 {@link String} 类型的实例 .
     *
     * @param locale
     *            the locale to set
     * @see com.feilong.taglib.display.LocaleSupportUtil#toLocal(Object, HttpServletRequest)
     * @see com.feilong.core.bean.ConvertUtil#toLocale(Object)
     */
    @Override
    public void setLocale(Object locale){
        this.locale = locale;
    }

    /**
     * 动态显示导航页码数量(默认是 {@link PagerConstants#DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG}).
     * 
     * <h3>背景:</h3>
     * <blockquote>
     * <p>
     * 一般的分页标签是固定的分页页码数量,一般是10个,如果当前页码页码大于1000的时候,还是10条页码的显示(如1001,1002,1003,1004,1005,1006,1007,1008,1009,1010),那么页面分页会很长
     * ,可能打乱页面布局.因此使用动态显示导航页码数量
     * </p>
     * </blockquote>
     * 
     * <h3>规则:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 示例:1000=6&100=8&1=10
     * </p>
     * 
     * <ol>
     * <li>含义:当当前页码{@code >=}1000的时候,显示6个页码;当当前页码{@code >=}100的时候显示8个页码;当当前页码{@code >=}1的时候,显示10个页码</li>
     * <li>设置规则类似于url的参数规则</li>
     * <li>分隔之后,key是当前页码参考值,value是显示页码数量,如果当前页码大于等于key的时候,那么页码的数量会显示值的数量
     * <p>
     * 比如上例中,如果当前页码是1001,那么分页页码会显示成6个, 而如果当前页码是5,那么会显示10个页码
     * </p>
     * </li>
     * <li>顺序不限制,不需要值大的写前面,程序会自动排序,比如你可以写成 1=10&100=8&1000=6</li>
     * <li>如果参数里面有相同名字的key,那么转换的时候取第一个值,比如<span style="color:red">1000</span>=6&<span style="color:red">1000</span>
     * =7&100=8&1=10,有效数据为1000=6&100=8&1=10</li>
     * <li>默认是 1000=6&100=8&1=10,如果设置为empty或者blank, 那么表示不使用动态显示的功能,永远显示10个页码</li>
     * <li>当然如果你需要不管什么时候都显示10个,除了将此值设置为empty或者blank外,你还可以设置为 <code>1000=10&100=10&1=10</code>,值设置为相同</li>
     * </ol>
     * </blockquote>
     *
     * @param dynamicNavigationPageNumberConfig
     *            the new 动态显示导航页码数量(默认是 {@link PagerConstants#DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG})
     * @since 1.9.2
     */
    public void setDynamicNavigationPageNumberConfig(String dynamicNavigationPageNumberConfig){
        this.dynamicNavigationPageNumberConfig = dynamicNavigationPageNumberConfig;
    }
}
