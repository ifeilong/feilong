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
package com.feilong.servlet.http.requestutil;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.feilong.test.AbstractTest;

public abstract class AbstractRequestTest extends AbstractTest {

    protected HttpServletResponse buildResponse() {
        return new HttpServletResponse() {

            @Override
            public void setLocale(Locale loc) {

            }

            @Override
            public void setContentType(String type) {

            }

            @Override
            public void setContentLength(int len) {

            }

            @Override
            public void setCharacterEncoding(String charset) {

            }

            @Override
            public void setBufferSize(int size) {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public void reset() {

            }

            @Override
            public boolean isCommitted() {

                return false;
            }

            @Override
            public PrintWriter getWriter() {

                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() {

                return null;
            }

            @Override
            public Locale getLocale() {

                return null;
            }

            @Override
            public String getContentType() {

                return null;
            }

            @Override
            public String getCharacterEncoding() {

                return null;
            }

            @Override
            public int getBufferSize() {

                return 0;
            }

            @Override
            public void flushBuffer() {

            }

            @Override
            public void setStatus(int sc, String sm) {

            }

            @Override
            public void setStatus(int sc) {

            }

            @Override
            public void setIntHeader(String name, int value) {

            }

            @Override
            public void setHeader(String name, String value) {

            }

            @Override
            public void setDateHeader(String name, long date) {

            }

            @Override
            public void sendRedirect(String location) {

            }

            @Override
            public void sendError(int sc, String msg) {

            }

            @Override
            public void sendError(int sc) {

            }

            @Override
            public int getStatus() {

                return 0;
            }

            @Override
            public Collection<String> getHeaders(String name) {

                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {

                return null;
            }

            @Override
            public String getHeader(String name) {

                return null;
            }

            @Override
            public String encodeUrl(String url) {

                return null;
            }

            @Override
            public String encodeURL(String url) {

                return null;
            }

            @Override
            public String encodeRedirectUrl(String url) {

                return null;
            }

            @Override
            public String encodeRedirectURL(String url) {

                return null;
            }

            @Override
            public boolean containsHeader(String name) {

                return false;
            }

            @Override
            public void addIntHeader(String name, int value) {

            }

            @Override
            public void addHeader(String name, String value) {

            }

            @Override
            public void addDateHeader(String name, long date) {

            }

            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public void setContentLengthLong(long len) {

            }
        };
    }

    protected HttpServletRequest buildRequest() {
        return new HttpServletRequest() {

            @Override
            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                return null;
            }

            @Override
            public AsyncContext startAsync() throws IllegalStateException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String env) {

            }

            @Override
            public void setAttribute(String name, Object o) {

            }

            @Override
            public void removeAttribute(String name) {

            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public boolean isAsyncSupported() {
                return false;
            }

            @Override
            public boolean isAsyncStarted() {
                return false;
            }

            @Override
            public ServletContext getServletContext() {

                return null;
            }

            @Override
            public int getServerPort() {

                return 0;
            }

            @Override
            public String getServerName() {

                return null;
            }

            @Override
            public String getScheme() {

                return null;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String path) {

                return null;
            }

            @Override
            public int getRemotePort() {

                return 0;
            }

            @Override
            public String getRemoteHost() {

                return null;
            }

            @Override
            public String getRemoteAddr() {

                return null;
            }

            @Override
            public String getRealPath(String path) {

                return null;
            }

            @Override
            public BufferedReader getReader() {

                return null;
            }

            @Override
            public String getProtocol() {

                return null;
            }

            @Override
            public String[] getParameterValues(String name) {

                return null;
            }

            @Override
            public Enumeration<String> getParameterNames() {

                return null;
            }

            @Override
            public Map<String, String[]> getParameterMap() {

                return null;
            }

            @Override
            public String getParameter(String name) {

                return null;
            }

            @Override
            public Enumeration<Locale> getLocales() {

                return null;
            }

            @Override
            public Locale getLocale() {

                return null;
            }

            @Override
            public int getLocalPort() {

                return 0;
            }

            @Override
            public String getLocalName() {

                return null;
            }

            @Override
            public String getLocalAddr() {

                return null;
            }

            @Override
            public ServletInputStream getInputStream() {
                return null;
            }

            @Override
            public DispatcherType getDispatcherType() {

                return null;
            }

            @Override
            public String getContentType() {

                return null;
            }

            @Override
            public int getContentLength() {

                return 0;
            }

            @Override
            public String getCharacterEncoding() {

                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {

                return null;
            }

            @Override
            public Object getAttribute(String name) {

                return null;
            }

            @Override
            public AsyncContext getAsyncContext() {

                return null;
            }

            @Override
            public void logout() {

            }

            @Override
            public void login(String username, String password) {

            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdValid() {

                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromUrl() {

                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromURL() {

                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromCookie() {

                return false;
            }

            @Override
            public Principal getUserPrincipal() {

                return null;
            }

            @Override
            public HttpSession getSession(boolean create) {

                return null;
            }

            @Override
            public HttpSession getSession() {

                return null;
            }

            @Override
            public String getServletPath() {

                return null;
            }

            @Override
            public String getRequestedSessionId() {

                return null;
            }

            @Override
            public StringBuffer getRequestURL() {

                return null;
            }

            @Override
            public String getRequestURI() {

                return null;
            }

            @Override
            public String getRemoteUser() {

                return null;
            }

            @Override
            public String getQueryString() {

                return null;
            }

            @Override
            public String getPathTranslated() {

                return null;
            }

            @Override
            public String getPathInfo() {

                return null;
            }

            @Override
            public Collection<Part> getParts() {
                return null;
            }

            @Override
            public Part getPart(String name) {
                return null;
            }

            @Override
            public String getMethod() {

                return null;
            }

            @Override
            public int getIntHeader(String name) {

                return 0;
            }

            @Override
            public Enumeration<String> getHeaders(String name) {

                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {

                return null;
            }

            @Override
            public String getHeader(String name) {

                return null;
            }

            @Override
            public long getDateHeader(String name) {

                return 0;
            }

            @Override
            public Cookie[] getCookies() {

                return null;
            }

            @Override
            public String getContextPath() {

                return null;
            }

            @Override
            public String getAuthType() {

                return null;
            }

            @Override
            public boolean authenticate(HttpServletResponse response) {
                return false;
            }

            @Override
            public long getContentLengthLong() {
                return 0;
            }

            @Override
            public String changeSessionId() {
                return null;
            }

            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) {
                return null;
            }
        };
    }

}
