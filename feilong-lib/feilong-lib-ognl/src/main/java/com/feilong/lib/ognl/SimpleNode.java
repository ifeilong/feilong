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

import java.io.PrintWriter;
import java.io.Serializable;

import com.feilong.lib.ognl.enhance.ExpressionAccessor;

/**
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
public abstract class SimpleNode implements Node,Serializable{

    /**
     * 
     */
    private static final long  serialVersionUID = -574743279687880552L;

    protected Node             _parent;

    protected Node[]           _children;

    protected int              _id;

    protected OgnlParser       _parser;

    private boolean            _constantValueCalculated;

    private volatile boolean   _hasConstantValue;

    private Object             _constantValue;

    private ExpressionAccessor _accessor;

    public SimpleNode(int i){
        _id = i;
    }

    public SimpleNode(OgnlParser p, int i){
        this(i);
        _parser = p;
    }

    @Override
    public void jjtOpen(){
    }

    @Override
    public void jjtClose(){
    }

    @Override
    public void jjtSetParent(Node n){
        _parent = n;
    }

    @Override
    public Node jjtGetParent(){
        return _parent;
    }

    @Override
    public void jjtAddChild(Node n,int i){
        if (_children == null){
            _children = new Node[i + 1];
        }else if (i >= _children.length){
            Node c[] = new Node[i + 1];
            System.arraycopy(_children, 0, c, 0, _children.length);
            _children = c;
        }
        _children[i] = n;
    }

    @Override
    public Node jjtGetChild(int i){
        return _children[i];
    }

    @Override
    public int jjtGetNumChildren(){
        return (_children == null) ? 0 : _children.length;
    }

    /*
     * You can override these two methods in subclasses of SimpleNode to customize the way the node
     * appears when the tree is dumped. If your output uses more than one line you should override
     * toString(String), otherwise overriding toString() is probably all you need to do.
     */

    @Override
    public String toString(){
        return OgnlParserTreeConstants.jjtNodeName[_id];
    }

    // OGNL additions

    public String toString(String prefix){
        return prefix + OgnlParserTreeConstants.jjtNodeName[_id] + " " + toString();
    }

    @Override
    public String toGetSourceString(OgnlContext context,Object target){
        return toString();
    }

    @Override
    public String toSetSourceString(OgnlContext context,Object target){
        return toString();
    }

    /*
     * Override this method if you want to customize how the node dumps out its children.
     */

    public void dump(PrintWriter writer,String prefix){
        writer.println(toString(prefix));

        if (_children != null){
            for (Node element : _children){
                SimpleNode n = (SimpleNode) element;
                if (n != null){
                    n.dump(writer, prefix + "  ");
                }
            }
        }
    }

    public int getIndexInParent(){
        int result = -1;

        if (_parent != null){
            int icount = _parent.jjtGetNumChildren();

            for (int i = 0; i < icount; i++){
                if (_parent.jjtGetChild(i) == this){
                    result = i;
                    break;
                }
            }
        }

        return result;
    }

    public Node getNextSibling(){
        Node result = null;
        int i = getIndexInParent();

        if (i >= 0){
            int icount = _parent.jjtGetNumChildren();

            if (i < icount){
                result = _parent.jjtGetChild(i + 1);
            }
        }
        return result;
    }

    protected Object evaluateGetValueBody(OgnlContext context,Object source) throws OgnlException{
        context.setCurrentObject(source);
        context.setCurrentNode(this);

        if (!_constantValueCalculated){
            _constantValueCalculated = true;
            boolean constant = isConstant(context);

            if (constant){
                _constantValue = getValueBody(context, source);
            }

            _hasConstantValue = constant;
        }

        return _hasConstantValue ? _constantValue : getValueBody(context, source);
    }

    protected void evaluateSetValueBody(OgnlContext context,Object target,Object value) throws OgnlException{
        context.setCurrentObject(target);
        context.setCurrentNode(this);
        setValueBody(context, target, value);
    }

    @Override
    public final Object getValue(OgnlContext context,Object source) throws OgnlException{
        Object result = null;

        if (context.getTraceEvaluations()){

            EvaluationPool pool = OgnlRuntime.getEvaluationPool();
            Throwable evalException = null;
            Evaluation evaluation = pool.create(this, source);

            context.pushEvaluation(evaluation);
            try{
                result = evaluateGetValueBody(context, source);
            }catch (OgnlException ex){
                evalException = ex;
                throw ex;
            }catch (RuntimeException ex){
                evalException = ex;
                throw ex;
            }finally{
                Evaluation eval = context.popEvaluation();

                eval.setResult(result);
                if (evalException != null){
                    eval.setException(evalException);
                }
                if ((evalException == null) && (context.getRootEvaluation() == null) && !context.getKeepLastEvaluation()){
                    //                    pool.recycleAll(eval);
                }
            }
        }else{
            result = evaluateGetValueBody(context, source);
        }

        return result;
    }

    /**
     * Subclasses implement this method to do the actual work of extracting the appropriate value from the source object.
     * 
     * @param context
     *            the OgnlContext within which to perform the operation.
     * @param source
     *            the Object from which to get the value body.
     * @return the value body from the source (as appropriate within the provided context).
     * @throws OgnlException
     *             if the value body get fails.
     */
    protected abstract Object getValueBody(OgnlContext context,Object source) throws OgnlException;

    @Override
    public final void setValue(OgnlContext context,Object target,Object value) throws OgnlException{
        if (context.getTraceEvaluations()){
            EvaluationPool pool = OgnlRuntime.getEvaluationPool();
            Throwable evalException = null;
            Evaluation evaluation = pool.create(this, target, true);

            context.pushEvaluation(evaluation);
            try{
                evaluateSetValueBody(context, target, value);
            }catch (OgnlException ex){
                evalException = ex;
                ex.setEvaluation(evaluation);
                throw ex;
            }catch (RuntimeException ex){
                evalException = ex;
                throw ex;
            }finally{
                Evaluation eval = context.popEvaluation();

                if (evalException != null){
                    eval.setException(evalException);
                }
                if ((evalException == null) && (context.getRootEvaluation() == null) && !context.getKeepLastEvaluation()){
                    //                    pool.recycleAll(eval);
                }
            }
        }else{
            evaluateSetValueBody(context, target, value);
        }
    }

    /**
     * Subclasses implement this method to do the actual work of setting the appropriate value in the target object. The default
     * implementation throws an
     * <code>InappropriateExpressionException</code>, meaning that it cannot be a set expression.
     * 
     * @param context
     *            the OgnlContext within which to perform the operation.
     * @param target
     *            the Object upon which to set the value body.
     * @param value
     *            the Object representing the value body to apply to the target.
     * @throws OgnlException
     *             if the value body set fails.
     */
    protected void setValueBody(OgnlContext context,Object target,Object value) throws OgnlException{
        throw new InappropriateExpressionException(this);
    }

    /**
     * Returns true iff this node is constant without respect to the children.
     * 
     * @param context
     *            the OgnlContext within which to perform the operation.
     * @return true if this node is a constant, false otherwise.
     * @throws OgnlException
     *             if the check fails.
     */
    public boolean isNodeConstant(OgnlContext context) throws OgnlException{
        return false;
    }

    public boolean isConstant(OgnlContext context) throws OgnlException{
        return isNodeConstant(context);
    }

    public boolean isNodeSimpleProperty(OgnlContext context) throws OgnlException{
        return false;
    }

    public boolean isSimpleProperty(OgnlContext context) throws OgnlException{
        return isNodeSimpleProperty(context);
    }

    public boolean isSimpleNavigationChain(OgnlContext context) throws OgnlException{
        return isSimpleProperty(context);
    }

    public boolean isEvalChain(OgnlContext context) throws OgnlException{
        if (_children == null){
            return false;
        }
        for (Node child : _children){
            if (child instanceof SimpleNode){
                if (((SimpleNode) child).isEvalChain(context)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSequence(OgnlContext context) throws OgnlException{
        if (_children == null){
            return false;
        }
        for (Node child : _children){
            if (child instanceof SimpleNode){
                if (((SimpleNode) child).isSequence(context)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOperation(OgnlContext context) throws OgnlException{
        if (_children == null){
            return false;
        }
        for (Node child : _children){
            if (child instanceof SimpleNode){
                if (((SimpleNode) child).isOperation(context)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isChain(OgnlContext context) throws OgnlException{
        if (_children == null){
            return false;
        }
        for (Node child : _children){
            if (child instanceof SimpleNode){
                if (((SimpleNode) child).isChain(context)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSimpleMethod(OgnlContext context) throws OgnlException{
        return false;
    }

    protected boolean lastChild(OgnlContext context){
        return _parent == null || context.get("_lastChild") != null;
    }

    /**
     * This method may be called from subclasses' jjtClose methods. It flattens the tree under this node by eliminating any children that
     * are of the same class as this
     * node and copying their children to this node.
     */
    protected void flattenTree(){
        boolean shouldFlatten = false;
        int newSize = 0;

        for (Node element : _children){
            if (element.getClass() == getClass()){
                shouldFlatten = true;
                newSize += element.jjtGetNumChildren();
            }else{
                ++newSize;
            }
        }

        if (shouldFlatten){
            Node[] newChildren = new Node[newSize];
            int j = 0;

            for (Node c : _children){
                if (c.getClass() == getClass()){
                    for (int k = 0; k < c.jjtGetNumChildren(); ++k){
                        newChildren[j++] = c.jjtGetChild(k);
                    }

                }else{
                    newChildren[j++] = c;
                }
            }

            if (j != newSize){
                throw new Error("Assertion error: " + j + " != " + newSize);
            }

            _children = newChildren;
        }
    }

    @Override
    public ExpressionAccessor getAccessor(){
        return _accessor;
    }

    @Override
    public void setAccessor(ExpressionAccessor accessor){
        _accessor = accessor;
    }
}
