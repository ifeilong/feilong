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
package com.feilong.json.jsonlib.transformer;

import net.sf.json.util.JavaIdentifierTransformer;

/**
 * 当json转成bean的时候,json字符串里面的属性名字可能有下划线,转成bean里面属性去掉下划线,并且下划线后面字母大写的转换器.
 * 
 * <p>
 * 比如 member_Id,但是我们的java bean里面的属性名字是标准的 驼峰命名法则,比如 memberId
 * </p>
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * <b>场景:</b> 从相关接口得到的json数据格式如下(注意:属性首字母是大写的):
 * </p>
 * 
 * <pre class="code">
 * {
 * "member_No":"11105000009"
 * }
 * </pre>
 * 
 * <p>
 * 但是我们的类是标准的java bean,属性符合驼峰命名规则,比如:
 * </p>
 * 
 * <pre class="code">
 * public class CrmMemberInfoCommand{
 * 
 *     <span style="color:green">//** 会员编号</span>
 *     private String memberNo;
 * 
 *     <span style="color:green">// setter getter</span>
 * }
 * </pre>
 * 
 * <p>
 * 此时可以使用该类,示例如下:
 * </p>
 * 
 * <pre class="code">
 * 
 * public void testToBean2(){
 *     String json = "{'member_no':'11105000009'}";
 * 
 *     CrmMemberInfoCommand crmMemberInfoCommand = JsonUtil
 *                     .toBean(new JsonToJavaConfig(CrmMemberInfoCommand.class, SeparatorToCamelCaseJavaIdentifierTransformer.INSTANCE));
 *     //.....
 * }
 * 
 * </pre>
 * 
 * 也支持以下的形式
 * 
 * <pre>
 * 
 * public void testToBean3(){
 *     String json = "{'member@no':'11105000009'}";
 * 
 *     CrmMemberInfoCommand crmMemberInfoCommand = JsonUtil.toBean(
 *                     json,
 *                     new JsonToJavaConfig(CrmMemberInfoCommand.class, new SeparatorToCamelCaseJavaIdentifierTransformer('@')));
 * }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-json/issues/11">issue 11</a>
 * @since 1.11.2
 */
public class SeparatorToCamelCaseJavaIdentifierTransformer extends JavaIdentifierTransformer{

    /** 下划线 transformer 'member_no' {@code =>} 'memberNo'. */
    public static final JavaIdentifierTransformer INSTANCE  = new SeparatorToCamelCaseJavaIdentifierTransformer();

    //---------------------------------------------------------------

    /** The separator. */
    private char                                  separator = '_';

    //---------------------------------------------------------------

    /**
     * Instantiates a new underscore to camel case java identifier transformer.
     */
    public SeparatorToCamelCaseJavaIdentifierTransformer(){
        super();
    }

    /**
     * Instantiates a new underscore to camel case java identifier transformer.
     *
     * @param separator
     *            the separator
     */
    public SeparatorToCamelCaseJavaIdentifierTransformer(char separator){
        super();
        this.separator = separator;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.util.JavaIdentifierTransformer#transformToJavaIdentifier(java.lang.String)
     */
    @Override
    public String transformToJavaIdentifier(String s){
        if (s == null){
            return null;
        }

        //---------------------------------------------------------------
        s = s.toLowerCase();

        //TODO 找找工具类
        StringBuilder sb = new StringBuilder(s.length());

        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);

            if (c == separator){
                upperCase = true;
            }else if (upperCase){
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * Sets the separator.
     *
     * @param separator
     *            the separator to set
     */
    public void setSeparator(char separator){
        this.separator = separator;
    }

}
