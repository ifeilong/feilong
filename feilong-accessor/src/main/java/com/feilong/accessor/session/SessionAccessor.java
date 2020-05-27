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
package com.feilong.accessor.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 基于session的key是固定的寄存器实现.
 * 
 * <p>
 * <b>场景:</b> 适用于 <br>
 * 
 * spring xml 配置:
 * 
 * <pre>
{@code
  <bean name="memberDetailsSessionAccessor" class="com.feilong.accessor.session.SessionAccessor">
        <property name="key" value="member" />
    </bean>
}
 * </pre>
 * 
 * 代码使用:
 * 
 * <pre>
 * 
 * &#64;Autowired
 * &#64;Qualifier("memberDetailsSessionAccessor")
 * private SessionAccessor memberDetailsSessionAccessor;
 * 
 * private void putMemberInSession(MemberCommand memberCommand,HttpServletRequest request,HttpServletResponse response){
 *  ...
 *  SsoMemberCommand ssoMember = memberManager.memberCommandConvertToSsoMemberCommand(memberCommand);
 *  <span style="color:red">memberDetailsSessionAccessor.save(ssoMember, request);</span>
 *  ...
 * }
 * </pre>
 * 
 * 这种固定 key 的场景
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
public class SessionAccessor extends AbstractSessionKeyAccessor{

    /** The key. */
    private String key;

    //---------------------------------------------------------------

    /**
     * Instantiates a new session accessor.
     */
    public SessionAccessor(){
    }

    /**
     * Instantiates a new session accessor.
     *
     * @param key
     *            the key
     */
    public SessionAccessor(String key){
        this.key = key;
    }

    //---------------------------------------------------------------
    /**
     * Save.
     *
     * @param serializable
     *            the serializable
     * @param request
     *            the request
     */
    public void save(Serializable serializable,HttpServletRequest request){
        super.commonSave(key, serializable, request);
    }

    /**
     * 获得.
     *
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @return the t
     */
    public <T extends Serializable> T get(HttpServletRequest request){
        return super.get(key, request);
    }

    /**
     * 删除.
     *
     * @param request
     *            the request
     */
    public void remove(HttpServletRequest request){
        super.remove(key, request);
    }

    //---------------------------------------------------------------

    /**
     * 设置 key.
     *
     * @param key
     *            the key to set
     */
    public void setKey(String key){
        this.key = key;
    }

    /**
     * 获得 key.
     *
     * @return the key
     */
    public String getKey(){
        return key;
    }
}