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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.feilong.lib.lang3.Validate;
import com.feilong.test.AbstractTest;

/**
 * The Class CodeCreatorTest.
 */
public class SimpleSequenceTypeOrderCodeCreatorTest extends AbstractTest{

    /** The code creator. */
    private SequenceTypeOrderCodeCreator sequenceTypeOrderCodeCreator;

    /** The Constant SET. */
    private static final Set<String>     SET = newLinkedHashSet();

    //---------------------------------------------------------------

    /**
     * Before code creator test.
     */
    @Before
    public void beforeCodeCreatorTest(){
        SimpleSequenceTypeOrderCodeCreator simpleSequenceTypeOrderCodeCreator = new SimpleSequenceTypeOrderCodeCreator();

        sequenceTypeOrderCodeCreator = simpleSequenceTypeOrderCodeCreator;
    }

    //---------------------------------------------------------------

    @Test
    public void testCreate(){
        LOGGER.debug(sequenceTypeOrderCodeCreator.create(1000, 11));

        assertTrue(true);
    }

    /**
     * Test create order.
     */
    @Test
    public void testCreateOrder(){
        for (int i = 980, j = 2000; i < j; ++i){
            String value = sequenceTypeOrderCodeCreator.create(i, 11);

            Validate.isTrue(!SET.contains(value), "SET:%s contains(%s)", SET, value);

            SET.add(value);
        }
        assertTrue(true);
    }
    //---------------------------------------------------------------

    /**
     * Test create zero sequence.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateZeroSequence(){
        sequenceTypeOrderCodeCreator.create(-10, 8);
    }

    //---------------------------------------------------------------

    /**
     * Test create zero max length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateZeroMaxLength(){
        sequenceTypeOrderCodeCreator.create(5, 0);
    }

    /**
     * Test create max length 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMaxLength1(){
        sequenceTypeOrderCodeCreator.create(5, -10);
    }

    /**
     * Test create max length sequence length.
     */
    @Test
    public void testCreateMaxLengthSequenceLength(){
        assertEquals("5000000", sequenceTypeOrderCodeCreator.create(5000000, 3));
    }
}
