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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.StringUtils;

/**
 * Domain 值格式化.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class DomainFormatter{

    /** Don't let anyone instantiate this class. */
    private DomainFormatter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 格式化 domain 成 http://www.feilong.com/ 形式.
     *
     * @param domain
     *            the domain
     * @return 如果 <code>domain</code> 是null或者empty,返回 {@link StringUtils#EMPTY}<br>
     *         如果 <code>domain</code> 不是以 单斜杆 / 结尾,拼接一个 单斜杆 / <br>
     *         如果 <code>domain</code> 是以 双斜杠 // 结尾,只保留一个 / <br>
     *         其他原样返回<br>
     */
    public static String format(String domain){
        if (isNullOrEmpty(domain)){
            return EMPTY;
        }

        //---------------------------------------------------------------
        //如果域名不是以 单斜杆 结尾, 那么拼接一个单斜杆
        if (!domain.endsWith("/")){
            return domain + "/";
        }

        //---------------------------------------------------------------
        //since 1.10.5
        //<script type="text/javascript" src="http://www.feilong.com/??http://img.adidas.com.cn/scripts/pdp/common.js,scripts/pdp/sub_salesProperties.js,scripts/pdp/pdp.js?2017"></script>

        //如果域名是以双斜杆结尾, 那么转成单斜杆
        if (domain.endsWith("//")){
            return StringUtils.removeEnd(domain, "//") + "/";
        }

        return domain;
    }
}
