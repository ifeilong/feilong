package com.feilong.namespace;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.accessor.session.SessionAccessor;
import com.feilong.core.TimeInterval;

@ContextConfiguration(locations = { "classpath*:applicationContext-SessionAccessor.xml" })
public class SessionAccessorTagTest extends AbstractJUnit4SpringContextTests{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAccessorTagTest.class);

    //---------------------------------------------------------------

    @Autowired
    @Qualifier("memeberResetPasswordMobileSessionAccessor")
    private SessionAccessor     sessionAccessor;

    @Autowired
    @Qualifier("aSessionAccessor")
    private SessionAccessor     aSessionAccessor;

    //---------------------------------------------------------------

    @Test
    public void test(){
        LOGGER.debug(sessionAccessor.getKey());
    }

    @Test
    public void test1(){
        assertEquals(TimeInterval.SECONDS_PER_YEAR, aSessionAccessor.getKey());
    }
}