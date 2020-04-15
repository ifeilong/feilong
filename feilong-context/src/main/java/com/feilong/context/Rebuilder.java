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
package com.feilong.context;

/**
 * 对象构造之后,再加工.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @see org.apache.commons.lang3.builder.Builder
 * @see "javax.enterprise.inject.spi.AfterBeanDiscovery"
 * @see "javax.enterprise.inject.spi.AfterDeploymentValidation"
 * @see "org.springframework.security.access.AfterInvocationProvider"
 * @see "org.springframework.aop.AfterAdvice"
 * @see "org.hibernate.resource.transaction.backend.jta.internal.synchronization.AfterCompletionAction"
 * @since 1.11.4
 */
public interface Rebuilder<T> {

    /**
     * 重新加工.
     *
     * @param t
     *            the t
     * @return the http request
     */
    T rebuild(T t);
}
