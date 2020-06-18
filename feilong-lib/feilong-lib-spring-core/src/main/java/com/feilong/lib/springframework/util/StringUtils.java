/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feilong.lib.springframework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Miscellaneous {@link String} utility methods.
 *
 * <p>
 * Mainly for internal use within the framework; consider
 * <a href="https://commons.apache.org/proper/commons-lang/">Apache's Commons Lang</a>
 * for a more comprehensive suite of {@code String} utilities.
 *
 * <p>
 * This class delivers some simple functionality that should really be
 * provided by the core Java {@link String} and {@link StringBuilder}
 * classes. It also provides easy-to-use methods to convert between
 * delimited strings, such as CSV strings, and collections and arrays.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Rick Evans
 * @author Arjen Poutsma
 * @author Sam Brannen
 * @author Brian Clozel
 * @since 16 April 2001
 */
public abstract class StringUtils{

    private static final String FOLDER_SEPARATOR         = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH                 = "..";

    private static final String CURRENT_PATH             = ".";

    //---------------------------------------------------------------------
    // General convenience methods for working with Strings
    //---------------------------------------------------------------------

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     * <p>
     * Note: this method returns {@code true} for a {@code String} that
     * purely consists of whitespace.
     * 
     * @param str
     *            the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null} and has length
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength(String str){
        return (str != null && !str.isEmpty());
    }

    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>
     * More specifically, this method returns {@code true} if the
     * {@code String} is not {@code null}, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     * 
     * @param str
     *            the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its
     *         length is greater than 0, and it does not contain whitespace only
     * @see #hasText(CharSequence)
     */
    public static boolean hasText(String str){
        return (hasLength(str) && containsText(str));
    }

    private static boolean containsText(CharSequence str){
        int strLen = str.length();
        for (int i = 0; i < strLen; i++){
            if (!Character.isWhitespace(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

    /**
     * Replace all occurrences of a substring within a string with another string.
     * 
     * @param inString
     *            {@code String} to examine
     * @param oldPattern
     *            {@code String} to replace
     * @param newPattern
     *            {@code String} to insert
     * @return a {@code String} with the replacements
     */
    public static String replace(String inString,String oldPattern,String newPattern){
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null){
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1){
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()){
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0; // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0){
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    /**
     * Delete any character in a given {@code String}.
     * 
     * @param inString
     *            the original {@code String}
     * @param charsToDelete
     *            a set of characters to delete.
     *            E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting {@code String}
     */
    public static String deleteAny(String inString,String charsToDelete){
        if (!hasLength(inString) || !hasLength(charsToDelete)){
            return inString;
        }

        StringBuilder sb = new StringBuilder(inString.length());
        for (int i = 0; i < inString.length(); i++){
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1){
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    //---------------------------------------------------------------------

    /**
     * Extract the filename from the given Java resource path,
     * e.g. {@code "mypath/myfile.txt" -> "myfile.txt"}.
     * 
     * @param path
     *            the file path (may be {@code null})
     * @return the extracted filename, or {@code null} if none
     */
    public static String getFilename(String path){
        if (path == null){
            return null;
        }

        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * Apply the given relative path to the given Java resource path,
     * assuming standard Java folder separation (i.e. "/" separators).
     * 
     * @param path
     *            the path to start from (usually a full file path)
     * @param relativePath
     *            the relative path to apply
     *            (relative to the full file path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path,String relativePath){
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1){
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)){
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        }
        return relativePath;
    }

    /**
     * Normalize the path by suppressing sequences like "path/.." and
     * inner simple dots.
     * <p>
     * The result is convenient for path comparison. For other uses,
     * notice that Windows separators ("\") are replaced by simple slashes.
     * 
     * @param path
     *            the original path
     * @return the normalized path
     */
    public static String cleanPath(String path){
        if (path == null){
            return null;
        }
        String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(':');
        String prefix = "";
        if (prefixIndex != -1){
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains(FOLDER_SEPARATOR)){
                prefix = "";
            }else{
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(FOLDER_SEPARATOR)){
            prefix = prefix + FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        List<String> pathElements = new LinkedList<>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--){
            String element = pathArray[i];
            if (CURRENT_PATH.equals(element)){
                // Points to current directory - drop it.
            }else if (TOP_PATH.equals(element)){
                // Registering top path found.
                tops++;
            }else{
                if (tops > 0){
                    // Merging path element with element corresponding to top path.
                    tops--;
                }else{
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++){
            pathElements.add(0, TOP_PATH);
        }

        return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    //---------------------------------------------------------------------

    /**
     * Copy the given {@link Collection} into a {@code String} array.
     * <p>
     * The {@code Collection} must contain {@code String} elements only.
     * 
     * @param collection
     *            the {@code Collection} to copy
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(Collection<String> collection){
        if (collection == null){
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a
     * {@code String} array.
     * <p>
     * A single {@code delimiter} may consist of more than one character,
     * but it will still be considered as a single delimiter string, rather
     * than as bunch of potential delimiter characters, in contrast to
     * {@link #tokenizeToStringArray}.
     * 
     * @param str
     *            the input {@code String}
     * @param delimiter
     *            the delimiter between elements (this is a single delimiter,
     *            rather than a bunch individual delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str,String delimiter){
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into
     * a {@code String} array.
     * <p>
     * A single {@code delimiter} may consist of more than one character,
     * but it will still be considered as a single delimiter string, rather
     * than as bunch of potential delimiter characters, in contrast to
     * {@link #tokenizeToStringArray}.
     * 
     * @param str
     *            the input {@code String}
     * @param delimiter
     *            the delimiter between elements (this is a single delimiter,
     *            rather than a bunch individual delimiter characters)
     * @param charsToDelete
     *            a set of characters to delete; useful for deleting unwanted
     *            line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a {@code String}
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str,String delimiter,String charsToDelete){
        if (str == null){
            return new String[0];
        }
        if (delimiter == null){
            return new String[] { str };
        }

        List<String> result = new ArrayList<>();
        if ("".equals(delimiter)){
            for (int i = 0; i < str.length(); i++){
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        }else{
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1){
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()){
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Convert a {@link Collection} to a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param coll
     *            the {@code Collection} to convert
     * @param delim
     *            the delimiter to use (typically a ",")
     * @param prefix
     *            the {@code String} to start each element with
     * @param suffix
     *            the {@code String} to end each element with
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString(Collection<?> coll,String delim,String prefix,String suffix){
        if (ObjectUtils.isEmpty(coll)){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()){
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()){
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param coll
     *            the {@code Collection} to convert
     * @param delim
     *            the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString(Collection<?> coll,String delim){
        return collectionToDelimitedString(coll, delim, "", "");
    }

}
