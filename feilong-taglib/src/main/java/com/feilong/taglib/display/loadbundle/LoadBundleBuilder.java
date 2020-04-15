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
package com.feilong.taglib.display.loadbundle;

import java.util.Map;

import com.feilong.taglib.display.CacheContentBuilder;
import com.feilong.taglib.display.LocaleSupportUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public class LoadBundleBuilder implements CacheContentBuilder<LoadBundleParam, Map<String, String>>{

    /** Static instance. */
    // the static instance works for all types
    public static final CacheContentBuilder<LoadBundleParam, Map<String, String>> INSTANCE = new LoadBundleBuilder();

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.display.CacheContentBuilder#build(com.feilong.taglib.display.CacheParam)
     */
    @Override
    public Map<String, String> build(LoadBundleParam loadBundleParam){
        return LocaleSupportUtil.build(loadBundleParam);
    }
}
