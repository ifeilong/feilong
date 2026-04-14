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
package com.feilong.net.bot;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.util.CollectionsUtil.size;

import java.util.List;
import java.util.function.Function;

import com.feilong.core.Validate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * markdown字符串生成器.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://open.dingtalk.com/document/orgapp/robot-overview">机器人</a>
 * @since 4.5.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MarkdownHelper{

    /**
     * 用font标签包裹.
     * 
     * <p>
     * <span style="color:red">使用{@code <font>}标签设置颜色，但该标签不在钉钉Markdown格式消息的官方支持范围内，为了业务稳定性不建议使用</span>
     * </p>
     *
     * @param text
     *            the text
     * @param color
     *            the color
     * @return the string
     */
    public static String font(String text,String color){
        Validate.notBlank(color, "color can't be blank!");

        return "<font color=\"" + color + "\">" + text + "</font>";
    }

    /**
     * 生成标签.
     *
     * @param text
     *            the text
     * @param url
     *            the url
     * @return the string
     */
    public static String link(String text,String url){
        Validate.notBlank(text, "text can't be blank!");
        Validate.notBlank(url, "url can't be blank!");

        return "[" + text + "](" + url + ")";
    }

    /**
     * 加粗.
     *
     * @param text
     *            the text
     * @return the string
     */
    public static String b(String text){
        return "**" + text + "**";
    }

    /**
     * 一个\n有时无法成功换行，建议使用两个\n\n确保换行效果。
     * 
     * @return 换行
     */
    public static String newLine(){
        return "\n\n"; //一个\n有时无法成功换行，建议使用两个\n\n确保换行效果
    }

    //将封装到 feilong

    /**
     * 创建markdown 格式的表格.
     * 
     * <p>
     * 会对以下特殊字符进行转义处理
     * </p>
     * 
     * <pre>
    {@code
    return cell.replace("\\", "\\\\") // 先转义反斜杠
                     .replace("|", "\\|") // 转义竖线
                     .replace("\n", "<br>") // 换行处理
                     .replace("\r", ""); // 回车处理
    }
     * </pre>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Member member = new Member();
     * member.setCode("9523");
     * member.setLoginName("zhouxingxing");
     * 
     * String msg = MarkdownHelper.createTable(
     *                 toList("code", "登录名"), //
     *                 toList(member),
     *                 toList(Member::getCode, Member::getLoginName));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    | code |登录名 |
    | ------- |------- |
    | 9523 |zhouxingxing |
     * </pre>
     * 
     * </blockquote>
     * 
     * @param titleList
     *            标题列表 , 比如传 姓名 年龄 这种
     * @param beanList
     *            bean 列表, 比如是user list
     * @param valueFunctionList
     *            提取数据的function 列表
     * @return 如果 <code>titleList</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>titleList</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>valueFunctionList</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>valueFunctionList</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>titleList的size 和valueFunctionList size 不等</code>,抛出 {@link IllegalArgumentException}<br>
     * @since 4.5.2
     */
    public static <O> String createTable(
                    List<String> titleList,//
                    List<O> beanList,
                    List<Function<O, ?>> valueFunctionList){

        Validate.notEmpty(titleList, "titleList can't be null/empty!");
        Validate.notEmpty(valueFunctionList, "valueFunctionList can't be null/empty!");

        Validate.isTrue(size(valueFunctionList) == size(titleList), "size(functionlist)!=size(titleList)");

        //---------------------------------------------------------------

        StringBuilder sb = new StringBuilder();
        sb.append("| ");
        //拼接标题
        for (String title : titleList){
            sb.append(title);
            sb.append(" |");
        }

        sb.append("\n");

        //---------------------------------------------------------------
        sb.append("| ");

        //拼接表题符
        for (@SuppressWarnings("unused")
        String title : titleList){
            sb.append("-------");
            sb.append(" |");
        }
        sb.append("\n");

        //---------------------------------------------------------------
        //拼接数据
        if (isNotNullOrEmpty(beanList)){
            for (O o : beanList){
                sb.append("| ");
                for (Function<O, ?> function : valueFunctionList){
                    Object applyResult = function.apply(o);
                    String cellValue = applyResult != null ? applyResult.toString() : EMPTY;
                    sb.append(escapeMarkdownTableCell(cellValue)).append(" |");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // Markdown 表格单元格转义方法
    private static String escapeMarkdownTableCell(String cell){
        if (cell == null){
            return "";
        }

        // 转义特殊字符
        return cell.replace("\\", "\\\\") // 先转义反斜杠
                        .replace("|", "\\|") // 转义竖线
                        .replace("\n", "<br>") // 换行处理
                        .replace("\r", ""); // 回车处理
    }

}
