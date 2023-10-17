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
package com.feilong.net.http;

import com.feilong.io.entity.MimeType;

/**
 * 字节流形式的请求体.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see "org.apache.http.HttpRequest"
 * @see java.io.ByteArrayInputStream
 * @since 4.0.1
 */
public class RequestByteArrayBody{

    /**
     * An array of bytes that was provided by the creator of the stream.
     * 
     * Elements <code>buf[0]</code> through <code>buf[count-1]</code> are the only bytes that can ever be read from the stream;
     * 
     * element <code>buf[pos]</code> is the next byte to be read.
     */
    private byte[]   buf;

    //---------------------------------------------------------------

    /**
     * the offset in the buffer of the first byte to read.
     */
    private Integer  off;

    /**
     * the maximum number of bytes to read from the buffer.
     */
    private Integer  length;

    /** The mime type. */
    private MimeType mimeType;

    //---------------------------------------------------------------

    /**
     * Instantiates a new request byte array body.
     *
     * @param buf
     *            An array of bytes that was provided by the creator of the stream.
     * 
     *            Elements <code>buf[0]</code> through <code>buf[count-1]</code> are the only bytes that can ever be read from the stream;
     * 
     *            element <code>buf[pos]</code> is the next byte to be read.
     */
    public RequestByteArrayBody(byte[] buf){
        super();
        this.buf = buf;
    }

    /**
     * Instantiates a new request byte array body.
     *
     * @param buf
     *            An array of bytes that was provided by the creator of the stream.
     * 
     *            Elements <code>buf[0]</code> through <code>buf[count-1]</code> are the only bytes that can ever be read from the stream;
     * 
     *            element <code>buf[pos]</code> is the next byte to be read.
     * @param off
     *            the off
     * @param length
     *            the length
     * @param mimeType
     *            the mime type
     */
    public RequestByteArrayBody(byte[] buf, Integer off, Integer length, MimeType mimeType){
        super();
        this.buf = buf;
        this.off = off;
        this.length = length;
        this.mimeType = mimeType;
    }

    /**
     * Instantiates a new request byte array body.
     *
     * @param buf
     *            An array of bytes that was provided by the creator of the stream.
     * 
     *            Elements <code>buf[0]</code> through <code>buf[count-1]</code> are the only bytes that can ever be read from the stream;
     * 
     *            element <code>buf[pos]</code> is the next byte to be read.
     * @param mimeType
     *            the mime type
     */
    public RequestByteArrayBody(byte[] buf, MimeType mimeType){
        super();
        this.buf = buf;
        this.mimeType = mimeType;
    }

    //---------------------------------------------------------------

    /**
     * 获得 an array of bytes that was provided by the creator of the stream.
     *
     * @return the buf
     */
    public byte[] getBuf(){
        return buf;
    }

    /**
     * 设置 an array of bytes that was provided by the creator of the stream.
     *
     * @param buf
     *            the buf to set
     */
    public void setBuf(byte[] buf){
        this.buf = buf;
    }

    /**
     * 获得 offset in the buffer of the first byte to read.
     *
     * @return the off
     */
    public Integer getOff(){
        return off;
    }

    /**
     * 设置 offset in the buffer of the first byte to read.
     *
     * @param off
     *            the off to set
     */
    public void setOff(Integer off){
        this.off = off;
    }

    /**
     * 获得 maximum number of bytes to read from the buffer.
     *
     * @return the length
     */
    public Integer getLength(){
        return length;
    }

    /**
     * 设置 maximum number of bytes to read from the buffer.
     *
     * @param length
     *            the length to set
     */
    public void setLength(Integer length){
        this.length = length;
    }

    /**
     * Gets the mime type.
     *
     * @return the mimeType
     */
    public MimeType getMimeType(){
        return mimeType;
    }

    /**
     * Sets the mime type.
     *
     * @param mimeType
     *            the mimeType to set
     */
    public void setMimeType(MimeType mimeType){
        this.mimeType = mimeType;
    }

}
