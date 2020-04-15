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
package com.feilong.io.iowriteutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.feilong.io.IOWriteUtil;

public class IOWriteUtilInputStreamTest{

    @Test(expected = NullPointerException.class)
    public void testIOWriteUtilInputStreamTestNull(){
        IOWriteUtil.write(null, new OutputStream(){

            @Override
            public void write(int b) throws IOException{

            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIOWriteUtilInputStreamTestEmpty(){
        IOWriteUtil.write(new InputStream(){

            @Override
            public int read() throws IOException{
                return 0;
            }
        }, null);
    }

}
