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
package com.feilong.taglib;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * The Class BaseTagExtraInfo.
 * 
 * <h3>{@link TagExtraInfo} can be used to:</h3>
 * <blockquote>
 * <ol>
 * <li>Define Scripting Variables</li>
 * <li>Validation</li>
 * </ol>
 * </blockquote>
 * 
 * 
 * <h3>{@link VariableInfo}:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * 
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link VariableInfo#AT_BEGIN AT_BEGIN}</td>
 * <td>Scope information that scripting variable is visible after start tag.</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link VariableInfo#AT_END AT_END}</td>
 * <td>Scope information that scripting variable is visible after end tag.</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link VariableInfo#NESTED NESTED}</td>
 * <td>Scope information that scripting variable is visible only within the start/end tags</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.servlet.jsp.tagext.TagExtraInfo
 * @see org.apache.taglibs.standard.tei.DeclareTEI
 * @see org.apache.taglibs.standard.tei.ForEachTEI
 * @see org.apache.taglibs.standard.tei.ImportTEI
 * @see org.apache.taglibs.standard.tei.XmlParseTEI
 * @see org.apache.taglibs.standard.tei.XmlTransformTEI
 * @since 1.0.3
 */
public abstract class BaseTEI extends TagExtraInfo{

    /**
     * 显示 {@link javax.servlet.jsp.tagext.TagData}里面的信息,一般用于 debug.
     *
     * @param tagData
     *            the tag data
     * @return the map< string, object>
     * @see javax.servlet.jsp.tagext.TagData#getAttributes()
     * @since 1.4.0
     */
    protected static Map<String, Object> getTagDataAttributeMap(TagData tagData){
        Map<String, Object> map = new TreeMap<>();

        Enumeration<String> attributes = tagData.getAttributes();
        while (attributes.hasMoreElements()){
            String key = attributes.nextElement();
            map.put(key, tagData.getAttribute(key));
        }
        return map;
    }
}
