package com.feilong.namespace;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.net.bot.wxwork.WxworkBot;

@ContextConfiguration(locations = { "classpath*:wxbot.xml" })
public class WxworkBotMarkdownTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("wxworkBot")
    private WxworkBot wxworkBot;

    @Test
    public void test(){
        wxworkBot.sendMessage(
                        "实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。\n" + "> 类型:<font color=\"comment\">用户反馈</font>\n"
                                        + "> 普通用户反馈:<font color=\"comment\">117例</font>\n"
                                        + "> VIP用户反馈:<font color=\"comment\">15例</font>");
    }
}