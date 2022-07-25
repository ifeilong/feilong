package com.feilong.namespace;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2Test{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Log4j2Test.class);

    //---------------------------------------------------------------

    @Test
    public void test(){
        LOGGER.info("test");
    }
}