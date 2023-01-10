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
package com.feilong.json.processor;

import java.util.Map;

import com.feilong.core.util.MapUtil;
import com.feilong.lib.json.processors.PropertyNameProcessor;

/**
 * 将指定类型下面所有属性名字<b>通过隐射mapping</b>的处理器.
 * 
 * <p>
 * 比如有以下对象:
 * </p>
 * 
 * <pre>
 * public class AlbumBrowseRecord{
 * 
 *     private Long albumId;
 * 
 *     private Long trackId;
 * 
 *     private Long browsedAt;
 *     
 *     <span style="color:green">// setter getter</span>
 * }
 * </pre>
 * 
 * 需要转成以下 json,
 * 
 * <pre>
{@code
 * [
 * {album_id:12891461,track_id:231223915,browse_at:1575451018446},
 * {album_id:12891461,track_id:231223915,browse_at:1575451018446}
 * ]
}
 * </pre>
 * 
 * 可以看到是个albumId 转成album_id ,或者自定义转换名字 ,你可以使用以下代码转换
 * 
 * <pre>
 * JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
 * 
 * Map{@code <String, String>} map = toMap("albumId", "album_id");
 * map.put("browsedAt", "browse_at");
 * map.put("trackId", "track_id");
 * javaToJsonConfig.setJsonTargetClassAndPropertyNameProcessorMap(toMap(AlbumBrowseRecord.class, new SimpleMapPropertyNameProcessor(map)));
 * 
 * //---------------------------------------------------------------
 * String json = JsonUtil.toString(command, javaToJsonConfig);
 * </pre>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://github.com/ifeilong/feilong/issues/504">新建 PropertyNameProcessor 简单的map 隐射实现</a>
 * @since 3.4.0
 */
public class SimpleMapPropertyNameProcessor implements PropertyNameProcessor{

    /** Singleton instance. */
    public static final PropertyNameProcessor INSTANCE = new SimpleMapPropertyNameProcessor();

    private Map<String, String>               map;

    //---------------------------------------------------------------

    public SimpleMapPropertyNameProcessor(){
    }

    public SimpleMapPropertyNameProcessor(Map<String, String> map){
        this.map = map;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.processors.PropertyNameProcessor#processPropertyName(java.lang.Class, java.lang.String)
     */
    @Override
    public String processPropertyName(@SuppressWarnings("rawtypes") Class beanClass,String currentPropertyName){
        return MapUtil.getOrDefault(map, currentPropertyName, currentPropertyName);
    }
}
