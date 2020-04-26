package com.feilong.namespace.http;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.context.invoker.http.HttpResponseStringBuilder;

@ContextConfiguration(locations = { "classpath*:spring-httpResponseStringBuilder.xml" })
public class HttpResponseStringBuilderTagTest extends AbstractJUnit4SpringContextTests{

    /** The Constant log. */
    private static final Logger       LOGGER = LoggerFactory.getLogger(HttpResponseStringBuilderTagTest.class);

    //---------------------------------------------------------------

    @Autowired
    @Qualifier("httpResponseStringBuilder")
    private HttpResponseStringBuilder httpResponseStringBuilder;

    //---------------------------------------------------------------

    @Test
    public void test(){
        String build = httpResponseStringBuilder.build(null);
        LOGGER.debug(build);
    }
}