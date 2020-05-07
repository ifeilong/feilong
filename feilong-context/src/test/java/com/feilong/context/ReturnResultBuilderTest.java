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
package com.feilong.context;

import org.junit.Test;

public class ReturnResultBuilderTest{

    /**
     * Test return result builder test null.
     */
    
    @Test(expected = NullPointerException.class)
    public void testReturnResultBuilderTestNull(){
        ReturnResultBuilder.buildFailureResult(null);
    }

    /**
     * Test return result builder test empty.
     */
    
    @Test(expected = IllegalArgumentException.class)
    public void testReturnResultBuilderTestEmpty(){
        ReturnResultBuilder.buildFailureResult("");
    }

    /**
     * Test return result builder test blank.
     */
    
    @Test(expected = IllegalArgumentException.class)
    public void testReturnResultBuilderTestBlank(){
        ReturnResultBuilder.buildFailureResult(" ");
    }
}
