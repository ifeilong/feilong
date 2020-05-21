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
package com.feilong.component;

import static com.feilong.core.util.MapUtil.newConcurrentHashMap;

import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.feilong.core.Validate;

/**
 * Spring Expression Language 工具类.
 * 
 * <p>
 * 同很多可用的Java 表达式语言相比，例如{@link <a href="http://commons.apache.org/proper/commons-ognl/">OGNL</a>}，{@link <a href=
 * "http://camel.apache.org/mvel.html">MVEL</a>}和JBoss EL，SpEL的诞生是为了给Spring社区提供一个可以给Spring目录中所有产品提供单一良好支持的表达式语言。
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring Expression
 *      Language (SpEL)</a>
 * @see <a href="http://commons.apache.org/proper/commons-ognl/">commons-ognl</a>
 * @see <a href="http://camel.apache.org/mvel.html/">mvel</a>
 * @since 1.0.4
 * @deprecated 将来使用feilong-spring里面的
 */
@Deprecated
final class SpelUtil{

    /** The expression parser. */
    private static ExpressionParser              expressionParser                     = new SpelExpressionParser();

    /**
     * expressionString和 {@link Expression} 简单的 cache.
     * 
     * @since 4.0.6
     */
    private static final Map<String, Expression> EXPRESSION_STRING_AND_EXPRESSION_MAP = newConcurrentHashMap();

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SpelUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Gets the template value.
     *
     * @param <T>
     *            the generic type
     * @param expressionString
     *            the expression string
     * @return the template value
     * @see org.springframework.expression.ParserContext#TEMPLATE_EXPRESSION
     * @see org.springframework.expression.common.TemplateParserContext
     */
    @SuppressWarnings("unchecked")
    public static <T> T getTemplateValue(String expressionString){
        Validate.notBlank(expressionString, "expressionString can't be blank!");

        //---------------------------------------------------------------
        Expression expression = load(expressionString, ParserContext.TEMPLATE_EXPRESSION);
        return (T) expression.getValue();
    }

    //---------------------------------------------------------------

    /**
     * Load.
     *
     * @param expressionString
     *            the expression string
     * @param context
     *            the context
     * @return the expression
     * @since 4.1.2
     */
    private static Expression load(String expressionString,ParserContext context){
        Expression expression = EXPRESSION_STRING_AND_EXPRESSION_MAP.get(expressionString);
        if (null != expression){
            return expression;
        }

        //---------------------------------------------------------------
        expression = expressionParser.parseExpression(expressionString, context);

        EXPRESSION_STRING_AND_EXPRESSION_MAP.put(expressionString, expression);

        return expression;
    }
}
