package com.feilong.namespace;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.accessor.session.SessionAccessor;
import com.feilong.accessor.session.SessionKeyAccessor;
import com.feilong.core.TimeInterval;
import com.feilong.json.JsonUtil;

@lombok.extern.slf4j.Slf4j
@ContextConfiguration(locations = { "classpath*:applicationContext-alltag.xml" })
public class AllTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("cookieAccessor")
    private CookieAccessor     cookieAccessor;

    //---------------------------------------------------------------

    @Autowired
    @Qualifier("sessionKeyAccessor")
    private SessionKeyAccessor sessionKeyAccessor;

    @Autowired
    @Qualifier("sessionAccessor")
    private SessionAccessor    sessionAccessor;

    //---------------------------------------------------------------

    @Test
    public void test(){
        log.info(JsonUtil.format(cookieAccessor));
    }

    @Test
    public void test1(){
        assertEquals("" + TimeInterval.SECONDS_PER_YEAR, sessionAccessor.getKey());
    }
}