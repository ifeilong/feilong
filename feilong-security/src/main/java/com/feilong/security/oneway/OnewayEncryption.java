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
package com.feilong.security.oneway;

import java.io.InputStream;
import java.security.MessageDigest;

import com.feilong.core.CharsetType;
import com.feilong.core.Validate;
import com.feilong.core.lang.StringUtil;
import com.feilong.io.InputStreamUtil;
import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.ByteUtil;
import com.feilong.security.EncryptionException;

/**
 * 单向加密算法.
 * 
 * <h3>提供以下核心方法:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #encode(OnewayType, String)}</li>
 * <li>{@link #encode(OnewayType, String, String)}</li>
 * <li>{@link #encode(OnewayType, byte[])}</li>
 * <li>{@link #encodeFile(OnewayType, String)}</li>
 * </ul>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.0 2012-3-25 上午7:19:18
 * @version 1.0.7 2014-7-10 14:30 update class type is final
 * @see OnewayType
 * @see com.feilong.lib.codec.digest.DigestUtils
 * @see org.springframework.util.DigestUtils
 */
//无访问控制符修饰的内容可以被同一个包中的类访问,
final class OnewayEncryption{

    /** Don't let anyone instantiate this class. */
    private OnewayEncryption(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用算法 单向加密字符串.
     *
     * @param onewayType
     *            单向加密
     * @param origin
     *            原始字符串,将使用默认的 {@link String#getBytes()} 转成字节数组<br>
     *            如果需要string 转码,请自行调用value.getBytes(string chartsetname),再调用{@link #encode(OnewayType, byte[])}
     * @return 加密之后的转成小写的16进制字符串
     * @see StringUtil#getBytes(String)
     * @see #encode(OnewayType, byte[])
     */
    public static String encode(OnewayType onewayType,String origin){
        byte[] inputBytes = StringUtil.getBytes(origin);
        return encode(onewayType, inputBytes);
    }

    /**
     * 使用算法 单向加密字符串.
     *
     * @param onewayType
     *            单向加密
     * @param origin
     *            原始字符串,将使用默认的 value.getBytes() 转成字节数组<br>
     *            如果需要string 转码,请自行调用value.getBytes(string chartsetname),再调用{@link #encode(OnewayType, byte[])}
     * @param charsetName
     *            受支持的 {@link CharsetType} 名称,比如 utf-8
     * @return 加密之后的转成小写的16进制字符串
     * @see StringUtil#getBytes(String, String)
     * @see #encode(OnewayType, byte[])
     */
    public static String encode(OnewayType onewayType,String origin,String charsetName){
        byte[] inputBytes = StringUtil.getBytes(origin, charsetName);
        return encode(onewayType, inputBytes);
    }

    //---------------------------------------------------------------

    /**
     * md5加密,byte[] 便于自定义编码,返回的值是小写的16进制字符串
     * 
     * <pre class="code">
     * 
     * <span style="color:green">// 签名(utf-8编码)</span>
     * byte[] bytes = StringUtil.toBytes(sb.toString(), UTF8);
     * return MD5Util.encode(bytes).toUpperCase();
     * </pre>
     *
     * @param onewayType
     *            the oneway type
     * @param inputBytes
     *            the input bytes
     * @return 加密之后的转成小写的16进制字符串
     * @see #getMessageDigest(OnewayType)
     * @see java.security.MessageDigest#digest(byte[])
     * @see ByteUtil#bytesToHexStringLowerCase(byte[])
     */
    public static String encode(OnewayType onewayType,byte[] inputBytes){
        MessageDigest messageDigest = getMessageDigest(onewayType);
        //对于给定数量的更新数据,digest 方法只能被调用一次.
        //在调用 digest 之后,MessageDigest 对象被重新设置成其初始状态.

        // 使用指定的 byte 数组对摘要进行最后更新,然后完成摘要计算.
        // 也就是说,此方法首先调用 update(input),向 update 方法传递 input 数组,然后调用 digest().

        // 和自己先写 update(input),再调用 digest()效果一样的
        byte[] bs = messageDigest.digest(inputBytes);
        return ByteUtil.bytesToHexStringLowerCase(bs);
    }

    //---------------------------------------------------------------

    /**
     * 计算文件的加密值.
     *
     * @param onewayType
     *            the oneway type
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @return 如果 <code>onewayType</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see java.io.File#File(String)
     * @see #getMessageDigest(OnewayType)
     * @see java.io.FileInputStream#read(byte[], int, int)
     * @see java.security.MessageDigest#update(byte[], int, int)
     * @see java.security.MessageDigest#digest()
     * @see com.feilong.lib.codec.digest.DigestUtils#updateDigest(MessageDigest, InputStream)
     */
    public static String encodeFile(OnewayType onewayType,String location){
        Validate.notNull(onewayType, "onewayType can't be null!");
        Validate.notBlank(location, "location can't be null/empty!");

        try (InputStream inputStream = InputStreamUtil.getInputStream(location)){

            MessageDigest messageDigest = getMessageDigest(onewayType);
            byte[] bytes = DigestUtils.updateDigest(messageDigest, inputStream).digest();
            return ByteUtil.bytesToHexStringLowerCase(bytes);//这个值和上面的一样

        }catch (Exception e){
            throw new EncryptionException(StringUtil.formatPattern("onewayType:[{}],filePath:[{}]", onewayType, location), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 使用 onewayType 创建 {@link MessageDigest} 对象.
     *
     * @param onewayType
     *            the oneway type
     * @return {@link java.security.MessageDigest#getInstance(String)}
     * @see java.security.MessageDigest#getInstance(String)
     * @see com.feilong.lib.codec.digest.DigestUtils#getDigest(String)
     */
    private static final MessageDigest getMessageDigest(OnewayType onewayType){
        Validate.notNull(onewayType, "onewayType can't be null!");
        return DigestUtils.getDigest(onewayType.getAlgorithm());
    }
}