package com.feilong.namespace;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.net.bot.wxwork.WxworkBot;

@ContextConfiguration(locations = { "classpath*:wxbot.xml" })
public class WxworkBotTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("wxworkBot")
    private WxworkBot wxworkBot;

    //---------------------------------------------------------------

    @Test
    public void test(){
        assertTrue(wxworkBot.sendMessage("hello world"));
    }
}