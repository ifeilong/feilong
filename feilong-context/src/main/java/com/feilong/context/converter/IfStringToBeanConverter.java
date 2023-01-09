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
 * 根据predicate 条件,来判断是使用 trueStringToBeanConverter 还是falseStringToBeanConverter来转换结果.
 * 
 * <h3>使用场景:</h3>
 * 
 * <blockquote>
 * 比如支付宝查询接口, 正常返回的结果是这样子的
 * 
 * <pre class="code">
{@code
 * <alipay>
 * <is_success>T</is_success>
 * <request>
 * <param name="trade_no">2010073000030344</param>
 * <param name="service">single_trade_query</param>
 * <param name="partner">2088002007018916</param>
 * </request>
 * <response>
 * <trade>
 * <body>合同催款通知</body>
 * <buyer_id>2088102002723445</buyer_id>
 * <discount>0.00</discount>
 * <gmt_payment>2010-07-30 12:30:29</gmt_payment>
 * <is_total_fee_adjust>F</is_total_fee_adjust>
 * <out_trade_no>1280463992953</out_trade_no>
 * </trade>
 * </response>
 * <sign>56ae9c3286886f76e57e0993625c71fe</sign>
 * <sign_type>MD5</sign_type>
 * </alipay>
 * }
 * </pre>
 * 
 * 发生异常返回的结果是这样子的
 * 
 * <pre>
 * {@code
 * <alipay>
 * <is_success>F</is_success>
 * <error>TRADE_IS_NOT_EXIST</error>
 * </alipay>
 * }
 * </pre>
 * 
 * 
 * 此时xml 可以配置成
 * 
 * <pre>
 * {@code
        <property name="stringToBeanConverter">
            <bean class="com.feilong.context.converter.IfStringToBeanConverter">

                <property name="predicate">
                    <bean class="com.feilong.core.util.predicate.ContainsStringPredicate" p:searchCharSequence="TRADE_NOT_EXIST" />
                </property>

                <property name="trueStringToBeanConverter">
                    <bean class="com.feilong.netpay.alipay.query.AlipayTradeNotExistStringToBeanConverter" />
                </property>

                <property name="falseStringToBeanConverter">
                    <bean class="com.feilong.context.converter.XMLMapBuilderStringToBeanConverter">
                        <property name="beanClass" value="com.feilong.netpay.alipay.query.AlipaySingleQueryResultCommand" />

                        <property name="nameAndValueMapBuilder">
                            <bean class="com.feilong.context.converter.builder.XmlNodeNameAndValueMapBuilder" p:xpathExpression=
"/alipay/response/trade/*" />
                        </property>

                        <property name="beanBuilder">
                            <bean class="com.feilong.context.converter.builder.AliasBeanBuilder" />
                        </property>
                    </bean>
                </property>
            </bean>
        </property>
       }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @see IfTransformer
 * @see IfClosure
 * @since 1.14.3
 */
public class IfStringToBeanConverter<T> extends AbstractStringToBeanConverter<T>{

    /** 指定的判定条件. */
    private Predicate<String>        predicate;

    /** 如果满足条件,使用的对应的字符串转bean converter. */
    private StringToBeanConverter<T> trueStringToBeanConverter;

    /** 如果不满足条件,使用的对应的字符串转bean converter. */
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
     * 设置 指定的判定条件.
     *
     * @param predicate
     *            the new 指定的判定条件
     */
    public void setPredicate(Predicate<String> predicate){
        this.predicate = predicate;
    }

    /**
     * 设置 如果满足条件,使用的对应的字符串转bean converter.
     *
     * @param trueStringToBeanConverter
     *            the new 如果满足条件,使用的对应的字符串转bean converter
     */
    public void setTrueStringToBeanConverter(StringToBeanConverter<T> trueStringToBeanConverter){
        this.trueStringToBeanConverter = trueStringToBeanConverter;
    }

    /**
     * 设置 如果不满足条件,使用的对应的字符串转bean converter.
     *
     * @param falseStringToBeanConverter
     *            the new 如果不满足条件,使用的对应的字符串转bean converter
     */
    public void setFalseStringToBeanConverter(StringToBeanConverter<T> falseStringToBeanConverter){
        this.falseStringToBeanConverter = falseStringToBeanConverter;
    }

}
