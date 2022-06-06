package com.feilong.namespace;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.net.bot.wxwork.WxworkBot;
import com.feilong.net.bot.wxwork.message.news.Article;

@ContextConfiguration(locations = { "classpath*:wxbot.xml" })
public class WxworkBotNewsMessageTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("wxworkBot")
    private WxworkBot wxworkBot;

    @Test
    public void test(){
        //是 标题，不超过128个字节，超过会自动截断
        String title = "提醒您,点击填Timesheet";

        //描述，不超过512个字节，超过会自动截断
        String description = "@all 点我直接填写Jira Timesheet" + "\n\n" + "我爱工作,工作使我快乐";

        //"点我直接填写Jira Timesheet",
        String url = "http://jira.xxx.cn/plugins/servlet/aio-ts/bridge/pages/aiotimeentry";
        String img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585320605587&di=9424f8862476b2ce819ac9f5637567b1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F503d269759ee3d6d55e89bf048166d224f4adeda.jpg";

        wxworkBot.sendNewsMessage(new Article(title, description, url, img));
    }
}