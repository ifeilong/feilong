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
package com.feilong.temp;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.util.RandomUtil;
import com.feilong.net.httpclient4.HttpClientUtil;
import com.feilong.test.AbstractTest;

public class HttpClientCodeTest extends AbstractTest{

    @Test
    public void test(){
        for (int i = 0; i < 100; ++i){
            //  for (int i = 0; i < 1000000; ++i){
            update("123" + i);
        }
    }

    @Test
    public void testThread(){
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        ThreadUtil.execute(new Runnable(){

            @Override
            public void run(){
                while (true){

                    update("123" + RandomUtil.createRandomWithLength(1000));

                    //int incrementAndGet = atomicInteger.incrementAndGet();

                    //LOGGER.info("" + incrementAndGet);
                }
            }

        }, 4);
    }

    //---------------------------------------------------------------

    private static void update(String code){
        String uri = "http://localhost:3001/user?code=" + code;
        assertEquals(code, HttpClientUtil.get(uri));
    }

}
