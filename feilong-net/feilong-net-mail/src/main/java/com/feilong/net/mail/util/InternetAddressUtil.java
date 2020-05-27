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
package com.feilong.net.mail.util;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * The Class InternetAddressUtil.
 * 
 * <pre class="code">
 * name&lt;xxx@xxx.com.cn&gt;
 * 
 * $to: The e-mail address or addresses where the message will be sent to. 
 * The formatting of this string will be validated with the PHP e-mail validation filter. 
 * 
 * Some examples are:
 *  user@example.com
 *  user@example.com, anotheruser@example.com
 *  User &lt;user@example.com&gt;
 *  User &lt;user@example.com&gt;, Another User &lt;anotheruser@example.com&gt;
 * </pre>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.8
 */
public final class InternetAddressUtil{

    /** The Constant CHARSET_PERSONAL. */
    private static final String CHARSET_PERSONAL = UTF8;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private InternetAddressUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * To address array.
     *
     * @param addresseArray
     *            the addresse array
     * @return the address[]
     * @throws AddressException
     *             the address exception
     * @since 1.13.0
     */
    public static Address[] toAddressArray(String[] addresseArray) throws AddressException{
        final int length = addresseArray.length;
        Address[] addresses = new InternetAddress[length];
        for (int i = 0; i < length; ++i){
            addresses[i] = new InternetAddress(addresseArray[i]);
        }
        return addresses;
    }

    //---------------------------------------------------------------

    /**
     * 将nameAndEmail map转成 InternetAddress数组.
     *
     * @param nameAndEmailMap
     *            the name and email map
     * @param charset
     *            the charset
     * @return the internet address[]
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     * @see javax.mail.internet.MimeUtility#encodeWord(String, String, String)
     * @see javax.mail.internet.MimeUtility#getDefaultMIMECharset()
     */
    public static final InternetAddress[] toInternetAddressArray(Map<String, String> nameAndEmailMap,String charset)
                    throws UnsupportedEncodingException{
        Validate.notEmpty(nameAndEmailMap, "nameAndEmailMap can't be null/empty!");
        //---------------------------------------------------------------

        InternetAddress[] internetAddresses = new InternetAddress[nameAndEmailMap.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : nameAndEmailMap.entrySet()){
            String name = entry.getKey();
            String email = entry.getValue();

            internetAddresses[i] = new InternetAddress(email, name, charset);
            i++;
        }
        return internetAddresses;
    }

    //---------------------------------------------------------------

    /**
     * 将 internetAddresses 数组 转成 toUnicodeString方法 list.
     *
     * @param internetAddresses
     *            the internet addresses
     * @return the list
     */
    public static final List<String> toUnicodeStringList(InternetAddress[] internetAddresses){
        Validate.notEmpty(internetAddresses, "internetAddresses can't be null/empty!");

        //---------------------------------------------------------------
        List<String> list = newArrayList();
        for (InternetAddress internetAddress : internetAddresses){
            list.add(internetAddress.toUnicodeString());
        }
        return list;
    }

    //---------------------------------------------------------------

    /**
     * To from address.
     *
     * @param personal
     *            the personal
     * @param fromAddress
     *            the from address
     * @return 如果 <code>fromAddress</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>fromAddress</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.13.0
     */
    public static Address buildFromAddress(String personal,String fromAddress){
        Validate.notBlank(fromAddress, "fromAddress can't be blank!");

        //---------------------------------------------------------------

        try{
            // 设置邮件消息的发送者
            if (isNotNullOrEmpty(personal)){
                //the encoding to be used. Currently supported values are "B" and "Q". 
                //If this parameter is null, then the "Q" encoding is used if most of characters to be encoded are in the ASCII charset, 
                //otherwise "B" encoding is used.
                //B为base64方式
                String encoding = "b";
                String encodeText = MimeUtility.encodeText(personal, CHARSET_PERSONAL, encoding);
                return new InternetAddress(fromAddress, encodeText);
            }
            return new InternetAddress(fromAddress);
        }catch (AddressException | UnsupportedEncodingException e){
            //since 1.13.2
            throw new DefaultRuntimeException(Slf4jUtil.format("personal:[{}],fromAddress:[{}]", personal, fromAddress), e);
        }
    }

}
