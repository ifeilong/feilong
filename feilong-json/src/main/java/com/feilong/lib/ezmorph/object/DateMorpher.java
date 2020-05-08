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

package com.feilong.lib.ezmorph.object;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.feilong.lib.ezmorph.MorphException;

/**
 * Morphs a String to a Date.<br>
 * <p>
 * This morpher will iterate through the supplied formats until one succeeds or
 * the default value is returned (if default value is configured).
 * </p>
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class DateMorpher extends AbstractObjectMorpher<Date>{

    /** The formats. */
    private final String[] formats;

    /** The lenient. */
    private final boolean  lenient;

    /** The locale. */
    private final Locale   locale;

    //---------------------------------------------------------------

    /**
     * Instantiates a new date morpher.
     *
     * @param formats
     *            a list of formats this morpher supports.
     */
    public DateMorpher(String...formats){
        this(formats, Locale.getDefault(), false);
    }

    /**
     * Instantiates a new date morpher.
     *
     * @param formats
     *            a list of formats this morpher supports.
     * @param locale
     *            the Locale used to parse each format.
     * @param lenient
     *            if the parsing should be lenient or not.
     */
    public DateMorpher(String[] formats, Locale locale, boolean lenient){
        if (formats == null || formats.length == 0){
            throw new MorphException("invalid array of formats");
        }
        // should use defensive copying ?
        this.formats = formats;
        this.locale = defaultIfNull(locale, Locale.getDefault());
        this.lenient = lenient;
    }

    //---------------------------------------------------------------

    /**
     * Morph.
     *
     * @param value
     *            the value
     * @return the object
     */
    @Override
    public Object morph(Object value){
        if (value == null){
            return null;
        }

        if (Date.class.isAssignableFrom(value.getClass())){
            return value;
        }

        if (!supports(value.getClass())){
            throw new MorphException(value.getClass() + " is not supported");
        }

        //---------------------------------------------------------------

        String strValue = (String) value;
        SimpleDateFormat dateParser = null;

        for (int i = 0; i < formats.length; i++){
            if (dateParser == null){
                dateParser = new SimpleDateFormat(formats[i], locale);
            }else{
                dateParser.applyPattern(formats[i]);
            }
            dateParser.setLenient(lenient);
            try{
                return dateParser.parse(strValue.toLowerCase());
            }catch (ParseException pe){
                // ignore exception, try the next format
            }
        }

        // unable to parse the date
        if (isUseDefault()){
            return defaultValue;
        }
        throw new MorphException("Unable to parse the date " + value);
    }

    //---------------------------------------------------------------

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return Date.class;
    }

    //---------------------------------------------------------------

    /**
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class<?> clazz){
        return String.class.isAssignableFrom(clazz);
    }
}