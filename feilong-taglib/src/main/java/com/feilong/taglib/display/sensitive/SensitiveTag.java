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
package com.feilong.taglib.display.sensitive;

import static com.feilong.core.Validator.isNullOrEmpty;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;

import com.feilong.taglib.AbstractStartWriteContentTag;

/**
 * 敏感数据mask标签.
 * 
 * <h3><a href="http://www.owasp.org.cn/owasp-project/download/mobile-top-10-2013-2">OWASP Top 10 – 2013</a></h3>
 * <blockquote>
 * <ol>
 * <li>A1 － 注入</li>
 * <li>A2 －失效的身份认证和会话管理</li>
 * <li>A3 －跨站脚本(XSS)</li>
 * <li>A4 － 不安全的直接对象引用</li>
 * <li>A5 －安全配置错误</li>
 * <li>A6 －敏感信息泄漏</li>
 * <li>A7 － 功能级访问控制缺失</li>
 * <li>A8 －跨站请求伪造(CSRF)</li>
 * <li>A9 － 使用含有已知漏洞的组件</li>
 * <li>A10 － 未验证的重定向和转发</li>
 * </ol>
 * </blockquote>
 * 
 * <h3><a href="https://www.owasp.org/index.php/Top_10_2013-A6-Sensitive_Data_Exposure">A6 －敏感信息泄漏</a></h3>
 * 
 * <blockquote>
 * <p>
 * 许多Web应用程序没有正确保护敏感数据,如信用卡,税务ID和身份验证凭据。攻击者可能会窃取或篡改这些弱保护的数据以进行信用卡诈骗、身份窃取,或其他犯罪。敏感数据值需额外的保护,比如在存放或在传输过程中的加密,以及在与浏览器交换时进行特殊的预防措施。
 * </p>
 * 
 * <h4>业务影响:</h4>
 * 
 * <blockquote>
 * <p>
 * 考虑丢失数据和声誉影响造成的商业损失。如果这些数据被泄露,那你要承担的法律责任是什么？另外考虑到对企业造成的声誉影响。
 * </p>
 * </blockquote>
 * 
 * <h4>防范措施:</h4>
 * 
 * <blockquote>
 * <ol>
 * <li>保存到存储介质(database等)的时候,敏感数据需要加密处理,并且需要有严格的访问权限控制</li>
 * <li>不要轻易的在日志中输出敏感数据</li>
 * <li>浏览器交互数据的时候,需要特殊的处理(传到后端需要加密;现实在前端需要mask,也即使用当前类 {@link SensitiveTag})</li>
 * </ol>
 * </blockquote>
 * 
 * <p>
 * 当然,其他的敏感数据还有 <b>SSL证书</b>,<b>密钥</b>,<b>服务器信息</b>,<b>软件版本</b>,<b>数据库类型</b>等
 * </p>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://www.owasp.org/index.php/Top_10_2013-Top_10">Top_10_2013-Top_10</a>
 * @see <a href="http://www.owasp.org.cn/owasp-project/download/mobile-top-10-2013-2">OWASP Top 10 – 2013(新版)</a>
 * @see <a href="https://www.owasp.org/index.php/Top_10_2013-A6-Sensitive_Data_Exposure">A6-Sensitive_Data_Exposure</a>
 * @see SensitiveType
 * @since 1.10.1
 */
public class SensitiveTag extends AbstractStartWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6402308838024650287L;

    //---------------------------------------------------------------

    /** 待mask字符串. */
    private String            value;

    /** mask的字符,比如*,#等,默认是*. */
    private char              maskChar         = '*';

    /**
     * 类型,可以有 mobile,CHINESENAME,address,email (忽视大小写),具体参见 {@link SensitiveType}.
     * 
     * @see SensitiveType
     */
    private String            type;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.AbstractWriteContentTag#buildContent(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object buildContent(HttpServletRequest request){
        Validate.notBlank(type, "type can not null or blank");
        //---------------------------------------------------------------
        if (isNullOrEmpty(value)){
            return value;
        }
        return SensitiveUtil.parse(value, new SensitiveConfig(type, maskChar));
    }

    //---------------------------------------------------------------

    /**
     * 设置 待mask字符串.
     *
     * @param value
     *            the new 待mask字符串
     */
    public void setValue(String value){
        this.value = value;
    }

    /**
     * 设置 类型, 可以有 mobile , chinese name,address,email.
     *
     * @param type
     *            the new 类型, 可以有 mobile , chinese name,address,email
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * 设置 mask的字符,比如*,#等,默认是*.
     *
     * @param maskChar
     *            the maskChar to set
     */
    public void setMaskChar(char maskChar){
        this.maskChar = maskChar;
    }
}
