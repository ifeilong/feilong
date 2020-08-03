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

import com.feilong.net.wxwork.bot.WxworkBot;
import com.feilong.net.wxwork.bot.message.WxworkResponse;

@ContextConfiguration(locations = { "classpath*:wxbot.xml" })
public class WxworkBotTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("wxworkBot")
    private WxworkBot wxworkBot;

    //---------------------------------------------------------------

    @Test
    public void test(){
        WxworkResponse wxworkResponse = wxworkBot.sendMessage("hello world");

        assertThat(
                        wxworkResponse,
                        allOf(
                                        hasProperty("isSuccess", is(true)), //
                                        hasProperty("errcode", is("0"))));
    }
}