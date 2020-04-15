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
package com.feilong.json.jsonlib.transformer;

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

import java.util.Map;

import org.apache.commons.lang3.Validate;

import net.sf.json.util.JavaIdentifierTransformer;

/**
 * 当json转成bean的时候,json字符串里面的属性名字可能有部分不符合我们的java属性命名规范,此时可以基于部分属性做转换器.
 * 
 * <p>
 * 比如 country_id,但是我们的java bean里面的属性名字是标准的 驼峰命名法则,比如 countryId
 * </p>
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * <b>场景:</b> 从相关接口得到的json数据格式如下(注意:部分属性 名字中间是 下划线):
 * </p>
 * 
 * <pre class="code">
 {
            "country": "中国",
            "country_id": "CN",
            "area": "华北",
            "area_id": "100000",
            "region": "北京市",
            "region_id": "110000",
            "city": "北京市",
            "city_id": "110100",
            "county": "",
            "county_id": "-1",
            "isp": "科技网",
            "isp_id": "1000114",
            "ip": "210.75.225.254"
        }
 * </pre>
 * 
 * <p>
 * 但是我们的类是标准的java bean,属性符合驼峰命名规则,比如:
 * </p>
 * 
 * <pre class="code">
 * public class IpInfoEntity{
 * 
 *     <span style="color:green">//ip比如 210.75.225.254.</span>
 *     private String ip;
 * 
 *     <span style="color:green">//** "country": "中国".</span>
 *     private String country;
 * 
 *     <span style="color:green">//** "area": "华北".</span>
 *     private String area;
 * 
 *     <span style="color:green">//** "region": "北京市".</span>
 *     private String region;
 * 
 *     <span style="color:green">//** "city": "北京市".</span>
 *     private String city;
 * 
 *     <span style="color:green">//** "isp": "电信".</span>
 *     private String isp;
 * 
 *     <span style="color:green">//** "country_id": "86".</span>
 *     private String countryId;
 * 
 *     <span style="color:green">//** "region_id": "110000".</span>
 *     private String regionId;
 * 
 *     <span style="color:green">//** "area_id": "100000".</span>
 *     private String areaId;
 * 
 *     <span style="color:green">//** "city_id": "110000".</span>
 *     private String cityId;
 * 
 *     <span style="color:green">//** "isp_id": "100017".</span>
 *     private String ispId;
 * 
 *     <span style="color:green">// setter /getter 方法省略</span>
 * 
 * }
 * 
 * </pre>
 * 
 * <p>
 * 此时可以使用该类,示例如下:
 * </p>
 * 
 * <pre class="code">
 * 
 * public void testToBean2(){
 *     String json = "{"ip":"210.75.225.254","country":"\u4e2d\u56fd","area":"\u534e\u5317",
       "region":"\u5317\u4eac\u5e02","city":"\u5317\u4eac\u5e02","county":"","isp":"\u7535\u4fe1",
      "country_id":"86","area_id":"100000","region_id":"110000","city_id":"110000",
     "county_id":"-1","isp_id":"100017"}";
     
      Map{@code <String, String>} map = newHashMap();
        map.put("area_id", "areaId");
        map.put("region_id", "regionId");
        map.put("city_id", "cityId");
        map.put("county_id", "countyId");
        map.put("country_id", "countryId");
        map.put("isp_id", "ispId");
        
        JsonToJavaConfig jsonToJavaConfig=new JsonToJavaConfig(IpInfoEntity.class, new CustomJavaIdentifierTransformer(map));

 *     IpInfoEntity ipInfoEntity = JsonUtil.toBean(data, jsonToJavaConfig);
 *     //.....
 * }
 * 
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see JavaIdentifierTransformer
 * @see UncapitalizeJavaIdentifierTransformer
 * @see <a href="https://github.com/venusdrogon/feilong-json/issues/1">issue 1</a>
 * @since 1.11.0
 */
public class CustomJavaIdentifierTransformer extends JavaIdentifierTransformer{

    /** The property name convert map. */
    private final Map<String, String> propertyNameConvertMap;

    //---------------------------------------------------------------

    /**
     * Instantiates a new map java identifier transformer.
     *
     * @param map
     *            the map
     */
    public CustomJavaIdentifierTransformer(Map<String, String> map){
        Validate.notEmpty(map, "map can't be null/empty!");
        this.propertyNameConvertMap = map;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.util.JavaIdentifierTransformer#transformToJavaIdentifier(java.lang.String)
     */
    @Override
    public String transformToJavaIdentifier(String propertyName){
        return defaultIfNullOrEmpty(propertyNameConvertMap.get(propertyName), propertyName);
    }

}
