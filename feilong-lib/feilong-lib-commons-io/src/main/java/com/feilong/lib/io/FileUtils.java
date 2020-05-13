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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

/**
 * General file manipulation utilities.
 * <p>
 * Facilities are provided in the following areas:
 * <ul>
 * <li>writing to a file
 * <li>reading from a file
 * <li>make a directory including parent directories
 * <li>copying files and directories
 * <li>deleting files and directories
 * <li>converting to and from a URL
 * <li>listing files and directories by filter and extension
 * <li>comparing file content
 * <li>file last changed date
 * <li>calculating a checksum
 * </ul>
 * <p>
 * Note that a specific charset should be specified whenever possible.
 * Relying on the platform default means that the code is Locale-dependent.
 * Only use the default if the files are known to always use the platform default.
 * <p>
 * Origin of code: Excalibur, Alexandria, Commons-Utils
 *
 */
public class FileUtils{

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long       ONE_KB           = 1024;

    /**
     * The number of bytes in a kilobyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_KB_BI        = BigInteger.valueOf(ONE_KB);

    /**
     * The number of bytes in a megabyte.
     */
    public static final long       ONE_MB           = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a megabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_MB_BI        = ONE_KB_BI.multiply(ONE_KB_BI);

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long       ONE_GB           = ONE_KB * ONE_MB;

    /**
     * The number of bytes in a gigabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_GB_BI        = ONE_KB_BI.multiply(ONE_MB_BI);

    /**
     * The number of bytes in a terabyte.
     */
    public static final long       ONE_TB           = ONE_KB * ONE_GB;

    /**
     * The number of bytes in a terabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_TB_BI        = ONE_KB_BI.multiply(ONE_GB_BI);

    /**
     * The number of bytes in a petabyte.
     */
    public static final long       ONE_PB           = ONE_KB * ONE_TB;

    /**
     * The number of bytes in a petabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_PB_BI        = ONE_KB_BI.multiply(ONE_TB_BI);

    /**
     * The number of bytes in an exabyte.
     */
    public static final long       ONE_EB           = ONE_KB * ONE_PB;

    /**
     * The number of bytes in an exabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_EB_BI        = ONE_KB_BI.multiply(ONE_PB_BI);

    /**
     * The number of bytes in a zettabyte.
     */
    public static final BigInteger ONE_ZB           = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));

    /**
     * The number of bytes in a yottabyte.
     */
    public static final BigInteger ONE_YB           = ONE_KB_BI.multiply(ONE_ZB);

    /**
     * An empty array of type <code>File</code>.
     */
    public static final File[]     EMPTY_FILE_ARRAY = new File[0];

    //-----------------------------------------------------------------------
    /**
     * Construct a file from the set of name elements.
     *
     * @param directory
     *            the parent directory
     * @param names
     *            the name elements
     * @return the file
     * @since 2.1
     */
    public static File getFile(final File directory,final String...names){
        if (directory == null){
            throw new NullPointerException("directory must not be null");
        }
        if (names == null){
            throw new NullPointerException("names must not be null");
        }
        File file = directory;
        for (final String name : names){
            file = new File(file, name);
        }
        return file;
    }

    /**
     * Construct a file from the set of name elements.
     *
     * @param names
     *            the name elements
     * @return the file
     * @since 2.1
     */
    public static File getFile(final String...names){
        if (names == null){
            throw new NullPointerException("names must not be null");
        }
        File file = null;
        for (final String name : names){
            if (file == null){
                file = new File(name);
            }else{
                file = new File(file, name);
            }
        }
        return file;
    }

    /**
     * Reads the contents of a file into a byte array.
     * The file is always closed.
     *
     * @param file
     *            the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     * @throws IOException
     *             in case of an I/O error
     * @since 1.1
     */
    public static byte[] readFileToByteArray(final File file) throws IOException{
        try (InputStream in = openInputStream(file)){
            final long fileLength = file.length();
            // file.length() may return 0 for system-dependent entities, treat 0 as unknown length - see IO-453
            return fileLength > 0 ? IOUtils.toByteArray(in, fileLength) : IOUtils.toByteArray(in);
        }
    }

    /**
     * Returns the path to the system temporary directory.
     *
     * @return the path to the system temporary directory.
     *
     * @since 2.0
     */
    public static String getTempDirectoryPath(){
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Returns the path to the user's home directory.
     *
     * @return the path to the user's home directory.
     *
     * @since 2.0
     */
    public static String getUserDirectoryPath(){
        return System.getProperty("user.home");
    }

    /**
     * Returns a {@link File} representing the user's home directory.
     *
     * @return the user's home directory.
     *
     * @since 2.0
     */
    public static File getUserDirectory(){
        return new File(getUserDirectoryPath());
    }

    //-----------------------------------------------------------------------
    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * An exception is thrown if the file does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be read.
     *
     * @param file
     *            the file to open for input, must not be {@code null}
     * @return a new {@link FileInputStream} for the specified file
     * @throws FileNotFoundException
     *             if the file does not exist
     * @throws IOException
     *             if the file object is a directory
     * @throws IOException
     *             if the file cannot be read
     * @since 1.3
     */
    public static FileInputStream openInputStream(final File file) throws IOException{
        if (file.exists()){
            if (file.isDirectory()){
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false){
                throw new IOException("File '" + file + "' cannot be read");
            }
        }else{
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    //-----------------------------------------------------------------------
    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
     *
     * @param file
     *            the file to open for output, must not be {@code null}
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException
     *             if the file object is a directory
     * @throws IOException
     *             if the file cannot be written to
     * @throws IOException
     *             if a parent directory needs creating but that fails
     * @since 1.3
     */
    public static FileOutputStream openOutputStream(final File file) throws IOException{
        return openOutputStream(file, false);
    }

    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
     *
     * @param file
     *            the file to open for output, must not be {@code null}
     * @param append
     *            if {@code true}, then bytes will be added to the
     *            end of the file rather than overwriting
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException
     *             if the file object is a directory
     * @throws IOException
     *             if the file cannot be written to
     * @throws IOException
     *             if a parent directory needs creating but that fails
     * @since 2.1
     */
    public static FileOutputStream openOutputStream(final File file,final boolean append) throws IOException{
        if (file.exists()){
            if (file.isDirectory()){
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false){
                throw new IOException("File '" + file + "' cannot be written to");
            }
        }else{
            final File parent = file.getParentFile();
            if (parent != null){
                if (!parent.mkdirs() && !parent.isDirectory()){
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts an array of file extensions to suffixes for use
     * with IOFileFilters.
     *
     * @param extensions
     *            an array of extensions. Format: {"java", "xml"}
     * @return an array of suffixes. Format: {".java", ".xml"}
     */
    private static String[] toSuffixes(final String[] extensions){
        final String[] suffixes = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++){
            suffixes[i] = "." + extensions[i];
        }
        return suffixes;
    }

    //-----------------------------------------------------------------------
    /**
     * Convert from a <code>URL</code> to a <code>File</code>.
     * <p>
     * From version 1.1 this method will decode the URL.
     * Syntax such as <code>file:///my%20docs/file.txt</code> will be
     * correctly decoded to <code>/my docs/file.txt</code>. Starting with version
     * 1.5, this method uses UTF-8 to decode percent-encoded octets to characters.
     * Additionally, malformed percent-encoded octets are handled leniently by
     * passing them through literally.
     *
     * @param url
     *            the file URL to convert, {@code null} returns {@code null}
     * @return the equivalent <code>File</code> object, or {@code null}
     *         if the URL's protocol is not <code>file</code>
     */
    public static File toFile(final URL url){
        if (url == null || !"file".equalsIgnoreCase(url.getProtocol())){
            return null;
        }
        String filename = url.getFile().replace('/', File.separatorChar);
        filename = decodeUrl(filename);
        return new File(filename);
    }

    /**
     * Decodes the specified URL as per RFC 3986, i.e. transforms
     * percent-encoded octets to characters by decoding with the UTF-8 character
     * set. This function is primarily intended for usage with
     * {@link java.net.URL} which unfortunately does not enforce proper URLs. As
     * such, this method will leniently accept invalid characters or malformed
     * percent-encoded octets and simply pass them literally through to the
     * result string. Except for rare edge cases, this will make unencoded URLs
     * pass through unaltered.
     *
     * @param url
     *            The URL to decode, may be {@code null}.
     * @return The decoded URL or {@code null} if the input was
     *         {@code null}.
     */
    static String decodeUrl(final String url){
        String decoded = url;
        if (url != null && url.indexOf('%') >= 0){
            final int n = url.length();
            final StringBuilder buffer = new StringBuilder();
            final ByteBuffer bytes = ByteBuffer.allocate(n);
            for (int i = 0; i < n;){
                if (url.charAt(i) == '%'){
                    try{
                        do{
                            final byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            bytes.put(octet);
                            i += 3;
                        }while (i < n && url.charAt(i) == '%');
                        continue;
                    }catch (final RuntimeException e){
                        // malformed percent-encoded octet, fall through and
                        // append characters literally
                    }finally{
                        if (bytes.position() > 0){
                            bytes.flip();
                            buffer.append(StandardCharsets.UTF_8.decode(bytes).toString());
                            bytes.clear();
                        }
                    }
                }
                buffer.append(url.charAt(i++));
            }
            decoded = buffer.toString();
        }
        return decoded;
    }

    /**
     * Converts each of an array of <code>URL</code> to a <code>File</code>.
     * <p>
     * Returns an array of the same size as the input.
     * If the input is {@code null}, an empty array is returned.
     * If the input contains {@code null}, the output array contains {@code null} at the same
     * index.
     * <p>
     * This method will decode the URL.
     * Syntax such as <code>file:///my%20docs/file.txt</code> will be
     * correctly decoded to <code>/my docs/file.txt</code>.
     *
     * @param urls
     *            the file URLs to convert, {@code null} returns empty array
     * @return a non-{@code null} array of Files matching the input, with a {@code null} item
     *         if there was a {@code null} at that index in the input array
     * @throws IllegalArgumentException
     *             if any file is not a URL file
     * @throws IllegalArgumentException
     *             if any file is incorrectly encoded
     * @since 1.1
     */
    public static File[] toFiles(final URL[] urls){
        if (urls == null || urls.length == 0){
            return EMPTY_FILE_ARRAY;
        }
        final File[] files = new File[urls.length];
        for (int i = 0; i < urls.length; i++){
            final URL url = urls[i];
            if (url != null){
                if (url.getProtocol().equals("file") == false){
                    throw new IllegalArgumentException("URL could not be converted to a File: " + url);
                }
                files[i] = toFile(url);
            }
        }
        return files;
    }

    /**
     * Converts each of an array of <code>File</code> to a <code>URL</code>.
     * <p>
     * Returns an array of the same size as the input.
     *
     * @param files
     *            the files to convert, must not be {@code null}
     * @return an array of URLs matching the input
     * @throws IOException
     *             if a file cannot be converted
     * @throws NullPointerException
     *             if the parameter is null
     */
    public static URL[] toURLs(final File[] files) throws IOException{
        final URL[] urls = new URL[files.length];

        for (int i = 0; i < urls.length; i++){
            urls[i] = files[i].toURI().toURL();
        }

        return urls;
    }

    //-----------------------------------------------------------------------
    /**
     * Deletes a directory recursively.
     *
     * @param directory
     *            directory to delete
     * @throws IOException
     *             in case deletion is unsuccessful
     * @throws IllegalArgumentException
     *             if {@code directory} does not exist or is not a directory
     */
    public static void deleteDirectory(final File directory) throws IOException{
        if (!directory.exists()){
            return;
        }

        if (!isSymlink(directory)){
            cleanDirectory(directory);
        }

        if (!directory.delete()){
            final String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file
     *            file or directory to delete, can be {@code null}
     * @return {@code true} if the file or directory was deleted, otherwise
     *         {@code false}
     *
     * @since 1.4
     */
    public static boolean deleteQuietly(final File file){
        if (file == null){
            return false;
        }
        try{
            if (file.isDirectory()){
                cleanDirectory(file);
            }
        }catch (final Exception ignored){}

        try{
            return file.delete();
        }catch (final Exception ignored){
            return false;
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory
     *            directory to clean
     * @throws IOException
     *             in case cleaning is unsuccessful
     * @throws IllegalArgumentException
     *             if {@code directory} does not exist or is not a directory
     */
    public static void cleanDirectory(final File directory) throws IOException{
        final File[] files = verifiedListFiles(directory);

        IOException exception = null;
        for (final File file : files){
            try{
                forceDelete(file);
            }catch (final IOException ioe){
                exception = ioe;
            }
        }

        if (null != exception){
            throw exception;
        }
    }

    /**
     * Lists files in a directory, asserting that the supplied directory satisfies exists and is a directory
     * 
     * @param directory
     *            The directory to list
     * @return The files in the directory, never null.
     * @throws IOException
     *             if an I/O error occurs
     */
    private static File[] verifiedListFiles(final File directory) throws IOException{
        if (!directory.exists()){
            final String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()){
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        final File[] files = directory.listFiles();
        if (files == null){ // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }
        return files;
    }

    //-----------------------------------------------------------------------

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file
     *            the file to write
     * @param data
     *            the content to write to the file
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @param append
     *            if {@code true}, then the String will be added to the
     *            end of the file rather than overwriting
     * @throws IOException
     *             in case of an I/O error
     * @since 2.3
     */
    public static void writeStringToFile(final File file,final String data,final Charset encoding,final boolean append) throws IOException{
        try (OutputStream out = openOutputStream(file, append)){
            IOUtils.write(data, out, encoding);
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file
     *            the file to write
     * @param data
     *            the content to write to the file
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @param append
     *            if {@code true}, then the String will be added to the
     *            end of the file rather than overwriting
     * @throws IOException
     *             in case of an I/O error
     * @throws java.nio.charset.UnsupportedCharsetException
     *             thrown instead of {@link java.io
     *             .UnsupportedEncodingException} in version 2.2 if the encoding is not supported by the VM
     * @since 2.1
     */
    public static void writeStringToFile(final File file,final String data,final String encoding,final boolean append) throws IOException{
        writeStringToFile(file, data, Charsets.toCharset(encoding), append);
    }

    /**
     * Writes a CharSequence to a file creating the file if it does not exist.
     *
     * @param file
     *            the file to write
     * @param data
     *            the content to write to the file
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @throws IOException
     *             in case of an I/O error
     * @since 2.3
     */
    public static void write(final File file,final CharSequence data,final Charset encoding) throws IOException{
        write(file, data, encoding, false);
    }

    /**
     * Writes a CharSequence to a file creating the file if it does not exist.
     *
     * @param file
     *            the file to write
     * @param data
     *            the content to write to the file
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @param append
     *            if {@code true}, then the data will be added to the
     *            end of the file rather than overwriting
     * @throws IOException
     *             in case of an I/O error
     * @since 2.3
     */
    public static void write(final File file,final CharSequence data,final Charset encoding,final boolean append) throws IOException{
        final String str = data == null ? null : data.toString();
        writeStringToFile(file, str, encoding, append);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The specified character encoding and the default line ending will be used.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created
     * if they do not exist.
     *
     * @param file
     *            the file to write to
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @throws IOException
     *             in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *             if the encoding is not supported by the VM
     * @since 1.1
     */
    public static void writeLines(final File file,final String encoding,final Collection<?> lines) throws IOException{
        writeLines(file, encoding, lines, null, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line, optionally appending.
     * The specified character encoding and the default line ending will be used.
     *
     * @param file
     *            the file to write to
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @param append
     *            if {@code true}, then the lines will be added to the
     *            end of the file rather than overwriting
     * @throws IOException
     *             in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *             if the encoding is not supported by the VM
     * @since 2.1
     */
    public static void writeLines(final File file,final String encoding,final Collection<?> lines,final boolean append) throws IOException{
        writeLines(file, encoding, lines, null, append);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the default line ending will be used.
     *
     * @param file
     *            the file to write to
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @throws IOException
     *             in case of an I/O error
     * @since 1.3
     */
    public static void writeLines(final File file,final Collection<?> lines) throws IOException{
        writeLines(file, null, lines, null, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the default line ending will be used.
     *
     * @param file
     *            the file to write to
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @param append
     *            if {@code true}, then the lines will be added to the
     *            end of the file rather than overwriting
     * @throws IOException
     *             in case of an I/O error
     * @since 2.1
     */
    public static void writeLines(final File file,final Collection<?> lines,final boolean append) throws IOException{
        writeLines(file, null, lines, null, append);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The specified character encoding and the line ending will be used.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created
     * if they do not exist.
     *
     * @param file
     *            the file to write to
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *            the line separator to use, {@code null} is system default
     * @throws IOException
     *             in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *             if the encoding is not supported by the VM
     * @since 1.1
     */
    public static void writeLines(final File file,final String encoding,final Collection<?> lines,final String lineEnding)
                    throws IOException{
        writeLines(file, encoding, lines, lineEnding, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The specified character encoding and the line ending will be used.
     *
     * @param file
     *            the file to write to
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *            the line separator to use, {@code null} is system default
     * @param append
     *            if {@code true}, then the lines will be added to the
     *            end of the file rather than overwriting
     * @throws IOException
     *             in case of an I/O error
     * @throws java.io.UnsupportedEncodingException
     *             if the encoding is not supported by the VM
     * @since 2.1
     */
    public static void writeLines(
                    final File file,
                    final String encoding,
                    final Collection<?> lines,
                    final String lineEnding,
                    final boolean append) throws IOException{
        try (OutputStream out = new BufferedOutputStream(openOutputStream(file, append))){
            IOUtils.writeLines(lines, lineEnding, out, encoding);
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the specified line ending will be used.
     *
     * @param file
     *            the file to write to
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *            the line separator to use, {@code null} is system default
     * @throws IOException
     *             in case of an I/O error
     * @since 1.3
     */
    public static void writeLines(final File file,final Collection<?> lines,final String lineEnding) throws IOException{
        writeLines(file, null, lines, lineEnding, false);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * the specified <code>File</code> line by line.
     * The default VM encoding and the specified line ending will be used.
     *
     * @param file
     *            the file to write to
     * @param lines
     *            the lines to write, {@code null} entries produce blank lines
     * @param lineEnding
     *            the line separator to use, {@code null} is system default
     * @param append
     *            if {@code true}, then the lines will be added to the
     *            end of the file rather than overwriting
     * @throws IOException
     *             in case of an I/O error
     * @since 2.1
     */
    public static void writeLines(final File file,final Collection<?> lines,final String lineEnding,final boolean append)
                    throws IOException{
        writeLines(file, null, lines, lineEnding, append);
    }

    //-----------------------------------------------------------------------
    /**
     * Deletes a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     * (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file
     *            file or directory to delete, must not be {@code null}
     * @throws NullPointerException
     *             if the directory is {@code null}
     * @throws FileNotFoundException
     *             if the file was not found
     * @throws IOException
     *             in case deletion is unsuccessful
     */
    public static void forceDelete(final File file) throws IOException{
        if (file.isDirectory()){
            deleteDirectory(file);
        }else{
            final boolean filePresent = file.exists();
            if (!file.delete()){
                if (!filePresent){
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                final String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories. If a file already exists with specified name but it is
     * not a directory then an IOException is thrown.
     * If the directory cannot be created (or does not already exist)
     * then an IOException is thrown.
     *
     * @param directory
     *            directory to create, must not be {@code null}
     * @throws NullPointerException
     *             if the directory is {@code null}
     * @throws IOException
     *             if the directory cannot be created or the file already exists but is not a directory
     */
    public static void forceMkdir(final File directory) throws IOException{
        if (directory.exists()){
            if (!directory.isDirectory()){
                final String message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        }else{
            if (!directory.mkdirs()){
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory()){
                    final String message = "Unable to create directory " + directory;
                    throw new IOException(message);
                }
            }
        }
    }

    /**
     * Makes any necessary but nonexistent parent directories for a given File. If the parent directory cannot be
     * created then an IOException is thrown.
     *
     * @param file
     *            file with parent to create, must not be {@code null}
     * @throws NullPointerException
     *             if the file is {@code null}
     * @throws IOException
     *             if the parent directory cannot be created
     * @since 2.5
     */
    public static void forceMkdirParent(final File file) throws IOException{
        final File parent = file.getParentFile();
        if (parent == null){
            return;
        }
        forceMkdir(parent);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the size of the specified file or directory. If the provided
     * {@link File} is a regular file, then the file's length is returned.
     * If the argument is a directory, then the size of the directory is
     * calculated recursively. If a directory or subdirectory is security
     * restricted, its size will not be included.
     * <p>
     * Note that overflow is not detected, and the return value may be negative if
     * overflow occurs. See {@link #sizeOfAsBigInteger(File)} for an alternative
     * method that does not overflow.
     *
     * @param file
     *            the regular file or directory to return the size
     *            of (must not be {@code null}).
     *
     * @return the length of the file, or recursive size of the directory,
     *         provided (in bytes).
     *
     * @throws NullPointerException
     *             if the file is {@code null}
     * @throws IllegalArgumentException
     *             if the file does not exist.
     *
     * @since 2.0
     */
    public static long sizeOf(final File file){

        if (!file.exists()){
            final String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (file.isDirectory()){
            return sizeOfDirectory0(file); // private method; expects directory
        }
        return file.length();

    }

    /**
     * Counts the size of a directory recursively (sum of the length of all files).
     * <p>
     * Note that overflow is not detected, and the return value may be negative if
     * overflow occurs. See {@link #sizeOfDirectoryAsBigInteger(File)} for an alternative
     * method that does not overflow.
     *
     * @param directory
     *            directory to inspect, must not be {@code null}
     * @return size of directory in bytes, 0 if directory is security restricted, a negative number when the real total
     *         is greater than {@link Long#MAX_VALUE}.
     * @throws NullPointerException
     *             if the directory is {@code null}
     */
    public static long sizeOfDirectory(final File directory){
        checkDirectory(directory);
        return sizeOfDirectory0(directory);
    }

    // Private method, must be invoked will a directory parameter

    /**
     * the size of a director
     * 
     * @param directory
     *            the directory to check
     * @return the size
     */
    private static long sizeOfDirectory0(final File directory){
        final File[] files = directory.listFiles();
        if (files == null){ // null if security restricted
            return 0L;
        }
        long size = 0;

        for (final File file : files){
            try{
                if (!isSymlink(file)){
                    size += sizeOf0(file); // internal method
                    if (size < 0){
                        break;
                    }
                }
            }catch (final IOException ioe){
                // Ignore exceptions caught when asking if a File is a symlink.
            }
        }

        return size;
    }

    // Internal method - does not check existence

    /**
     * the size of a file
     * 
     * @param file
     *            the file to check
     * @return the size of the file
     */
    private static long sizeOf0(final File file){
        if (file.isDirectory()){
            return sizeOfDirectory0(file);
        }
        return file.length(); // will be 0 if file does not exist
    }

    // Must be called with a directory

    /**
     * Finds the size of a directory
     *
     * @param directory
     *            The directory
     * @return the size
     */
    private static BigInteger sizeOfDirectoryBig0(final File directory){
        final File[] files = directory.listFiles();
        if (files == null){ // null if security restricted
            return BigInteger.ZERO;
        }
        BigInteger size = BigInteger.ZERO;

        for (final File file : files){
            try{
                if (!isSymlink(file)){
                    size = size.add(sizeOfBig0(file));
                }
            }catch (final IOException ioe){
                // Ignore exceptions caught when asking if a File is a symlink.
            }
        }

        return size;
    }

    // internal method; if file does not exist will return 0

    /**
     * Returns the size of a file
     * 
     * @param fileOrDir
     *            The file
     * @return the size
     */
    private static BigInteger sizeOfBig0(final File fileOrDir){
        if (fileOrDir.isDirectory()){
            return sizeOfDirectoryBig0(fileOrDir);
        }
        return BigInteger.valueOf(fileOrDir.length());
    }

    /**
     * Checks that the given {@code File} exists and is a directory.
     *
     * @param directory
     *            The {@code File} to check.
     * @throws IllegalArgumentException
     *             if the given {@code File} does not exist or is not a directory.
     */
    private static void checkDirectory(final File directory){
        if (!directory.exists()){
            throw new IllegalArgumentException(directory + " does not exist");
        }
        if (!directory.isDirectory()){
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    /**
     * Determines whether the specified file is a Symbolic Link rather than an actual file.
     * <p>
     * Will not return true if there is a Symbolic Link anywhere in the path,
     * only if the specific file is.
     * <p>
     * When using jdk1.7, this method delegates to {@code boolean java.nio.file.Files.isSymbolicLink(Path path)}
     *
     * <b>Note:</b> the current implementation always returns {@code false} if running on
     * jkd1.6 and the system is detected as Windows using {@link FilenameUtils#isSystemWindows()}
     * <p>
     * For code that runs on Java 1.7 or later, use the following method instead:
     * <br>
     * {@code boolean java.nio.file.Files.isSymbolicLink(Path path)}
     * 
     * @param file
     *            the file to check
     * @return true if the file is a Symbolic Link
     * @throws IOException
     *             if an IO error occurs while checking the file
     * @since 2.0
     */
    public static boolean isSymlink(final File file) throws IOException{
        if (file == null){
            throw new NullPointerException("File must not be null");
        }
        return Files.isSymbolicLink(file.toPath());
    }
}
