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
package com.feilong.context.condition;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.context.Condition;

@ContextConfiguration("classpath:spring-SimpleParamNameConfigCondition.xml")
public class SimpleParamNameConfigConditionTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("simpleParamNameConfigCondition")
    private Condition condition;

    //---------------------------------------------------------------

    @Test
    public void test(){
        assertTrue(condition.canRun());
    }
}
