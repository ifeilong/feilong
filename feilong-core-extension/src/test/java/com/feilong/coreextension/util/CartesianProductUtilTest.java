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

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.test.AbstractTest;

public class CartesianProductUtilTest extends AbstractTest{

    @Test
    public void test1(){
        Integer[] array1 = { 1, 2, 3 };
        Integer[] array2 = { 1, 2 };
        Integer[] array3 = { 5 };

        int nIterations = array1.length * array2.length * array3.length;
        for (int i = 0; i < nIterations; i++){
            LOGGER.debug("{} {} {}", array1[i % array1.length], array2[i % array2.length], array3[i % array3.length]);
        }
    }

    @Test
    public void test2(){
        Integer[] array1 = { 1, 2, 3 };
        Integer[] array2 = { 1, 2 };
        Integer[] array3 = { 5 };
        Integer[] array4 = { 4, 8 };

        List<List<Integer>> cartesianProduct = CartesianProductUtil.cartesianProduct(array1, array2, array3, array4);
        String format = JsonUtil.format(cartesianProduct, 0, 4);
        //LOGGER.debug(format);

        assertEquals(
                        "[[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8],[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8]]",
                        format);

    }

    @Test
    public void test3(){
        List<List<Integer>> result = CartesianProductUtil.cartesianProduct(//
                        toList(1, 2, 3),
                        toList(1, 2),
                        toList(5),
                        toList(4, 8));
        String format = JsonUtil.format(result, 0, 0);
        //LOGGER.debug(format);

        assertEquals(
                        "[[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8],[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8]]",
                        format);
    }
}
