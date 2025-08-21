package com.feilong.namespace.http;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.context.invoker.http.HttpResponseStringBuilder;

@lombok.extern.slf4j.Slf4j
@ContextConfiguration(locations = { "classpath*:spring-httpResponseStringBuilder.xml" })
public class HttpResponseStringBuilderTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("httpResponseStringBuilder")
    private HttpResponseStringBuilder httpResponseStringBuilder;

    //---------------------------------------------------------------

    @Test
    public void test(){
        String build = httpResponseStringBuilder.build(null);
        log.debug(build);
    }
}