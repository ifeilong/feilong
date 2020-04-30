package com.feilong.namespace;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.context.beanproperty.SimpleHttpTypeBeanProperty;

@ContextConfiguration(locations = { "classpath*:applicationContext-SimpleHttpTypeBeanProperty.xml" })
public class SimpleHttpTypeBeanPropertyTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("httpTypeBeanProperty")
    private SimpleHttpTypeBeanProperty simpleHttpTypeBeanProperty;

    //---------------------------------------------------------------

    @Test
    public void test(){
        assertThat(
                        simpleHttpTypeBeanProperty,
                        allOf(//
                                        hasProperty("method", is("get")),
                                        hasProperty("uri", is("www.baidu.com"))));
    }

}