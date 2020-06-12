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

import static com.feilong.core.bean.ConvertUtil.toMap;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.3
 */
public class CustomJavaIdentifierTransformerTest{

    private final CustomJavaIdentifierTransformer customJavaIdentifierTransformer = new CustomJavaIdentifierTransformer(
                    toMap("name", "Name"));

    @Test
    public void testCustomJavaIdentifierTransformerTest(){
        assertEquals("Name", customJavaIdentifierTransformer.transformToJavaIdentifier("name"));
    }

    @Test
    public void testCustomJavaIdentifierTransformerTest2(){
        assertEquals("name1", customJavaIdentifierTransformer.transformToJavaIdentifier("name1"));
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void test(){
        new CustomJavaIdentifierTransformer(null);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void test1(){
        new CustomJavaIdentifierTransformer(emptyMap());
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testCustomJavaIdentifierTransformerTestNull(){
        customJavaIdentifierTransformer.transformToJavaIdentifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomJavaIdentifierTransformerTestEmpty(){
        customJavaIdentifierTransformer.transformToJavaIdentifier("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomJavaIdentifierTransformerTestBlank(){
        customJavaIdentifierTransformer.transformToJavaIdentifier(" ");
    }
}
