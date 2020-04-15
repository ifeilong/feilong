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

import static com.feilong.core.CharsetType.UTF8;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.feilong.core.CharsetType;
import com.feilong.taglib.display.CacheParam;
import com.feilong.taglib.display.pager.PagerBuilder;

/**
 * 方法参数.
 * 
 * <p>
 * 用于{@link PagerBuilder#buildContent(PagerParams)}参数封装
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see PagerVMParam
 * @since 1.0.3
 */
public class PagerParams implements Serializable,CacheParam{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID                  = 7310948528499709685L;

    /** 总数据条数. */
    private Integer           totalCount;

    /** 每页显示多少条. */
    private Integer           pageSize                          = PagerConstants.DEFAULT_PAGESIZE;

    /** 当前第几页. */
    private Integer           currentPageNo;

    /**
     * The pager type.
     * 
     * @since 1.4.0
     */
    private PagerType         pagerType                         = PagerType.REDIRECT;

    //---------------------------------------------------------------

    /** 分页的 基础 url. */
    private String            pageUrl;

    /** 皮肤:可选. */
    private String            skin                              = PagerConstants.DEFAULT_SKIN;

    /** 分页参数名称. */
    private String            pageParamName                     = PagerConstants.DEFAULT_PAGE_PARAM_NAME;

    /** vm的路径. */
    private String            vmPath                            = PagerConstants.DEFAULT_TEMPLATE_IN_CLASSPATH;

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
     * 编码集.
     * 
     * @since 1.0.5
     */
    private String            charsetType                       = UTF8;

    /**
     * 获得此 Java 虚拟机实例的当前默认语言环境值. <br>
     * Java 虚拟机根据主机的环境在启动期间设置默认语言环境.如果没有明确地指定语言环境,则很多语言环境敏感的方法都使用该方法.可使用 setDefault 方法更改该值..
     * 
     * @since 1.0.5
     */
    private Locale            locale                            = Locale.getDefault();

    /** debug 模式. */
    private boolean           debugIsNotParseVM;

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

    //-----------------------------------------------------------------------------------------

    /**
     * The Constructor.
     * 
     * @param totalCount
     *            总数据条数 the total count
     * @param pageUrl
     *            分页的 基础 url the page url
     */
    public PagerParams(Integer totalCount, String pageUrl){
        this.totalCount = totalCount;
        this.pageUrl = pageUrl;
    }

    /**
     * The Constructor.
     *
     * @param totalCount
     *            the total count
     * @param pagerType
     *            the pager type
     */
    public PagerParams(Integer totalCount, PagerType pagerType){
        super();
        this.totalCount = totalCount;
        this.pagerType = pagerType;
    }

    /**
     * 获得 分页的 基础 url.
     * 
     * @return the pageUrl
     */
    public String getPageUrl(){
        return pageUrl;
    }

    /**
     * 设置 分页的 基础 url.
     * 
     * @param pageUrl
     *            the pageUrl to set
     */
    public void setPageUrl(String pageUrl){
        this.pageUrl = pageUrl;
    }

    /**
     * 获得 总数据条数.
     * 
     * @return the totalCount
     */
    public Integer getTotalCount(){
        return totalCount;
    }

    /**
     * 设置 总数据条数.
     * 
     * @param totalCount
     *            the totalCount to set
     */
    public void setTotalCount(Integer totalCount){
        this.totalCount = totalCount;
    }

    /**
     * 获得 当前第几页.
     * 
     * @return the currentPageNo
     */
    public Integer getCurrentPageNo(){
        return currentPageNo;
    }

    /**
     * 设置 当前第几页.
     * 
     * @param currentPageNo
     *            the currentPageNo to set
     */
    public void setCurrentPageNo(Integer currentPageNo){
        this.currentPageNo = currentPageNo;
    }

    /**
     * 获得 每页显示多少条.
     * 
     * @return the pageSize
     */
    public Integer getPageSize(){
        return pageSize;
    }

    /**
     * 设置 每页显示多少条.
     * 
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }

    /**
     * 获得 皮肤:可选.
     * 
     * @return the skin
     */
    public String getSkin(){
        return skin;
    }

    /**
     * 设置 皮肤:可选.
     * 
     * @param skin
     *            the skin to set
     */
    public void setSkin(String skin){
        this.skin = skin;
    }

    /**
     * 获得 分页参数名称.
     * 
     * @return the pageParamName
     */
    public String getPageParamName(){
        return pageParamName;
    }

    /**
     * 设置 分页参数名称.
     * 
     * @param pageParamName
     *            the pageParamName to set
     */
    public void setPageParamName(String pageParamName){
        this.pageParamName = pageParamName;
    }

    /**
     * 获得 vm的路径.
     * 
     * @return the vmPath
     */
    public String getVmPath(){
        return vmPath;
    }

    /**
     * 设置 vm的路径.
     * 
     * @param vmPath
     *            the vmPath to set
     */
    public void setVmPath(String vmPath){
        this.vmPath = vmPath;
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
     * @return the maxShowPageNo
     */
    public Integer getMaxShowPageNo(){
        return maxShowPageNo;
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
     * 获得 编码集.
     * 
     * @return the charsetType
     */
    public String getCharsetType(){
        return charsetType;
    }

    /**
     * 设置 编码集.
     * 
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     */
    public void setCharsetType(String charsetType){
        this.charsetType = charsetType;
    }

    /**
     * 获得 获得此 Java 虚拟机实例的当前默认语言环境值. <br>
     * Java 虚拟机根据主机的环境在启动期间设置默认语言环境.如果没有明确地指定语言环境,则很多语言环境敏感的方法都使用该方法.可使用 setDefault 方法更改该值..
     * 
     * @return the locale
     */
    public Locale getLocale(){
        return locale;
    }

    /**
     * 设置 获得此 Java 虚拟机实例的当前默认语言环境值. <br>
     * Java 虚拟机根据主机的环境在启动期间设置默认语言环境.如果没有明确地指定语言环境,则很多语言环境敏感的方法都使用该方法.可使用 setDefault 方法更改该值..
     * 
     * @param locale
     *            the locale to set
     */
    public void setLocale(Locale locale){
        this.locale = locale;
    }

    /**
     * 获得 debug 模式.
     * 
     * @return the debugIsNotParseVM
     */
    public boolean getDebugIsNotParseVM(){
        return debugIsNotParseVM;
    }

    /**
     * 设置 debug 模式.
     * 
     * @param debugIsNotParseVM
     *            the debugIsNotParseVM to set
     */
    public void setDebugIsNotParseVM(boolean debugIsNotParseVM){
        this.debugIsNotParseVM = debugIsNotParseVM;
    }

    /**
     * 设置 the pager type.
     *
     * @param pagerType
     *            the pagerType to set
     */
    public void setPagerType(PagerType pagerType){
        this.pagerType = pagerType;
    }

    /**
     * 获得 the pager type.
     *
     * @return the pagerType
     */
    public PagerType getPagerType(){
        return pagerType;
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
     * @return the 动态显示导航页码数量(默认是 {@link PagerConstants#DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG})
     * @since 1.9.2
     */
    public String getDynamicNavigationPageNumberConfig(){
        return dynamicNavigationPageNumberConfig;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
        //返回该对象的哈希码值  支持此方法是为了提高哈希表(例如 java.util.Hashtable 提供的哈希表)的性能. 

        //当我们向一个集合中添加某个元素,集合会首先调用hashCode方法,这样就可以直接定位它所存储的位置,
        //若该处没有其他元素,则直接保存.

        //若该处已经有元素存在,
        //就调用equals方法来匹配这两个元素是否相同,相同则不存,
        //不同则散列到其他位置(具体情况请参考(Java提高篇()-----HashMap)).

        //这样处理,当我们存入大量元素时就可以大大减少调用equals()方法的次数,极大地提高了效率.

        //hashCode在上面扮演的角色为寻域(寻找某个对象在集合中区域位置)

        //---------------------------------------------------------------
        //可替代地,存在使用反射来确定测试中的字段的方法.
        //因为这些字段通常是私有的,该方法中,reflectionHashCode,使用AccessibleObject.setAccessible改变字段的可见性.
        //这点会在一个安全管理器失败,除非相应的权限设置是否正确.
        //它也比明确地测试速度较慢. 
        //HashCodeBuilder.reflectionHashCode(this)
        //---------------------------------------------------------------
        //你选择一个硬编码,随机选择,不为零,奇数 
        //理想情况下 每个类不同
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(3, 5);
        return hashCodeBuilder//
                        .append(debugIsNotParseVM)//
                        .append(charsetType) //
                        .append(locale)//
                        .append(pageParamName)//
                        .append(pageUrl)//
                        .append(skin)//
                        .append(vmPath)//

                        .append(debugIsNotParseVM)//

                        .append(currentPageNo)//
                        .append(dynamicNavigationPageNumberConfig)//
                        .append(maxShowPageNo)//
                        .append(pageSize)//
                        .append(totalCount)//
                        .append(pagerType)//
                        .toHashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }
        if (obj.getClass() != getClass()){
            return false;
        }

        //存在使用反射来确定测试中的字段的方法.因为这些字段通常是私有的,该方法中,reflectionEquals,使用AccessibleObject.setAccessible改变字段的可见性.
        //这点会在一个安全管理器失败,除非相应的权限设置是否正确.
        //它也比明确地测试速度较慢. 
        //EqualsBuilder.reflectionEquals(this, obj)

        PagerParams pagerParams = (PagerParams) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();

        return equalsBuilder //
                        //.appendSuper(super.equals(obj))//
                        .append(this.charsetType, pagerParams.getCharsetType())//
                        .append(this.locale, pagerParams.getLocale())//
                        .append(this.pageParamName, pagerParams.getPageParamName())//
                        .append(this.pageUrl, pagerParams.getPageUrl())//
                        .append(this.skin, pagerParams.getSkin())//
                        .append(this.vmPath, pagerParams.getVmPath())//

                        .append(this.debugIsNotParseVM, pagerParams.getDebugIsNotParseVM())//
                        .append(this.currentPageNo, pagerParams.getCurrentPageNo())//
                        .append(this.dynamicNavigationPageNumberConfig, pagerParams.getDynamicNavigationPageNumberConfig())//
                        .append(this.maxShowPageNo, pagerParams.getMaxShowPageNo())//
                        .append(this.pageSize, pagerParams.getPageSize())//
                        .append(this.totalCount, pagerParams.getTotalCount())//
                        .append(this.pagerType, pagerParams.getPagerType())//
                        .isEquals();
    }

}