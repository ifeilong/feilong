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
package com.feilong.xml.xstream.converters;

import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.feilong.core.Validate;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * 简单的map 输出.
 * 
 * <p>
 * XStream maps Java class names and field names to XML tags or attributes. Unfortunately this mapping cannot be 1:1, since some characters
 * used for identifiers in Java are invalid in XML names.
 * </p>
 * 
 * <p>
 * Therefore {@link XStream} uses an {@link XmlFriendlyNameCoder} to replace these characters with a replacement. <br>
 * By default this {@link NameCoder} uses an underscore as escape character and has therefore to escape the underscore itself also.
 * </p>
 * 
 * 
 * <p>
 * You may provide a different configured instance of the {@link XmlFriendlyNameCoder} or a complete different implementation like the
 * {@link NoNameCoder} to prevent name coding at all.
 * </p>
 * 
 * <p>
 * However it is your responsibility then to ensure, that the resulting names are valid for XML.
 * </p>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * 
 * @see <a href="https://x-stream.github.io/faq.html#XML_double_underscores">Why do field names suddenly have double underscores in the
 *      generated XML?</a>
 * @since 1.10.7
 */
public class SimpleMapConverter implements Converter{

    /** Static instance. */
    public static final SimpleMapConverter INSTANCE = new SimpleMapConverter();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter,
     * com.thoughtworks.xstream.converters.MarshallingContext)
     */
    @Override
    public void marshal(Object value,HierarchicalStreamWriter writer,MarshallingContext context){
        Map<?, ?> map = (Map<?, ?>) value;

        for (Entry<?, ?> entry : map.entrySet()){
            Object key = entry.getKey();
            Validate.notNull(key, "key can't be null!");

            writer.startNode(Objects.toString(key));
            writer.setValue(Objects.toString(entry.getValue(), EMPTY));

            //writer.setValue("<![CDATA[" + Objects.toString(entry.getValue(), EMPTY) + "]]>")

            writer.endNode();
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
     * com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        Map<String, String> map = newLinkedHashMap();
        while (reader.hasMoreChildren()){
            reader.moveDown();
            map.put(reader.getNodeName(), reader.getValue());
            reader.moveUp();
        }
        return map;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz){
        return Map.class.isAssignableFrom(clazz);
    }

}