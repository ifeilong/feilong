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
package com.feilong.json;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.json.format.FormatSuiteTests;
import com.feilong.json.processor.AlbumBrowseRecordJsonTest;
import com.feilong.json.processor.CrmAddpointCommandJsonTest;
import com.feilong.json.processor.JsonValueProcessorSuiteTests;
import com.feilong.json.tobean.ToBeanSuiteTests;
import com.feilong.json.tostring.ToStringSuiteTests;
import com.feilong.json.transformer.JavaIdentifierTransformerSuiteTests;
import com.feilong.lib.json.util.PropertySetStrategyTest;

@RunWith(Suite.class)
@SuiteClasses({ //

                //JsonHelperTest.class,
                JsonHelperIsKeyValueJsonStringTest.class,
                JsonHelperIsCommonStringTest.class,

                JsonValueProcessorSuiteTests.class,

                FormatSuiteTests.class,
                ToStringSuiteTests.class,

                ToBeanSuiteTests.class,

                JavaIdentifierTransformerSuiteTests.class,

                PropertySetStrategyTest.class,

                AlbumBrowseRecordJsonTest.class,
                CrmAddpointCommandJsonTest.class

//
})
public class SuiteTests{

}
