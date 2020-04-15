package com.feilong.namespace;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.accessor.session.SessionAccessor;
import com.feilong.core.TimeInterval;

@ContextConfiguration(locations = { "classpath*:applicationContext-SessionAccessor.xml" })
public class SessionAccessorTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("memeberResetPasswordMobileSessionAccessor")
    private SessionAccessor sessionAccessor;

    @Autowired
    @Qualifier("aSessionAccessor")
    private SessionAccessor aSessionAccessor;

    //---------------------------------------------------------------

    @Test
    public void test(){
        System.out.println(sessionAccessor.getKey());

    }

    @Test
    public void test1(){
        assertEquals(TimeInterval.SECONDS_PER_YEAR, aSessionAccessor.getKey());
    }
}