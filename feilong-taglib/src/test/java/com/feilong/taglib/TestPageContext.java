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
package com.feilong.taglib;

import java.io.IOException;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class TestPageContext extends PageContext{

    @Override
    public void initialize(
                    Servlet servlet,
                    ServletRequest request,
                    ServletResponse response,
                    String errorPageURL,
                    boolean needsSession,
                    int bufferSize,
                    boolean autoFlush) throws IOException,IllegalStateException,IllegalArgumentException{

    }

    @Override
    public void release(){

    }

    @Override
    public HttpSession getSession(){

        return null;
    }

    @Override
    public Object getPage(){

        return null;
    }

    @Override
    public ServletRequest getRequest(){

        return null;
    }

    @Override
    public ServletResponse getResponse(){

        return null;
    }

    @Override
    public Exception getException(){

        return null;
    }

    @Override
    public ServletConfig getServletConfig(){

        return null;
    }

    @Override
    public ServletContext getServletContext(){

        return null;
    }

    @Override
    public void forward(String relativeUrlPath) throws ServletException,IOException{

    }

    @Override
    public void include(String relativeUrlPath) throws ServletException,IOException{

    }

    @Override
    public void include(String relativeUrlPath,boolean flush) throws ServletException,IOException{

    }

    @Override
    public void handlePageException(Exception e) throws ServletException,IOException{

    }

    @Override
    public void handlePageException(Throwable t) throws ServletException,IOException{

    }

    @Override
    public void setAttribute(String name,Object value){

    }

    @Override
    public void setAttribute(String name,Object value,int scope){

    }

    @Override
    public Object getAttribute(String name){

        return null;
    }

    @Override
    public Object getAttribute(String name,int scope){

        return null;
    }

    @Override
    public Object findAttribute(String name){

        return null;
    }

    @Override
    public void removeAttribute(String name){

    }

    @Override
    public void removeAttribute(String name,int scope){

    }

    @Override
    public int getAttributesScope(String name){

        return 0;
    }

    @Override
    public Enumeration<String> getAttributeNamesInScope(int scope){

        return null;
    }

    @Override
    public JspWriter getOut(){

        return null;
    }

    @Override
    public ExpressionEvaluator getExpressionEvaluator(){

        return null;
    }

    @Override
    public VariableResolver getVariableResolver(){

        return null;
    }

    @Override
    public ELContext getELContext(){

        return null;
    }

}
