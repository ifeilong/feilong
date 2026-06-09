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
package com.feilong.lib.lang3.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Substitutes variables within a string by values.
 * <p>
 * This class takes a piece of text and substitutes all the variables within it.
 * The default definition of a variable is {@code ${variableName}}.
 * The prefix and suffix can be changed via constructors and set methods.
 * <p>
 * Variable values are typically resolved from a map, but could also be resolved
 * from system properties, or by supplying a custom variable resolver.
 * <p>
 * The simplest example is to use this class to replace Java System properties. For example:
 * 
 * <pre>
 * StrSubstitutor.replaceSystemProperties("You are running with java.version = ${java.version} and os.name = ${os.name}.");
 * </pre>
 * <p>
 * Typical usage of this class follows the following pattern: First an instance is created
 * and initialized with the map that contains the values for the available variables.
 * If a prefix and/or suffix for variables should be used other than the default ones,
 * the appropriate settings can be performed. After that the {@code replace()}
 * method can be called passing in the source text for interpolation. In the returned
 * text all variable references (as long as their values are known) will be resolved.
 * The following example demonstrates this:
 * 
 * <pre>
 * Map valuesMap = HashMap();
 * valuesMap.put(&quot;animal&quot;, &quot;quick brown fox&quot;);
 * valuesMap.put(&quot;target&quot;, &quot;lazy dog&quot;);
 * String templateString = &quot;The ${animal} jumps over the ${target}.&quot;;
 * StrSubstitutor sub = new StrSubstitutor(valuesMap);
 * String resolvedString = sub.replace(templateString);
 * </pre>
 * 
 * yielding:
 * 
 * <pre>
 *      The quick brown fox jumps over the lazy dog.
 * </pre>
 * <p>
 * Also, this class allows to set a default value for unresolved variables.
 * The default value for a variable can be appended to the variable name after the variable
 * default value delimiter. The default value of the variable default value delimiter is ':-',
 * as in bash and other *nix shells, as those are arguably where the default ${} delimiter set originated.
 * The variable default value delimiter can be manually set by calling {@link #setValueDelimiterMatcher(StrMatcher)},
 * {@link #setValueDelimiter(char)} or {@link #setValueDelimiter(String)}.
 * The following shows an example with variable default value settings:
 * 
 * <pre>
 * Map valuesMap = HashMap();
 * valuesMap.put(&quot;animal&quot;, &quot;quick brown fox&quot;);
 * valuesMap.put(&quot;target&quot;, &quot;lazy dog&quot;);
 * String templateString = &quot;The ${animal} jumps over the ${target}. ${undefined.number:-1234567890}.&quot;;
 * StrSubstitutor sub = new StrSubstitutor(valuesMap);
 * String resolvedString = sub.replace(templateString);
 * </pre>
 * 
 * yielding:
 * 
 * <pre>
 *      The quick brown fox jumps over the lazy dog. 1234567890.
 * </pre>
 * <p>
 * In addition to this usage pattern there are some static convenience methods that
 * cover the most common use cases. These methods can be used without the need of
 * manually creating an instance. However if multiple replace operations are to be
 * performed, creating and reusing an instance of this class will be more efficient.
 * <p>
 * Variable replacement works in a recursive way. Thus, if a variable value contains
 * a variable then that variable will also be replaced. Cyclic replacements are
 * detected and will cause an exception to be thrown.
 * <p>
 * Sometimes the interpolation's result must contain a variable prefix. As an example
 * take the following source text:
 * 
 * <pre>
 *   The variable ${${name}} must be used.
 * </pre>
 * 
 * Here only the variable's name referred to in the text should be replaced resulting
 * in the text (assuming that the value of the {@code name} variable is {@code x}):
 * 
 * <pre>
 *   The variable ${x} must be used.
 * </pre>
 * 
 * To achieve this effect there are two possibilities: Either set a different prefix
 * and suffix for variables which do not conflict with the result text you want to
 * produce. The other possibility is to use the escape character, by default '$'.
 * If this character is placed before a variable reference, this reference is ignored
 * and won't be replaced. For example:
 * 
 * <pre>
 *   The variable $${${name}} must be used.
 * </pre>
 * <p>
 * In some complex scenarios you might even want to perform substitution in the
 * names of variables, for instance
 * 
 * <pre>
 * ${jre-${java.specification.version}}
 * </pre>
 * 
 * {@code StrSubstitutor} supports this recursive substitution in variable
 * names, but it has to be enabled explicitly by setting the
 * {@link #setEnableSubstitutionInVariables(boolean) enableSubstitutionInVariables}
 * property to <b>true</b>.
 * <p>
 * This class is <b>not</b> thread safe.
 * </p>
 *
 * @since 2.2
 * @deprecated as of 3.6, use commons-text
 *             <a href="https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/StringSubstitutor.html">
 *             StringSubstitutor</a> instead
 */
@Deprecated
public class StrSubstitutor{

    /**
     * Constant for the default escape character.
     */
    public static final char       DEFAULT_ESCAPE          = '$';

    /**
     * Constant for the default variable prefix.
     */
    public static final StrMatcher DEFAULT_PREFIX          = StrMatcher.stringMatcher("${");

    /**
     * Constant for the default variable suffix.
     */
    public static final StrMatcher DEFAULT_SUFFIX          = StrMatcher.stringMatcher("}");

    /**
     * Constant for the default value delimiter of a variable.
     * 
     * @since 3.2
     */
    public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");

    /**
     * Stores the escape character.
     */
    private char                   escapeChar;

    /**
     * Stores the variable prefix.
     */
    private StrMatcher             prefixMatcher;

    /**
     * Stores the variable suffix.
     */
    private StrMatcher             suffixMatcher;

    /**
     * Stores the default variable value delimiter
     */
    private StrMatcher             valueDelimiterMatcher;

    /**
     * Variable resolution is delegated to an implementor of VariableResolver.
     */
    private StrLookup<?>           variableResolver;

    /**
     * The flag whether substitution in variable names is enabled.
     */
    private boolean                enableSubstitutionInVariables;

    /**
     * Whether escapes should be preserved. Default is false;
     */
    private final boolean                preserveEscapes         = false;

    //-----------------------------------------------------------------------
    /**
     * Replaces all the occurrences of variables in the given source object with
     * their matching values from the map.
     *
     * @param <V>
     *            the type of the values in the map
     * @param source
     *            the source text containing the variables to substitute, null returns null
     * @param valueMap
     *            the map with the values, may be null
     * @return the result of the replace operation
     */
    public static <V> String replace(final Object source,final Map<String, V> valueMap){
        return new StrSubstitutor(valueMap).replace(source);
    }

    /**
     * Creates a new instance and initializes it. Uses defaults for variable
     * prefix and suffix and the escaping character.
     *
     * @param <V>
     *            the type of the values in the map
     * @param valueMap
     *            the map with the variables' values, may be null
     */
    public <V> StrSubstitutor(final Map<String, V> valueMap){
        this(StrLookup.mapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver
     *            the variable resolver, may be null
     * @param prefixMatcher
     *            the prefix for variables, not null
     * @param suffixMatcher
     *            the suffix for variables, not null
     * @param escape
     *            the escape character
     * @throws IllegalArgumentException
     *             if the prefix or suffix is null
     */
    public StrSubstitutor(final StrLookup<?> variableResolver, final StrMatcher prefixMatcher, final StrMatcher suffixMatcher,
                    final char escape){
        this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver
     *            the variable resolver, may be null
     * @param prefixMatcher
     *            the prefix for variables, not null
     * @param suffixMatcher
     *            the suffix for variables, not null
     * @param escape
     *            the escape character
     * @param valueDelimiterMatcher
     *            the variable default value delimiter matcher, may be null
     * @throws IllegalArgumentException
     *             if the prefix or suffix is null
     * @since 3.2
     */
    public StrSubstitutor(final StrLookup<?> variableResolver, final StrMatcher prefixMatcher, final StrMatcher suffixMatcher,
                    final char escape, final StrMatcher valueDelimiterMatcher){
        this.setVariableResolver(variableResolver);
        this.setVariablePrefixMatcher(prefixMatcher);
        this.setVariableSuffixMatcher(suffixMatcher);
        this.setEscapeChar(escape);
        this.setValueDelimiterMatcher(valueDelimiterMatcher);
    }

    //-----------------------------------------------------------------------
    /**
     * Replaces all the occurrences of variables in the given source object with
     * their matching values from the resolver. The input source object is
     * converted to a string using {@code toString} and is not altered.
     *
     * @param source
     *            the source to replace in, null returns null
     * @return the result of the replace operation
     */
    public String replace(final Object source){
        if (source == null){
            return null;
        }
        final StrBuilder buf = new StrBuilder().append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    /**
     * Internal method that substitutes the variables.
     * <p>
     * Most users of this class do not need to call this method. This method will
     * be called automatically by another (public) method.
     * <p>
     * Writers of subclasses can override this method if they need access to
     * the substitution process at the start or end.
     *
     * @param buf
     *            the string builder to substitute into, not null
     * @param offset
     *            the start offset within the builder, must be valid
     * @param length
     *            the length within the builder to be processed, must be valid
     * @return true if altered
     */
    protected boolean substitute(final StrBuilder buf,final int offset,final int length){
        return substitute(buf, offset, length, null) > 0;
    }

    /**
     * Recursive handler for multiple levels of interpolation. This is the main
     * interpolation method, which resolves the values of all variable references
     * contained in the passed in text.
     *
     * @param buf
     *            the string builder to substitute into, not null
     * @param offset
     *            the start offset within the builder, must be valid
     * @param length
     *            the length within the builder to be processed, must be valid
     * @param priorVariables
     *            the stack keeping track of the replaced variables, may be null
     * @return the length change that occurs, unless priorVariables is null when the int
     *         represents a boolean flag as to whether any change occurred.
     */
    private int substitute(final StrBuilder buf,final int offset,final int length,List<String> priorVariables){
        final StrMatcher pfxMatcher = getVariablePrefixMatcher();
        final StrMatcher suffMatcher = getVariableSuffixMatcher();
        final char escape = getEscapeChar();
        final StrMatcher valueDelimMatcher = getValueDelimiterMatcher();
        final boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();

        final boolean top = priorVariables == null;
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = buf.buffer;
        int bufEnd = offset + length;
        int pos = offset;
        while (pos < bufEnd){
            final int startMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
            if (startMatchLen == 0){
                pos++;
            }else{
                // found variable start marker
                if (pos > offset && chars[pos - 1] == escape){
                    // escaped
                    if (preserveEscapes){
                        pos++;
                        continue;
                    }
                    buf.deleteCharAt(pos - 1);
                    chars = buf.buffer; // in case buffer was altered
                    lengthChange--;
                    altered = true;
                    bufEnd--;
                }else{
                    // find suffix
                    final int startPos = pos;
                    pos += startMatchLen;
                    int endMatchLen = 0;
                    int nestedVarCount = 0;
                    while (pos < bufEnd){
                        if (substitutionInVariablesEnabled && (endMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd)) != 0){
                            // found a nested variable start
                            nestedVarCount++;
                            pos += endMatchLen;
                            continue;
                        }

                        endMatchLen = suffMatcher.isMatch(chars, pos, offset, bufEnd);
                        if (endMatchLen == 0){
                            pos++;
                        }else{
                            // found variable end marker
                            if (nestedVarCount == 0){
                                String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
                                if (substitutionInVariablesEnabled){
                                    final StrBuilder bufName = new StrBuilder(varNameExpr);
                                    substitute(bufName, 0, bufName.length());
                                    varNameExpr = bufName.toString();
                                }
                                pos += endMatchLen;
                                final int endPos = pos;

                                String varName = varNameExpr;
                                String varDefaultValue = null;

                                if (valueDelimMatcher != null){
                                    final char[] varNameExprChars = varNameExpr.toCharArray();
                                    int valueDelimiterMatchLen = 0;
                                    for (int i = 0; i < varNameExprChars.length; i++){
                                        // if there's any nested variable when nested variable substitution disabled, then stop resolving name and default value.
                                        if (!substitutionInVariablesEnabled
                                                        && pfxMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0){
                                            break;
                                        }
                                        if ((valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i)) != 0){
                                            varName = varNameExpr.substring(0, i);
                                            varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
                                            break;
                                        }
                                    }
                                }

                                // on the first call initialize priorVariables
                                if (priorVariables == null){
                                    priorVariables = new ArrayList<>();
                                    priorVariables.add(new String(chars, offset, length));
                                }

                                // handle cyclic substitution
                                checkCyclicSubstitution(varName, priorVariables);
                                priorVariables.add(varName);

                                // resolve the variable
                                String varValue = resolveVariable(varName, buf, startPos, endPos);
                                if (varValue == null){
                                    varValue = varDefaultValue;
                                }
                                if (varValue != null){
                                    // recursive replace
                                    final int varLen = varValue.length();
                                    buf.replace(startPos, endPos, varValue);
                                    altered = true;
                                    int change = substitute(buf, startPos, varLen, priorVariables);
                                    change = change + varLen - (endPos - startPos);
                                    pos += change;
                                    bufEnd += change;
                                    lengthChange += change;
                                    chars = buf.buffer; // in case buffer was
                                                        // altered
                                }

                                // remove variable from the cyclic stack
                                priorVariables.remove(priorVariables.size() - 1);
                                break;
                            }
                            nestedVarCount--;
                            pos += endMatchLen;
                        }
                    }
                }
            }
        }
        if (top){
            return altered ? 1 : 0;
        }
        return lengthChange;
    }

    /**
     * Checks if the specified variable is already in the stack (list) of variables.
     *
     * @param varName
     *            the variable name to check
     * @param priorVariables
     *            the list of prior variables
     */
    private static void checkCyclicSubstitution(final String varName,final List<String> priorVariables){
        if (priorVariables.contains(varName) == false){
            return;
        }
        final StrBuilder buf = new StrBuilder(256);
        buf.append("Infinite loop in property interpolation of ");
        buf.append(priorVariables.remove(0));
        buf.append(": ");
        buf.appendWithSeparators(priorVariables, "->");
        throw new IllegalStateException(buf.toString());
    }

    /**
     * Internal method that resolves the value of a variable.
     * <p>
     * Most users of this class do not need to call this method. This method is
     * called automatically by the substitution process.
     * <p>
     * Writers of subclasses can override this method if they need to alter
     * how each substitution occurs. The method is passed the variable's name
     * and must return the corresponding value. This implementation uses the
     * {@link #getVariableResolver()} with the variable's name as the key.
     *
     * @param variableName
     *            the name of the variable, not null
     * @param buf
     *            the buffer where the substitution is occurring, not null
     * @param startPos
     *            the start position of the variable including the prefix, valid
     * @param endPos
     *            the end position of the variable including the suffix, valid
     * @return the variable's value or <b>null</b> if the variable is unknown
     */
    protected String resolveVariable(final String variableName,final StrBuilder buf,final int startPos,final int endPos){
        final StrLookup<?> resolver = getVariableResolver();
        if (resolver == null){
            return null;
        }
        return resolver.lookup(variableName);
    }

    // Escape
    //-----------------------------------------------------------------------
    /**
     * Returns the escape character.
     *
     * @return the character used for escaping variable references
     */
    public char getEscapeChar(){
        return this.escapeChar;
    }

    /**
     * Sets the escape character.
     * If this character is placed before a variable reference in the source
     * text, this variable will be ignored.
     *
     * @param escapeCharacter
     *            the escape character (0 for disabling escaping)
     */
    public void setEscapeChar(final char escapeCharacter){
        this.escapeChar = escapeCharacter;
    }

    // Prefix
    //-----------------------------------------------------------------------
    /**
     * Gets the variable prefix matcher currently in use.
     * <p>
     * The variable prefix is the character or characters that identify the
     * start of a variable. This prefix is expressed in terms of a matcher
     * allowing advanced prefix matches.
     *
     * @return the prefix matcher in use
     */
    public StrMatcher getVariablePrefixMatcher(){
        return prefixMatcher;
    }

    /**
     * Sets the variable prefix matcher currently in use.
     * <p>
     * The variable prefix is the character or characters that identify the
     * start of a variable. This prefix is expressed in terms of a matcher
     * allowing advanced prefix matches.
     *
     * @param prefixMatcher
     *            the prefix matcher to use, null ignored
     * @return this, to enable chaining
     * @throws IllegalArgumentException
     *             if the prefix matcher is null
     */
    public StrSubstitutor setVariablePrefixMatcher(final StrMatcher prefixMatcher){
        if (prefixMatcher == null){
            throw new IllegalArgumentException("Variable prefix matcher must not be null.");
        }
        this.prefixMatcher = prefixMatcher;
        return this;
    }

    // Suffix
    //-----------------------------------------------------------------------
    /**
     * Gets the variable suffix matcher currently in use.
     * <p>
     * The variable suffix is the character or characters that identify the
     * end of a variable. This suffix is expressed in terms of a matcher
     * allowing advanced suffix matches.
     *
     * @return the suffix matcher in use
     */
    public StrMatcher getVariableSuffixMatcher(){
        return suffixMatcher;
    }

    /**
     * Sets the variable suffix matcher currently in use.
     * <p>
     * The variable suffix is the character or characters that identify the
     * end of a variable. This suffix is expressed in terms of a matcher
     * allowing advanced suffix matches.
     *
     * @param suffixMatcher
     *            the suffix matcher to use, null ignored
     * @return this, to enable chaining
     * @throws IllegalArgumentException
     *             if the suffix matcher is null
     */
    public StrSubstitutor setVariableSuffixMatcher(final StrMatcher suffixMatcher){
        if (suffixMatcher == null){
            throw new IllegalArgumentException("Variable suffix matcher must not be null.");
        }
        this.suffixMatcher = suffixMatcher;
        return this;
    }

    // Variable Default Value Delimiter
    //-----------------------------------------------------------------------
    /**
     * Gets the variable default value delimiter matcher currently in use.
     * <p>
     * The variable default value delimiter is the character or characters that delimit the
     * variable name and the variable default value. This delimiter is expressed in terms of a matcher
     * allowing advanced variable default value delimiter matches.
     * <p>
     * If it returns null, then the variable default value resolution is disabled.
     *
     * @return the variable default value delimiter matcher in use, may be null
     * @since 3.2
     */
    public StrMatcher getValueDelimiterMatcher(){
        return valueDelimiterMatcher;
    }

    /**
     * Sets the variable default value delimiter matcher to use.
     * <p>
     * The variable default value delimiter is the character or characters that delimit the
     * variable name and the variable default value. This delimiter is expressed in terms of a matcher
     * allowing advanced variable default value delimiter matches.
     * <p>
     * If the {@code valueDelimiterMatcher} is null, then the variable default value resolution
     * becomes disabled.
     *
     * @param valueDelimiterMatcher
     *            variable default value delimiter matcher to use, may be null
     * @return this, to enable chaining
     * @since 3.2
     */
    public StrSubstitutor setValueDelimiterMatcher(final StrMatcher valueDelimiterMatcher){
        this.valueDelimiterMatcher = valueDelimiterMatcher;
        return this;
    }

    // Resolver
    //-----------------------------------------------------------------------
    /**
     * Gets the VariableResolver that is used to lookup variables.
     *
     * @return the VariableResolver
     */
    public StrLookup<?> getVariableResolver(){
        return this.variableResolver;
    }

    /**
     * Sets the VariableResolver that is used to lookup variables.
     *
     * @param variableResolver
     *            the VariableResolver
     */
    public void setVariableResolver(final StrLookup<?> variableResolver){
        this.variableResolver = variableResolver;
    }

    // Substitution support in variable names
    //-----------------------------------------------------------------------
    /**
     * Returns a flag whether substitution is done in variable names.
     *
     * @return the substitution in variable names flag
     * @since 3.0
     */
    public boolean isEnableSubstitutionInVariables(){
        return enableSubstitutionInVariables;
    }

    /**
     * Returns the flag controlling whether escapes are preserved during
     * substitution.
     *
     * @return the preserve escape flag
     * @since 3.5
     */
    public boolean isPreserveEscapes(){
        return preserveEscapes;
    }

}
