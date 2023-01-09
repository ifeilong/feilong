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
package com.feilong.xml;

import com.feilong.xml.xstream.XStreamBuilder;
import com.feilong.xml.xstream.XStreamConfig;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.3
 */
class XStreamUtil{

    /** Don't let anyone instantiate this class. */
    private XStreamUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static String toXML(Object bean,XStreamConfig xStreamConfig){
        XStream xstream = XStreamBuilder.build(xStreamConfig);
        return xstream.toXML(bean);
    }

    @SuppressWarnings("unchecked")
    static <T> T toBean(String xml,XStreamConfig xStreamConfig){
        XStream xstream = XStreamBuilder.build(xStreamConfig);
        return (T) xstream.fromXML(xml);
    }
}
