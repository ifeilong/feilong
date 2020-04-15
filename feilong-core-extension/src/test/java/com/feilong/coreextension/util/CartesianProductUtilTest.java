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
package com.feilong.coreextension.util;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;

import static com.feilong.core.bean.ConvertUtil.toList;

/**
 * The Class CartesianProductTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.2
 */
public class CartesianProductUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CartesianProductUtilTest.class);

    /**
     * TestCollectionUtilTest.
     */
    @Test
    public void testCollectionUtilTest1(){
        Integer[] array1 = { 1, 2, 3 };
        Integer[] array2 = { 1, 2 };
        Integer[] array3 = { 5 };

        int nIterations = array1.length * array2.length * array3.length;
        for (int i = 0; i < nIterations; i++){
            LOGGER.debug("{} {} {}", array1[i % array1.length], array2[i % array2.length], array3[i % array3.length]);
        }
    }

    /**
     * Test collection util test11.
     */
    @Test
    public void testCollectionUtilTest11(){
        Integer[] array1 = { 1, 2, 3 };
        Integer[] array2 = { 1, 2 };
        Integer[] array3 = { 5 };
        Integer[] array4 = { 4, 8 };

        LOGGER.debug(JsonUtil.format(CartesianProductUtil.cartesianProduct(array1, array2, array3, array4), 0, 4));
        LOGGER.debug(JsonUtil.format(CartesianProductUtil.cartesianProduct(array1)));

    }

    /**
     * Test collection util test11.
     */
    @Test
    public void testCollectionUtilTest112(){
        List<List<Integer>> result = CartesianProductUtil.cartesianProduct(toList(1, 2, 3), toList(1, 2), toList(5), toList(4, 8));
        LOGGER.debug(JsonUtil.format(result, 0, 4));
    }
}
