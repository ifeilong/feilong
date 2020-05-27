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
package com.feilong.spring.expression;

import static com.feilong.core.util.MapUtil.newConcurrentHashMap;

import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.feilong.core.Validate;

/**
 * Spring Expression Language 工具类.
 * 
 * <p>
 * 同很多可用的Java 表达式语言相比，例如{@link <a href="http://commons.apache.org/proper/commons-ognl/">OGNL</a>}，{@link <a href=
 * "http://camel.apache.org/mvel.html">MVEL</a>}和JBoss EL，SpEL的诞生是为了给Spring社区提供一个可以给Spring目录中所有产品提供单一良好支持的表达式语言。
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring Expression
 *      Language (SpEL)</a>
 * @see <a href="http://commons.apache.org/proper/commons-ognl/">commons-ognl</a>
 * @see <a href="http://camel.apache.org/mvel.html/">mvel</a>
 * @since 3.0.2 move from feilong-spring project
 */
public final class SpelUtil{

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

    /**
     * Gets the value.
     * 
     * <h3>示例1:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * String ex = "'Hello,World'";
     * 
     * assertEquals("Hello,World", SpelUtil.getValue(ex));
     * assertEquals(11, SpelUtil.getValue(ex + ".length()"));
     * assertEquals("Hello,World!", SpelUtil.getValue(ex + ".concat('!')"));
     * assertEquals(String.class, SpelUtil.getValue(ex + ".class"));
     * assertEquals(11, SpelUtil.getValue(ex + ".bytes.length"));
     * assertEquals("HELLO,WORLD", SpelUtil.getValue("new String(" + ex + ").toUpperCase()"));
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>示例2:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * String expressionString = "T(com.feilong.core.lang.StringUtil).tokenizeToStringArray('xin,jin',',')";
     * 
     * String[] values = SpelUtil.getValue(expressionString);
     * assertThat(toList(values), allOf(hasItem("xin"), hasItem("jin")));
     * 
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param expressionString
     *            the expression string
     * @return 如果 <code>expressionString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>expressionString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     * @see org.springframework.expression.Expression#getValue()
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(String expressionString){
        Validate.notBlank(expressionString, "expressionString can't be blank!");

        //---------------------------------------------------------------
        Expression expression = load(expressionString, null);
        return (T) expression.getValue();
    }

    //---------------------------------------------------------------

    /**
     * Gets the value.
     *
     * @param <V>
     *            the value type
     * @param <T>
     *            the generic type
     * @param expressionString
     *            the expression string
     * @param rootObject
     *            the root object to use, see
     *            {@link org.springframework.expression.spel.support.StandardEvaluationContext#StandardEvaluationContext(Object)
     *            StandardEvaluationContext}
     * @return 如果 <code>expressionString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>expressionString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>rootObject</code> 是null或者empty,返回 {@link #getValue(String)}<br>
     * @see org.springframework.expression.Expression#getValue(EvaluationContext)
     * @see StandardEvaluationContext
     * @see org.springframework.expression.spel.support.SimpleEvaluationContext
     * @since 4.0.6
     */
    @SuppressWarnings("unchecked")
    public static <V, T> V getValue(String expressionString,T rootObject){
        Validate.notBlank(expressionString, "expressionString can't be blank!");
        //---------------------------------------------------------------
        if (null == rootObject){
            return getValue(expressionString);
        }

        //---------------------------------------------------------------
        EvaluationContext evaluationContext = new StandardEvaluationContext(rootObject);

        Expression expression = load(expressionString, null);
        return (V) expression.getValue(evaluationContext);
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
