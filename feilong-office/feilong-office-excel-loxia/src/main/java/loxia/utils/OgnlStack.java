package loxia.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.NullHandler;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.OgnlRuntime;

public class OgnlStack{

    private final List<Object>        stack       = new ArrayList<Object>();

    private final Map<String, Object> context;

    private final Map<String, Object> expressions = new HashMap<String, Object>();

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

    public void setValue(String expr,Object value) throws OgnlException{
        Object root = stack.get(0);
        if (root == null)
            throw new IllegalArgumentException();
        Ognl.setValue(getExpression(expr), context, root, value);
    }

    public OgnlStack(Object obj){
        this(obj, new HashMap<String, Object>());
    }

    public OgnlStack(Object obj, Map<String, Object> context){
        NullHandler nullHandler = null;
        synchronized (OgnlStack.class){
            try{
                nullHandler = OgnlRuntime.getNullHandler(Object.class);
                if (nullHandler == null || !(nullHandler instanceof InstantiatingNullHandler))
                    OgnlRuntime.setNullHandler(Object.class, new InstantiatingNullHandler(nullHandler, Arrays.asList("java")));
            }catch (OgnlException e){
                e.printStackTrace();
            }
        }

        stack.add(obj);
        this.context = context == null ? new HashMap<String, Object>() : context;
        this.context.put(InstantiatingNullHandler.USING_LOXIA_NULL_HANDLER, true);
    }

    public void push(Object obj){
        stack.add(0, obj);
    }

    public Object pop(){
        if (stack.size() == 0)
            throw new RuntimeException("No elements to pop");
        return stack.remove(0);
    }

    public Object peek(){
        if (stack.size() == 0)
            throw new RuntimeException("No elements in stack");
        return stack.get(0);
    }

    public void addContext(String key,Object value){
        this.context.put(key, value);
    }

    public void removeContext(String key){
        this.context.remove(key);
    }

    public Object getContext(String key){
        return this.context.get(key);
    }

    public Map<String, Object> getContextMap(){
        return this.context;
    }
}
