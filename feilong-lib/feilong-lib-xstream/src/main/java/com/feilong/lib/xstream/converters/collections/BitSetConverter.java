/*
 * Copyright (C) 2004 Joe Walnes.
 * Copyright (C) 2006, 2007, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 07. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.collections;

import java.util.BitSet;
import java.util.StringTokenizer;

import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;

/**
 * Converts a java.util.BitSet to XML, as a compact
 * comma delimited list of ones and zeros.
 *
 * @author Joe Walnes
 */
public class BitSetConverter implements Converter{

    @Override
    public boolean canConvert(Class type){
        return type == BitSet.class;
    }

    @Override
    public void marshal(Object source,HierarchicalStreamWriter writer,MarshallingContext context){
        BitSet bitSet = (BitSet) source;
        StringBuffer buffer = new StringBuffer();
        boolean seenFirst = false;
        for (int i = 0; i < bitSet.length(); i++){
            if (bitSet.get(i)){
                if (seenFirst){
                    buffer.append(',');
                }else{
                    seenFirst = true;
                }
                buffer.append(i);
            }
        }
        writer.setValue(buffer.toString());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        BitSet result = new BitSet();
        StringTokenizer tokenizer = new StringTokenizer(reader.getValue(), ",", false);
        while (tokenizer.hasMoreTokens()){
            int index = Integer.parseInt(tokenizer.nextToken());
            result.set(index);
        }
        return result;
    }
}
