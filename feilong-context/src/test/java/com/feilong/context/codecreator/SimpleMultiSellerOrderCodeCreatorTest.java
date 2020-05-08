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
package com.feilong.context.codecreator;

import static com.feilong.core.util.CollectionsUtil.newLinkedHashSet;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;

import com.feilong.test.AbstractTest;

public class SimpleMultiSellerOrderCodeCreatorTest extends AbstractTest{

    /** The code creator. */
    private MultiSellerOrderCodeCreator multiSellerOrderCodeCreator;

    /** The Constant SET. */
    private static final Set<String>    SET = newLinkedHashSet();

    //---------------------------------------------------------------

    /**
     * Before code creator test.
     */
    @Before
    public void beforeCodeCreatorTest(){
        SimpleMultiSellerOrderCodeCreator simpleMultiSellerOrderCodeCreator = new SimpleMultiSellerOrderCodeCreator();

        SimpleMultiSellerOrderCodeCreatorConfig simpleMultiSellerOrderCodeCreatorConfig = new SimpleMultiSellerOrderCodeCreatorConfig();
        //simpleMultiSellerOrderCodeCreatorConfig.setPrefix("TB");
        simpleMultiSellerOrderCodeCreatorConfig.setIsDebug(true);

        simpleMultiSellerOrderCodeCreator.setSimpleMultiSellerOrderCodeCreatorConfig(simpleMultiSellerOrderCodeCreatorConfig);

        multiSellerOrderCodeCreator = simpleMultiSellerOrderCodeCreator;
    }

    //---------------------------------------------------------------

    @Test
    public void createOrderCode(){
        LOGGER.debug(multiSellerOrderCodeCreator.create(35191L, 555L));
        assertTrue(true);
    }

    @Test
    public void createOrderCode12(){
        LOGGER.debug(multiSellerOrderCodeCreator.create(1161L, 5555555L));
        assertTrue(true);
    }

    @Test
    public void createOrderCode1(){
        for (int i = 0, j = 100; i < j; ++i){
            String value = multiSellerOrderCodeCreator.create(35191L, 555L);

            Validate.isTrue(!SET.contains(value), "SET:%s contains(%s)", SET, value);

            SET.add(value);
        }
        assertTrue(true);
    }

    @Test
    public void createReturnOrderCode(){
        LOGGER.debug(multiSellerOrderCodeCreator.create(111121L, 5555555L));
        assertTrue(true);
    }

}
