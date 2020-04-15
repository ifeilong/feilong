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
package com.feilong.taglib.display.httpconcat.builder;

import java.util.List;

import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.taglib.display.httpconcat.handler.DomainFormatter;
import com.feilong.taglib.display.httpconcat.handler.RootFormatter;
import com.feilong.taglib.display.httpconcat.handler.TypeFormatter;
import com.feilong.taglib.display.httpconcat.handler.VersionFormatter;

/**
 * {@link HttpConcatParam} 构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class HttpConcatParamBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpConcatParamBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 标准化 httpConcatParam,比如标准化domain等等.
     * 
     * @param itemSrcList
     *            the item src list
     * @param httpConcatParam
     *            the http concat param
     * @return the http concat param
     */
    public static HttpConcatParam standardHttpConcatParam(List<String> itemSrcList,HttpConcatParam httpConcatParam){
        HttpConcatParam standardHttpConcatParam = new HttpConcatParam();

        standardHttpConcatParam.setDomain(DomainFormatter.format(httpConcatParam.getDomain()));
        standardHttpConcatParam.setRoot(RootFormatter.format(httpConcatParam.getRoot()));
        standardHttpConcatParam.setType(TypeFormatter.format(httpConcatParam.getType(), itemSrcList));
        standardHttpConcatParam.setVersion(VersionFormatter.format(httpConcatParam.getVersion()));

        standardHttpConcatParam.setContent(httpConcatParam.getContent());
        standardHttpConcatParam.setHttpConcatSupport(httpConcatParam.getHttpConcatSupport());
        return standardHttpConcatParam;
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param blockContent
     *            the block content
     * @param type
     *            the type
     * @param domain
     *            the domain
     * @param root
     *            the root
     * @param version
     *            the version
     * @param httpConcatSupport
     *            the http concat support
     * @return the http concat param
     */
    public static HttpConcatParam build(String blockContent,String type,String domain,String root,String version,Boolean httpConcatSupport){
        HttpConcatParam httpConcatParam = new HttpConcatParam();

        httpConcatParam.setDomain(domain);
        httpConcatParam.setRoot(root);
        httpConcatParam.setType(type);
        httpConcatParam.setVersion(version);
        httpConcatParam.setContent(blockContent.trim());
        httpConcatParam.setHttpConcatSupport(httpConcatSupport);
        return httpConcatParam;
    }
}
