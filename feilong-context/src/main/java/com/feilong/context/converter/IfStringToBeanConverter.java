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
package com.feilong.context.converter;

import org.apache.commons.collections4.Predicate;

import com.feilong.lib.collection4.functors.IfClosure;
import com.feilong.lib.collection4.functors.IfTransformer;

/**
 * The Class IfStringToBeanConverter.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @see IfTransformer
 * @see IfClosure
 * @since 1.14.3
 */
public class IfStringToBeanConverter<T> extends AbstractStringToBeanConverter<T>{

    /** The test. */
    private Predicate<String>        predicate;

    /** The transformer to use if true. */
    private StringToBeanConverter<T> trueStringToBeanConverter;

    /** The transformer to use if false. */
    private StringToBeanConverter<T> falseStringToBeanConverter;

    //---------------------------------------------------------------

    /**
     * Instantiates a new if string to bean converter.
     */
    public IfStringToBeanConverter(){

    }

    /**
     * Instantiates a new if string to bean converter.
     *
     * @param predicate
     *            the predicate
     * @param trueStringToBeanConverter
     *            the true string to bean converter
     * @param falseStringToBeanConverter
     *            the false string to bean converter
     */
    public IfStringToBeanConverter(Predicate<String> predicate, StringToBeanConverter<T> trueStringToBeanConverter,
                    StringToBeanConverter<T> falseStringToBeanConverter){
        super();
        this.predicate = predicate;
        this.trueStringToBeanConverter = trueStringToBeanConverter;
        this.falseStringToBeanConverter = falseStringToBeanConverter;
    }

    //---------------------------------------------------------------

    /**
     * Transforms the input using the true or false transformer based to the result of the predicate.
     *
     * @param inputString
     *            the input string
     * @return the transformed result
     */
    @Override
    protected T handler(String inputString){
        if (predicate.evaluate(inputString)){
            return trueStringToBeanConverter.convert(inputString);
        }
        return falseStringToBeanConverter.convert(inputString);
    }

    //---------------------------------------------------------------

    /**
     * 设置 test.
     *
     * @param predicate
     *            the predicate to set
     */
    public void setPredicate(Predicate<String> predicate){
        this.predicate = predicate;
    }

    /**
     * 设置 transformer to use if true.
     *
     * @param trueStringToBeanConverter
     *            the trueStringToBeanConverter to set
     */
    public void setTrueStringToBeanConverter(StringToBeanConverter<T> trueStringToBeanConverter){
        this.trueStringToBeanConverter = trueStringToBeanConverter;
    }

    /**
     * 设置 transformer to use if false.
     *
     * @param falseStringToBeanConverter
     *            the falseStringToBeanConverter to set
     */
    public void setFalseStringToBeanConverter(StringToBeanConverter<T> falseStringToBeanConverter){
        this.falseStringToBeanConverter = falseStringToBeanConverter;
    }

}
