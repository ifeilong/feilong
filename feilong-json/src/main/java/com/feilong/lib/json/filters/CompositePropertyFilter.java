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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.feilong.lib.json.util.PropertyFilter;

/**
 * The Class CompositePropertyFilter.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class CompositePropertyFilter implements PropertyFilter{

    /** The filters. */
    private final List filters = new ArrayList();

    //---------------------------------------------------------------

    /**
     * Instantiates a new composite property filter.
     */
    public CompositePropertyFilter(){
        this(null);
    }

    /**
     * Instantiates a new composite property filter.
     *
     * @param filters
     *            the filters
     */
    public CompositePropertyFilter(List filters){
        if (filters != null){
            for (Iterator i = filters.iterator(); i.hasNext();){
                Object filter = i.next();
                if (filter instanceof PropertyFilter){
                    this.filters.add(filter);
                }
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 添加 property filter.
     *
     * @param filter
     *            the filter
     */
    public void addPropertyFilter(PropertyFilter filter){
        if (filter != null){
            filters.add(filter);
        }
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
        for (Iterator i = filters.iterator(); i.hasNext();){
            PropertyFilter filter = (PropertyFilter) i.next();
            if (filter.apply(source, name, value)){
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 删除 property filter.
     *
     * @param filter
     *            the filter
     */
    public void removePropertyFilter(PropertyFilter filter){
        if (filter != null){
            filters.remove(filter);
        }
    }
}