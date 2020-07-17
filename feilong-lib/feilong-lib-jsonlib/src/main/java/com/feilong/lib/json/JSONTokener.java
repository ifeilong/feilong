/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.lib.json;

import static com.feilong.core.lang.StringUtil.EMPTY;

import com.feilong.lib.lang3.math.NumberUtils;

/**
 * A JSONTokener takes a source string and extracts characters and tokens from it.
 * 
 * <p>
 * It is used by the JSONObject and JSONArray constructors to parse JSON source strings.
 * </p>
 *
 * @author JSON.org
 * @version 4
 */
public class JSONTokener{

    /**
     * The index of the next character.
     */
    private int          myIndex;

    /**
     * The source string being tokenized.
     */
    private final String sourceJson;

    //---------------------------------------------------------------

    /**
     * Construct a JSONTokener from a string.
     *
     * @param json
     *            A source string.
     */
    public JSONTokener(String json){
        this.myIndex = 0;

        this.sourceJson = json != null ? json.trim() : EMPTY;
        if (sourceJson.length() > 0){
            char first = sourceJson.charAt(0);
            char last = sourceJson.charAt(sourceJson.length() - 1);

            if (first == '[' && last != ']'){
                throw syntaxError("Found start '[' ,but miss ']' at the end.");
            }
            if (first == '{' && last != '}'){
                throw syntaxError("Found start '{' ,but miss '}' at the end.");
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Back up one character. This provides a sort of lookahead capability, so
     * that you can test for a digit or letter before attempting to parse the
     * next number or identifier.
     */
    public void back(){
        if (this.myIndex > 0){
            this.myIndex -= 1;
        }
    }

    /**
     * Get the next character in the source string.
     *
     * @return The next character, or 0 if past the end of the source string.
     */
    public char next(){
        if (more()){
            char c = this.sourceJson.charAt(this.myIndex);
            this.myIndex += 1;
            return c;
        }
        return 0;
    }

    /**
     * Get the next n characters.
     *
     * @param n
     *            The number of characters to take.
     * @return A string of n characters.
     * @throws JSONException
     *             Substring bounds error if there are not n characters remaining in the source string.
     */
    public String next(int n){
        int i = this.myIndex;
        int j = i + n;
        if (j >= this.sourceJson.length()){
            throw syntaxError("Substring bounds error");
        }
        this.myIndex += n;
        return this.sourceJson.substring(i, j);
    }

    /**
     * Get the next char in the string, skipping whitespace and comments (slashslash, slashstar, and hash).
     *
     * @return A character, or 0 if there are no more characters.
     */
    public char nextClean(){
        for (;;){
            char c = next();

            if (c == '/'){
                switch (next()) {
                    case '/':
                        do{
                            c = next();
                        }while (c != '\n' && c != '\r' && c != 0);

                        break;

                    case '*':
                        for (;;){
                            c = next();
                            if (c == 0){
                                throw syntaxError("Unclosed comment.");
                            }
                            if (c == '*'){
                                if (next() == '/'){
                                    break;
                                }
                                back();
                            }
                        }

                        break;

                    default:
                        back();

                        return '/';
                }

            }else if (c == '#'){
                do{
                    c = next();
                }while (c != '\n' && c != '\r' && c != 0);
            }else if (c == 0 || c > ' '){
                return c;
            }
        }
    }

    /**
     * Return the characters up to the next close quote character.
     * 
     * Backslash processing is done.
     * 
     * The formal JSON format does not allow strings in single quotes, but an implementation is allowed to accept them.
     *
     * @param quote
     *            The quoting character, either <code>"</code>&nbsp;<small>(double quote)</small> or <code>'</code>&nbsp;<small>(single
     *            quote)</small>.
     * @return A String.
     */
    private String nextString(char quote){
        char c;
        StringBuffer sb = new StringBuffer();
        for (;;){
            c = next();
            switch (c) {
                case 0:
                case '\n':
                case '\r':
                    throw syntaxError("Unterminated string");
                case '\\':
                    c = next();
                    switch (c) {
                        case 'b':
                            sb.append('\b');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 'u':
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        case 'x':
                            sb.append((char) Integer.parseInt(next(2), 16));
                            break;
                        default:
                            sb.append(c);
                    }
                    break;
                default:
                    if (c == quote){
                        return sb.toString();
                    }
                    sb.append(c);
            }
        }
    }

    /**
     * Get the next value.
     * 
     * <p>
     * The value can be a Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONObject.NULL
     * object.
     * </p>
     * 
     * @param jsonConfig
     *
     * @return An object.
     */
    public Object nextValue(JsonConfig jsonConfig){
        char c = nextClean();
        String s;

        switch (c) {
            case '"':
            case '\'':
                return nextString(c);
            case '{':
                back();
                return JSONTokenerParser.toJSONObject(this, jsonConfig);
            case '[':
                back();
                return JSONTokenerParser.toJSONArray(this, jsonConfig);
            default:
                // empty
        }

        //---------------------------------------------------------------

        //Handle unquoted text. This could be the values true, false, or null, or it can be a number. An implementation (such as this one) is allowed to also accept non-standard forms. Accumulate characters until we reach the end of the text or a formatting character.
        StringBuffer sb = new StringBuffer();
        char b = c;
        while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0){
            sb.append(c);
            c = next();
        }
        back();

        //---------------------------------------------------------------

        //If it is true, false, or null, return the proper value.
        s = sb.toString().trim();
        if (s.equals("")){
            throw syntaxError("Missing value.");
        }
        if (s.equalsIgnoreCase("true")){
            return Boolean.TRUE;
        }
        if (s.equalsIgnoreCase("false")){
            return Boolean.FALSE;
        }
        if (s.equals("null")){
            return JSONNull.getInstance();
        }

        //---------------------------------------------------------------

        //If it might be a number, try converting it. We support the 0- and 0x-conventions. If a number cannot be produced, then the value will just be a string. Note that the 0-, 0x-, plus, and implied string conventions are non-standard. A JSON parser is free to accept non-JSON forms as long as it accepts all correct JSON forms.
        if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+'){
            if (b == '0'){
                if (s.length() > 2 && (s.charAt(1) == 'x' || s.charAt(1) == 'X')){
                    try{
                        return Integer.parseInt(s.substring(2), 16);
                    }catch (Exception e){
                        /* Ignore the error */
                    }
                }else{
                    try{
                        return Integer.parseInt(s, 8);
                    }catch (Exception e){
                        /* Ignore the error */
                    }
                }
            }

            try{
                return NumberUtils.createNumber(s);
            }catch (Exception e){
                return s;
            }
        }

        //---------------------------------------------------------------
        switch (peek()) {
            case ',':
            case '}':
            case '{':
            case '[':
            case ']':
                throw new JSONException("Unquotted string '" + s + "'");
            default:
                break;
        }

        //---------------------------------------------------------------

        return s;
    }

    /**
     * Look at the next character in the source string.
     *
     * @return The next character, or 0 if past the end of the source string.
     */
    public char peek(){
        if (more()){
            char c = this.sourceJson.charAt(this.myIndex);
            return c;
        }
        return 0;
    }

    public void reset(){
        this.myIndex = 0;
    }

    /**
     * Determine if the source string still contains characters that next() can
     * consume.
     *
     * @return true if not yet at the end of the source.
     */
    private boolean more(){
        return this.myIndex < this.sourceJson.length();
    }

    //---------------------------------------------------------------

    /**
     * Make a JSONException to signal a syntax error.
     *
     * @param message
     *            The error message.
     * @return A JSONException object, suitable for throwing
     */
    public JSONException syntaxError(String message){
        return new JSONException(message + toString());
    }

    /**
     * Make a printable string of this JSONTokener.
     *
     * @return " at character [this.myIndex] of [this.mySource]"
     */
    @Override
    public String toString(){
        return " at character " + this.myIndex + " of " + this.sourceJson;
    }
}
