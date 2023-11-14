/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.lib.lang3;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * <p>
 * Helper methods for working with {@link Annotation} instances.
 * </p>
 *
 * <p>
 * This class contains various utility methods that make working with
 * annotations simpler.
 * </p>
 *
 * <p>
 * {@link Annotation} instances are always proxy objects; unfortunately
 * dynamic proxies cannot be depended upon to know how to implement certain
 * methods in the same manner as would be done by "natural" {@link Annotation}s.
 * The methods presented in this class can be used to avoid that possibility. It
 * is of course also possible for dynamic proxies to actually delegate their
 * e.g. {@link Annotation#equals(Object)}/{@link Annotation#hashCode()}/
 * {@link Annotation#toString()} implementations to {@link AnnotationUtils}.
 * </p>
 *
 * <p>
 * #ThreadSafe#
 * </p>
 *
 * @since 3.0
 */
public class AnnotationUtils{

    /**
     * A style that prints annotations as recommended.
     */
    private static final ToStringStyle TO_STRING_STYLE = new ToStringStyle(){

        /** Serialization version */
        private static final long serialVersionUID = 1L;

        {
            setDefaultFullDetail(true);
            setArrayContentDetail(true);
            setUseClassName(true);
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
            setContentStart("(");
            setContentEnd(")");
            setFieldSeparator(", ");
            setArrayStart("[");
            setArrayEnd("]");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getShortClassName(final Class<?> cls){
            for (final Class<?> iface : ClassUtils.getAllInterfaces(cls)){
                if (Annotation.class.isAssignableFrom(iface)){
                    return "@" + iface.getName();
                }
            }
            return "";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void appendDetail(final StringBuffer buffer,final String fieldName,Object value){
            if (value instanceof Annotation){
                value = AnnotationUtils.toString((Annotation) value);
            }
            super.appendDetail(buffer, fieldName, value);
        }

    };

    //-----------------------------------------------------------------------

    /**
     * <p>
     * Generate a string representation of an Annotation, as suggested by
     * {@link Annotation#toString()}.
     * </p>
     *
     * @param annotation
     *            the annotation of which a string representation is desired
     * @return the standard string representation of an annotation, not
     *         {@code null}
     */
    public static String toString(final Annotation annotation){
        final ToStringBuilder builder = new ToStringBuilder(annotation, TO_STRING_STYLE);
        for (final Method method : annotation.annotationType().getDeclaredMethods()){
            if (method.getParameterTypes().length > 0){
                continue; //wtf?
            }
            try{
                builder.append(method.getName(), method.invoke(annotation));
            }catch (final RuntimeException ex){
                throw ex;
            }catch (final Exception ex){
                throw new RuntimeException(ex);
            }
        }
        return builder.build();
    }



}
