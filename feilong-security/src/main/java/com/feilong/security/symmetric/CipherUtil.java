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

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.feilong.security.EncryptionException;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * {@link Cipher} 类为加密和解密提供密码功能.它构成了 Java Cryptographic Extension (JCE) 框架的核心..
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.5.0 2015年10月20日 下午6:09:51
 * @since 1.5.0
 */
public class CipherUtil{

    /** Don't let anyone instantiate this class. */
    private CipherUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 加密.
     *
     * @param bytes
     *            the bytes
     * @param transformation
     *            <p>
     *            转换transformation始终包括加密算法的名称(例如,DES),后面可能跟有一个反馈模式和填充方案.
     *            </p>
     * 
     *            <p>
     *            使用 CFB 和 OFB 之类的模式,Cipher 块可以加密单元中小于该 Cipher 的实际块大小的数据.<br>
     *            请求这样一个模式时,可以指定一次处理的位数(可选):将此数添加到模式名称中,正如 "DES/CFB8/NoPadding" 和 "DES/OFB32/PKCS5Padding" 转换所示.
     *            </p>
     * 
     *            <p>
     *            如果未指定该数,则将使用特定于提供者的默认值.(例如,SunJCE 提供者对 DES 使用默认的 64 位).
     *            </p>
     *            <p>
     *            因此,通过使用如 CFB8 或 OFB8 的 8 位模式,Cipher 块可以被转换为面向字节的 Cipher 流.
     *            </p>
     * @param key
     *            the key
     * @return the byte[]
     * @since 1.11.0
     */
    public static byte[] encrypt(byte[] bytes,String transformation,Key key){
        return opBytes(bytes, Cipher.ENCRYPT_MODE, transformation, key);
    }

    /**
     * 解密.
     *
     * @param bytes
     *            the bytes
     * @param transformation
     *            <p>
     *            转换transformation始终包括加密算法的名称(例如,DES),后面可能跟有一个反馈模式和填充方案.
     *            </p>
     * 
     *            <p>
     *            使用 CFB 和 OFB 之类的模式,Cipher 块可以加密单元中小于该 Cipher 的实际块大小的数据.<br>
     *            请求这样一个模式时,可以指定一次处理的位数(可选):将此数添加到模式名称中,正如 "DES/CFB8/NoPadding" 和 "DES/OFB32/PKCS5Padding" 转换所示.
     *            </p>
     * 
     *            <p>
     *            如果未指定该数,则将使用特定于提供者的默认值.(例如,SunJCE 提供者对 DES 使用默认的 64 位).
     *            </p>
     *            <p>
     *            因此,通过使用如 CFB8 或 OFB8 的 8 位模式,Cipher 块可以被转换为面向字节的 Cipher 流.
     *            </p>
     * @param key
     *            the key
     * @return the byte[]
     * @since 1.11.0
     */
    public static byte[] decrypt(byte[] bytes,String transformation,Key key){
        return opBytes(bytes, Cipher.DECRYPT_MODE, transformation, key);
    }

    //---------------------------------------------------------------

    /**
     * 操作字节数组.
     *
     * @param bytes
     *            the bytes
     * @param opmode
     *            模式,{@link Cipher#ENCRYPT_MODE} or {@link Cipher#DECRYPT_MODE}
     * @param transformation
     *            <p>
     *            转换transformation始终包括加密算法的名称(例如,DES),后面可能跟有一个反馈模式和填充方案.
     *            </p>
     * 
     *            <p>
     *            使用 CFB 和 OFB 之类的模式,Cipher 块可以加密单元中小于该 Cipher 的实际块大小的数据.<br>
     *            请求这样一个模式时,可以指定一次处理的位数(可选):将此数添加到模式名称中,正如 "DES/CFB8/NoPadding" 和 "DES/OFB32/PKCS5Padding" 转换所示.
     *            </p>
     * 
     *            <p>
     *            如果未指定该数,则将使用特定于提供者的默认值.(例如,SunJCE 提供者对 DES 使用默认的 64 位).
     *            </p>
     *            <p>
     *            因此,通过使用如 CFB8 或 OFB8 的 8 位模式,Cipher 块可以被转换为面向字节的 Cipher 流.
     *            </p>
     * @param key
     *            the key
     * @return the new buffer with the result
     * @see Cipher
     * @see Cipher#getInstance(String)
     * @see Cipher#init(int, Key)
     * @see Cipher#doFinal(byte[])
     */
    private static byte[] opBytes(byte[] bytes,int opmode,String transformation,Key key){
        try{
            Cipher cipher = Cipher.getInstance(transformation);

            // 结束时,此方法将此 Cipher 对象重置为上一次调用 init 初始化得到的状态.
            // 即该对象被重置,并可用于加密或解密(具体取决于调用 init 时指定的操作模式)更多的数据.
            cipher.init(opmode, key);

            return cipher.doFinal(bytes);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException
                        | InvalidKeyException e){
            throw new EncryptionException(Slf4jUtil.format("opmode:[{}],transformation:[{}]", opmode, transformation), e);
        }
    }
}
