package com.feilong.namespace;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.core.TimeInterval;
import com.feilong.servlet.http.entity.CookieEntity;

@ContextConfiguration(locations = { "classpath*:applicationContext-CookieAccessor.xml" })
public class CookieAccessorTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("cookieAccessor")
    private CookieAccessor cookieAccessor;

    //---------------------------------------------------------------

    @Test
    public void test(){
        CookieEntity cookieEntity = cookieAccessor.getCookieEntity();

        assertEquals("aaa", cookieEntity.getName());
        assertEquals(true, cookieEntity.getHttpOnly());
        assertEquals(TimeInterval.SECONDS_PER_YEAR, cookieEntity.getMaxAge());
    }
}