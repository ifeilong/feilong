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
package com.feilong.component;

import static com.feilong.core.bean.ConvertUtil.toList;

import java.util.List;

import com.feilong.context.DataListQuery;

public class TradeDataDataListQuery implements DataListQuery<DefaultTradeData>{

    /** Static instance. */
    // the static instance works for all types
    public static final TradeDataDataListQuery INSTANCE = new TradeDataDataListQuery();

    @Override
    public List<DefaultTradeData> query(){
        return toList(//
                        new DefaultTradeData("AB12345", "2"),
                        new DefaultTradeData("AB12345666", "3"),
                        new DefaultTradeData("AB1234555", "4"));
    }
}
