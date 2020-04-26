/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package loxia.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ognl.NullHandler;
import ognl.Ognl;
import ognl.OgnlRuntime;

public class InstantiatingNullHandler implements NullHandler{

    private static final Log   LOG                      = LogFactory.getLog(InstantiatingNullHandler.class);

    public static final String USING_LOXIA_NULL_HANDLER = "loxia.useingLoxiaNullHandler";

    private List<String>       ignoreList               = new ArrayList<String>();

    private final NullHandler  handlerWrapper;

    public InstantiatingNullHandler(NullHandler nullHandler){
        this.handlerWrapper = nullHandler;
    }

    public InstantiatingNullHandler(NullHandler nullHandler, List<String> ignoreList){
        this(nullHandler);
        this.ignoreList.addAll(ignoreList);
    }

    public List<String> getIgnoreList(){
        return ignoreList;
    }

    public void setIgnoreList(List<String> ignoreList){
        this.ignoreList = ignoreList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object nullMethodResult(Map context,Object target,String methodName,Object[] args){
        Boolean flag = (Boolean) context.get(USING_LOXIA_NULL_HANDLER);
        if (handlerWrapper != null && ((flag == null) || !flag))
            return handlerWrapper.nullMethodResult(context, target, methodName, args);
        if (LOG.isDebugEnabled()){
            LOG.debug("Entering nullMethodResult ");
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object nullPropertyValue(Map context,Object target,Object property){
        Boolean flag = (Boolean) context.get(USING_LOXIA_NULL_HANDLER);
        if (handlerWrapper != null && ((flag == null) || !flag))
            return handlerWrapper.nullPropertyValue(context, target, property);
        if (LOG.isDebugEnabled()){
            LOG.debug("Entering nullPropertyValue [target=" + target + ", property=" + property + "]");
        }

        if ((target == null) || (property == null)){
            return null;
        }

        try{
            String propName = property.toString();
            Class clazz = null;

            if (target != null){
                PropertyDescriptor pd = OgnlRuntime.getPropertyDescriptor(target.getClass(), propName);
                if (pd == null){
                    return null;
                }

                clazz = pd.getPropertyType();
            }

            if (clazz == null){
                // can't do much here!
                return null;
            }

            //ignore those class in ignored package
            if (ignoreClass(clazz))
                return null;

            Object param = createObject(clazz, target, propName, context);

            Ognl.setValue(propName, context, target, param);

            return param;
        }catch (Exception e){
            LOG.error("Could not create and/or set value back on to object", e);
        }

        return null;
    }

    private boolean ignoreClass(Class<?> clazz){
        if (clazz.isPrimitive() || clazz.isArray())
            return true;
        String clazzFullName = clazz.getName();
        for (String name : ignoreList){
            if (clazzFullName.startsWith(name))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Object createObject(Class clazz,Object target,String property,Map context) throws Exception{
        if (Collection.class.isAssignableFrom(clazz)){
            return new ArrayList();
        }else if (clazz == Map.class){
            return new HashMap();
        }

        return clazz.newInstance();
    }
}
