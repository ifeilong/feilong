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
package com.feilong.formatter.transformer;

import static com.feilong.core.bean.ConvertUtil.toLong;

import org.apache.commons.collections4.Transformer;

import com.feilong.core.lang.NumberUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.7
 */
public class MoneyTransformer implements Transformer<Number, String>{

    /** Static instance. */
    // the static instance works for all types
    public static final MoneyTransformer INSTANCE = new MoneyTransformer();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
     */
    @Override
    public String transform(Number input){
        long number2 = toLong(input);
        if (NumberUtil.isGatherThan(number2, 5_0000_0000L)){
            return ">=5亿";
        }
        if (NumberUtil.isGatherThan(number2, 1_0000_0000L)){
            return ">=1亿";
        }
        if (NumberUtil.isGatherThan(number2, 5000_0000L)){
            return ">=5千万";
        }
        if (NumberUtil.isGatherThan(number2, 1000_0000L)){
            return ">=1千万";
        }
        if (NumberUtil.isGatherThan(number2, 0L)){
            return ">=0";
        }
        return null;
    }
}
