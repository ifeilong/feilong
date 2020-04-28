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
package com.feilong.office.excel.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ognl.NullHandler;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.OgnlRuntime;

/**
 * The Class OgnlStack.
 */
public class OgnlStack{

    /** The Constant log. */
    private static final Logger       LOGGER      = LoggerFactory.getLogger(OgnlStack.class);

    //---------------------------------------------------------------

    /** The stack. */
    private final List<Object>        stack       = new ArrayList<>();

    /** The context. */
    private final Map<String, Object> context;

    /** The expressions. */
    private final Map<String, Object> expressions = new HashMap<>();

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
    private Object getExpression(String expr) throws OgnlException{
        synchronized (expressions){
            Object o = expressions.get(expr);
            if (o == null){
                o = Ognl.parseExpression(expr);
                expressions.put(expr, o);
            }
            return o;
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
        for (Object obj : stack){
            try{
                if (expr.indexOf("top") >= 0){
                    //contains top evaluation
                    context.put("__top", obj);
                    expr = expr.replaceAll("top", "#__top");
                }
                return Ognl.getValue(getExpression(expr), context, obj);
            }catch (OgnlException e){

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
        Object root = stack.get(0);
        if (root == null){
            throw new IllegalArgumentException();
        }
        Ognl.setValue(getExpression(expr), context, root, value);
    }

    /**
     * Instantiates a new ognl stack.
     *
     * @param obj
     *            the obj
     */
    public OgnlStack(Object obj){
        this(obj, new HashMap<String, Object>());
    }

    /**
     * Instantiates a new ognl stack.
     *
     * @param obj
     *            the obj
     * @param context
     *            the context
     */
    public OgnlStack(Object obj, Map<String, Object> context){
        NullHandler nullHandler = null;
        synchronized (OgnlStack.class){
            try{
                nullHandler = OgnlRuntime.getNullHandler(Object.class);
                if (nullHandler == null || !(nullHandler instanceof InstantiatingNullHandler)){
                    OgnlRuntime.setNullHandler(Object.class, new InstantiatingNullHandler(nullHandler, Arrays.asList("java")));
                }
            }catch (OgnlException e){
                LOGGER.error("", e);
            }
        }

        stack.add(obj);
        this.context = context == null ? new HashMap<>() : context;
        this.context.put(InstantiatingNullHandler.USING_LOXIA_NULL_HANDLER, true);
    }

    //---------------------------------------------------------------

    /**
     * Push.
     *
     * @param obj
     *            the obj
     */
    public void push(Object obj){
        stack.add(0, obj);
    }

    /**
     * Pop.
     *
     * @return the object
     */
    public Object pop(){
        if (stack.size() == 0){
            throw new RuntimeException("No elements to pop");
        }
        return stack.remove(0);
    }

    /**
     * Peek.
     *
     * @return the object
     */
    public Object peek(){
        if (stack.size() == 0){
            throw new RuntimeException("No elements in stack");
        }
        return stack.get(0);
    }

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

    /**
     * Gets the context.
     *
     * @param key
     *            the key
     * @return the context
     */
    public Object getContext(String key){
        return this.context.get(key);
    }

    //---------------------------------------------------------------

    /**
     * Gets the context map.
     *
     * @return the context map
     */
    public Map<String, Object> getContextMap(){
        return this.context;
    }
}
