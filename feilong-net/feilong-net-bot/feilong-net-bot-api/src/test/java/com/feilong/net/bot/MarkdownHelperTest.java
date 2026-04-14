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

import static com.feilong.core.bean.ConvertUtil.toList;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.feilong.store.member.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.2
 */
@Slf4j
public class MarkdownHelperTest{

    private final List<String>              titleList         = toList("code", "登录名");

    private final List<Function<Member, ?>> valueFunctionList = toList(Member::getCode, Member::getLoginName);

    //---------------------------------------------------------------

    @Test
    public void test(){
        Member member = new Member();
        member.setCode("9523");
        member.setLoginName("zhouxingxing");

        String msg = MarkdownHelper.createTable(
                        titleList, //
                        toList(member),
                        valueFunctionList);

        System.out.println(msg);//TODO:remove
    }

    @Test
    public void test1(){
        String msg = MarkdownHelper.createTable(titleList, null, valueFunctionList);
        System.out.println(msg);//TODO:remove
    }

    @Test
    public void testEmpty(){
        String msg = MarkdownHelper.createTable(titleList, emptyList(), valueFunctionList);
        System.out.println(msg);//TODO:remove
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testMarkdownHelperTestNullTitleList(){
        Member member = new Member();
        member.setCode("9523");
        member.setLoginName("zhouxingxing");
        MarkdownHelper.createTable(null, toList(member), valueFunctionList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkdownHelperTestEmptyTitleList(){
        Member member = new Member();
        member.setCode("9523");
        member.setLoginName("zhouxingxing");
        MarkdownHelper.createTable(emptyList(), toList(member), valueFunctionList);
    }

    //---------------------------------------------------------------
    @Test(expected = NullPointerException.class)
    public void testMarkdownHelperTestNullValueFunctionList(){
        Member member = new Member();
        member.setCode("9523");
        member.setLoginName("zhouxingxing");
        MarkdownHelper.createTable(titleList, toList(member), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkdownHelperTestEmptyValueFunctionList(){
        Member member = new Member();
        member.setCode("9523");
        member.setLoginName("zhouxingxing");
        MarkdownHelper.createTable(titleList, toList(member), emptyList());
    }

    //---------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testMarkdownHelperTestSizeFunctionList(){
        Member member = new Member();
        member.setCode("9523");
        member.setLoginName("zhouxingxing");
        MarkdownHelper.createTable(toList("code"), toList(member), valueFunctionList);
    }

}
