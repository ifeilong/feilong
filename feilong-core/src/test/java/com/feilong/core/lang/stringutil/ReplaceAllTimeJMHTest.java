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
package com.feilong.core.lang.stringutil;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.feilong.core.lang.StringUtil;

/**
 * The Class StringUtilReplaceAllParameterizedTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * 
 * @see <a href="http://deepoove.com/jmh-visual-chart/"></a>
 * @see <a href="https://jmh.morethan.io//"></a>
 */
//进行 fork 的次数，可用于类或者方法上。如果 fork 数是 2 的话，则 JMH 会 fork 出两个进程来进行测试。
@Fork(0)
//    用来配置 Mode 选项，可用于类或者方法上，这个注解的 value 是一个数组，可以把几种 Mode 集合在一起执行，如：@BenchmarkMode({Mode.SampleTime, Mode.AverageTime})，还可以设置为 Mode.All，即全部执行一遍。
//
//    Throughput：整体吞吐量，每秒执行了多少次调用，单位为 ops/time
//    AverageTime：用的平均时间，每次操作的平均时间，单位为 time/op
//    SampleTime：随机取样，最后输出取样结果的分布
//    SingleShotTime：只运行一次，往往同时把 Warmup 次数设为 0，用于测试冷启动时的性能
//    All：上面的所有模式都执行一次
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ReplaceAllTimeJMHTest{

    private final String content     = "<article class=\"intro Q_v\" style=\"height: 9833px; max-height: 9833px;\"><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica,"
                    + " Arial, sans-serif;hyphens:auto;text-align:left;\"><span data-flag=\"tag\" style=\"font-size:16px;padding:5px;margin:10px 0px;color:#fff;background:#FC5832;"
                    + "display:inline-block;\"><b>【冒泡有奖】</b></span></p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica,Arial,sans-serif;hyphens:auto;text-align:justify;\">"
                    + "听说杨千幻那厮要与我一较高下，我许七安要开始装叉了！</p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;\">快进入声音播放页戳下方输入框，冒个泡偷偷告诉我，我要用哪些诗词才能胜过他？</p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;\">说得好的，有赏！</p><span><br></span><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;\"><span data-flag=\"tag\" style=\"font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;color:#333333;font-weight:normal;\"><b>好书推荐</b></span></p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;\"><span data-flag=\"tag\" style=\"font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;color:#333333;\"><span><a ref=\"nofollow\" href=\"https://m.ximalaya.com/album/71330983\" style=\"color:#4990E2;text-decoration:none;\">2023年爆款仙侠，爆笑大世界修仙！《择日飞升》点击传送</a></span><br></span></p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\"><span data-flag=\"strong\" style=\"color:#FC5832;word-break:break-all;font-family:Helvetica, Arial, sans-serif;\"><a ref=\"nofollow\" href=\"https://m.ximalaya.com/album/68724415\" style=\"color:#4990E2;text-decoration:none;\">头陀渊工作室超震撼有声剧《头狼》点击收听</a><br></span></p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:justify;\" data-flag=\"normal\"><span data-flag=\"strong\" style=\"font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;color:#333333;font-weight:normal;\"><span data-flag=\"strong\" style=\"font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;color:#333333;font-weight:normal;\"><a ref=\"nofollow\" href=\"https://www.ximalaya.com/youshengshu/54079667/\" style=\"color:#4990E2;text-decoration:none;\">辰东著作|经典小说|限时免费：《神墓》三集就上瘾！速速点击！</a></span></span></p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\"><span data-flag=\"tag\" style=\"font-size:16px;padding:5px;margin:10px 0px;color:#fff;background:#FC5832;display:inline-block;\">2021年全网神书</span></p><p style=\"font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;color:#333333;font-weight:normal;text-align:left;\" data-flag=\"normal\"><img data-key=\"0\" src=\"https://fdfs.xmcdn.com/storages/8449-audiofreehighqps/10/B6/CMCoOSIFOyXDAB17TgDr4b5q.jpg\" alt=\"\" data-origin=\"https://fdfs.xmcdn.com/storages/8449-audiofreehighqps/10/B6/CMCoOSIFOyXDAB17TgDr4b5q.jpg\" data-large=\"https://fdfs.xmcdn.com/storages/8449-audiofreehighqps/10/B6/CMCoOSIFOyXDAB17TgDr4b5q.jpg\" data-large-width=\"750\" data-large-height=\"6800\" data-preview=\"https://fdfs.xmcdn.com/storages/8449-audiofreehighqps/10/B6/CMCoOSIFOyXDAB17TgDr4b5q.jpg\" data-preview-width=\"140\" data-preview-height=\"1269\"></p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\"><span data-flag=\"tag\" style=\"font-size:16px;padding:5px;margin:10px 0px;color:#fff;background:#FC5832;display:inline-block;\">内容简介</span></p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">这个世界，有儒；有道；有佛；有妖；有术士。</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">警校毕业的许七安幽幽醒来，发现自己身处牢狱之中，三日后流放边陲......</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">他起初的目的是自保，顺便在这个没有人权的社会里当个富家翁悠闲度日。</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">.......</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">多年后，许七安回首前尘，身后是早已逝去的敌人，以及累累白骨。</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">滚滚长江东逝水，浪花淘尽英雄，是非成败转头空。</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">青山依旧在，几度夕阳红。</p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\"><span data-flag=\"tag\" style=\"font-size:16px;padding:5px;margin:10px 0px;color:#fff;background:#FC5832;display:inline-block;\">staff</span></p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">版权方：阅文集团<br>作  者：卖报小郎君<br>策  划：头陀渊，魏  健，小桃红<br>画  本：林吖林<br>审  听：站直咯<br>后  期：小太阳<br>美  工：小  沙</p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\"><span data-flag=\"tag\" style=\"font-size:16px;padding:5px;margin:10px 0px;color:#fff;background:#FC5832;display:inline-block;\">cast</span></p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">男：<br>头陀渊-饰：旁  白，许七安<br>大  斌-饰：李慕白，宋廷风<br>丸  子-饰：张  慎，朱广孝<br>乐  羊-饰：许新年<br>百  年-饰：许平志<br>大  树-饰：魏  渊<br>才  子-饰：楚元缜，金莲道长<br>海  风-饰：宋  卿，神殊和尚<br>仁  猫-饰：杨千幻，朱金锣<br>十  六-饰：李灵素，南宫倩柔，东宫太子<br>涵  奕-饰：监  正，巡抚大人<br>顾  炎-饰：杨  砚，王首辅，姬  玄<br>优  雅-饰：王捕头，赵  守，刘公公<br>狐  狸-饰：净  心，袁  雄，天  机<br>叶  凡-饰：李典史<br>西  瓜-饰：元景帝<br>黩  武-饰：姜律中，誉  王<br>肃  说-饰：白衣术士<br>厘  荒-饰：银  锣<br>申  徒-饰：术  士<br>子  话-饰：许元魁，净  尘<br>子  舟-饰：李少云<br>夏  虫-饰：大  汉<br>铁  蛋-饰：杨崔雪，历  王<br>怪猫病-饰：恒  远，紫  莲<br>崔仕云-饰：弟  子<br>东方懿-饰：老吏员<br>麦先生-饰：苗有方，刘御史<br>胖大海-饰：度难金刚，护法金刚<br>萧天意-饰：杨  恭，镇北王<br>慕倾风-饰：许平风<br>拔刀问情-饰：管  家，铜  锣<br>飞你不渴-饰：李玉春<br>博叔话事-饰：张  奉<br>南苑书生-饰：柳公子，小宦官<br>八零居士-饰：阙永修，萨伦阿古<br>丁帅先生-饰：度  难，士  卒<br>胖汁肉包-饰：干  尸，纳兰天禄<br>女：<br>小桃红-饰：浮  香，钟  璃，临安公主<br>吱  毛-饰：许玲音，丽  娜<br>文  文-饰：许玲月<br>千  央-饰：李  茹<br>箬  瓷-饰：长公主<br>大  笑-饰：李妙真<br>闲  戈-饰：苏  苏，明  砚，东方婉蓉<br>无  言-饰：王思慕<br>苏  晨-饰：红  菱<br>故  纸-饰：白  姬<br>妖奈奈-饰：褚采薇，洛玉衡<br>大饺子-饰：吕  青，慕南栀<br>白小花-饰：道  童，天蛊婆婆<br>王珑娇-饰：宫  女<br>沈二凤-饰：许元霜<br>易小美玉-饰：九尾天狐<br>紫荷吟月-饰：冰夷元君，东方婉清<br>闰土土呀-饰：天  枢<br>曲音静子-饰：蓉  蓉</p><p data-flag=\"normal\" style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\"><span data-flag=\"tag\" style=\"font-size:16px;padding:5px;margin:10px 0px;color:#fff;background:#FC5832;display:inline-block;\">购买须知</span></p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">1、本作品为付费有声书，前97集为免费试听，购买成功后，即可收听，可下载重复收听。</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">2、版权归原作者所有，严禁翻录成任何形式，严禁在任何第三方平台传播，违者将追究其法律责任。</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">3、如在充值／购买环节遇到问题，您可通过页面右上方按钮，将页面分享至微信内使用微信支付完成购买。</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">4、在购买过程中，如果您有任何问题，可以按以下步骤咨询在线客服：</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">第一步：您可在喜马拉雅APP【账号-联系客服】中咨询在线客服；</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">第二步：如果您无法联系上APP内在线客服，可关注【喜马拉雅APP】公众号，通过下方菜单栏里【我的-在线客服】咨询在线客服；</p><p style=\"color:#333333;font-weight:normal;font-size:16px;line-height:30px;font-family:Helvetica, Arial, sans-serif;hyphens:auto;text-align:left;\" data-flag=\"normal\">第三步：如果在线客服都未取得联系，也可拨打客服电话：400-838-5616</p><span><br></span></article>";

    private final String regex       = "免费|合集|全集|完结";

    private final String replacement = "";

    //    private final int    j2          = 10000;

    public static void main(String[] args) throws RunnerException{
        final Options opts = new OptionsBuilder().include(ReplaceAllTimeJMHTest.class.getSimpleName())//
                        //                        .forks(1)//
                        .measurementIterations(5)//
                        .warmupIterations(5)//

                        .result("result.json")//
                        .resultFormat(ResultFormatType.JSON)//
                        .build();
        new Runner(opts).run();
    }

    //---------------------------------------------------------------

    @Benchmark
    public void extracted(){
        StringUtil.replaceAll(content, regex, replacement);
    }

    @Benchmark
    public void extractedOrg(){
        content.replaceAll(regex, replacement);
    }

}