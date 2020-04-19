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
package com.feilong.json.jsonlib.processor;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 通用的将类型直接 toString 的处理器实现.
 * 
 * <p>
 * 如果不使用这个处理器,对于 File 格式输出成json,在File to json的时候,会输出很多不关心的信息
 * </p>
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
"file":         {
            "totalSpace": 499963174912,
            "absoluteFile": null,
            "canonicalFile": null,
            "parent": "/Users/feilong/work",
            "absolutePath": "/Users/feilong/work/eclipse feilong.sh",
            "absolute": true,
            "freeSpace": 14801756160,
            "usableSpace": 9794330624,
            "directory": false,
            "file": true,
            "hidden": false,
            "name": "eclipse feilong.sh",
            "path": "/Users/feilong/work/eclipse feilong.sh",
            "parentFile":             {
                "totalSpace": 499963174912,
                "absoluteFile": null,
                "canonicalFile": null,
                "parent": "/Users/feilong",
                "absolutePath": "/Users/feilong/work",
                "absolute": true,
                "freeSpace": 14801756160,
                "usableSpace": 9794330624,
                "directory": true,
                "file": false,
                "hidden": false,
                "name": "work",
                "path": "/Users/feilong/work",
                "parentFile":                 {
                    "totalSpace": 499963174912,
                    "absoluteFile": null,
                    "canonicalFile": null,
                    "parent": "/Users",
                    "absolutePath": "/Users/feilong",
                    "absolute": true,
                    "freeSpace": 14801756160,
                    "usableSpace": 9794330624,
                    "directory": true,
                    "file": false,
                    "hidden": false,
                    "name": "feilong",
                    "path": "/Users/feilong",
                    "parentFile":                     {
                        "totalSpace": 499963174912,
                        "absoluteFile": null,
                        "canonicalFile": null,
                        "parent": "/",
                        "absolutePath": "/Users",
                        "absolute": true,
                        "freeSpace": 14801756160,
                        "usableSpace": 9794330624,
                        "directory": true,
                        "file": false,
                        "hidden": false,
                        "name": "Users",
                        "path": "/Users",
                        "parentFile":                         {
                            "totalSpace": 499963174912,
                            "absoluteFile": null,
                            "canonicalFile": null,
                            "parent": "",
                            "absolutePath": "/",
                            "absolute": true,
                            "freeSpace": 14801756160,
                            "usableSpace": 9794330624,
                            "directory": true,
                            "file": false,
                            "hidden": false,
                            "name": "",
                            "path": "/",
                            "parentFile": null,
                            "canonicalPath": "/"
                        },
                        "canonicalPath": "/Users"
                    },
                    "canonicalPath": "/Users/feilong"
                },
                "canonicalPath": "/Users/feilong/work"
            },
            "canonicalPath": "/Users/feilong/work/eclipse feilong.sh"
        },
        "name": "jim"
 * </pre>
 * 
 * 
 * <p>
 * 可以看出,这个File 输出并不是我们期望看到的结果
 * </p>
 * 
 * </blockquote>
 * 
 * <p>
 * 此时,你可以使用该处理器,做自定义解析
 * </p>
 * 
 * <p>
 * 为了简化操作,{@link com.feilong.json.jsonlib.builder.JsonConfigBuilder#buildDefaultJavaToJsonConfig()} 内置了
 * ,如果你想输出成其他的类型,也可以使用这个类来提前渲染
 * </p>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 */
public class ToStringJsonValueProcessor extends AbstractJsonValueProcessor{

    /** 默认Singleton instance. */
    public static final JsonValueProcessor DEFAULT_INSTANCE = new ToStringJsonValueProcessor();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.json.jsonlib.processor.AbstractJsonValueProcessor#processValue(java.lang.Object, net.sf.json.JsonConfig)
     */
    @Override
    protected Object processValue(Object value,JsonConfig jsonConfig){
        if (null == value){
            return null;
        }
        return value.toString();
    }
}