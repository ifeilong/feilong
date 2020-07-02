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

import org.junit.Test;

import com.feilong.net.filetransfer.sftp.SFTPFileTransfer;
import com.feilong.test.AbstractTest;

public class SFTPFileTransferCommonTest extends AbstractTest{

    private final FileTransfer fileTransfer = new SFTPFileTransfer();

    @Test(expected = NullPointerException.class)
    public void testSFTPFileTransferCommonTestNull(){
        fileTransfer.getFileEntityMap(null, "a.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSFTPFileTransferCommonTestEmpty(){
        fileTransfer.getFileEntityMap("", "a.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSFTPFileTransferCommonTestBlank(){
        fileTransfer.getFileEntityMap(" ", "a.jpg");
    }

}
