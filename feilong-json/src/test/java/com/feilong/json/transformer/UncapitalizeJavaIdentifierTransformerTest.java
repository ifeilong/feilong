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
package com.feilong.json.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.lib.json.util.JavaIdentifierTransformer;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.3
 */
public class UncapitalizeJavaIdentifierTransformerTest{

    private final JavaIdentifierTransformer uncapitalize = UncapitalizeJavaIdentifierTransformer.UNCAPITALIZE;

    @Test
    public void testUncapitalizeJavaIdentifierTransformerTest(){
        assertEquals("name", uncapitalize.transformToJavaIdentifier("Name"));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testUncapitalizeJavaIdentifierTransformerTestNull(){
        uncapitalize.transformToJavaIdentifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUncapitalizeJavaIdentifierTransformerTestEmpty(){
        uncapitalize.transformToJavaIdentifier("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUncapitalizeJavaIdentifierTransformerTestBlank(){
        uncapitalize.transformToJavaIdentifier(" ");
    }
}
