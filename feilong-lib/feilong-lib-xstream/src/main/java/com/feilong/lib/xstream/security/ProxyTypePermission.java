/*
 * Copyright (C) 2014 XStream Committers.
 * All rights reserved.
 *
 * Created on 19. January 2014 by Joerg Schaible
 */
package com.feilong.lib.xstream.security;

import java.lang.reflect.Proxy;

import com.feilong.lib.xstream.mapper.DynamicProxyMapper;

/**
 * Permission for any array type.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.4.7
 */
public class ProxyTypePermission implements TypePermission{

    /**
     * @since 1.4.7
     */
    public static final TypePermission PROXIES = new ProxyTypePermission();

    @Override
    public boolean allows(final Class type){
        return type != null && (Proxy.isProxyClass(type) || type == DynamicProxyMapper.DynamicProxy.class);
    }

    @Override
    public int hashCode(){
        return 17;
    }

    @Override
    public boolean equals(final Object obj){
        return obj != null && obj.getClass() == ProxyTypePermission.class;
    }
}
