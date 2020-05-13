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
package com.feilong.lib.loxia.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.lib.lang3.Validate;
import com.feilong.tools.slf4j.Slf4jUtil;

import ognl.DefaultMemberAccess;
import ognl.NullHandler;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

/**
 * The Class OgnlStack.
 */
public class OgnlStack{

    /** The stack. */
    private final List<Object>        stackList   = new ArrayList<>();

    /** The context. */
    private final Map<String, Object> context;

    /** The expressions. */
    private final Map<String, Object> expressions = new HashMap<>();

    //---------------------------------------------------------------

    /**
     * Instantiates a new ognl stack.
     *
     * @param obj
     *            the obj
     */
    public OgnlStack(Object obj){
        NullHandler nullHandler = null;
        synchronized (OgnlStack.class){
            try{
                nullHandler = OgnlRuntime.getNullHandler(Object.class);
                if (nullHandler == null || !(nullHandler instanceof InstantiatingNullHandler)){
                    OgnlRuntime.setNullHandler(Object.class, new InstantiatingNullHandler(nullHandler, Arrays.asList("java")));
                }
            }catch (OgnlException e){
                throw new DefaultRuntimeException(e);
            }
        }

        //---------------------------------------------------------------
        stackList.add(obj);
        this.context = new HashMap<>();
        this.context.put(InstantiatingNullHandler.USING_LOXIA_NULL_HANDLER, true);
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
    private Object getExpression(String expr) throws OgnlException{
        synchronized (expressions){
            Object object = expressions.get(expr);
            if (object == null){
                object = Ognl.parseExpression(expr);
                expressions.put(expr, object);
            }
            return object;
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
                expr = expr.replaceAll("top", "#__top");
            }
            try{
                Object expression = getExpression(expr);
                return Ognl.getValue(expression, buildOgnlContext(context), obj);
            }catch (Exception e){
                throw new DefaultRuntimeException(Slf4jUtil.format("expr:[{}],obj:[{}]", expr, obj), e);
            }
        }
        return null;
    }

    /**
     * Builds the ognl context.
     *
     * @param context
     *            the context
     * @return the ognl context
     * @since 3.0.0
     */
    private static OgnlContext buildOgnlContext(Map<String, Object> context){
        return new OgnlContext(new DefaultMemberAccess(true), null, null, context);
    }

    /**
     * 设置 value.
     *
     * @param expr
     *            the expr
     * @param value
     *            the value
     * @throws Exception
     *             the exception
     */
    public void setValue(String expr,Object value) throws Exception{
        Object root = stackList.get(0);
        Validate.notNull(root, "root can't be null!");

        Ognl.setValue(getExpression(expr), buildOgnlContext(context), root, value);
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
        Validate.isTrue(stackList.size() > 0, "stackList.size() must > 0");
        return stackList.remove(0);
    }

    /**
     * Peek.
     *
     * @return the object
     */
    public Object peek(){
        Validate.isTrue(stackList.size() > 0, "stackList.size() must > 0");
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
