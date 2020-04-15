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
package com.feilong.taglib.display.httpconcat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.taglib.display.httpconcat.builder.TemplateFactoryTest;
import com.feilong.taglib.display.httpconcat.builder.VersionRebuilderTest;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParamEqualsTest;
import com.feilong.taglib.display.httpconcat.handler.ConcatLinkResolverTest;
import com.feilong.taglib.display.httpconcat.handler.DomainFormatterParameterizedTest;
import com.feilong.taglib.display.httpconcat.handler.ItemSrcExtractorParameterizedTest;
import com.feilong.taglib.display.httpconcat.handler.ItemSrcExtractorParseTest;
import com.feilong.taglib.display.httpconcat.handler.ItemSrcListResolverTest;
import com.feilong.taglib.display.httpconcat.handler.RootFormatterParameterizedTest;
import com.feilong.taglib.display.httpconcat.handler.TypeFormatterTest;
import com.feilong.taglib.display.httpconcat.handler.VersionFormatterTest;

@RunWith(Suite.class)
@SuiteClasses({ //

                RootFormatterParameterizedTest.class,
                DomainFormatterParameterizedTest.class,

                TypeFormatterTest.class,
                VersionRebuilderTest.class,

                HttpConcatParamEqualsTest.class,

                ItemSrcExtractorParseTest.class,
                ItemSrcExtractorParameterizedTest.class,

                TemplateFactoryTest.class,
                ConcatLinkResolverTest.class,
                VersionFormatterTest.class,

                HttpConcatUtilConcatTest.class,

                ItemSrcListResolverTest.class,
        //                
})
public class FeiLongConcatSuiteTests{

}
