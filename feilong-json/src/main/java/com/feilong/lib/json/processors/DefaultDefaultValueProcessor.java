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

package com.feilong.lib.json.processors;

import com.feilong.lib.json.JSONArray;
import com.feilong.lib.json.JSONNull;
import com.feilong.lib.json.util.JSONUtils;

/**
 * Base implementation for DefaultDefaultValueProcessor.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class DefaultDefaultValueProcessor implements DefaultValueProcessor{

    /**
     * Gets the default value.
     *
     * @param type
     *            the type
     * @return the default value
     */
    @Override
    public Object getDefaultValue(Class type){
        if (JSONUtils.isArray(type)){
            return new JSONArray();
        }else if (JSONUtils.isNumber(type)){
            if (JSONUtils.isDouble(type)){
                return new Double(0);
            }
            return new Integer(0);
        }else if (JSONUtils.isBoolean(type)){
            return Boolean.FALSE;
        }else if (JSONUtils.isString(type)){
            return "";
        }
        return JSONNull.getInstance();
    }
}