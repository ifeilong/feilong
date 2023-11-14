/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.feilong.lib.org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;

import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.util.Args;
import com.feilong.lib.org.apache.http.util.LangUtils;

/**
 * {@link Credentials} implementation for Microsoft Windows platforms that includes
 * Windows specific attributes such as name of the domain the user belongs to.
 *
 * @since 4.0
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class NTCredentials implements Credentials,Serializable{

    private static final long     serialVersionUID = -7385699315228907265L;

    /** The user principal */
    private final NTUserPrincipal principal;

    /** Password */
    private final String          password;

    /** The host the authentication request is originating from. */
    private final String          workstation;

    /**
     * Constructor.
     * 
     * @param userName
     *            The user name. This should not include the domain to authenticate with.
     *            For example: "user" is correct whereas "DOMAIN&#x5c;user" is not.
     * @param password
     *            The password.
     * @param workstation
     *            The workstation the authentication request is originating from.
     *            Essentially, the computer name for this machine.
     * @param domain
     *            The domain to authenticate within.
     */
    public NTCredentials(final String userName, final String password, final String workstation, final String domain){
        super();
        Args.notNull(userName, "User name");
        this.principal = new NTUserPrincipal(domain, userName);
        this.password = password;
        if (workstation != null){
            this.workstation = workstation.toUpperCase(Locale.ROOT);
        }else{
            this.workstation = null;
        }
    }

    @Override
    public Principal getUserPrincipal(){
        return this.principal;
    }

    public String getUserName(){
        return this.principal.getUsername();
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    /**
     * Retrieves the name to authenticate with.
     *
     * @return String the domain these credentials are intended to authenticate with.
     */
    public String getDomain(){
        return this.principal.getDomain();
    }

    /**
     * Retrieves the workstation name of the computer originating the request.
     *
     * @return String the workstation the user is logged into.
     */
    public String getWorkstation(){
        return this.workstation;
    }

    @Override
    public int hashCode(){
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.principal);
        hash = LangUtils.hashCode(hash, this.workstation);
        return hash;
    }

    @Override
    public boolean equals(final Object o){
        if (this == o){
            return true;
        }
        if (o instanceof NTCredentials){
            final NTCredentials that = (NTCredentials) o;
            if (LangUtils.equals(this.principal, that.principal) && LangUtils.equals(this.workstation, that.workstation)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        final StringBuilder buffer = new StringBuilder();
        buffer.append("[principal: ");
        buffer.append(this.principal);
        buffer.append("][workstation: ");
        buffer.append(this.workstation);
        buffer.append("]");
        return buffer.toString();
    }

}
