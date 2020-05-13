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
package com.feilong.taglib.display.httpconcat;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.lib.lang3.ObjectUtils.defaultIfNull;
import static java.lang.System.lineSeparator;

import java.util.List;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.ToStringConfig;
import com.feilong.io.IOReaderUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.test.AbstractTest;

/**
 * The Class BaseHttpConcatTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.7
 */
public abstract class BaseHttpConcatTest extends AbstractTest{

    protected static final String vmFloder = "/Users/feilong/workspace/feilong/feilong/feilong-taglib/src/test/resources/velocity/";

    //---------------------------------------------------------------

    /**
     * @param filePath
     * @return
     * @since 1.12.8
     */
    protected static String read(String filePath){
        return IOReaderUtil.readToString(vmFloder + filePath, UTF8);
    }

    //---------------------------------------------------------------

    /**
     * 获得 http concat param.
     *
     * @return the http concat param
     */
    protected HttpConcatParam getHttpConcatParam(){
        return getHttpConcatParamByIndex(null);
    }

    /**
     * 获得 http concat param by i.
     *
     * @param i
     *            the i
     * @return the http concat param by i
     */
    protected HttpConcatParam getHttpConcatParamByIndex(Integer i){
        HttpConcatParam httpConcatParam1 = new HttpConcatParam();
        httpConcatParam1.setType("js");
        //httpConcatParam.setDomain("http://www.feilong.com");
        httpConcatParam1.setRoot("/js" + defaultIfNull(i, "") + "/");
        httpConcatParam1.setHttpConcatSupport(true);

        List<String> itemSrcList = buildList();
        httpConcatParam1.setContent(ConvertUtil.toString(itemSrcList, new ToStringConfig(lineSeparator())));
        httpConcatParam1.setVersion("20140517");
        return httpConcatParam1;
    }

    //---------------------------------------------------------------

    /**
     * @return
     * @since 1.10.4
     */
    private List<String> buildList(){
        List<String> itemSrcList = newArrayList();
        itemSrcList.add("public/js/common.js");

        itemSrcList.add("public/js/jquery.lazyload.min.js");
        itemSrcList.add("marketplace/js/item/searchItem.js");
        itemSrcList.add("public/js/loxia2/jquery.loxia.locale_${request.getAttribute('locale')}.js");
        itemSrcList.add("marketplace/js/marketplace.js");
        itemSrcList.add("public/js/ajax.extend.js");

        itemSrcList.add("public/js/jquery.jqzoom-core.js");
        itemSrcList.add("marketplace/js/item/memberFavorite.js");
        itemSrcList.add("marketplace/js/buyNow/addShoppingCart.js");
        itemSrcList.add("marketplace/messages/messageDetail_in_ID.js");
        itemSrcList.add("marketplace/js/item/itemPDPDetail.js");
        itemSrcList.add("marketplace/js/item/itemPDPDetail_subData.js");
        itemSrcList.add("marketplace/js/item/itemPDPDetail_paging.js");
        itemSrcList.add("public/js/cascading/jquery.cascading.data.js");
        itemSrcList.add("public/js/cascading/jquery.cascading.js");
        itemSrcList.add("marketplace/js/item/youMayLikeItemForPDP.js");
        itemSrcList.add("livechat/js/livechat.js");
        itemSrcList.add("trade/js/tradeFeedbacks/rating.js");
        itemSrcList.add("member/js/dialogSignIn.js");
        itemSrcList.add("public/components/faceBook/faceBookInit.js");
        itemSrcList.add("member/messages/message_dialogSignIn_in_ID.js");
        return itemSrcList;
    }
}