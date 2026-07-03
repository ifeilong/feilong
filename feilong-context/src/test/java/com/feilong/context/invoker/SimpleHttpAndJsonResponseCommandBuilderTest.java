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
package com.feilong.context.invoker;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.2
 */
@Slf4j
public class SimpleHttpAndJsonResponseCommandBuilderTest{

    @Test
    public void test(){
        SimpleHttpAndJsonResponseCommandBuilder simpleHttpAndJsonResponseCommandBuilder = new SimpleHttpAndJsonResponseCommandBuilder();

        simpleHttpAndJsonResponseCommandBuilder.setResponseCommandRootClass(String.class);
        //        一、专用“探针级”测试站点（返回短，适合连通性验证）
        //
        //        1️⃣ httpbin.org⭐ 最推荐
        //
        //        Kenneth Reitz 做的，专给开发者测 HTTP 客户端用的，返回极短、语义清晰。
        //        GET 连通：https://httpbin.org/get
        //        HEAD 连通：https://httpbin.org/head
        //        返回 200 + 一小段 JSON，含你请求的 IP、Header 等
        //        还能测：/status/404、/status/500、/delay/2、/post、/put等

        simpleHttpAndJsonResponseCommandBuilder.setUri("https://httpbin.org/get");

        Object build = simpleHttpAndJsonResponseCommandBuilder.build(null);

        log.info("" + build);

    }

}
