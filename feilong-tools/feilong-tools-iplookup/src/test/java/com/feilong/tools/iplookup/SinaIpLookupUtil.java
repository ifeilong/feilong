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
package com.feilong.tools.iplookup;

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import com.feilong.json.jsonlib.JsonToJavaConfig;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.json.jsonlib.transformer.CustomJavaIdentifierTransformer;
import com.feilong.net.httpclient4.HttpClientUtil;
import com.feilong.test.AbstractTest;
import com.feilong.tools.slf4j.Slf4jUtil;

import net.sf.json.util.JavaIdentifierTransformer;

/**
 * The Class IpUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="http://ip.taobao.com/instructions.php">ip.taobao</a>
 * @since 1.11.0
 */
public final class SinaIpLookupUtil extends AbstractTest{

    /** <code>{@value}</code>. */
    private static final String SINA_IP_URI_PATTERN = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip={}";

    //---------------------------------------------------------------

    /**
     * Gets the ip info entity.
     * 
     * <h3>1. 请求接口（GET）:</h3>
     * <blockquote>
     * http://ip.taobao.com/service/getIpInfo.php?ip=[ip地址字串]
     * </blockquote>
     * 
     * <h3>2. 响应信息:</h3>
     * <blockquote>
     * （json格式的）国家 、省（自治区或直辖市）、市（县）、运营商
     * </blockquote>
     * 
     * <h3>3. 返回数据格式：</h3>
     * <blockquote>
     * 
     * <pre>
    {@code
     {"code":0,"data":{"ip":"210.75.225.254","country":"\u4e2d\u56fd","area":"\u534e\u5317",
       "region":"\u5317\u4eac\u5e02","city":"\u5317\u4eac\u5e02","county":"","isp":"\u7535\u4fe1",
      "country_id":"86","area_id":"100000","region_id":"110000","city_id":"110000",
     "county_id":"-1","isp_id":"100017"}
     }
    }
     * </pre>
     * 
     * 其中code的值的含义为，0：成功，1：失败。
     * 
     * <p>
     * 使用 json 格式化,结果
     * </p>
     * 
     * <pre>
    {@code
    {
        "code": 0,
        "data":         {
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
    }
    }
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>访问限制：为了保障服务正常运行，每个用户的访问频率需小于10qps</li>
     * <li>详细请参看API主页：http://ip.taobao.com/</li>
     * </ol>
     * </blockquote>
     *
     * @param ip
     *            the ip
     * @return 如果 <code>ip</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>ip</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see <a href="http://ip.taobao.com/instructions.php">ip.taobao</a>
     */
    public static IpInfo getIpInfo(String ip){
        Validate.notBlank(ip, "ip can't be blank!");

        //---------------------------------------------------------------
        String uri = Slf4jUtil.format(SINA_IP_URI_PATTERN, ip);
        String responseString = HttpClientUtil.get(uri);
        Validate.notBlank(responseString, "responseString can't be blank!");

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("result format :[{}]", JsonUtil.format(responseString));
        }
        return toIpInfoEntity(responseString);
    }

    /**
     * To ip info entity.
     *
     * @param responseString
     *            the response string
     * @return the ip info entity
     */
    private static IpInfo toIpInfoEntity(String responseString){

        //var remote_ip_info = {"ret":1,"start":-1,"end":-1,"country":"\u4e2d\u56fd","province":"\u5317\u4eac","city":"\u5317\u4eac","district":"","isp":"","type":"","desc":""};

        Map<String, Object> map = JsonUtil.toMap(responseString);
        Object code = map.get("code");

        //0：成功，1：失败
        Validate.isTrue("0".equals(Objects.toString(code)), "code value not [0],but is [%s],responseString:%s", code, responseString);

        //---------------------------------------------------------------

        Object data = map.get("data");
        Validate.notNull(data, "data can't be null!,responseString:%s", responseString);

        //---------------------------------------------------------------
        JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(IpInfo.class, build());

        //历史错误的属性名字
        jsonToJavaConfig.setExcludes("county", "county_id");
        return JsonUtil.toBean(data, jsonToJavaConfig);
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @return the java identifier transformer
     * @see net.sf.json.util.JavaIdentifierTransformer
     * @see com.feilong.json.jsonlib.transformer.CustomJavaIdentifierTransformer
     */
    private static JavaIdentifierTransformer build(){
        //        "country_id": "CN",
        //        "area_id": "100000",
        //        "region_id": "110000",
        //        "city_id": "110100",
        //        "county_id": "-1",
        //        "isp_id": "1000114",

        Map<String, String> map = newHashMap();
        map.put("area_id", "areaId");
        map.put("region_id", "regionId");
        map.put("city_id", "cityId");
        map.put("county_id", "countyId");
        map.put("country_id", "countryId");
        map.put("isp_id", "ispId");

        return new CustomJavaIdentifierTransformer(map);
    }

}
