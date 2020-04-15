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
package com.feilong.context.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.core.net.URIUtil;

/**
 * 使用 URIUtil.decode(text, charsetType) 解码.
 * 
 * <p>
 * 使用方法:
 * </p>
 * 
 * <code>
 * <pre class="code">
 * &#064;InitBinder({ &quot;categoryCode&quot; })
 * <span style="color:green">// 此处的参数也可以是ServletRequestDataBinder类型</span>
 * public void initBinder(WebDataBinder binder) throws Exception{
 *     <span style="color:green">// 注册自定义的属性编辑器</span>
 *     binder.registerCustomEditor(String.class, new URLDecoderEditor(UTF8));
 * }
 * </pre></code>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.8.3 move from feilong-spring-core
 */
public class URLDecoderEditor extends PropertyEditorSupport{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(URLDecoderEditor.class);

    /** 编码. */
    private final String        charsetType;

    //---------------------------------------------------------------

    /**
     * Instantiates a new uRL decoder editor.
     * 
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     */
    public URLDecoderEditor(String charsetType){
        this.charsetType = charsetType;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text){
        String newText = URIUtil.decode(text, charsetType);
        LOGGER.debug("the old text:{},new text:{}", text, newText);
        super.setValue(newText);
    }
}
