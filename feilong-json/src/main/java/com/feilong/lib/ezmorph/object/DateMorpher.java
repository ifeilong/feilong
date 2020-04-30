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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
public final class DateMorpher extends AbstractObjectMorpher{

    /** The default value. */
    private Date           defaultValue;

    /** The formats. */
    private final String[] formats;

    /** The lenient. */
    private final boolean  lenient;

    /** The locale. */
    private Locale         locale;

    //---------------------------------------------------------------

    /**
     * Instantiates a new date morpher.
     *
     * @param formats
     *            a list of formats this morpher supports.
     */
    public DateMorpher(String[] formats){
        this(formats, Locale.getDefault(), false);
    }

    /**
     * Instantiates a new date morpher.
     *
     * @param formats
     *            a list of formats this morpher supports.
     * @param lenient
     *            if the parsing should be lenient or not.
     */
    public DateMorpher(String[] formats, boolean lenient){
        this(formats, Locale.getDefault(), lenient);
    }

    /**
     * Instantiates a new date morpher.
     *
     * @param formats
     *            a list of formats this morpher supports.
     * @param defaultValue
     *            return value if the value to be morphed is null.
     */
    public DateMorpher(String[] formats, Date defaultValue){
        this(formats, defaultValue, Locale.getDefault(), false);
    }

    /**
     * Instantiates a new date morpher.
     *
     * @param formats
     *            a list of formats this morpher supports.
     * @param defaultValue
     *            return value if the value to be morphed is null.
     * @param locale
     *            the Locale used to parse each format.
     * @param lenient
     *            if the parsing should be lenient or not.
     */
    public DateMorpher(String[] formats, Date defaultValue, Locale locale, boolean lenient){
        super(true);
        if (formats == null || formats.length == 0){
            throw new MorphException("invalid array of formats");
        }
        // should use defensive copying ?
        this.formats = formats;

        if (locale == null){
            this.locale = Locale.getDefault();
        }else{
            this.locale = locale;
        }

        this.lenient = lenient;
        setDefaultValue(defaultValue);
    }

    /**
     * Instantiates a new date morpher.
     *
     * @param formats
     *            a list of formats this morpher supports.
     * @param locale
     *            the Locale used to parse each format.
     */
    public DateMorpher(String[] formats, Locale locale){
        this(formats, locale, false);
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

        if (locale == null){
            this.locale = Locale.getDefault();
        }else{
            this.locale = locale;
        }

        this.lenient = lenient;
    }

    /**
     * Equals.
     *
     * @param obj
     *            the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }

        if (!(obj instanceof DateMorpher)){
            return false;
        }

        DateMorpher other = (DateMorpher) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(formats, other.formats);
        builder.append(locale, other.locale);
        builder.append(lenient, other.lenient);
        if (isUseDefault() && other.isUseDefault()){
            builder.append(getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }else if (!isUseDefault() && !other.isUseDefault()){
            return builder.isEquals();
        }else{
            return false;
        }
    }

    /**
     * Returns the default value for this Morpher.
     *
     * @return the default value
     */
    public Date getDefaultValue(){
        return (Date) defaultValue.clone();
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode(){
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(formats);
        builder.append(locale);
        builder.append(lenient);
        if (isUseDefault()){
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

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

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class morphsTo(){
        return Date.class;
    }

    /**
     * Sets the defaultValue to use if the value to be morphed is null.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public void setDefaultValue(Date defaultValue){
        this.defaultValue = (Date) defaultValue.clone();
    }

    /**
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class clazz){
        return String.class.isAssignableFrom(clazz);
    }
}