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
package com.feilong.net.filetransfer;

import static com.feilong.core.TimeInterval.MILLISECOND_PER_MINUTE;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.feilong.core.lang.ThreadUtil;

public class MultiTest extends AbstractFileTransferTest{

    @Autowired
    private MultiSFTPFileTransferDownloadServer1 mutiSFTPFileTransferDownloadTest1;

    @Autowired
    private MultiSFTPFileTransferDownloadServer2 mutiSFTPFileTransferDownloadTest2;

    @Test
    public void testMutiTest(){

        addTimerTask(new TimerTask(){

            @Override
            public void run(){
                mutiSFTPFileTransferDownloadTest1.downloadFile();
            }
        });

        addTimerTask(new TimerTask(){

            @Override
            public void run(){
                mutiSFTPFileTransferDownloadTest2.downloadFile();
            }
        });

        //睡眠5分钟
        ThreadUtil.sleep(MILLISECOND_PER_MINUTE * 5);
    }

    private void addTimerTask(TimerTask timerTask){
        Timer timer = new Timer();

        //delay 行任务前的延迟时间，单位是毫秒。
        //period - 执行各后续任务之间的时间间隔，单位是毫秒。 
        timer.schedule(timerTask, 2L, MILLISECOND_PER_SECONDS * 2);
    }

}
