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

package com.feilong.lib.org.apache.http.client.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.feilong.lib.org.apache.http.HttpEntity;
import com.feilong.lib.org.apache.http.NameValuePair;
import com.feilong.lib.org.apache.http.entity.AbstractHttpEntity;
import com.feilong.lib.org.apache.http.entity.BasicHttpEntity;
import com.feilong.lib.org.apache.http.entity.ByteArrayEntity;
import com.feilong.lib.org.apache.http.entity.ContentType;
import com.feilong.lib.org.apache.http.entity.FileEntity;
import com.feilong.lib.org.apache.http.entity.InputStreamEntity;
import com.feilong.lib.org.apache.http.entity.SerializableEntity;
import com.feilong.lib.org.apache.http.entity.StringEntity;

/**
 * Builder for {@link HttpEntity} instances.
 * <p>
 * Several setter methods of this builder are mutually exclusive.
 * 
 * In case of multiple invocations of the following methods only the last one will have effect:
 * </p>
 * <ul>
 * <li>{@link #setText(String)}</li>
 * <li>{@link #setBinary(byte[])}</li>
 * <li>{@link #setStream(java.io.InputStream)}</li>
 * <li>{@link #setSerializable(java.io.Serializable)}</li>
 * <li>{@link #setParameters(java.util.List)}</li>
 * <li>{@link #setParameters(com.feilong.lib.org.apache.http.NameValuePair...)}</li>
 * <li>{@link #setFile(java.io.File)}</li>
 * </ul>
 *
 * @since 4.3
 */
public class EntityBuilder{

    /** The text. */
    private String              text;

    /** The binary. */
    private byte[]              binary;

    /** The stream. */
    private InputStream         stream;

    /** The parameters. */
    private List<NameValuePair> parameters;

    /** The serializable. */
    private Serializable        serializable;

    /** The file. */
    private File                file;

    /** The content type. */
    private ContentType         contentType;

    /** The content encoding. */
    private String              contentEncoding;

    /** The chunked. */
    private boolean             chunked;

    /** The gzip compress. */
    private boolean             gzipCompress;

    /**
     * Instantiates a new entity builder.
     */
    EntityBuilder(){
        super();
    }

    /**
     * 创建.
     *
     * @return the entity builder
     */
    public static EntityBuilder create(){
        return new EntityBuilder();
    }

    /**
     * 清除 content.
     */
    private void clearContent(){
        this.text = null;
        this.binary = null;
        this.stream = null;
        this.parameters = null;
        this.serializable = null;
        this.file = null;
    }

    /**
     * Returns entity content as a string if set using {@link #setText(String)} method.
     *
     * @return the text
     */
    public String getText(){
        return text;
    }

    /**
     * Sets entity content as a string. This method is mutually exclusive with:
     * <ul>
     * <li>{@link #setBinary(byte[])}</li>
     * <li>{@link #setStream(java.io.InputStream)}</li>
     * <li>{@link #setSerializable(java.io.Serializable)}</li>
     * <li>{@link #setParameters(java.util.List)}</li>
     * <li>{@link #setParameters(com.feilong.lib.org.apache.http.NameValuePair...)}</li>
     * <li>{@link #setFile(java.io.File)}</li>
     * </ul>
     *
     * @param text
     *            the text
     * @return the entity builder
     */
    public EntityBuilder setText(final String text){
        clearContent();
        this.text = text;
        return this;
    }

    /**
     * Returns entity content as a byte array if set using
     * {@link #setBinary(byte[])} method.
     *
     * @return the binary
     */
    public byte[] getBinary(){
        return binary;
    }

    /**
     * Sets entity content as a byte array. This method is mutually exclusive
     * with:
     * <ul>
     * <li>{@link #setText(String)}</li>
     * <li>{@link #setStream(java.io.InputStream)}</li>
     * <li>{@link #setSerializable(java.io.Serializable)}</li>
     * <li>{@link #setParameters(java.util.List)}</li>
     * <li>{@link #setParameters(com.feilong.lib.org.apache.http.NameValuePair...)}</li>
     * <li>{@link #setFile(java.io.File)}</li>
     * </ul>
     *
     * @param binary
     *            the binary
     * @return the entity builder
     */
    public EntityBuilder setBinary(final byte[] binary){
        clearContent();
        this.binary = binary;
        return this;
    }

    /**
     * Returns entity content as a {@link InputStream} if set using
     * {@link #setStream(java.io.InputStream)} method.
     *
     * @return the stream
     */
    public InputStream getStream(){
        return stream;
    }

    /**
     * Sets entity content as a {@link InputStream}. This method is mutually
     * exclusive with:
     * <ul>
     * <li>{@link #setText(String)}</li>
     * <li>{@link #setBinary(byte[])}</li>
     * <li>{@link #setSerializable(java.io.Serializable)}</li>
     * <li>{@link #setParameters(java.util.List)}</li>
     * <li>{@link #setParameters(com.feilong.lib.org.apache.http.NameValuePair...)}</li>
     * <li>{@link #setFile(java.io.File)}</li>
     * </ul>
     *
     * @param stream
     *            the stream
     * @return the entity builder
     */
    public EntityBuilder setStream(final InputStream stream){
        clearContent();
        this.stream = stream;
        return this;
    }

    /**
     * Returns entity content as a parameter list if set using
     * {@link #setParameters(java.util.List)} or
     * {@link #setParameters(com.feilong.lib.org.apache.http.NameValuePair...)} methods.
     *
     * @return the parameters
     */
    public List<NameValuePair> getParameters(){
        return parameters;
    }

    /**
     * Sets entity content as a parameter list. This method is mutually
     * exclusive with:
     * <ul>
     * <li>{@link #setText(String)}</li>
     * <li>{@link #setBinary(byte[])}</li>
     * <li>{@link #setStream(java.io.InputStream)}</li>
     * <li>{@link #setSerializable(java.io.Serializable)}</li>
     * <li>{@link #setFile(java.io.File)}</li>
     * </ul>
     *
     * @param parameters
     *            the parameters
     * @return the entity builder
     */
    public EntityBuilder setParameters(final List<NameValuePair> parameters){
        clearContent();
        this.parameters = parameters;
        return this;
    }

    /**
     * Sets entity content as a parameter list. This method is mutually exclusive with:
     * <ul>
     * <li>{@link #setText(String)}</li>
     * <li>{@link #setBinary(byte[])}</li>
     * <li>{@link #setStream(java.io.InputStream)}</li>
     * <li>{@link #setSerializable(java.io.Serializable)}</li>
     * <li>{@link #setFile(java.io.File)}</li>
     * </ul>
     *
     * @param parameters
     *            the parameters
     * @return the entity builder
     */
    public EntityBuilder setParameters(final NameValuePair...parameters){
        return setParameters(Arrays.asList(parameters));
    }

    /**
     * Returns entity content as a {@link Serializable} if set using
     * {@link #setSerializable(java.io.Serializable)} method.
     *
     * @return the serializable
     */
    public Serializable getSerializable(){
        return serializable;
    }

    /**
     * Sets entity content as a {@link Serializable}. This method is mutually exclusive with:
     * <ul>
     * <li>{@link #setText(String)}</li>
     * <li>{@link #setBinary(byte[])}</li>
     * <li>{@link #setStream(java.io.InputStream)}</li>
     * <li>{@link #setParameters(java.util.List)}</li>
     * <li>{@link #setParameters(com.feilong.lib.org.apache.http.NameValuePair...)}</li>
     * <li>{@link #setFile(java.io.File)}</li>
     * </ul>
     *
     * @param serializable
     *            the serializable
     * @return the entity builder
     */
    public EntityBuilder setSerializable(final Serializable serializable){
        clearContent();
        this.serializable = serializable;
        return this;
    }

    /**
     * Returns entity content as a {@link File} if set using
     * {@link #setFile(java.io.File)} method.
     *
     * @return the file
     */
    public File getFile(){
        return file;
    }

    /**
     * Sets entity content as a {@link File}. This method is mutually exclusive with:
     * <ul>
     * <li>{@link #setText(String)}</li>
     * <li>{@link #setBinary(byte[])}</li>
     * <li>{@link #setStream(java.io.InputStream)}</li>
     * <li>{@link #setParameters(java.util.List)}</li>
     * <li>{@link #setParameters(com.feilong.lib.org.apache.http.NameValuePair...)}</li>
     * <li>{@link #setSerializable(java.io.Serializable)}</li>
     * </ul>
     *
     * @param file
     *            the file
     * @return the entity builder
     */
    public EntityBuilder setFile(final File file){
        clearContent();
        this.file = file;
        return this;
    }

    /**
     * Returns {@link ContentType} of the entity, if set.
     *
     * @return the content type
     */
    public ContentType getContentType(){
        return contentType;
    }

    /**
     * Sets {@link ContentType} of the entity.
     *
     * @param contentType
     *            the content type
     * @return the entity builder
     */
    public EntityBuilder setContentType(final ContentType contentType){
        this.contentType = contentType;
        return this;
    }

    /**
     * Returns content encoding of the entity, if set.
     *
     * @return the content encoding
     */
    public String getContentEncoding(){
        return contentEncoding;
    }

    /**
     * Sets content encoding of the entity.
     *
     * @param contentEncoding
     *            the content encoding
     * @return the entity builder
     */
    public EntityBuilder setContentEncoding(final String contentEncoding){
        this.contentEncoding = contentEncoding;
        return this;
    }

    /**
     * Returns {@code true} if entity is to be chunk coded, {@code false} otherwise.
     *
     * @return true, if is chunked
     */
    public boolean isChunked(){
        return chunked;
    }

    /**
     * Makes entity chunk coded.
     *
     * @return the entity builder
     */
    public EntityBuilder chunked(){
        this.chunked = true;
        return this;
    }

    /**
     * Returns {@code true} if entity is to be GZIP compressed, {@code false} otherwise.
     *
     * @return true, if is gzip compress
     */
    public boolean isGzipCompress(){
        return gzipCompress;
    }

    /**
     * Makes entity GZIP compressed.
     *
     * @return the entity builder
     */
    public EntityBuilder gzipCompress(){
        this.gzipCompress = true;
        return this;
    }

    /**
     * Gets the content or default.
     *
     * @param def
     *            the def
     * @return the content or default
     */
    private ContentType getContentOrDefault(final ContentType def){
        return this.contentType != null ? this.contentType : def;
    }

    /**
     * Creates new instance of {@link HttpEntity} based on the current state.
     *
     * @return the http entity
     */
    public HttpEntity build(){
        final AbstractHttpEntity e;
        if (this.text != null){
            e = new StringEntity(this.text, getContentOrDefault(ContentType.DEFAULT_TEXT));
        }else if (this.binary != null){
            e = new ByteArrayEntity(this.binary, getContentOrDefault(ContentType.DEFAULT_BINARY));
        }else if (this.stream != null){
            e = new InputStreamEntity(this.stream, -1, getContentOrDefault(ContentType.DEFAULT_BINARY));
        }else if (this.parameters != null){
            e = new UrlEncodedFormEntity(this.parameters, this.contentType != null ? this.contentType.getCharset() : null);
        }else if (this.serializable != null){
            e = new SerializableEntity(this.serializable);
            e.setContentType(ContentType.DEFAULT_BINARY.toString());
        }else if (this.file != null){
            e = new FileEntity(this.file, getContentOrDefault(ContentType.DEFAULT_BINARY));
        }else{
            e = new BasicHttpEntity();
        }
        if (e.getContentType() != null && this.contentType != null){
            e.setContentType(this.contentType.toString());
        }
        e.setContentEncoding(this.contentEncoding);
        e.setChunked(this.chunked);
        if (this.gzipCompress){
            return new GzipCompressingEntity(e);
        }
        return e;
    }

}
