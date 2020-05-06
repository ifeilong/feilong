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
package com.feilong.lib.json;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.feilong.lib.json.util.JSONUtils;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class ToStringUtil{

    /** Don't let anyone instantiate this class. */
    private ToStringUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static String toString(Map properties,int indentFactor,int indent){
        StringBuffer sb = new StringBuffer("{");

        int newindent = indent + indentFactor;
        Iterator keys = properties.keySet().iterator();

        if (properties.size() == 1){
            Object o = keys.next();
            sb.append(JSONUtils.quote(o.toString()));
            sb.append(": ");
            sb.append(JSONUtils.valueToString(properties.get(o), indentFactor, indent));
        }else{
            while (keys.hasNext()){
                Object o = keys.next();
                if (sb.length() > 1){
                    sb.append(",\n");
                }else{
                    sb.append('\n');
                }
                for (int i = 0; i < newindent; i += 1){
                    sb.append(' ');
                }
                sb.append(JSONUtils.quote(o.toString()));
                sb.append(": ");
                sb.append(JSONUtils.valueToString(properties.get(o), indentFactor, newindent));
            }
            if (sb.length() > 1){
                sb.append('\n');
                for (int i = 0; i < indent; i += 1){
                    sb.append(' ');
                }
            }
            for (int i = 0; i < indent; i += 1){
                sb.insert(0, ' ');
            }
        }

        sb.append('}');
        return sb.toString();
    }

    static String toString(Map properties){
        StringBuffer sb = new StringBuffer("{");
        try{

            Iterator keys = properties.keySet().iterator();
            while (keys.hasNext()){
                if (sb.length() > 1){
                    sb.append(',');
                }
                Object o = keys.next();
                sb.append(JSONUtils.quote(o.toString()));
                sb.append(':');

                sb.append(JSONUtils.valueToString(properties.get(o)));
            }
            sb.append('}');
            return sb.toString();
        }catch (Exception e){
            return null;
        }
    }

    static String toString(List elements2,int indentFactor,int indent){
        StringBuffer sb = new StringBuffer("[");

        int len = elements2.size();
        if (len == 1){
            sb.append(JSONUtils.valueToString(elements2.get(0), indentFactor, indent));
        }else{
            int newindent = indent + indentFactor;
            sb.append('\n');
            for (int i = 0; i < len; i += 1){
                if (i > 0){
                    sb.append(",\n");
                }
                for (int j = 0; j < newindent; j += 1){
                    sb.append(' ');
                }
                sb.append(JSONUtils.valueToString(elements2.get(i), indentFactor, newindent));
            }
            sb.append('\n');
            for (int i = 0; i < indent; i += 1){
                sb.append(' ');
            }
            for (int i = 0; i < indent; i += 1){
                sb.insert(0, ' ');
            }
        }

        sb.append(']');
        return sb.toString();
    }
}
