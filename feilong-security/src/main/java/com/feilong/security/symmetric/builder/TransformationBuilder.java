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
package com.feilong.security.symmetric.builder;

import com.feilong.core.Validate;
import com.feilong.security.symmetric.CipherMode;
import com.feilong.security.symmetric.CipherPadding;

/**
 * The Class TransformationBuilder.
 * 转换的名称,例如 DES/CBC/PKCS5Padding.
 * <p>
 * 有关标准转换名称的信息,请参见 Java Cryptography Architecture Reference Guide 的附录 A.
 * </p>
 * 
 * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html">StandardNames</a>
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class TransformationBuilder{

    /** Don't let anyone instantiate this class. */
    private TransformationBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 转换的名称,例如 DES/CBC/PKCS5Padding.
     * <p>
     * 有关标准转换名称的信息,请参见 Java Cryptography Architecture Reference Guide 的附录 A.
     * </p>
     * 
     * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html">StandardNames</a>
     *
     * @param algorithm
     *            the algorithm
     * @param cipherMode
     *            the cipher mode
     * @param cipherPadding
     *            the cipher padding
     * @return 如果 <code>algorithm</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>algorithm</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>null == cipherMode && null == cipherPadding</code> ,返回 <code>algorithm</code><br>
     */
    public static String build(String algorithm,CipherMode cipherMode,CipherPadding cipherPadding){
        Validate.notBlank(algorithm, "algorithm can't be blank!");

        //---------------------------------------------------------------
        if (null == cipherMode && null == cipherPadding){
            return algorithm;
        }

        //---------------------------------------------------------------
        String transformation = algorithm;
        if (null != cipherMode){
            transformation += "/" + cipherMode;
        }

        //继续追加
        if (null != cipherPadding){
            transformation += "/" + cipherPadding;
        }
        return transformation;
    }
}
