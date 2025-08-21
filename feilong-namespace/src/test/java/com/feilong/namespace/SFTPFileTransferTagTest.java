package com.feilong.namespace;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.net.filetransfer.sftp.SFTPFileTransfer;

@lombok.extern.slf4j.Slf4j
@ContextConfiguration(locations = { "classpath*:SFTPFileTransfer.xml" })
public class SFTPFileTransferTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("sftpFileTransfer")
    private SFTPFileTransfer sftpFileTransfer;

    //---------------------------------------------------------------

    @Test
    public void test(){

        log.debug("222");

        //        sftpFileTransfer.
        //
        //        assertEquals("aaa", cookieEntity.getName());
        //        assertEquals(true, cookieEntity.getHttpOnly());
        //        assertEquals(TimeInterval.SECONDS_PER_YEAR, cookieEntity.getMaxAge());
    }
}