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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.LF;
import static com.feilong.core.util.CollectionsUtil.removeDuplicate;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import com.feilong.core.Validate;
import com.feilong.core.lang.StringUtil;

/**
 * 专门用来提取标签体内容的.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
@lombok.extern.slf4j.Slf4j
public final class ItemSrcListResolver{

    /** Don't let anyone instantiate this class. */
    private ItemSrcListResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Resolve.
     *
     * @param blockContent
     *            内容,目前 以 \n 分隔
     * @param domain
     *            the domain
     * @return 如果 <code>blockContent</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>blockContent</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         使用换行符,转成字符串数组,如果 <code>数组</code> 是null或者是empty,抛出 {@link IllegalArgumentException}<br>
     * @since 1.11.1 remove type param
     */
    public static List<String> resolve(String blockContent,String domain){
        Validate.notBlank(blockContent, "blockContent can't be blank!");

        //---------------------------------------------------------------
        //可能有内容, 数组不是null或者empty
        String[] items = StringUtil.split(blockContent.trim(), LF);
        int length = items.length;
        List<String> list = new ArrayList<>(length);
        for (int i = 0; i < length; ++i){
            String item = items[i];

            //是否忽略
            if (isIgnore(item)){
                continue;
            }

            //---------------------------------------------------------------
            list.add(ItemSrcExtractor.extract(item, domain));
        }
        //---------------------------------------------------------------
        if (isNullOrEmpty(list)){
            if (log.isWarnEnabled()){
                log.warn(
                                "list isNullOrEmpty,need list to create links,now return emptyList(),blockContent info:[{}],domain:[{}]",
                                blockContent,
                                domain);
            }
            return emptyList();
        }

        return rework(blockContent, list);
    }

    //---------------------------------------------------------------

    /**
     * 是否忽略.
     * 
     * @param item
     * @return
     * @since 1.12.8
     */
    private static boolean isIgnore(String item){
        if (isNullOrEmpty(item)){// 忽视空行
            return true;
        }

        //---------------------------------------------------------------
        //since 1.11.1
        //<!-- 公共 CSS 部分开始 -->
        //忽视html注释行
        return item.trim().startsWith("<!--");
    }

    //---------------------------------------------------------------

    /**
     * 再加工.
     * 
     * <p>
     * 含去重处理
     * </p>
     *
     * @param blockContent
     *            the block content
     * @param itemSrcList
     *            the item src list
     * @return the list
     */
    private static List<String> rework(String blockContent,List<String> itemSrcList){
        Validate.notEmpty(itemSrcList, "itemSrcList can't be null/empty!");

        // 去重,元素不重复
        List<String> noRepeatItemList = removeDuplicate(itemSrcList);

        //---------------------------------------------------------------
        int noRepeatitemListSize = noRepeatItemList.size();
        int itemSrcListSize = itemSrcList.size();

        if (noRepeatitemListSize != itemSrcListSize && log.isWarnEnabled()){
            log.warn("noRepeatList.size:[{}]!= srcList.size:[{}],blockContent:{}", noRepeatitemListSize, itemSrcListSize, blockContent);
        }
        return noRepeatItemList;
    }
}
