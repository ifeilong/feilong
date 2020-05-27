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
 * 填充类型
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.7 2014年7月8日 下午5:12:49
 * @since 1.0.7
 */
public enum CipherPadding{

    /**
     * (不填充)
     */
    NoPadding,

    /**
     * This padding for block ciphers is described in 5.2 Block Encryption Algorithms in the W3C's "XML Encryption Syntax and Processing"
     * document.
     */
    ISO10126Padding,

    /**
     * The padding scheme described in PKCS1, used with the RSA algorithm.
     */
    PKCS1Padding,

    /**
     * The padding scheme described in RSA Laboratories, "PKCS5: Password-Based Encryption Standard," version 1.5, November 1993.
     */
    PKCS5Padding,

    /**
     * <pre class="code">
     * {@code
     * The padding scheme defined in the SSL Protocol Version 3.0, November 18, 1996, section 5.2.3.2 (CBC block cipher):
     *     block-ciphered struct} {{@code
     *  opaque content[SSLCompressed.length];
     *  opaque MAC[CipherSpec.hash_size];
     *  uint8 padding[
     *      GenericBlockCipher.padding_length];
     *  uint8 padding_length;
     *     }}{@code GenericBlockCipher;
     * The size of an instance of a GenericBlockCipher must be a multiple of the block cipher's block length. 
     * 
     * The padding length, which is always present, contributes to the padding, which implies that if:
     *     sizeof(content) + sizeof(MAC) % block_length = 0, 
     * padding has to be (block_length - 1) bytes long, because of the existence of padding_length. 
     * 
     * This make the padding scheme similar (but not quite) to PKCS5Padding, where the padding length is encoded in the padding (and ranges from 1 to block_length). With the SSL scheme, the sizeof(padding) is encoded in the always present padding_length and therefore ranges from 0 to block_length-1.
     * }
     * </pre>
     */
    SSL3Padding
}
