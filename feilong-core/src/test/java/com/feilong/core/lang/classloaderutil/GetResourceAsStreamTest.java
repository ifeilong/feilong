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
package com.feilong.core.lang.classloaderutil;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.core.lang.ClassLoaderUtil;

/**
 * The Class ClassLoaderUtilGetResourceAsStreamTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class GetResourceAsStreamTest{

    /**
     * Test get resource as stream null resource name.
     */
    @Test(expected = NullPointerException.class)
    public void testGetResourceAsStreamNullResourceName(){
        ClassLoaderUtil.getResourceAsStream(null, this.getClass());
    }

    /**
     * Test get resource as stream empty resource name.
     */
    @Test
    public void testGetResourceAsStreamEmptyResourceName(){
        assertThat(ClassLoaderUtil.getResourceAsStream("", this.getClass()), is(notNullValue()));
    }

    /**
     * Test get resource as stream not exist.
     */
    @Test
    public void testGetResourceAsStreamNotExist(){
        assertEquals(null, ClassLoaderUtil.getResourceAsStream("notexist", this.getClass()));
    }
}
