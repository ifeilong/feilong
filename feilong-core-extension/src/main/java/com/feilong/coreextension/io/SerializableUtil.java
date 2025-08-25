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
package com.feilong.coreextension.io;

import static com.feilong.core.CharsetType.ISO_8859_1;
import static com.feilong.core.CharsetType.UTF8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.net.URIUtil;
import com.feilong.io.IOUtil;
import com.feilong.io.InputStreamUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link java.io.Serializable}util.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see java.io.Serializable
 * @see "c3p0 com.mchange.v2.ser.SerializableUtils#serializeToByteArray(Object)"
 * @see com.feilong.lib.lang3.SerializationUtils
 * @see "org.hibernate.util.SerializationHelper"
 * @see "org.springframework.util.SerializationUtils"
 * @see "org.springframework.core.serializer.DefaultSerializer"
 * @since 1.2.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SerializableUtil{

    /**
     * To byte array.
     *
     * @param serializable
     *            the serializable
     * @return the byte[]
     * @see com.feilong.lib.lang3.SerializationUtils#serialize(Serializable)
     */
    public static byte[] serialize(Serializable serializable){
        return com.feilong.lib.lang3.SerializationUtils.serialize(serializable);
    }

    /**
     * Deserialize.
     *
     * @param <T>
     *            the generic type
     * @param objectData
     *            the object data
     * @return the t
     * @see com.feilong.lib.lang3.SerializationUtils#deserialize(byte[])
     */
    public static <T> T deserialize(byte[] objectData){
        return com.feilong.lib.lang3.SerializationUtils.deserialize(objectData);
    }

    /**
     * 返回对象内存大小.
     * 
     * <p>
     * <span style="color:red">只有支持 {@link java.io.Serializable Serializable}或 {@link java.io.Externalizable Externalizable} 接口的对象才能被
     * {@link java.io.ObjectInputStream ObjectInputStream}/{@link java.io.ObjectOutputStream ObjectOutputStream}所操作！</span>
     * </p>
     *
     * @param serializable
     *            the serializable
     * @return the int
     * @see #toByteArrayOutputStream(Serializable)
     * @see java.io.ByteArrayOutputStream#size()
     */
    public static int size(Serializable serializable){
        ByteArrayOutputStream byteArrayOutputStream = null;
        try{
            byteArrayOutputStream = toByteArrayOutputStream(serializable);
            return byteArrayOutputStream.size();
        }finally{
            IOUtil.closeQuietly(byteArrayOutputStream);
        }
    }

    //---------------------------------------------------------------

    /**
     * To byte array output stream.
     *
     * @param serializable
     *            the serializable
     * @return the byte array output stream
     * @see java.io.ObjectOutputStream#ObjectOutputStream(OutputStream)
     * @see java.io.ObjectOutputStream#writeObject(Object)
     * @see com.feilong.lib.lang3.SerializationUtils#serialize(Serializable, OutputStream)
     */
    private static ByteArrayOutputStream toByteArrayOutputStream(Serializable serializable){
        ObjectOutputStream objectOutputStream = null;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);

            return byteArrayOutputStream;
        }catch (IOException e){
            throw new DefaultRuntimeException(e);
        }finally{
            IOUtil.closeQuietly(objectOutputStream);
        }
    }

    //---------------------------------------------------------------

    /**
     * To string.
     *
     * @param serializable
     *            the serializable
     * @return the string
     * @see #toByteArrayOutputStream(Serializable)
     * @deprecated 转字符串值得商榷
     */
    @Deprecated
    public static String toString(Serializable serializable){
        ByteArrayOutputStream byteArrayOutputStream = null;
        try{
            byteArrayOutputStream = toByteArrayOutputStream(serializable);

            String serializableString = byteArrayOutputStream.toString(ISO_8859_1);
            return URIUtil.encode(serializableString, UTF8);
        }catch (IOException e){
            throw new DefaultRuntimeException(e);
        }finally{
            IOUtil.closeQuietly(byteArrayOutputStream);
        }
    }

    //---------------------------------------------------------------

    /**
     * To object.
     *
     * @param <T>
     *            the generic type
     * @param serializableString
     *            the serializable string
     * @return the t
     * @see "com.feilong.lib.io.IOUtils#toInputStream(String, Charset)"
     * @see com.feilong.lib.lang3.SerializationUtils#deserialize(InputStream)
     * @deprecated 转字符串值得商榷
     */
    @Deprecated
    public static <T> T toObject(String serializableString){
        InputStream inputStream = null;
        try{
            String decodeString = URIUtil.encode(serializableString, UTF8);
            inputStream = InputStreamUtil.newByteArrayInputStream(decodeString, ISO_8859_1);
            return com.feilong.lib.lang3.SerializationUtils.deserialize(inputStream);
        }finally{
            IOUtil.closeQuietly(inputStream);
        }
    }
}
