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
package com.feilong.tools.log;

import org.junit.Test;

import com.feilong.core.lang.ThreadUtil;

public class LogHelperTest{

    @Test
    public void test(){
        int all = 7;

        long allBeginTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < all; ++i){

            long beginTimeMillis = System.currentTimeMillis();
            ThreadUtil.sleepSeconds(2);

            System.out.println(LogHelper.getProcessLog(new ProcessLogParamEntity(i + 1, all, beginTimeMillis, allBeginTimeMillis)));//TODO:remove

        }
    }

    @Test
    public void test1(){
        int all = 5;

        long allBeginTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < all; ++i){

            long beginTimeMillis = System.currentTimeMillis();
            ThreadUtil.sleepSeconds(2);

            System.out.println(LogHelper.getProcessLog(new ProcessLogParamEntity(null, null, beginTimeMillis, allBeginTimeMillis)));//TODO:remove

        }
    }

}
