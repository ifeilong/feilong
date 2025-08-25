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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * build {@link Transformer}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see OutputKeys
 * @since 3.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class TransformerBuilder{

    public static final Transformer DEFAULT_TRANSFORMER = build();

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @return the transformer
     * @see javax.xml.transform.Transformer#setOutputProperty(String, String)
     */
    private static Transformer build(){
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try{
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            return transformer;
        }catch (TransformerException e){
            throw new UncheckedXmlParseException(e);
        }
    }
}
