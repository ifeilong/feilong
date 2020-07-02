package com.feilong.namespace;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.net.filetransfer.sftp.SFTPFileTransfer;

@ContextConfiguration(locations = { "classpath*:SFTPFileTransfer.xml" })
public class SFTPFileTransferTagTest extends AbstractJUnit4SpringContextTests{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPFileTransferTagTest.class);

    //---------------------------------------------------------------

    @Autowired
    @Qualifier("sftpFileTransfer")
    private SFTPFileTransfer    sftpFileTransfer;

    //---------------------------------------------------------------

    @Test
    public void test(){

        LOGGER.debug("222");

        //        sftpFileTransfer.
        //
        //        assertEquals("aaa", cookieEntity.getName());
        //        assertEquals(true, cookieEntity.getHttpOnly());
        //        assertEquals(TimeInterval.SECONDS_PER_YEAR, cookieEntity.getMaxAge());
    }
}