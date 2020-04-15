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
package com.feilong.taglib.display.loadbundle;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.feilong.taglib.AbstractLocaleSupportTag;
import com.feilong.taglib.display.LocaleSupportUtil;
import com.feilong.taglib.display.SimpleTagParamCacheManager;

/**
 * 将i18n配置文件,转成map,加载到request 作用域, 实现国际化功能,简化开发.
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * <p>
 * 适用于 需要做国际化的 一组单选框或者复选框,我们以教育水平 (大学,高中,小学等)为例:
 * </p>
 * 
 * <pre>
{@code
    <feilongDisplay:loadBundle var="educationMap" baseName="i18n/education" />

    <c:forEach var="educationEntry" items="}${educationMap }{@code">
        <feilong:isContains value="}${educationEntry.key }{@code" collection="}${paramValues['educationCheckbox1'] }{@code">
            <input name="educationCheckbox1" type="checkbox" value="}${educationEntry.key }{@code" checked="checked" />
        </feilong:isContains>
        <feilong:isNotContains value="}${educationEntry.key }" collection="${paramValues['educationCheckbox1'] }"{@code>
            <input name="educationCheckbox1" type="checkbox" value="}${educationEntry.key }{@code" />
        </feilong:isNotContains>
        }${educationEntry.value }&nbsp;{@code
        
    </c:forEach>
}
 * </pre>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.taglibs.standard.tag.rt.fmt.BundleTag
 * @see org.apache.taglibs.standard.tag.rt.fmt.SetBundleTag
 * @since 1.10.3
 */
public class LoadBundleTag extends AbstractLocaleSupportTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4523036188885941366L;

    /** 存储在作用于里面的属性名称. */
    private String            var;

    //----------------------------------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.AbstractWriteContentTag#writeContent()
     */
    @Override
    protected Object buildContent(HttpServletRequest request){
        LoadBundleParam loadBundleParam = new LoadBundleParam();
        loadBundleParam.setBaseName(baseName);
        loadBundleParam.setLocale(LocaleSupportUtil.toLocal(locale, request));

        //----------------------------------------------------------------------------------------
        Map<String, String> map = SimpleTagParamCacheManager.getContent(loadBundleParam, LoadBundleBuilder.INSTANCE);

        request.setAttribute(var, map);

        return null;
    }

    //----------------------------------------------------------------------------------------

    /**
     * 设置 存储在作用于里面的属性名称.
     *
     * @param var
     *            the var to set
     */
    public void setVar(String var){
        this.var = var;
    }
}