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
package com.feilong.coreextension.lang;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.lang.thread.PartitionRunnableBuilder;
import com.feilong.core.lang.thread.PartitionThreadEntity;

/**
 * The Class ThreadUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class ThreadUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtilTest.class);

    @Test
    public void testThreadUtilTest() throws InterruptedException{

        LOGGER.debug("start");
        Thread thread = new Thread(buildRunnable("feilong", null));

        LOGGER.debug("thread.getState :[{}]", thread.getState());

        thread.run();
        LOGGER.debug("thread.run state:[{}]", thread.getState());

        thread.start();
        LOGGER.debug("thread.start state:[{}]", thread.getState());

        thread.join();

        LOGGER.debug("end");

    }

    /**
     * TestThreadUtilTest.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testThreadUtilTest1() throws InterruptedException{
        LOGGER.debug("begin");

        Thread t0 = new Thread(buildRunnable("A", buildList(0, 1000)));
        Thread t1 = new Thread(buildRunnable("B", buildList(2000, 3000)));
        Thread t2 = new Thread(buildRunnable("C", buildList(3000, 4000)));

        t2.setPriority(Thread.MAX_PRIORITY);
        t0.setPriority(Thread.MIN_PRIORITY);
        t1.setPriority(Thread.MIN_PRIORITY);

        t0.start();
        t1.start();
        t2.start();

        // 执行完清空操作后，再进行后续操作
        t0.join();
        t1.join();
        t2.join();

        LOGGER.debug("end");
    }

    @Test
    public void testThreadUtilTest2() throws InterruptedException{
        //----------构造数据----------------------------------------------
        List<Integer> list = buildList(0, 10000);
        int perSize = 100;//每次操作的行数

        ThreadUtil.execute(list, perSize, null, new PartitionRunnableBuilder<Integer>(){

            @Override
            public Runnable build(List<Integer> perBatchList,PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){
                return buildRunnable("" + partitionThreadEntity.getBatchNumber(), perBatchList);
            }
        });
    }

    /**
     * Test get method name.
     */
    @Test
    public void testGetMethodName(){
        Thread currentThread = Thread.currentThread();
        LOGGER.debug(ThreadExtensionUtil.getCurrentMethodName(currentThread));
    }

    /**
     * Test get method name1.
     */
    @Test
    public void testGetMethodName1(){
        LOGGER.debug("1");
        testGetMethodName2();
    }

    /**
     * Test get method name2.
     */
    @Test
    public void testGetMethodName2(){
        LOGGER.debug("2");
        testGetMethodName3();
    }

    /**
     * Test get method name3.
     */
    @Test
    public void testGetMethodName3(){
        LOGGER.debug("3");
        testGetMethodName();
    }

    /**
     * @param <T>
     * @return
     * @since 1.10.2
     */
    private <T> Runnable buildRunnable(final String id,final List<T> list){
        return new Runnable(){

            @Override
            public void run(){
                LOGGER.debug("id:{}", id);

                if (isNotNullOrEmpty(list)){
                    for (T i : list){
                        LOGGER.debug("[{}]--->[{}]", id, i);

                        try{
                            Thread.sleep(2);
                        }catch (InterruptedException e){
                            LOGGER.error("", e);
                        }
                    }
                }
            }
        };
    }

    /**
     * @param start
     * @param end
     * @return
     * @since 1.10.2
     */
    List<Integer> buildList(int start,int end){
        List<Integer> list = newArrayList();
        for (int i = start; i < end; ++i){
            list.add(i);
        }
        return list;
    }

}
