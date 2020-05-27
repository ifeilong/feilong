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
package com.feilong.context.codecreator;

import static com.feilong.core.DatePattern.COMMON_DATE;
import static com.feilong.core.bean.ConvertUtil.toLong;
import static com.feilong.core.date.DateUtil.getIntervalTime;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.date.DateUtil.toDate;

import java.util.concurrent.atomic.AtomicLong;

import com.feilong.core.util.RandomUtil;

/**
 * 默认的游客购物车行id 创造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
public class DefaultGuestShoppingcartLineIdCreator implements GuestShoppingcartLineIdCreator{

    /**
     * AtomicLong.
     * <ul>
     * <li>使用 2017-01-01 主要为了生成的长度短一些</li>
     * <li>游客购物车主要是当前用户cookie的增删改,即使和其他用户重复了也没有关系</li>
     * <li>初始值理论上,每次重启都是不一样的, 且不会重复,只要服务器时间设置是ok的</li>
     * </ul>
     */
    private static AtomicLong counter = new AtomicLong(getIntervalTime(now(), toDate("2017-01-01", COMMON_DATE)));

    //---------------------------------------------------------------

    /**
     * 创建 id.
     * 
     * <p>
     * 游客的购物车行id 仅仅用于增删修改等操作,不会存储到DB
     * </p>
     *
     * @return the long
     * @see <a href="/NB-1074">游客购物车行id规则bug</a>
     */
    @Override
    public long create(){ //shoppingCartLines.size() 不能解决删除的问题

        StringBuilder sb = new StringBuilder();
        sb.append(counter.getAndIncrement());
        sb.append(RandomUtil.createRandomWithLength(4));//减少因为服务器时间等场景带来的幺蛾子事情

        return toLong(sb);
    }
}
