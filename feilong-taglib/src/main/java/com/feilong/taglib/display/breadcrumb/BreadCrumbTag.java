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
package com.feilong.taglib.display.breadcrumb;

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.taglib.AbstractStartWriteContentTag;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbConstants;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbEntity;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbParams;

/**
 * 面包屑标签.
 * 
 * <p>
 * 默认通过解析 {@link BreadCrumbConstants#DEFAULT_TEMPLATE_IN_CLASSPATH}
 * </p>
 * 
 * <h3>扩展:</h3>
 * <blockquote>
 * <ol>
 * <li>{@link #constructBreadCrumbEntityList()}支持扩展</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>面包屑导航</h3>
 * 
 * <blockquote>
 * <p>
 * 面包屑导航(英语:Breadcrumb Trail)是在用户界面中的一种导航辅助。它是用户一个在程序或文件中确定和转移他们位置的一种方法。面包屑导航(BreadcrumbNavigation)这个概念来自童话故事"汉赛尔和格莱特",当汉赛尔和格莱特穿过森林时,不小心迷路了,
 * 但是他们发现在沿途走过的地方都撒下了面包屑,让这些面包屑来帮助他们找到回家的路。所以,面包屑导航的作用是告诉访问者他们目前在网站中的位置以及如何返回。
 * </p>
 * 
 * <p>
 * 面包屑导航通常在页面顶部水平出现,一般会位于标题或页头的下方。<br>
 * 它们提供给用户返回之前任何一个页面的链接(这些链接也是能到达当前页面的路径),在层级架构中通常是这个页面的父级页面。面包屑导航提供给用户回溯到网站首页或入口页面的一条路径,通常是以大于号(>)出现,
 * 尽管一些设计是其他的符号(如»)。
 * </p>
 * 
 * </blockquote>
 * 
 * <h3>面包屑导航的分类</h3>
 * 
 * <blockquote>
 * <p>
 * 一共有三种类型的网站面包屑导航:
 * </p>
 * 
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>位置型</td>
 * <td>这种类型是最常见的。<br>
 * 这种类型的面包屑导航可以很好的指出当前页面与整个站点的层次结构。这种面包屑导航可以显示当前页面的前一个页面或者目录的链接。<br>
 * 可以使访客了解自己的位置,以及可以更快的找到自己想要到达的页面。<br>
 * 可以很好的提高用户的友好体验。</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>属性型</td>
 * <td>这种面包屑导航最常出现在电子商务站点。<br>
 * 这种面包屑导航可以很好的指出当前页面内产品的其他属性或者类别。<br>
 * 对于一个产品来说,所具有的属性往往不只有一种,而通过这种面包屑导航可以给消费者一个更加直观的了解</td>
 * </tr>
 * <tr valign="top">
 * <td>路径型</td>
 * <td>这种面包屑导航是最不常见到的。<br>
 * 这种面包屑导航和上文所说的童话故事类型很像。他们可以显示访客在到达页面前所访问过的网页的链接。<br>
 * 这种面包屑导航不是很受欢迎,因为他们的功能基本上是和前进和后退的按钮是一样的。[1]</td>
 * </tr>
 * </table>
 * 
 * </blockquote>
 * 
 * 
 * <h3>作用:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>让用户了解当前所处位置,以及当前页面在整个网站中的位置。</li>
 * <li>体现了网站的架构层级,能够帮助用户快速学习和了解网站内容和组织方式,从而形成很好的位置感。</li>
 * <li>提供返回各个层级的快速入口,方便用户操作。</li>
 * <li>Google已经将面包屑导航整合到搜索结果里面,因此优化面包屑导航每个层级的名称,多使用关键字,都可以实现SEO优化。面包屑路径,对于提高用户体验来说,是很有帮助的。</li>
 * <li>方便用户,面包屑主要用于为用户提供导航一个网站的次要方法,通过为一个大型多级网站的所有页面提供面包屑路径,用户可以更容易的定位到上一次目录,引导用户通行;</li>
 * <li>减少返回到上一级页面的点击或操作,不用使用浏览器的“返回”按钮或网站的主要导航来返回到上一级页面;</li>
 * <li>不用常常占用屏幕空间,因为它们通常是水平排列以及简单的样式,面包屑路径不会占用页面太多的空间。这样的好处是,从内容过载方面来说,他们几乎没有任何负面影响;</li>
 * <li>降低跳出率,面包屑路径会是一个诱惑首次访问者在进入一个页面后去浏览这个网站的非常好的方法。比如说,一个用户通过谷歌搜索到一个页面,然后看到一个面包屑路径,这将会诱使用户点击上一级页面去浏览感兴趣的相关主题。这样,从而,可以降低网站的总体跳出率。</li>
 * <li>有利于百度蜘蛛对网站的抓取,蜘蛛直接沿着那个链走就可以了,很方便。</li>
 * <li>面包屑有利于网站内链的建设,用面包屑大大增加了网站的内部连接,提高用户体验。</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://zh.wikipedia.org/wiki/%E9%9D%A2%E5%8C%85%E5%B1%91%E5%AF%BC%E8%88%AA">面包屑导航</a>
 * @see <a href="http://baike.baidu.com/view/2122303.htm">面包屑导航</a>
 * @see <a href="http://www.webdesignpractices.com/navigation/breadcrumb.html">Breadcrumb Navigation</a>
 * @since 1.2.2
 */
public class BreadCrumbTag extends AbstractStartWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long              serialVersionUID = -8596553099620845748L;

    /** The Constant LOGGER. */
    private static final Logger            LOGGER           = LoggerFactory.getLogger(BreadCrumbTag.class);

    //---------------------------------------------------------------
    /** breadCrumbEntityList,用户所有可以访问的菜单url List,不要求已经排完序. */
    private List<BreadCrumbEntity<Object>> breadCrumbEntityList;

    /** url前缀, 用来拼接 {@link BreadCrumbEntity#getPath()},可以不设置,那么原样输出{@link BreadCrumbEntity#getPath()}. */
    private String                         urlPrefix        = EMPTY;

    /** 连接符,默认>. */
    private String                         connector        = BreadCrumbConstants.DEFAULT_CONNECTOR;

    /** vm的路径. */
    private String                         vmPath           = BreadCrumbConstants.DEFAULT_TEMPLATE_IN_CLASSPATH;

    //---------------------------------------------------------------

    /**
     * 实现自定义站点地图数据提供程序的途径.
     *
     * @param request
     *            the request
     * @return the object
     */
    @Override
    protected Object buildContent(HttpServletRequest request){
        List<BreadCrumbEntity<Object>> opBreadCrumbEntityList = constructBreadCrumbEntityList();

        if (isNullOrEmpty(opBreadCrumbEntityList)){
            LOGGER.warn("breadCrumbEntityList is NullOrEmpty!!,return empty!!");
            return EMPTY;
        }

        BreadCrumbParams breadCrumbParams = new BreadCrumbParams();
        breadCrumbParams.setBreadCrumbEntityList(opBreadCrumbEntityList);
        breadCrumbParams.setConnector(connector);
        breadCrumbParams.setVmPath(vmPath);
        breadCrumbParams.setUrlPrefix(urlPrefix);
        return BreadCrumbUtil.getBreadCrumbContent(breadCrumbParams);
    }

    //---------------------------------------------------------------
    /**
     * Construct bread crumb entity list.
     *
     * @return the list< bread crumb entity< object>>
     */
    protected List<BreadCrumbEntity<Object>> constructBreadCrumbEntityList(){
        return breadCrumbEntityList;
    }

    /**
     * 设置 连接符,默认>.
     *
     * @param connector
     *            the connector to set
     */
    public void setConnector(String connector){
        this.connector = connector;
    }

    /**
     * 设置 siteMapEntityList,用户所有可以访问的菜单url List.
     *
     * @param breadCrumbEntityList
     *            the breadCrumbEntityList to set
     */
    public void setBreadCrumbEntityList(List<BreadCrumbEntity<Object>> breadCrumbEntityList){
        this.breadCrumbEntityList = breadCrumbEntityList;
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
     * url前缀, 用来拼接 {@link BreadCrumbEntity#getPath()},可以不设置,那么原样输出{@link BreadCrumbEntity#getPath()}.
     *
     * @param urlPrefix
     *            the urlPrefix to set
     */
    public void setUrlPrefix(String urlPrefix){
        this.urlPrefix = urlPrefix;
    }

    /**
     * 获得 url前缀, 用来拼接 {@link BreadCrumbEntity#getPath()},可以不设置,那么原样输出{@link BreadCrumbEntity#getPath()}.
     *
     * @return the urlPrefix
     */
    public String getUrlPrefix(){
        return urlPrefix;
    }

    /**
     * 获得 连接符,默认>.
     *
     * @return the connector
     */
    public String getConnector(){
        return connector;
    }

    /**
     * 获得 vm的路径.
     *
     * @return the vmPath
     */
    public String getVmPath(){
        return vmPath;
    }
}
