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
package com.feilong.security.symmetric;

/**
 * 分组模式(The following names can be specified as the mode component in a transformation when requesting an instance of Cipher.)
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0.7 2014年7月8日 下午4:45:24
 * @since 1.0.7
 */
public enum CipherMode{

    /**
     * No mode.
     */
    NONE,

    /**
     * (electronic codebook)
     * 
     * RC4 (ARCFOUR) does not support ECB (electronic codebook)
     * 
     * <pre class="code">
     * {@code
     *  优点:
     *  1.简单;
     *  2.有利于并行计算;
     *  3.误差不会被传送;
     *  缺点:
     *  1.不能隐藏明文的模式;
     *  2.可能对明文进行主动攻击.
     * 
     * }
     * </pre>
     */
    ECB,

    /**
     * (cipher block chaining) 密码分组链接模式
     * RC4 (ARCFOUR) does not support CBC (cipher block chaining).
     * 
     * <pre class="code">
     * {@code
     *  优点:
     *  1.不容易主动攻击,安全性好于ECB,适合传输长度长的报文,是SSL、IPSec的标准.
     *  缺点:
     *  1、不利于并行计算;
     *  2、误差传递;
     *  3、需要初始化向量IV.
     * 
     * }
     * </pre>
     */
    CBC,

    /**
     * (cipher feedback) 密码发反馈模式
     * RC4 (ARCFOUR) does not support CFB (cipher feedback).
     * 
     * <pre class="code">
     * {@code
     *  优点:
     *  1、隐藏了明文模式;
     *  2、分组密码转化为流模式;
     *  3、可以及时加密传送小于分组的数据.
     *  缺点:
     *  1、不利于并行计算;
     *  2、误差传送:一个明文单元损坏影响多个单元;
     *  3、唯一的IV.
     * 
     * }
     * </pre>
     */
    CFB,

    /**
     * (output feedback, in 8bit) 输出反馈模式
     * RC4 (ARCFOUR) does not support OFB (output feedback, in 8bit).
     * 
     * <pre class="code">
     * {@code
     *  优点:
     *  1、隐藏了明文模式;
     *  2、分组密码转化为流模式;
     *  3、可以及时加密传送小于分组的数据.
     *  缺点:
     *  1、不利于并行计算;
     *  2、对明文的主动攻击是可能的;
     *  3、误差传送:一个明文单元损坏影响多个单元.
     * 
     * }
     * </pre>
     */
    OFB,

    /**
     * (output feedback, in nbit)
     * 
     * AES does not support NOFB mode.
     * RC4 (ARCFOUR) does not support NOFB (output feedback, in nbit)
     */
    NOFB

}
