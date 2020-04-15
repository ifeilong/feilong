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
package com.feilong.taglib.display.option;

import javax.servlet.http.HttpServletRequest;

import com.feilong.taglib.AbstractLocaleSupportTag;
import com.feilong.taglib.display.LocaleSupportUtil;
import com.feilong.taglib.display.SimpleTagParamCacheManager;

/**
 * 用来基于 i18n配置文件,渲染select option选项,实现国际化功能,简化开发.
 * 
 * <p>
 * 你可以访问 wiki 查看更多 <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-option">feilongDisplay-option</a>
 * </p>
 * 
 * <h3>示例:</h3>
 * <blockquote>
 * 
 * 假设有 <b>i18n/education_zh_CN.properties</b> 内容如下:
 * 
 * <pre class="code">
 * edu.option1=初中
 * edu.option2=高中
 * edu.option3=中专
 * edu.option4=大专
 * edu.option5=本科
 * edu.option6=硕士
 * edu.option7=博士
 * edu.option8=其他
 * </pre>
 * 
 * 我们现在需要在jsp中渲染成 <b>select option</b> 项,我们可以使用下面的方式:
 * 
 * <pre class="code">
{@code <%@ taglib prefix="feilongDisplay" uri="http://java.feilong.com/tags-display"%>}
{@code
<select name="education">
    <feilongDisplay:option baseName="i18n/education" />
</select>
}
 * </pre>
 * 
 * 此时页面渲染结果为:
 * 
 * <pre class="code">
 * {@code 
        <select name="education">
            <option value="edu.option1">初中</option>
            <option value="edu.option2">高中</option>
            <option value="edu.option3">中专</option>
            <option value="edu.option4">大专</option>
            <option value="edu.option5">本科</option>
            <option value="edu.option6">硕士</option>
            <option value="edu.option7">博士</option>
            <option value="edu.option8">其他</option>
        </select>
   }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>关于 selectedKey:</h3>
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * 如果要做有值默认选中,那么可以设置  <b>selectedKey</b> 参数
 * {@code
    <select name="education2">
        <feilongDisplay:option baseName="i18n/education" selectedKey="edu.option6" />
    </select>
}
 * 此时页面渲染结果为
 * {@code 
    <select name="education2">
        <option value="edu.option1">初中</option>
        <option value="edu.option2">高中</option>
        <option value="edu.option3">中专</option>
        <option value="edu.option4">大专</option>
        <option value="edu.option5">本科</option>
        <option value="edu.option6" selected="selected">硕士</option>
        <option value="edu.option7">博士</option>
        <option value="edu.option8">其他</option>
    </select>
   }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>关于 locale:</h3>
 * <blockquote>
 * <p>
 * 如果此时我们有英文语言站点,他的配置文件是<b>i18n/education_en.properties</b>,内容如下:
 * 
 * <pre class="code">
 * 
 * edu.option1=Middle School
 * edu.option2=Junior College
 * edu.option3=Bachelor
 * edu.option4=Others(Master/PHD)
 * </pre>
 * 
 * </p>
 * 
 * 可以传递 locale参数,如下:
 * 
 * <pre class="code">
    {@code <%}
        <span style="color:red">//此处是演示代码,实际开发过程中,不允许使用 {@code  <% %>} 里面写jsp scriptlet</span>
        request.setAttribute("enLocale", Locale.ENGLISH);
    {@code %>}
    
    {@code <select name="education3">}
        {@code <}feilongDisplay:option baseName="i18n/education" selectedKey="edu.option2" locale="${enLocale}"{@code />}
    {@code </select>}
 * </pre>
 * 
 * <b>返回:</b>
 * 
 * <pre class="code">
{@code 
    <select name="education3">
        <option value="edu.option1">Middle School</option>
        <option value="edu.option2" selected="selected">Junior College</option>
        <option value="edu.option3">Bachelor</option>
        <option value="edu.option4">Others(Master/PHD)</option>
    </select>
}
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-option">feilongDisplay-option</a>
 * @since 1.5.4
 */
public class OptionTag extends AbstractLocaleSupportTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4523036188885941366L;

    /** 选中的key,可以不传,那么没有option会是选中状态,如果设置了,那么对应的key option的状态是 选中. */
    private String            selectedKey;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.AbstractWriteContentTag#writeContent()
     */
    @Override
    protected Object buildContent(HttpServletRequest request){
        OptionParam optionParam = new OptionParam();
        optionParam.setBaseName(baseName);
        optionParam.setLocale(LocaleSupportUtil.toLocal(locale, request));
        optionParam.setSelectedKey(selectedKey);

        return SimpleTagParamCacheManager.getContent(optionParam, OptionCacheContentBuilder.INSTANCE);
    }

    //---------------------------------------------------------------

    /**
     * 设置 选中的key,可以不传,那么没有option会是选中状态,如果设置了,那么对应的key option的状态是 选中.
     *
     * @param selectedKey
     *            the selectedKey to set
     */
    public void setSelectedKey(String selectedKey){
        this.selectedKey = selectedKey;
    }
}