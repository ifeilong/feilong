package com.feilong.lib.ognl.enhance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.feilong.lib.javassist.CannotCompileException;
import com.feilong.lib.javassist.ClassPool;
import com.feilong.lib.javassist.CtClass;
import com.feilong.lib.javassist.CtField;
import com.feilong.lib.javassist.CtMethod;
import com.feilong.lib.javassist.LoaderClassPath;
import com.feilong.lib.javassist.NotFoundException;
import com.feilong.lib.ognl.ASTAnd;
import com.feilong.lib.ognl.ASTChain;
import com.feilong.lib.ognl.ASTConst;
import com.feilong.lib.ognl.ASTCtor;
import com.feilong.lib.ognl.ASTList;
import com.feilong.lib.ognl.ASTMethod;
import com.feilong.lib.ognl.ASTOr;
import com.feilong.lib.ognl.ASTProperty;
import com.feilong.lib.ognl.ASTRootVarRef;
import com.feilong.lib.ognl.ASTStaticField;
import com.feilong.lib.ognl.ASTStaticMethod;
import com.feilong.lib.ognl.ASTThisVarRef;
import com.feilong.lib.ognl.ASTVarRef;
import com.feilong.lib.ognl.ExpressionNode;
import com.feilong.lib.ognl.Node;
import com.feilong.lib.ognl.OgnlContext;
import com.feilong.lib.ognl.OgnlRuntime;

/**
 * Responsible for managing/providing functionality related to compiling generated java source
 * expressions via bytecode enhancements for a given ognl expression.
 */
public class ExpressionCompiler implements OgnlExpressionCompiler{

    /**
     * Key used to store any java source string casting statements in the {@link OgnlContext} during
     * class compilation.
     */
    public static final String PRE_CAST      = "_preCast";

    /**
     * {@link ClassLoader} instances.
     */
    protected Map              _loaders      = new HashMap();

    /**
     * Javassist class definition poool.
     */
    protected ClassPool        _pool;

    protected int              _classCounter = 0;

    /**
     * Default constructor, does nothing.
     */
    public ExpressionCompiler(){
    }

    /**
     * Used by {@link #castExpression(com.feilong.lib.ognl.OgnlContext, com.feilong.lib.ognl.Node, String)} to store the cast java
     * source string in to the current {@link OgnlContext}. This will either add to the existing
     * string present if it already exists or create a new instance and store it using the static key
     * of {@link #PRE_CAST}.
     *
     * @param context
     *            The current execution context.
     * @param cast
     *            The java source string to store in to the context.
     */
    public static void addCastString(OgnlContext context,String cast){
        String value = (String) context.get(PRE_CAST);

        if (value != null){
            value = cast + value;
        }else{
            value = cast;
        }

        context.put(PRE_CAST, value);
    }

    /**
     * Returns the appropriate casting expression (minus parens) for the specified class type.
     *
     * <p>
     * For instance, if given an {@link Integer} object the string <code>"java.lang.Integer"</code>
     * would be returned. For an array of primitive ints <code>"int[]"</code> and so on..
     * </p>
     *
     * @param type
     *            The class to cast a string expression for.
     * @return The converted raw string version of the class name.
     */
    public static String getCastString(Class type){
        if (type == null){
            return null;
        }

        return type.isArray() ? type.getComponentType().getName() + "[]" : type.getName();
    }

    /**
     * Convenience method called by many different property/method resolving AST types to get a root expression
     * resolving string for the given node. The callers are mostly ignorant and rely on this method to properly
     * determine if the expression should be cast at all and take the appropriate actions if it should.
     *
     * @param expression
     *            The node to check and generate a root expression to if necessary.
     * @param root
     *            The root object for this execution.
     * @param context
     *            The current execution context.
     * @return Either an empty string or a root path java source string compatible with javassist compilations
     *         from the root object up to the specified {@link Node}.
     */
    public static String getRootExpression(Node expression,Object root,OgnlContext context){
        String rootExpr = "";

        if (!shouldCast(expression)){
            return rootExpr;
        }

        if ((!ASTList.class.isInstance(expression) && !ASTVarRef.class.isInstance(expression)
                        && !ASTStaticMethod.class.isInstance(expression) && !ASTStaticField.class.isInstance(expression)
                        && !ASTConst.class.isInstance(expression) && !ExpressionNode.class.isInstance(expression)
                        && !ASTCtor.class.isInstance(expression) && !ASTStaticMethod.class.isInstance(expression) && root != null)
                        || (root != null && ASTRootVarRef.class.isInstance(expression))){

            Class castClass = OgnlRuntime.getCompiler().getRootExpressionClass(expression, context);

            if (castClass.isArray() || ASTRootVarRef.class.isInstance(expression) || ASTThisVarRef.class.isInstance(expression)){
                rootExpr = "((" + getCastString(castClass) + ")$2)";

                if (ASTProperty.class.isInstance(expression) && !((ASTProperty) expression).isIndexedAccess()){
                    rootExpr += ".";
                }
            }else if ((ASTProperty.class.isInstance(expression) && ((ASTProperty) expression).isIndexedAccess())
                            || ASTChain.class.isInstance(expression)){
                rootExpr = "((" + getCastString(castClass) + ")$2)";
            }else{
                rootExpr = "((" + getCastString(castClass) + ")$2).";
            }
        }

        return rootExpr;
    }

    /**
     * Used by {@link #getRootExpression(com.feilong.lib.ognl.Node, Object, com.feilong.lib.ognl.OgnlContext)} to determine if the
     * expression
     * needs to be cast at all.
     *
     * @param expression
     *            The node to check against.
     * @return Yes if the node type should be cast - false otherwise.
     */
    public static boolean shouldCast(Node expression){
        if (ASTChain.class.isInstance(expression)){
            Node child = expression.jjtGetChild(0);
            if (ASTConst.class.isInstance(child) || ASTStaticMethod.class.isInstance(child) || ASTStaticField.class.isInstance(child)
                            || (ASTVarRef.class.isInstance(child) && !ASTRootVarRef.class.isInstance(child))){
                return false;
            }
        }

        return !ASTConst.class.isInstance(expression);
    }

    @Override
    public String castExpression(OgnlContext context,Node expression,String body){
        // ok - so this looks really f-ed up ...and it is ..eh if you can do it better I'm all for it :)

        if (context.getCurrentAccessor() == null || context.getPreviousType() == null
                        || context.getCurrentAccessor().isAssignableFrom(context.getPreviousType())
                        || (context.getCurrentType() != null && context.getCurrentObject() != null
                                        && context.getCurrentType().isAssignableFrom(context.getCurrentObject().getClass())
                                        && context.getCurrentAccessor().isAssignableFrom(context.getPreviousType()))
                        || body == null || body.trim().length() < 1
                        || (context.getCurrentType() != null && context.getCurrentType().isArray()
                                        && (context.getPreviousType() == null || context.getPreviousType() != Object.class))
                        || ASTOr.class.isInstance(expression) || ASTAnd.class.isInstance(expression)
                        || ASTRootVarRef.class.isInstance(expression) || context.getCurrentAccessor() == Class.class
                        || (context.get(ExpressionCompiler.PRE_CAST) != null
                                        && ((String) context.get(ExpressionCompiler.PRE_CAST)).startsWith("new"))
                        || ASTStaticField.class.isInstance(expression) || ASTStaticMethod.class.isInstance(expression)
                        || (OrderedReturn.class.isInstance(expression) && ((OrderedReturn) expression).getLastExpression() != null)){
            return body;
        }

        ExpressionCompiler.addCastString(context, "((" + ExpressionCompiler.getCastString(context.getCurrentAccessor()) + ")");

        return ")" + body;
    }

    @Override
    public String getClassName(Class clazz){
        if (clazz.getName().equals("java.util.AbstractList$Itr")){
            return Iterator.class.getName();
        }

        if (Modifier.isPublic(clazz.getModifiers()) && clazz.isInterface()){
            return clazz.getName();
        }

        return _getClassName(clazz, clazz.getInterfaces());
    }

    private String _getClassName(Class clazz,Class[] intf){
        for (Class element : intf){
            if (element.getName().indexOf("util.List") > 0){
                return element.getName();
            }else if (element.getName().indexOf("Iterator") > 0){
                return element.getName();
            }
        }

        final Class superclazz = clazz.getSuperclass();
        if (superclazz != null){
            final Class[] superclazzIntf = superclazz.getInterfaces();
            if (superclazzIntf.length > 0){
                return _getClassName(superclazz, superclazzIntf);
            }
        }

        return clazz.getName();
    }

    @Override
    public Class getSuperOrInterfaceClass(Method m,Class clazz){
        Class[] intfs = clazz.getInterfaces();
        if (intfs != null && intfs.length > 0){
            Class intClass;

            for (Class intf : intfs){
                intClass = getSuperOrInterfaceClass(m, intf);

                if (intClass != null){
                    return intClass;
                }

                if (Modifier.isPublic(intf.getModifiers()) && containsMethod(m, intf)){
                    return intf;
                }
            }
        }

        if (clazz.getSuperclass() != null){
            Class superClass = getSuperOrInterfaceClass(m, clazz.getSuperclass());

            if (superClass != null){
                return superClass;
            }
        }

        if (Modifier.isPublic(clazz.getModifiers()) && containsMethod(m, clazz)){
            return clazz;
        }

        return null;
    }

    /**
     * Helper utility method used by compiler to help resolve class-&gt;method mappings
     * during method calls to {@link OgnlExpressionCompiler#getSuperOrInterfaceClass(java.lang.reflect.Method, Class)}.
     *
     * @param m
     *            The method to check for existance of.
     * @param clazz
     *            The class to check for the existance of a matching method definition to the method passed in.
     * @return True if the class contains the specified method, false otherwise.
     */
    public boolean containsMethod(Method m,Class clazz){
        Method[] methods = clazz.getMethods();

        if (methods == null){
            return false;
        }

        for (Method method : methods){
            if (method.getName().equals(m.getName()) && method.getReturnType() == m.getReturnType()){
                Class[] parms = m.getParameterTypes();
                if (parms == null){
                    continue;
                }

                Class[] mparms = method.getParameterTypes();
                if (mparms == null || mparms.length != parms.length){
                    continue;
                }

                boolean parmsMatch = true;
                for (int p = 0; p < parms.length; p++){
                    if (parms[p] != mparms[p]){
                        parmsMatch = false;
                        break;
                    }
                }

                if (!parmsMatch){
                    continue;
                }

                Class[] exceptions = m.getExceptionTypes();
                if (exceptions == null){
                    continue;
                }

                Class[] mexceptions = method.getExceptionTypes();
                if (mexceptions == null || mexceptions.length != exceptions.length){
                    continue;
                }

                boolean exceptionsMatch = true;
                for (int e = 0; e < exceptions.length; e++){
                    if (exceptions[e] != mexceptions[e]){
                        exceptionsMatch = false;
                        break;
                    }
                }

                if (!exceptionsMatch){
                    continue;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public Class getInterfaceClass(Class clazz){
        if (clazz.getName().equals("java.util.AbstractList$Itr")){
            return Iterator.class;
        }

        if (Modifier.isPublic(clazz.getModifiers()) && clazz.isInterface() || clazz.isPrimitive()){
            return clazz;
        }

        return _getInterfaceClass(clazz, clazz.getInterfaces());
    }

    private Class _getInterfaceClass(Class clazz,Class[] intf){
        for (Class element : intf){
            if (List.class.isAssignableFrom(element)){
                return List.class;
            }else if (Iterator.class.isAssignableFrom(element)){
                return Iterator.class;
            }else if (Map.class.isAssignableFrom(element)){
                return Map.class;
            }else if (Set.class.isAssignableFrom(element)){
                return Set.class;
            }else if (Collection.class.isAssignableFrom(element)){
                return Collection.class;
            }
        }

        final Class superclazz = clazz.getSuperclass();
        if (superclazz != null){
            final Class[] superclazzIntf = superclazz.getInterfaces();
            if (superclazzIntf.length > 0){
                return _getInterfaceClass(superclazz, superclazzIntf);
            }
        }

        return clazz;
    }

    @Override
    public Class getRootExpressionClass(Node rootNode,OgnlContext context){
        if (context.getRoot() == null){
            return null;
        }

        Class ret = context.getRoot().getClass();

        if (context.getFirstAccessor() != null && context.getFirstAccessor().isInstance(context.getRoot())){
            ret = context.getFirstAccessor();
        }

        return ret;
    }

    protected String generateGetter(
                    OgnlContext context,
                    CtClass newClass,
                    CtClass objClass,
                    ClassPool pool,
                    CtMethod valueGetter,
                    Node expression,
                    Object root) throws Exception{
        String pre = "";
        String post = "";
        String body;

        context.setRoot(root);

        // the ExpressionAccessor API has to reference the generic Object class for get/set operations, so this sets up that known
        // type beforehand

        context.remove(PRE_CAST);

        // Recursively generate the java source code representation of the top level expression

        String getterCode = expression.toGetSourceString(context, root);

        if (getterCode == null || getterCode.trim().length() <= 0 && !ASTVarRef.class.isAssignableFrom(expression.getClass())){
            getterCode = "null";
        }

        String castExpression = (String) context.get(PRE_CAST);

        if (context.getCurrentType() == null || context.getCurrentType().isPrimitive()
                        || Character.class.isAssignableFrom(context.getCurrentType()) || Object.class == context.getCurrentType()){
            pre = pre + " ($w) (";
            post = post + ")";
        }

        String rootExpr = !getterCode.equals("null") ? getRootExpression(expression, root, context) : "";

        String noRoot = (String) context.remove("_noRoot");
        if (noRoot != null){
            rootExpr = "";
        }

        createLocalReferences(context, pool, newClass, objClass, valueGetter.getParameterTypes());

        if (OrderedReturn.class.isInstance(expression) && ((OrderedReturn) expression).getLastExpression() != null){
            body = "{ " + (ASTMethod.class.isInstance(expression) || ASTChain.class.isInstance(expression) ? rootExpr : "")
                            + (castExpression != null ? castExpression : "") + ((OrderedReturn) expression).getCoreExpression() + " return "
                            + pre + ((OrderedReturn) expression).getLastExpression() + post + ";}";

        }else{

            body = "{  return " + pre + (castExpression != null ? castExpression : "") + rootExpr + getterCode + post + ";}";
        }

        if (body.indexOf("..") >= 0){
            body = body.replaceAll("\\.\\.", ".");
        }

        valueGetter.setBody(body);
        newClass.addMethod(valueGetter);

        return body;
    }

    @Override
    public String createLocalReference(OgnlContext context,String expression,Class type){
        String referenceName = "ref" + context.incrementLocalReferenceCounter();
        context.addLocalReference(referenceName, new LocalReferenceImpl(referenceName, expression, type));

        String castString = "";
        if (!type.isPrimitive()){
            castString = "(" + ExpressionCompiler.getCastString(type) + ") ";
        }

        return castString + referenceName + "($$)";
    }

    void createLocalReferences(OgnlContext context,ClassPool pool,CtClass clazz,CtClass objClass,CtClass[] params)
                    throws CannotCompileException,NotFoundException{
        Map referenceMap = context.getLocalReferences();
        if (referenceMap == null || referenceMap.size() < 1){
            return;
        }

        Iterator it = referenceMap.values().iterator();

        while (it.hasNext()){
            LocalReference ref = (LocalReference) it.next();

            String widener = ref.getType().isPrimitive() ? " " : " ($w) ";

            String body = "{";
            body += " return  " + widener + ref.getExpression() + ";";
            body += "}";

            if (body.indexOf("..") >= 0){
                body = body.replaceAll("\\.\\.", ".");
            }

            CtMethod ctMethod = new CtMethod(pool.get(getCastString(ref.getType())), ref.getName(), params, clazz);
            ctMethod.setBody(body);

            clazz.addMethod(ctMethod);

            it.remove();
        }
    }

    protected String generateSetter(
                    OgnlContext context,
                    CtClass newClass,
                    CtClass objClass,
                    ClassPool pool,
                    CtMethod valueSetter,
                    Node expression,
                    Object root) throws Exception{
        if (ExpressionNode.class.isInstance(expression) || ASTConst.class.isInstance(expression)){
            throw new UnsupportedCompilationException("Can't compile expression/constant setters.");
        }

        context.setRoot(root);
        context.remove(PRE_CAST);

        String body;

        String setterCode = expression.toSetSourceString(context, root);
        String castExpression = (String) context.get(PRE_CAST);

        if (setterCode == null || setterCode.trim().length() < 1){
            throw new UnsupportedCompilationException("Can't compile null setter body.");
        }

        if (root == null){
            throw new UnsupportedCompilationException("Can't compile setters with a null root object.");
        }

        String pre = getRootExpression(expression, root, context);

        String noRoot = (String) context.remove("_noRoot");
        if (noRoot != null){
            pre = "";
        }

        createLocalReferences(context, pool, newClass, objClass, valueSetter.getParameterTypes());

        body = "{" + (castExpression != null ? castExpression : "") + pre + setterCode + ";}";

        if (body.indexOf("..") >= 0){
            body = body.replaceAll("\\.\\.", ".");
        }

        valueSetter.setBody(body);
        newClass.addMethod(valueSetter);

        return body;
    }

    /**
     * Fail safe getter creation when normal compilation fails.
     *
     * @param clazz
     *            The javassist class the new method should be attached to.
     * @param valueGetter
     *            The method definition the generated code will be contained within.
     * @param node
     *            The root expression node.
     * @return The generated source string for this method, the method will still be
     *         added via the javassist API either way so this is really a convenience
     *         for exception reporting / debugging.
     * @throws Exception
     *             If a javassist error occurs.
     */
    protected String generateOgnlGetter(CtClass clazz,CtMethod valueGetter,CtField node) throws Exception{
        String body = "return " + node.getName() + ".getValue($1, $2);";

        valueGetter.setBody(body);
        clazz.addMethod(valueGetter);

        return body;
    }

    /**
     * Fail safe setter creation when normal compilation fails.
     *
     * @param clazz
     *            The javassist class the new method should be attached to.
     * @param valueSetter
     *            The method definition the generated code will be contained within.
     * @param node
     *            The root expression node.
     * @return The generated source string for this method, the method will still be
     *         added via the javassist API either way so this is really a convenience
     *         for exception reporting / debugging.
     * @throws Exception
     *             If a javassist error occurs.
     */
    protected String generateOgnlSetter(CtClass clazz,CtMethod valueSetter,CtField node) throws Exception{
        String body = node.getName() + ".setValue($1, $2, $3);";

        valueSetter.setBody(body);
        clazz.addMethod(valueSetter);

        return body;
    }

    /**
     * Creates a {@link ClassLoader} instance compatible with the javassist classloader and normal
     * OGNL class resolving semantics.
     *
     * @param context
     *            The current execution context.
     *
     * @return The created {@link ClassLoader} instance.
     */
    protected EnhancedClassLoader getClassLoader(OgnlContext context){
        EnhancedClassLoader ret = (EnhancedClassLoader) _loaders.get(context.getClassResolver());

        if (ret != null){
            return ret;
        }

        ClassLoader classLoader = new ContextClassLoader(OgnlContext.class.getClassLoader(), context);

        ret = new EnhancedClassLoader(classLoader);
        _loaders.put(context.getClassResolver(), ret);

        return ret;
    }

    /**
     * Loads a new class definition via javassist for the specified class.
     *
     * @param searchClass
     *            The class to load.
     * @return The javassist class equivalent.
     *
     * @throws NotFoundException
     *             When the class definition can't be found.
     */
    private CtClass getCtClass(Class searchClass) throws NotFoundException{
        return _pool.get(searchClass.getName());
    }

    /**
     * Gets either a new or existing {@link ClassPool} for use in compiling javassist
     * classes. A new class path object is inserted in to the returned {@link ClassPool} using
     * the passed in <code>loader</code> instance if a new pool needs to be created.
     *
     * @param context
     *            The current execution context.
     * @param loader
     *            The {@link ClassLoader} instance to use - as returned by {@link #getClassLoader(com.feilong.lib.ognl.OgnlContext)}.
     * @return The existing or new {@link ClassPool} instance.
     */
    protected ClassPool getClassPool(OgnlContext context,EnhancedClassLoader loader){
        if (_pool != null){
            return _pool;
        }

        _pool = ClassPool.getDefault();
        _pool.insertClassPath(new LoaderClassPath(loader.getParent()));

        return _pool;
    }
}
