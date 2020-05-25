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
package com.feilong.component.upload;

import static com.feilong.core.lang.SystemUtil.USER_HOME;

import org.junit.Test;

public class SingleUploadTest extends AbstractUploadMultipartFileResolverTest{

    @Test(expected = NullPointerException.class)
    public void testDefaultMultipartFileResolverTestNull(){
        multipartFileResolver.upload(null, USER_HOME, "1.jpg");
    }

    //---------------------------------------------------------------
    @Test(expected = NullPointerException.class)
    public void testDefaultMultipartFileResolverTestNull22(){
        multipartFileResolver.upload(build(), null, "1.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestEmpty22(){
        multipartFileResolver.upload(build(), "", "1.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestBlank22(){
        multipartFileResolver.upload(build(), " ", "1.jpg");
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testDefaultMultipartFileResolverTestNull11(){
        multipartFileResolver.upload(build(), USER_HOME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestEmpty2222(){
        multipartFileResolver.upload(build(), USER_HOME, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultMultipartFileResolverTestBlank2222(){
        multipartFileResolver.upload(build(), USER_HOME, " ");
    }

}
