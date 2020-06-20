// --------------------------------------------------------------------------
// Copyright (c) 1998-2004, Drew Davidson and Luke Blanshard
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the Drew Davidson nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
// BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
// AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
// DAMAGE.
// --------------------------------------------------------------------------
package com.feilong.lib.ognl;

import java.io.StringReader;
import java.util.Map;

/**
 * <P>
 * This class provides static methods for parsing and interpreting OGNL expressions.
 * </P>
 * <P>
 * The simplest use of the Ognl class is to get the value of an expression from an object, without
 * extra context or pre-parsing.
 * </P>
 *
 * <PRE>
 *
 * import ognl.Ognl; import ognl.OgnlException; try { result = Ognl.getValue(expression, root); }
 * catch (OgnlException ex) { // Report error or recover }
 *
 * </PRE>
 *
 * <P>
 * This will parse the expression given and evaluate it against the root object given, returning the
 * result. If there is an error in the expression, such as the property is not found, the exception
 * is encapsulated into an {@link com.feilong.lib.ognl.OgnlException OgnlException}.
 * </P>
 * <P>
 * Other more sophisticated uses of Ognl can pre-parse expressions. This provides two advantages: in
 * the case of user-supplied expressions it allows you to catch parse errors before evaluation and
 * it allows you to cache parsed expressions into an AST for better speed during repeated use. The
 * pre-parsed expression is always returned as an <CODE>Object</CODE> to simplify use for programs
 * that just wish to store the value for repeated use and do not care that it is an AST. If it does
 * care it can always safely cast the value to an <CODE>AST</CODE> type.
 * </P>
 * <P>
 * The Ognl class also takes a <I>context map</I> as one of the parameters to the set and get
 * methods. This allows you to put your own variables into the available namespace for OGNL
 * expressions. The default context contains only the <CODE>#root</CODE> and <CODE>#context</CODE>
 * keys, which are required to be present. The <CODE>addDefaultContext(Object, Map)</CODE> method
 * will alter an existing <CODE>Map</CODE> to put the defaults in. Here is an example that shows
 * how to extract the <CODE>documentName</CODE> property out of the root object and append a
 * string with the current user name in parens:
 * </P>
 *
 * <PRE>
 *
 * private Map context = new HashMap(); public void setUserName(String value) {
 * context.put("userName", value); } try { // get value using our own custom context map result =
 * Ognl.getValue("documentName + \" (\" + ((#userName == null) ? \"&lt;nobody&gt;\" : #userName) +
 * \")\"", context, root); } catch (OgnlException ex) { // Report error or recover }
 *
 * </PRE>
 *
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 * @version 27 June 1999
 */
public abstract class Ognl{

    private static volatile Integer expressionMaxLength = null;

    /**
     * Parses the given OGNL expression and returns a tree representation of the expression that can
     * be used by <CODE>Ognl</CODE> static methods.
     *
     * @param expression
     *            the OGNL expression to be parsed
     * @return a tree representation of the expression
     * @throws ExpressionSyntaxException
     *             if the expression is malformed
     * @throws OgnlException
     *             if there is a pathological environmental problem
     */
    public static Object parseExpression(String expression) throws OgnlException{
        final Integer currentExpressionMaxLength = Ognl.expressionMaxLength; // Limit access to the volatile variable to a single operation
        if (currentExpressionMaxLength != null && expression != null && expression.length() > currentExpressionMaxLength){
            throw new OgnlException(
                            "Parsing blocked due to security reasons!",
                            new SecurityException("This expression exceeded maximum allowed length: " + expression));
        }
        try{
            OgnlParser parser = new OgnlParser(new StringReader(expression));
            return parser.topLevelExpression();
        }catch (ParseException e){
            throw new ExpressionSyntaxException(expression, e);
        }catch (TokenMgrError e){
            throw new ExpressionSyntaxException(expression, e);
        }
    }

    /**
     * Appends the standard naming context for evaluating an OGNL expression into the context given
     * so that cached maps can be used as a context.
     *
     * @param root
     *            the root of the object graph
     * @param context
     *            the context to which OGNL context will be added.
     * @return Context Map with the keys <CODE>root</CODE> and <CODE>context</CODE> set
     *         appropriately
     * @deprecated will be removed soon
     */
    @Deprecated
    private static Map addDefaultContext(Object root,Map context){
        return addDefaultContext(root, null, null, null, context);
    }

    /**
     * Appends the standard naming context for evaluating an OGNL expression into the context given
     * so that cached maps can be used as a context.
     *
     * @param root
     *            the root of the object graph
     * @param memberAccess
     *            Definition for handling private/protected access.
     * @param classResolver
     *            The class loading resolver that should be used to resolve class references.
     * @param converter
     *            The type converter to be used by default.
     * @param context
     *            Default context to use, if not an {@link OgnlContext} will be dumped into
     *            a new {@link OgnlContext} object.
     * @return Context Map with the keys <CODE>root</CODE> and <CODE>context</CODE> set
     *         appropriately
     */
    private static Map addDefaultContext(
                    Object root,
                    MemberAccess memberAccess,
                    ClassResolver classResolver,
                    TypeConverter converter,
                    Map context){
        OgnlContext result;

        if (context instanceof OgnlContext){
            result = (OgnlContext) context;
        }else{
            result = new OgnlContext(memberAccess, classResolver, converter, context);
        }

        result.setRoot(root);
        return result;
    }

    /**
     * Gets the currently configured {@link TypeConverter} for the given context - if any.
     *
     * @param context
     *            The context to get the converter from.
     *
     * @return The converter - or null if none found.
     */
    private static TypeConverter getTypeConverter(Map context){
        if (context instanceof OgnlContext){
            return ((OgnlContext) context).getTypeConverter();
        }
        return null;
    }

    /**
     * Evaluates the given OGNL expression tree to extract a value from the given root object. The
     * default context is set for the given context and root via <CODE>addDefaultContext()</CODE>.
     *
     * @param tree
     *            the OGNL expression tree to evaluate, as returned by parseExpression()
     * @param context
     *            the naming context for the evaluation
     * @param root
     *            the root object for the OGNL expression
     * @return the result of evaluating the expression
     * @throws MethodFailedException
     *             if the expression called a method which failed
     * @throws NoSuchPropertyException
     *             if the expression referred to a nonexistent property
     * @throws InappropriateExpressionException
     *             if the expression can't be used in this context
     * @throws OgnlException
     *             if there is a pathological environmental problem
     */
    public static Object getValue(Object tree,Map context,Object root) throws OgnlException{
        return getValue(tree, context, root, null);
    }

    /**
     * Evaluates the given OGNL expression tree to extract a value from the given root object. The
     * default context is set for the given context and root via <CODE>addDefaultContext()</CODE>.
     *
     * @param tree
     *            the OGNL expression tree to evaluate, as returned by parseExpression()
     * @param context
     *            the naming context for the evaluation
     * @param root
     *            the root object for the OGNL expression
     * @param resultType
     *            the converted type of the resultant object, using the context's type converter
     * @return the result of evaluating the expression
     * @throws MethodFailedException
     *             if the expression called a method which failed
     * @throws NoSuchPropertyException
     *             if the expression referred to a nonexistent property
     * @throws InappropriateExpressionException
     *             if the expression can't be used in this context
     * @throws OgnlException
     *             if there is a pathological environmental problem
     */
    private static Object getValue(Object tree,Map context,Object root,Class resultType) throws OgnlException{
        Object result;
        OgnlContext ognlContext = (OgnlContext) addDefaultContext(root, context);

        Node node = (Node) tree;

        if (node.getAccessor() != null){
            result = node.getAccessor().get(ognlContext, root);
        }else{
            result = node.getValue(ognlContext, root);
        }

        if (resultType != null){
            result = getTypeConverter(context).convertValue(context, root, null, null, result, resultType);
        }
        return result;
    }

    /**
     * Evaluates the given OGNL expression tree to insert a value into the object graph rooted at
     * the given root object. The default context is set for the given context and root via <CODE>addDefaultContext()</CODE>.
     *
     * @param tree
     *            the OGNL expression tree to evaluate, as returned by parseExpression()
     * @param context
     *            the naming context for the evaluation
     * @param root
     *            the root object for the OGNL expression
     * @param value
     *            the value to insert into the object graph
     * @throws MethodFailedException
     *             if the expression called a method which failed
     * @throws NoSuchPropertyException
     *             if the expression referred to a nonexistent property
     * @throws InappropriateExpressionException
     *             if the expression can't be used in this context
     * @throws OgnlException
     *             if there is a pathological environmental problem
     */
    public static void setValue(Object tree,Map context,Object root,Object value) throws OgnlException{
        OgnlContext ognlContext = (OgnlContext) addDefaultContext(root, context);
        Node n = (Node) tree;

        if (n.getAccessor() != null){
            n.getAccessor().set(ognlContext, root, value);
            return;
        }

        n.setValue(ognlContext, root, value);
    }

    /**
     * Evaluates the given OGNL expression to insert a value into the object graph rooted at the
     * given root object given the context.
     *
     * @param expression
     *            the OGNL expression to be parsed
     * @param root
     *            the root object for the OGNL expression
     * @param context
     *            the naming context for the evaluation
     * @param value
     *            the value to insert into the object graph
     * @throws MethodFailedException
     *             if the expression called a method which failed
     * @throws NoSuchPropertyException
     *             if the expression referred to a nonexistent property
     * @throws InappropriateExpressionException
     *             if the expression can't be used in this context
     * @throws OgnlException
     *             if there is a pathological environmental problem
     */
    public static void setValue(String expression,Map context,Object root,Object value) throws OgnlException{
        setValue(parseExpression(expression), context, root, value);
    }

    /** You can't make one of these. */
    private Ognl(){
    }
}
