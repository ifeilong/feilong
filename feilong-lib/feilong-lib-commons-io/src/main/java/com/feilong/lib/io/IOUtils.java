/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.lib.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;

import com.feilong.lib.io.output.StringBuilderWriter;

/**
 * General IO stream manipulation utilities.
 * <p>
 * This class provides static utility methods for input/output operations.
 * <ul>
 * <li><b>[Deprecated]</b> closeQuietly - these methods close a stream ignoring nulls and exceptions
 * <li>toXxx/read - these methods read data from a stream
 * <li>write - these methods write data to a stream
 * <li>copy - these methods copy all the data from one stream to another
 * <li>contentEquals - these methods compare the content of two streams
 * </ul>
 * <p>
 * The byte-to-char methods and char-to-byte methods involve a conversion step.
 * Two methods are provided in each case, one that uses the platform default
 * encoding and the other which allows you to specify an encoding. You are
 * encouraged to always specify an encoding because relying on the platform
 * default can lead to unexpected results, for example when moving from
 * development to production.
 * <p>
 * All the methods in this class that read a stream are buffered internally.
 * This means that there is no cause to use a <code>BufferedInputStream</code>
 * or <code>BufferedReader</code>. The default buffer size of 4K has been shown
 * to be efficient in tests.
 * <p>
 * The various copy methods all delegate the actual copying to one of the following methods:
 * <ul>
 * <li>{@link #copyLarge(InputStream, OutputStream, byte[])}</li>
 * <li>{@link #copyLarge(InputStream, OutputStream, long, long, byte[])}</li>
 * <li>{@link #copyLarge(Reader, Writer, char[])}</li>
 * <li>{@link #copyLarge(Reader, Writer, long, long, char[])}</li>
 * </ul>
 * For example, {@link #copy(InputStream, OutputStream)} calls {@link #copyLarge(InputStream, OutputStream)}
 * which calls {@link #copy(InputStream, OutputStream, int)} which creates the buffer and calls
 * {@link #copyLarge(InputStream, OutputStream, byte[])}.
 * <p>
 * Applications can re-use buffers by using the underlying methods directly.
 * This may improve performance for applications that need to do a lot of copying.
 * <p>
 * Wherever possible, the methods in this class do <em>not</em> flush or close
 * the stream. This is to avoid making non-portable assumptions about the
 * streams' origin and further use. Thus the caller is still responsible for
 * closing streams after use.
 * <p>
 * Origin of code: Excalibur.
 *
 */
public class IOUtils{
    // NOTE: This class is focused on InputStream, OutputStream, Reader and
    // Writer. Each method should take at least one of these as a parameter,
    // or return one of them.

    /**
     * Represents the end-of-file (or stream).
     * 
     * @since 2.5 (made public)
     */
    public static final int    EOF                    = -1;

    /**
     * The Unix directory separator character.
     */
    public static final char   DIR_SEPARATOR_UNIX     = '/';

    /**
     * The system directory separator character.
     */
    public static final char   DIR_SEPARATOR          = File.separatorChar;

    /**
     * The Unix line separator string.
     */
    public static final String LINE_SEPARATOR_UNIX    = "\n";

    /**
     * The Windows line separator string.
     */
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    /**
     * The system line separator string.
     */
    public static final String LINE_SEPARATOR;

    static{
        // avoid security issues
        try (final StringBuilderWriter buf = new StringBuilderWriter(4); final PrintWriter out = new PrintWriter(buf)){
            out.println();
            LINE_SEPARATOR = buf.toString();
        }
    }

    /**
     * The default buffer size ({@value}) to use for
     * {@link #copyLarge(InputStream, OutputStream)}
     * and
     * {@link #copyLarge(Reader, Writer)}
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    //-----------------------------------------------------------------------

    /**
     * Gets the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input
     *            the <code>InputStream</code> to read from
     * @return the requested byte array
     * @throws NullPointerException
     *             if the input is null
     * @throws IOException
     *             if an I/O error occurs
     */
    public static byte[] toByteArray(final InputStream input) throws IOException{
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()){
            copy(input, output);
            return output.toByteArray();
        }
    }

    /**
     * Copies bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Large streams (over 2GB) will return a bytes copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of bytes cannot be returned as an int. For large streams
     * use the <code>copyLarge(InputStream, OutputStream)</code> method.
     *
     * @param input
     *            the <code>InputStream</code> to read from
     * @param output
     *            the <code>OutputStream</code> to write to
     * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 1.1
     */
    public static int copy(final InputStream input,final OutputStream output) throws IOException{
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE){
            return -1;
        }
        return (int) count;
    }

    /**
     * Copies bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @param input
     *            the <code>InputStream</code> to read from
     * @param output
     *            the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 1.3
     */
    public static long copyLarge(final InputStream input,final OutputStream output) throws IOException{
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Copies bytes from an <code>InputStream</code> to an <code>OutputStream</code> using an internal buffer of the
     * given size.
     * <p>
     * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
     * <p>
     *
     * @param input
     *            the <code>InputStream</code> to read from
     * @param output
     *            the <code>OutputStream</code> to write to
     * @param bufferSize
     *            the bufferSize used to copy from the input to the output
     * @return the number of bytes copied
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 2.5
     */
    public static long copy(final InputStream input,final OutputStream output,final int bufferSize) throws IOException{
        return copyLarge(input, output, new byte[bufferSize]);
    }

    /**
     * Copies bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     *
     * @param input
     *            the <code>InputStream</code> to read from
     * @param output
     *            the <code>OutputStream</code> to write to
     * @param buffer
     *            the buffer to use for the copy
     * @return the number of bytes copied
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 2.2
     */
    public static long copyLarge(final InputStream input,final OutputStream output,final byte[] buffer) throws IOException{
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))){
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Returns the given reader if it is a {@link BufferedReader}, otherwise creates a BufferedReader from the given
     * reader.
     *
     * @param reader
     *            the reader to wrap or return (not null)
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException
     *             if the input parameter is null
     * @since 2.2
     */
    public static BufferedReader toBufferedReader(final Reader reader){
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    // read toByteArray
    //-----------------------------------------------------------------------

    /**
     * Gets contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * Use this method instead of <code>toByteArray(InputStream)</code>
     * when <code>InputStream</code> size is known.
     * <b>NOTE:</b> the method checks that the length can safely be cast to an int without truncation
     * before using {@link IOUtils#toByteArray(java.io.InputStream, int)} to read into the byte array.
     * (Arrays can have no more than Integer.MAX_VALUE entries anyway)
     *
     * @param input
     *            the <code>InputStream</code> to read from
     * @param size
     *            the size of <code>InputStream</code>
     * @return the requested byte array
     * @throws IOException
     *             if an I/O error occurs or <code>InputStream</code> size differ from parameter
     *             size
     * @throws IllegalArgumentException
     *             if size is less than zero or size is greater than Integer.MAX_VALUE
     * @see IOUtils#toByteArray(java.io.InputStream, int)
     * @since 2.1
     */
    public static byte[] toByteArray(final InputStream input,final long size) throws IOException{

        if (size > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }

        return toByteArray(input, (int) size);
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * Use this method instead of <code>toByteArray(InputStream)</code>
     * when <code>InputStream</code> size is known
     *
     * @param input
     *            the <code>InputStream</code> to read from
     * @param size
     *            the size of <code>InputStream</code>
     * @return the requested byte array
     * @throws IOException
     *             if an I/O error occurs or <code>InputStream</code> size differ from parameter
     *             size
     * @throws IllegalArgumentException
     *             if size is less than zero
     * @since 2.1
     */
    public static byte[] toByteArray(final InputStream input,final int size) throws IOException{

        if (size < 0){
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }

        if (size == 0){
            return new byte[0];
        }

        final byte[] data = new byte[size];
        int offset = 0;
        int read;

        while (offset < size && (read = input.read(data, offset, size - offset)) != EOF){
            offset += read;
        }

        if (offset != size){
            throw new IOException("Unexpected read size. current: " + offset + ", expected: " + size);
        }

        return data;
    }

    // read toString

    /**
     * Gets the contents of a <code>Reader</code> as a String.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input
     *            the <code>Reader</code> to read from
     * @return the requested String
     * @throws NullPointerException
     *             if the input is null
     * @throws IOException
     *             if an I/O error occurs
     */
    public static String toString(final Reader input) throws IOException{
        try (final StringBuilderWriter sw = new StringBuilderWriter()){
            copy(input, sw);
            return sw.toString();
        }
    }

    /**
     * Copies chars from a <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Large streams (over 2GB) will return a chars copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the <code>copyLarge(Reader, Writer)</code> method.
     *
     * @param input
     *            the <code>Reader</code> to read from
     * @param output
     *            the <code>Writer</code> to write to
     * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 1.1
     */
    public static int copy(final Reader input,final Writer output) throws IOException{
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE){
            return -1;
        }
        return (int) count;
    }

    /**
     * Copies chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @param input
     *            the <code>Reader</code> to read from
     * @param output
     *            the <code>Writer</code> to write to
     * @return the number of characters copied
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 1.3
     */
    public static long copyLarge(final Reader input,final Writer output) throws IOException{
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * Copies chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     *
     * @param input
     *            the <code>Reader</code> to read from
     * @param output
     *            the <code>Writer</code> to write to
     * @param buffer
     *            the buffer to be used for the copy
     * @return the number of characters copied
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 2.2
     */
    public static long copyLarge(final Reader input,final Writer output,final char[] buffer) throws IOException{
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))){
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    // readLines
    //-----------------------------------------------------------------------

    /**
     * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
     * using the specified character encoding.
     * <p>
     * This method uses {@link String#String(byte[], String)}.
     *
     * @param data
     *            the byte array to write, do not modify during output,
     *            null ignored
     * @param output
     *            the <code>Writer</code> to write to
     * @param encoding
     *            the encoding to use, null means platform default
     * @throws NullPointerException
     *             if output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 2.3
     */
    public static void write(final byte[] data,final Writer output,final Charset encoding) throws IOException{
        if (data != null){
            output.write(new String(data, Charsets.toCharset(encoding)));
        }
    }

    // write String
    //-----------------------------------------------------------------------

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data
     *            the <code>String</code> to write, null ignored
     * @param output
     *            the <code>OutputStream</code> to write to
     * @param encoding
     *            the encoding to use, null means platform default
     * @throws NullPointerException
     *             if output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 2.3
     */
    public static void write(final String data,final OutputStream output,final Charset encoding) throws IOException{
        if (data != null){
            output.write(data.getBytes(Charsets.toCharset(encoding)));
        }
    }

    // writeLines
    //-----------------------------------------------------------------------

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the specified character
     * encoding and the specified line ending.
     *
     * @param lines
     *            the lines to write, null entries produce blank lines
     * @param lineEnding
     *            the line separator to use, null is system default
     * @param output
     *            the <code>OutputStream</code> to write to, not null, not closed
     * @param encoding
     *            the encoding to use, null means platform default
     * @throws NullPointerException
     *             if the output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 2.3
     */
    public static void writeLines(final Collection<?> lines,String lineEnding,final OutputStream output,final Charset encoding)
                    throws IOException{
        if (lines == null){
            return;
        }
        if (lineEnding == null){
            lineEnding = LINE_SEPARATOR;
        }
        final Charset cs = Charsets.toCharset(encoding);
        for (final Object line : lines){
            if (line != null){
                output.write(line.toString().getBytes(cs));
            }
            output.write(lineEnding.getBytes(cs));
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the specified character
     * encoding and the specified line ending.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param lines
     *            the lines to write, null entries produce blank lines
     * @param lineEnding
     *            the line separator to use, null is system default
     * @param output
     *            the <code>OutputStream</code> to write to, not null, not closed
     * @param encoding
     *            the encoding to use, null means platform default
     * @throws NullPointerException
     *             if the output is null
     * @throws IOException
     *             if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException
     *             thrown instead of {@link java.io
     *             .UnsupportedEncodingException} in version 2.2 if the
     *             encoding is not supported.
     * @since 1.1
     */
    public static void writeLines(final Collection<?> lines,final String lineEnding,final OutputStream output,final String encoding)
                    throws IOException{
        writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
    }

    // copy from InputStream

}
