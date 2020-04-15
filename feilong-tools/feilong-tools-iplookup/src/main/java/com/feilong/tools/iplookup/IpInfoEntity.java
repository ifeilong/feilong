/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.tools.iplookup;

/**
 * The Class IpInfoEntity.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class IpInfoEntity{

    /**
     * ip比如 210.75.225.254.
     */
    private String ip;

    //---------------------------------------------------------------

    /** "country": "中国". */
    private String country;

    /** "area": "华北". */
    private String area;

    /** "region": "北京市". */
    private String region;

    /** "city": "北京市". */
    private String city;

    //---------------------------------------------------------------

    /** "isp": "电信". */
    private String isp;

    //---------------------------------------------------------------

    /** "country_id": "86". */
    private String countryId;

    /** "region_id": "110000". */
    private String regionId;

    /** "area_id": "100000". */
    private String areaId;

    /** "city_id": "110000". */
    private String cityId;

    /** "isp_id": "100017". */
    private String ispId;

    //---------------------------------------------------------------

    /**
     * 获得 ip比如 210.75.225.254.
     *
     * @return the ip
     */
    public String getIp(){
        return ip;
    }

    /**
     * 设置 ip比如 210.75.225.254.
     *
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip){
        this.ip = ip;
    }

    /**
     * 获得 "country": "中国".
     *
     * @return the country
     */
    public String getCountry(){
        return country;
    }

    /**
     * 设置 "country": "中国".
     *
     * @param country
     *            the country to set
     */
    public void setCountry(String country){
        this.country = country;
    }

    /**
     * 获得 "area": "华北".
     *
     * @return the area
     */
    public String getArea(){
        return area;
    }

    /**
     * 设置 "area": "华北".
     *
     * @param area
     *            the area to set
     */
    public void setArea(String area){
        this.area = area;
    }

    /**
     * 获得 "region": "北京市".
     *
     * @return the region
     */
    public String getRegion(){
        return region;
    }

    /**
     * 设置 "region": "北京市".
     *
     * @param region
     *            the region to set
     */
    public void setRegion(String region){
        this.region = region;
    }

    /**
     * 获得 "city": "北京市".
     *
     * @return the city
     */
    public String getCity(){
        return city;
    }

    /**
     * 设置 "city": "北京市".
     *
     * @param city
     *            the city to set
     */
    public void setCity(String city){
        this.city = city;
    }

    /**
     * 获得 "isp": "电信".
     *
     * @return the isp
     */
    public String getIsp(){
        return isp;
    }

    /**
     * 设置 "isp": "电信".
     *
     * @param isp
     *            the isp to set
     */
    public void setIsp(String isp){
        this.isp = isp;
    }

    /**
     * 获得 "country_id": "86".
     *
     * @return the countryId
     */
    public String getCountryId(){
        return countryId;
    }

    /**
     * 设置 "country_id": "86".
     *
     * @param countryId
     *            the countryId to set
     */
    public void setCountryId(String countryId){
        this.countryId = countryId;
    }

    /**
     * 获得 "region_id": "110000".
     *
     * @return the regionId
     */
    public String getRegionId(){
        return regionId;
    }

    /**
     * 设置 "region_id": "110000".
     *
     * @param regionId
     *            the regionId to set
     */
    public void setRegionId(String regionId){
        this.regionId = regionId;
    }

    /**
     * 获得 "area_id": "100000".
     *
     * @return the areaId
     */
    public String getAreaId(){
        return areaId;
    }

    /**
     * 设置 "area_id": "100000".
     *
     * @param areaId
     *            the areaId to set
     */
    public void setAreaId(String areaId){
        this.areaId = areaId;
    }

    /**
     * 获得 "city_id": "110000".
     *
     * @return the cityId
     */
    public String getCityId(){
        return cityId;
    }

    /**
     * 设置 "city_id": "110000".
     *
     * @param cityId
     *            the cityId to set
     */
    public void setCityId(String cityId){
        this.cityId = cityId;
    }

    /**
     * 获得 "isp_id": "100017".
     *
     * @return the ispId
     */
    public String getIspId(){
        return ispId;
    }

    /**
     * 设置 "isp_id": "100017".
     *
     * @param ispId
     *            the ispId to set
     */
    public void setIspId(String ispId){
        this.ispId = ispId;
    }
}
