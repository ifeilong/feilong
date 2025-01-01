/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.net.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.Test;

import com.feilong.io.IOReaderUtil;
import com.feilong.test.AbstractTest;

@SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
public class DescriptionTest extends AbstractTest{

    @Test
    public void test(){
        String unsafe = IOReaderUtil.readToString("classpath:description.txt");

        //        System.out.println(unsafe);//TODO:remove

        //        Safelist basic = Safelist.basic();
        //        System.out.println(Jsoup.clean(unsafe, basic));//TODO:remove

        Safelist addTags = new Safelist().addTags("p");
        addTags.removeAttributes("p", "style");
        System.out.println(Jsoup.clean(unsafe, addTags));//TODO:remove

        //        <p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><span><b>剧情环环相扣、悬念陡生，凶手成迷</b></span></p><p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><span><span><b>一部家喻户晓的国剧，</b></span><b>国产悬疑侦探剧的扛鼎之作。</b></span></p><p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><b>《神探狄仁杰》全四部，正式上线。</b></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;" data-flag="normal"><b>每日精彩，听个过瘾！</b><br /></p><p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><b><span>“元芳，你怎么看？”<br />听众老爷们，你们怎么看？</span></b></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;" data-flag="normal"><b><span><b>日更，欢迎订阅、关注！</b><br /><b>【万卷出版有限责任公司出版】</b></span></b></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;" data-flag="normal"><span><b>【<b>辽</b>宁无限穿越新媒体有限公司重点投入制作】</b></span></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica,Arial,sans-serif;hyphens:auto;text-align:justify;"><span><img data-key="0" src="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&device_type=ios&upload_type=attachment&name=mobile_large" alt="" data-origin="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg?op_type=0" data-large="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&device_type=ios&upload_type=attachment&name=mobile_large" data-large-width="750" data-large-height="6000" data-preview="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&device_type=ios&upload_type=attachment&name=mobile_large" data-preview-width="140" data-preview-height="1120" /><br /></span></p>
        //        <p><span><b>剧情环环相扣、悬念陡生，凶手成迷</b></span></p>
        //        <p><span><span><b>一部家喻户晓的国剧，</b></span><b>国产悬疑侦探剧的扛鼎之作。</b></span></p>
        //        <p><b>《神探狄仁杰》全四部，正式上线。</b></p>
        //        <p><b>每日精彩，听个过瘾！</b><br></p>
        //        <p><b><span>“元芳，你怎么看？”<br>
        //           听众老爷们，你们怎么看？</span></b></p>
        //        <p><b><span><b>日更，欢迎订阅、关注！</b><br><b>【万卷出版有限责任公司出版】</b></span></b></p>
        //        <p><span><b>【<b>辽</b>宁无限穿越新媒体有限公司重点投入制作】</b></span></p>
        //        <p><span><img src="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&amp;device_type=ios&amp;upload_type=attachment&amp;name=mobile_large" alt=""><br></span></p>
        //        System.out.println(Jsoup.clean(unsafe, Safelist.basicWithImages()));//TODO:remove

        //剧情环环相扣、悬念陡生，凶手成迷一部家喻户晓的国剧，国产悬疑侦探剧的扛鼎之作。《神探狄仁杰》全四部，正式上线。每日精彩，听个过瘾！“元芳，你怎么看？”听众老爷们，你们怎么看？日更，欢迎订阅、关注！【万卷出版有限责任公司出版】【辽宁无限穿越新媒体有限公司重点投入制作】
        //        System.out.println(Jsoup.clean(unsafe, Safelist.none()));//TODO:remove

        //        <p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><span><b>剧情环环相扣、悬念陡生，凶手成迷</b></span></p><p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><span><span><b>一部家喻户晓的国剧，</b></span><b>国产悬疑侦探剧的扛鼎之作。</b></span></p><p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><b>《神探狄仁杰》全四部，正式上线。</b></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;" data-flag="normal"><b>每日精彩，听个过瘾！</b><br /></p><p style="font-size:16px;line-height:30px;color:#333333;font-weight:normal;" data-flag="normal"><b><span>“元芳，你怎么看？”<br />听众老爷们，你们怎么看？</span></b></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;" data-flag="normal"><b><span><b>日更，欢迎订阅、关注！</b><br /><b>【万卷出版有限责任公司出版】</b></span></b></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;" data-flag="normal"><span><b>【<b>辽</b>宁无限穿越新媒体有限公司重点投入制作】</b></span></p><p style="color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica,Arial,sans-serif;hyphens:auto;text-align:justify;"><span><img data-key="0" src="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&device_type=ios&upload_type=attachment&name=mobile_large" alt="" data-origin="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg?op_type=0" data-large="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&device_type=ios&upload_type=attachment&name=mobile_large" data-large-width="750" data-large-height="6000" data-preview="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&device_type=ios&upload_type=attachment&name=mobile_large" data-preview-width="140" data-preview-height="1120" /><br /></span></p>
        //        <p><span><b>剧情环环相扣、悬念陡生，凶手成迷</b></span></p>
        //        <p><span><span><b>一部家喻户晓的国剧，</b></span><b>国产悬疑侦探剧的扛鼎之作。</b></span></p>
        //        <p><b>《神探狄仁杰》全四部，正式上线。</b></p>
        //        <p><b>每日精彩，听个过瘾！</b><br></p>
        //        <p><b><span>“元芳，你怎么看？”<br>
        //           听众老爷们，你们怎么看？</span></b></p>
        //        <p><b><span><b>日更，欢迎订阅、关注！</b><br><b>【万卷出版有限责任公司出版】</b></span></b></p>
        //        <p><span><b>【<b>辽</b>宁无限穿越新媒体有限公司重点投入制作】</b></span></p>
        //        <p><span><img src="http://imagev2.xmcdn.com/storages/2fc0-audiofreehighqps/70/08/GMCoOR8J8mmsADae3gLGYxL7.jpg!op_type=4&amp;device_type=ios&amp;upload_type=attachment&amp;name=mobile_large" alt=""><br></span></p>
        //        System.out.println(Jsoup.clean(unsafe, Safelist.relaxed()));//TODO:remove

        //<b>剧情环环相扣、悬念陡生，凶手成迷</b><b>一部家喻户晓的国剧，</b><b>国产悬疑侦探剧的扛鼎之作。</b><b>《神探狄仁杰》全四部，正式上线。</b><b>每日精彩，听个过瘾！</b><b>“元芳，你怎么看？”听众老爷们，你们怎么看？</b><b><b>日更，欢迎订阅、关注！</b><b>【万卷出版有限责任公司出版】</b></b><b>【<b>辽</b>宁无限穿越新媒体有限公司重点投入制作】</b>
        //        System.out.println(Jsoup.clean(unsafe, Safelist.simpleText()));//TODO:remove
    }
}