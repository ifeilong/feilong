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
package com.feilong.json.jsonlib.builder;

import net.sf.json.util.PropertyFilter;

/**
 * 排除属性过滤器, {@code json ---> java} 的时候,想排除某个属性.
 * 
 * <p>
 * 比如 taobao 接口返回的数据, 有老的废弃属性 county (可能早期由于单词写错了. 为了兼容老代码)
 * </p>
 * 
 * <pre>
 * {@code
 * {
 *             "country": "中国",
 *             "country_id": "CN",
 *             "area": "华北",
 *             "area_id": "100000",
 *             "region": "北京市",
 *             "region_id": "110000",
 *             "city": "北京市",
 *             "city_id": "110100",
 *             "county": "",
 *             "county_id": "-1",
 *             "isp": "科技网",
 *             "isp_id": "1000114",
 *             "ip": "210.75.225.254"
 *         }
 * }
 * </pre>
 * 
 * 
 * 但是我们的java bean 没有这个属性
 * 
 * 导致转换的时候, 由于 使用了 PropertyStrategyWrapper
 * 
 * <pre>
 * {@code
 *   public void setProperty(Object bean,String key,Object value){
 *         try{
 *             propertySetStrategy.setProperty(bean, key, value);
 *         }catch (Exception e){
 *             LOGGER.warn(e.getMessage(), e);
 *         }
 *     }
 * }
 * </pre>
 * 
 * 会提示 warn 级别的日志, 很烦人
 * 
 * <pre>
 * {@code
 * 
 * 20:33:38 WARN  (PropertyStrategyWrapper.java:69) [setProperty()] java.lang.NoSuchMethodException: Unknown property 'county' on class 'class com.feilong.tools.IpInfoEntity'
 * net.sf.json.JSONException: java.lang.NoSuchMethodException: Unknown property 'county' on class 'class com.feilong.tools.IpInfoEntity'
 *     at net.sf.json.util.PropertySetStrategy$DefaultPropertySetStrategy._setProperty(PropertySetStrategy.java:69)
 *     at net.sf.json.util.PropertySetStrategy$DefaultPropertySetStrategy.setProperty(PropertySetStrategy.java:60)
 *     at net.sf.json.util.PropertySetStrategy$DefaultPropertySetStrategy.setProperty(PropertySetStrategy.java:45)
 *     at com.feilong.json.jsonlib.PropertyStrategyWrapper.setProperty(PropertyStrategyWrapper.java:67)
 *     at net.sf.json.util.PropertySetStrategy.setProperty(PropertySetStrategy.java:40)
 *     at net.sf.json.JSONObject.setProperty(JSONObject.java:1394)
 *     at net.sf.json.JSONObject.toBean(JSONObject.java:429)
 *     at com.feilong.json.jsonlib.JsonUtil.toBean(JsonUtil.java:1275)
 *     at com.feilong.tools.IpUtil.toIpInfoEntity(IpUtil.java:162)
 *     at com.feilong.tools.IpUtil.getIpInfoEntity(IpUtil.java:138)
 *     at com.feilong.tools.IpUtilTest.test(IpUtilTest.java:36)
 *     at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
 *     at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
 *     at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
 *     at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:86)
 *     at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:460)
 *     at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:206)
 * Caused by: java.lang.NoSuchMethodException: Unknown property 'county' on class 'class com.feilong.tools.IpInfoEntity'
 *     at org.apache.commons.beanutils.PropertyUtilsBean.setSimpleProperty(PropertyUtilsBean.java:2091)
 *     at org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(PropertyUtils.java:928)
 *     at net.sf.json.util.PropertySetStrategy$DefaultPropertySetStrategy._setProperty(PropertySetStrategy.java:67)
 *     ... 33 more
 * }
 * </pre>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-json/issues/3">json 转java 的时候,想排除某个属性</a>
 * @since 1.11.0
 */
class ArrayExcludePropertyNamesPropertyFilter implements PropertyFilter{

    /** The property names. */
    private final String[] propertyNames;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param propertyNames
     *            the property names
     */
    public ArrayExcludePropertyNamesPropertyFilter(String...propertyNames){
        this.propertyNames = propertyNames;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.util.PropertyFilter#apply(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public boolean apply(Object source,String name,Object value){
        // [source] the owner of the property
        // [name] the name of the property
        // [value] the value of the property
        return org.apache.commons.lang3.ArrayUtils.contains(propertyNames, name);
    }
}
