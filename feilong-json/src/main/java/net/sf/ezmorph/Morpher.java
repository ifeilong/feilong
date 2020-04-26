/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.ezmorph;

/**
 * Marker interface for morphers.<br>
 * All implementations must have a <code>morph( Object value )</code> method
 * that returns the appropiate morphed value.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public interface Morpher{

    /**
     * Returns the target Class for conversion.
     *
     * @return the target Class for conversion.
     */
    Class morphsTo();

    /**
     * Returns true if the Morpher supports conversion from this Class.
     *
     * @param clazz
     *            the source Class
     * @return true if clazz is supported by this morpher, false otherwise.
     */
    boolean supports(Class clazz);
}