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
package com.feilong.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ZClientTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class ZClientTemp{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZClientTemp.class);

    /**
     * Gets the files.
     * 
     * @param ftp
     *            the ftp
     * @param localDir
     *            the local dir
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static void testGetFiles(FTPClient ftp,File localDir) throws IOException{
        String[] names = ftp.listNames();
        for (String name : names){
            File file = new File(localDir.getPath() + File.separator + name);
            if (!file.exists()){
                file.createNewFile();
            }
            long pos = file.length();
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(pos);
            ftp.setRestartOffset(pos);
            InputStream is = ftp.retrieveFileStream(name);
            if (is == null){
                LOGGER.debug("no such file:" + name);
            }else{
                LOGGER.debug("start getting file:" + name);
                int b;
                while ((b = is.read()) != IOUtils.EOF){
                    raf.write(b);
                }
                is.close();
                if (ftp.completePendingCommand()){
                    LOGGER.debug("done!");
                }else{
                    LOGGER.debug("can't get file:" + name);
                }
            }
            raf.close();
        }
    }
}
