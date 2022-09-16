package com.feilong.namespace;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.core.lang.ThreadUtil;
import com.feilong.net.bot.dingtalk.DingTalkBot;

@ContextConfiguration(locations = { "classpath*:dingtalkbot.xml" })
public class DingtalkBotTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("dingtalkBot")
    private DingTalkBot dingTalkBot;

    //---------------------------------------------------------------

    @Test
    public void test(){
        assertTrue(dingTalkBot.sendMessage("lalalal"));
    }

    @Test
    public void test1(){
        String content = "## 今晚去喝酒吗😁 \n" + //
        //  "@15001841318 \n" + //
        //  "@金鑫 \n" + //
                        "![](https://img.alicdn.com/tfs/TB1bB9QKpzqK1RjSZFoXXbfcXXa-576-96.png) \n" + //
                        "> 曾经有一段真挚的爱情 \n" + //
                        "1. 美女 \n" + //
                        "2. 帅哥 \n" + //
                        "- **喝酒** \n" + //
                        "- [百度](http://baidu.com) \n" + //
                        "- *唱歌* @金鑫 \n";
        dingTalkBot.sendMessage("测试测试", content, "15001841318");

        ThreadUtil.sleepSeconds(3);

    }
}