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
package com.feilong.tools;

/**
 * 钱工具类,可以将 人民币转成大写,目前只能处理万亿级别.
 * 
 * @author 徐新望
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.2
 */
public final class MoneyUtil{

    /** 中文的 0123456789. */
    private static final char[] S1 = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };

    /** 中文的 分角元. */
    private static final char[] S4 = { '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万' };

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private MoneyUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将数字金额转换成中文大写.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * double value = 100203.04;
     * LOGGER.info(MoneyUtil.toChineseMoney(value));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 壹拾万零贰佰零叁元零肆分
     * </pre>
     * 
     * </blockquote>
     * 
     * 
     * <h3>《正确填写票据和结算凭证的基本规定》:</h3>
     * <blockquote>
     * <p>
     * 银行、单位和个人填写的各种票据和结算凭证是办理支付结算和现金收付的重要依据,直接关系到支付结算的准确、及时和安全.<br>
     * 票据和结算凭证是银行、单位和个人凭以记载账务的会计凭证,是记载经济业务和明确经济责任的一种书面证明.<br>
     * 因此,填写票据和结算凭证,必须做到标准化、 规范化,要 要素齐全、数字正确、字迹清晰、不错漏、不潦草,防止涂改.<br>
     * <p>
     * 
     * <ul>
     * 
     * <li>一、中文大写金额数字应用正楷或行书填写,如<br>
     * 壹(壹)、贰(贰)、叁、肆(肆)、伍(伍)、陆(陆)、柒、捌、玖、拾、佰、仟、万(万)、亿、元、角、分、零、整(正)等字样.<br>
     * 不得用一、二(两)、三、四、五、六、七、八、九、十、念、毛、另(或0)填写,不得自造简化字.<br>
     * 如果金额数字书写中使用繁体字,如貳、陸、億、萬、圓的,也应受理.<br>
     * </li>
     * 
     * <li>二、中文大写金额数字到"元"为止的,在"元"之后,应写"整"(或"正")字,在"角"之后可以不写" 整"(或"正")字.<br>
     * 大写金额数字有"分"的,"分"后面不写"整"(或"正")字.<br>
     * </li>
     * 
     * <li>三、中文大写金额数字前应标明"人民币"字样,大写金额数字应紧接"人民币"字样填写,不得留有空白.<br>
     * 大写金额数字前未印"人民币"字样的,应加填"人民币"三字.<br>
     * 在票据和结算凭证大写金额栏内不得预印固定的"仟、佰、拾、万、仟、伯、拾、元、角、分"字样.<br>
     * </li>
     * 
     * <li>四、阿拉伯小写金额数字中有"0"时,<br>
     * 中文大写应按照汉语语言规律、金额数字构成和防止涂改的要求进行书写.<br>
     * 举例如下:<br>
     * (一)阿拉伯数字中间有"O"时,中文大写金额要写"零"字.如￥1,409.50,应写成人民币壹仟肆佰零玖元伍角.<br>
     * (二)阿拉伯数字中间连续有几个"0"时,中文大写金额中间可以只写一个"零"字.如￥6,007.14 ,应写成人民币陆仟零柒元壹角肆分.<br>
     * (三)阿拉伯金额数字万位或元位是"0",或者数字中间连续有几个"0",万位、元位也是"0’,但千位 、角位不是"0"时,<br>
     * 中文大写金额中可以只写一个零字,也可以不写"零"字.如￥1,680.32,应写成人民币壹仟陆佰捌拾元零叁角贰分,<br>
     * 或者写成人民币壹仟陆佰捌拾元叁角贰分;又如￥107,000.53,应写成人民币壹拾万柒仟元零伍角叁分,或者写成人民币壹拾万零柒仟元伍角叁分.<br>
     * (四)阿拉伯金额数字角位是"0",而分位不是"0"时,中文大写金额"元"后面应写"零"字.如￥16 ,409.02,<br>
     * 应写成人民币壹万陆仟肆佰零玖元零贰分;又如￥325.04,应写成人民币叁佰贰拾伍元零肆分.<br>
     * </li>
     * 
     * <li>五、阿拉伯小写金额数字前面,均应填写入民币符号"￥"(或草写:).<br>
     * 阿拉伯小写金额数字要认真填写,不得连写分辨不清.<br>
     * </li>
     * 
     * <li>六、票据的出票日期必须使用中文大写.<br>
     * 为防止变造票据的出禀日期,在填写月、日时,月为壹、贰和壹拾的,日为壹至玖和壹拾、贰拾和叁抬的,应在其前加"零";<br>
     * 日为抬壹至拾玖的,应在其前加"壹".如1月15日,应写成零壹月壹拾伍日.再如10月20日,应写成零壹拾月零贰拾日.<br>
     * </li>
     * 
     * <li>七、票据出票日期使用小写填写的,银行不予受理.大写日期未按要求规范填写的,银行可予受理,但由此造成损失的,由出票入自行承担.</li>
     * 
     * </ul>
     * 
     * </p>
     * </blockquote>
     * 
     * @param money
     *            要转换的数字金额
     * @return 大写的金额
     */
    public static String toChineseMoney(double money){

        // YUAN_TO_FEN_RATIO FIX_FLOAT_ERR_NUMBER
        // 看看变量名很清楚的:
        // YUAN_TO_FEN_RATIO 元到分的转换比率 100
        // FIX_FLOAT_ERR_NUMBER 去除浮点误差(当时项目接口必须是double型,没办法) 0.000001
        String str = String.valueOf(Math.round(money * 100 + 0.00001));
        String result = "";
        int strLength = str.length();
        for (int i = 0; i < strLength; ++i){
            int n = str.charAt(strLength - 1 - i) - '0';
            result = S1[n] + "" + S4[i] + result;
        }
        result = result.replaceAll("零仟", "零");
        result = result.replaceAll("零佰", "零");
        result = result.replaceAll("零拾", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("零角", "零");
        result = result.replaceAll("零分", "零");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("亿万", "亿");
        result = result.replaceAll("零$", "");
        result = result.replaceAll("元$", "元整");
        return result;
    }
}
