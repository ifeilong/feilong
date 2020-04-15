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

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_PAGE_ATTRIBUTE_PAGER_HTML_NAME;

import java.util.Map;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.servlet.jsp.tagext.VariableInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.taglib.BaseTEI;

/**
 * The Class PagerTagExtraInfo.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.3
 */
public class PagerTagExtraInfo extends BaseTEI{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PagerTagExtraInfo.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
     */
    @Override
    public VariableInfo[] getVariableInfo(TagData tagData){
        //如果不设置 使用 ${feilongPagerHtml1 } 是正常的
        //但是如果使用 <%=feilongPagerHtml1%> 会提示 feilongPagerHtml1 cannot be resolved to a variable

        String pagerHtmlAttributeName = defaultIfNullOrEmpty(
                        tagData.getAttributeString("pagerHtmlAttributeName"),
                        DEFAULT_PAGE_ATTRIBUTE_PAGER_HTML_NAME);

        VariableInfo variableInfo = new VariableInfo(pagerHtmlAttributeName, String.class.getName(), true, VariableInfo.AT_END);
        return ConvertUtil.toArray(variableInfo);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagExtraInfo#validate(javax.servlet.jsp.tagext.TagData)
     */
    @Override
    // JSP 2.0 and higher containers call validate() instead of isValid().
    // The default implementation of this method is to call isValid().

    // If isValid() returns false, a generic ValidationMessage[] is returned indicating isValid() returned false.
    public ValidationMessage[] validate(TagData tagData){
        if (LOGGER.isDebugEnabled()){
            Map<String, Object> map = getTagDataAttributeMap(tagData);
            LOGGER.debug(JsonUtil.format(map));
        }
        return super.validate(tagData);
    }

}
