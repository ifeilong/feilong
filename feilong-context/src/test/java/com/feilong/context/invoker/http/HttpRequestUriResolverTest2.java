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
package com.feilong.context.invoker.http;

import static com.feilong.context.invoker.http.HttpRequestUriResolver.resolve;

import org.junit.Test;

import com.feilong.net.http.HttpClientUtil;
import com.feilong.store.member.Member;

public class HttpRequestUriResolverTest2{

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testTemplate12(){
        Member member = new Member();
        member.setCode("cfei long-jinxin");
        String resolve = resolve("http://127.0.0.1:8223/${member.code}", member);

        HttpClientUtil.get(resolve);

        //assertEquals("http://www.feilong.com/fei long", resolve);
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testTemplate122(){
        Member member = new Member();
        member.setCode("cfei long-中国");
        String resolve = resolve("http://127.0.0.1:8223/${member.code}", member);

        HttpClientUtil.get(resolve);
        //assertEquals("http://www.feilong.com/fei long", resolve);
    }

}
