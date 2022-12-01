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
package com.feilong.lib.excel.ognl;

import static com.feilong.core.bean.ConvertUtil.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.core.lang.StringUtil;
import com.feilong.lib.ognl.Ognl;
import com.feilong.lib.ognl.OgnlException;

/**
 * The Class OgnlStack.
 */
public class OgnlStack{

    /** The stack. */
    private final List<Object>        stackList      = new ArrayList<>();

    /** The context. */
    private final Map<String, Object> context;

    /** The expressions. */
    private final Map<String, Object> expressionsMap = new HashMap<>();

    //---------------------------------------------------------------

    /**
     * Instantiates a new ognl stack.
     *
     * @param obj
     *            the obj
     */
    public OgnlStack(Object obj){
        OgnlUtil.setNullHandler();

        //---------------------------------------------------------------
        stackList.add(obj);

        this.context = toMap(InstantiatingNullHandler.USING_LOXIA_NULL_HANDLER, true);
    }

    //---------------------------------------------------------------

    /**
     * Gets the expression.
     *
     * @param expr
     *            the expr
     * @return the expression
     * @throws OgnlException
     *             the ognl exception
     */
    private Object getExpression(String expr){
        synchronized (expressionsMap){
            return expressionsMap.computeIfAbsent(expr, key -> {
                try{
                    return Ognl.parseExpression(expr);
                }catch (OgnlException e){
                    throw new DefaultRuntimeException(e);
                }
            });
        }
    }

    /**
     * Gets the value.
     *
     * @param expr
     *            the expr
     * @return the value
     */
    public Object getValue(String expr){
        for (Object obj : stackList){
            if (expr.indexOf("top") >= 0){
                //contains top evaluation
                context.put("__top", obj);
                expr = expr.replace("top", "#__top");
            }
            try{
                Object expression = getExpression(expr);
                return Ognl.getValue(expression, OgnlContextBuilder.build(context), obj);
            }catch (Exception e){
                throw new DefaultRuntimeException(StringUtil.formatPattern("expr:[{}],obj:[{}]", expr, obj), e);
            }
        }
        return null;
    }

    /**
     * 设置 value.
     *
     * @param expr
     *            the expr
     * @param value
     *            the value
     * @throws OgnlException
     *             the ognl exception
     */
    public void setValue(String expr,Object value) throws OgnlException{
        Object root = stackList.get(0);
        Validate.notNull(root, "root can't be null!");

        Ognl.setValue(getExpression(expr), OgnlContextBuilder.build(context), root, value);
    }

    //---------------------------------------------------------------

    /**
     * Push.
     *
     * @param obj
     *            the obj
     */
    public void push(Object obj){
        stackList.add(0, obj);
    }

    /**
     * Pop.
     *
     * @return the object
     */
    public Object pop(){
        Validate.isTrue(!stackList.isEmpty(), "stackList must not empty");
        return stackList.remove(0);
    }

    /**
     * Peek.
     *
     * @return the object
     */
    public Object peek(){
        Validate.isTrue(!stackList.isEmpty(), "stackList must not empty");
        return stackList.get(0);
    }

    //---------------------------------------------------------------

    /**
     * 添加 context.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void addContext(String key,Object value){
        this.context.put(key, value);
    }

    /**
     * 删除 context.
     *
     * @param key
     *            the key
     */
    public void removeContext(String key){
        this.context.remove(key);
    }
}
