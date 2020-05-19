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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.xml.dom.FormatTest;
import com.feilong.xml.dom.GetNodeNameAndStringValueMapTest;
import com.feilong.xml.dom.GetNodeAttributeValueAndStringValueMapTest;
import com.feilong.xml.xstream.ToBeanTest;
import com.feilong.xml.xstream.ToBeanXStreamConfigTest;
import com.feilong.xml.xstream.ToMapTest;
import com.feilong.xml.xstream.ToXmlMapIsPrettyPrintFalseTest;
import com.feilong.xml.xstream.ToXmlMapIsPrintTest;
import com.feilong.xml.xstream.ToXmlTest;
import com.feilong.xml.xstream.ToXmlWithConfigMapTest;
import com.feilong.xml.xstream.ToXmlWithConfigTest;

@RunWith(Suite.class)
@SuiteClasses({

                ToBeanTest.class,
                ToMapTest.class,
                ToBeanXStreamConfigTest.class,
                ToXmlMapIsPrintTest.class,
                ToXmlMapIsPrettyPrintFalseTest.class,
                ToXmlTest.class,
                ToXmlWithConfigMapTest.class,
                ToXmlWithConfigTest.class,

                GetNodeNameAndStringValueMapTest.class,
                GetNodeAttributeValueAndStringValueMapTest.class,

                FormatTest.class,

                FeilongDocumentBuilderTest.class,

        //
})
public class SuiteTests{

}
