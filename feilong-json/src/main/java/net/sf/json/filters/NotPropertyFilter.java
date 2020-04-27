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

package net.sf.json.filters;

import net.sf.json.util.PropertyFilter;

/**
 * The Class NotPropertyFilter.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class NotPropertyFilter implements PropertyFilter{

    /** The filter. */
    private final PropertyFilter filter;

    /**
     * Instantiates a new not property filter.
     *
     * @param filter
     *            the filter
     */
    public NotPropertyFilter(PropertyFilter filter){
        this.filter = filter;
    }

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
        if (filter != null){
            return !filter.apply(source, name, value);
        }
        return false;
    }
}