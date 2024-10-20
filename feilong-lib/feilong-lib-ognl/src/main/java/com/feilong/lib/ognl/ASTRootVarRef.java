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

import com.feilong.lib.ognl.enhance.ExpressionCompiler;

/**
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
public class ASTRootVarRef extends ASTVarRef{

    /**
     * 
     */
    private static final long serialVersionUID = 3373614874806333000L;

    public ASTRootVarRef(int id){
        super(id);
    }

    public ASTRootVarRef(OgnlParser p, int id){
        super(p, id);
    }

    @Override
    protected Object getValueBody(OgnlContext context,Object source) throws OgnlException{
        return context.getRoot();
    }

    @Override
    protected void setValueBody(OgnlContext context,Object target,Object value) throws OgnlException{
        context.setRoot(value);
    }

    @Override
    public String toString(){
        return "#root";
    }

    @Override
    public String toGetSourceString(OgnlContext context,Object target){
        if (target != null){
            _getterClass = target.getClass();
        }

        if (_getterClass != null){

            context.setCurrentType(_getterClass);
        }

        if (_parent == null || (_getterClass != null && _getterClass.isArray())){
            return "";
        }
        return ExpressionCompiler.getRootExpression(this, target, context);
    }

    @Override
    public String toSetSourceString(OgnlContext context,Object target){
        if (_parent == null || (_getterClass != null && _getterClass.isArray())){
            return "";
        }
        return "$3";
    }
}
