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
package com.feilong.template;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

/**
 * Velocity 工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * 
 * @see org.apache.velocity.VelocityContext
 * @see org.apache.velocity.app.VelocityEngine
 * @see org.apache.velocity.Template
 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader
 * @since 1.2.1
 */
final class VelocityUtil extends AbstractTemplateUtil{

    /**
     * Static instance.
     * 
     * @since 1.12.5
     */
    // the static instance works for all types
    public static final VelocityUtil INSTANCE = new VelocityUtil();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.velocity.BaseVelocityUtil#constructContext(java.util.Map)
     */
    @Override
    protected Context buildContext(Map<String, ?> contextKeyValues){
        Map<String, Object> map = toMap(contextKeyValues);
        return new VelocityContext(map);
    }

}
