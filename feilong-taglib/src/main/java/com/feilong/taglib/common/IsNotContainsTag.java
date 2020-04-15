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
package com.feilong.taglib.common;

/**
 * 判断一个集合(或者可以被转成Iterator) 是否 没有 一个值 (或者说这个value 不在 collection当中).
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 */
public class IsNotContainsTag extends AbstractContainsSupport{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8239319419199818297L;

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.common.AbstractContainsSupport#condition()
     */
    @Override
    public boolean condition(){
        return !containsByStringValue();
    }

}
