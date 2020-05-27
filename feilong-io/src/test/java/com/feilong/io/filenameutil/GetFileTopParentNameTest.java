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
package com.feilong.io.filenameutil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.io.FilenameUtil;
import com.feilong.test.AbstractTest;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.0
 */
public class GetFileTopParentNameTest extends AbstractTest{

    /**
     * Test get file top parent name.
     */
    @Test
    public void testGetFileTopParentName(){
        assertEquals("E:/", FilenameUtil.getFileTopParentName("E:/"));
        assertEquals(
                        "mp2-product",
                        FilenameUtil.getFileTopParentName(
                                        "mp2-product\\mp2-product-impl\\src\\main\\java\\com\\mp2\\rpc\\impl\\item\\repo\\package-info.java"));
        assertEquals(
                        "mp2-product",
                        FilenameUtil.getFileTopParentName(
                                        "mp2-product\\mp2-product-impl\\src\\..\\java\\com\\mp2\\rpc\\impl\\item\\repo\\package-info.java"));
        assertEquals("package-info.java", FilenameUtil.getFileTopParentName("package-info.java"));
    }

}
