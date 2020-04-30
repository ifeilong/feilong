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

package com.feilong.lib.json.filters;

import com.feilong.lib.json.util.PropertyFilter;

/**
 * The Class FalsePropertyFilter.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class FalsePropertyFilter implements PropertyFilter{

    /**
     * Apply.
     *
     * @param source
     *            the source
     * @param name
     *            the name
     * @param value
     *            the value
     * @return true, if successful
     */
    @Override
    public boolean apply(Object source,String name,Object value){
        return false;
    }
}