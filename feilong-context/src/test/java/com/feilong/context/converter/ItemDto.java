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
package com.feilong.context.converter;

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Item.
 *
 * @author: fan.chen1
 * @date: 2013年11月18日
 */
public class ItemDto implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8216660496058800809L;

    /** The id. */
    private Long              id;

    /** The title. */
    private String            title;

    /** The category id. */
    private Long              categoryId;

    /** The type. */
    private String            type;

    /** The price. */
    private BigDecimal        price;

    /** The description. */
    private String            description;

    /** The properties. */
    private String            properties;

    /** The property names. */
    private String            propertyNames;

    /** The property alias. */
    private String            propertyAlias;

    /** The sell point. */
    private String            sellPoint;

    /** The nick name. */
    private String            nickName;

    /** The postage id. */
    private String            postageId;

    /** The timing. */
    private boolean           timing;

    /** The list time. */
    private Date              listTime;

    /** The delist time. */
    private Date              delistTime;

    /** The template id. */
    private Long              templateId;

    /** The image url. */
    private String            imageUrl;

    /** The support pod. */
    private boolean           supportPOD;

    /** pod费用 *. */
    private BigDecimal        podFee;

    /** The created. */
    private Date              created;

    /** The c time. */
    private String            cTime;

    /** The modified. */
    private Date              modified;

    /** The status. */
    private String            status;

    /** The shop id. */
    private Long              shopId;

    /** The seller id. */
    private Long              sellerId;

    /** The shop category id. */
    private List<Long>        shopCategoryId   = newArrayList();

    /** The list price. */
    private BigDecimal        listPrice;

    /** The state. */
    private String            state;

    /** The city. */
    private String            city;

    /** The district. */
    private String            district;

    /** The weight. */
    private Double            weight;

    //    private String volume;

    /** The startime. */
    private String            startime;

    /** The endtime. */
    private String            endtime;

    /** The shopname. */
    private String            shopname;

    //商品打的标签
    /** The attach tags. */
    private String            attachTags;

    //isHaveTag -->allItem 查所有商品   isHaveTag --> tag 查打了标签的商品    isHaveTag -->untag 查没有打标签的商品
    /** The is have tag. */
    private String            isHaveTag;

    /** 不在表中出现的数据 start *. */
    /**
     * 库存,库存数据现在是存在于SKU中,这里是汇总库存,也就是查出该商品对应的SKU,累加所有库存
     */
    private int               inStock;

    /** seller(卖家承担),buyer(买家承担). */
    private String            freightPayer;

    /** 快递费用 *. */
    private Integer           expressFee;

    /** 长. */
    private Integer           length;

    /** 宽. */
    private Integer           width;

    /** 高. */
    private Integer           height;

    /** 用户返点积分. */
    private Integer           buyerObtainPoint;

    /** The old sku. */
    private String            oldSku;

    /** 是否修改敏感信息. */
    private boolean           mustCheck;

    /** The excel path. */
    private String            excelPath;

    /** The excel index. */
    private int               excelIndex;

    /**
     * 获得 id.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 id.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 title.
     *
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * 设置 title.
     *
     * @param title
     *            the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * 获得 category id.
     *
     * @return the categoryId
     */
    public Long getCategoryId(){
        return categoryId;
    }

    /**
     * 设置 category id.
     *
     * @param categoryId
     *            the categoryId to set
     */
    public void setCategoryId(Long categoryId){
        this.categoryId = categoryId;
    }

    /**
     * 获得 type.
     *
     * @return the type
     */
    public String getType(){
        return type;
    }

    /**
     * 设置 type.
     *
     * @param type
     *            the type to set
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * 获得 price.
     *
     * @return the price
     */
    public BigDecimal getPrice(){
        return price;
    }

    /**
     * 设置 price.
     *
     * @param price
     *            the price to set
     */
    public void setPrice(BigDecimal price){
        this.price = price;
    }

    /**
     * 获得 description.
     *
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * 设置 description.
     *
     * @param description
     *            the description to set
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * 获得 properties.
     *
     * @return the properties
     */
    public String getProperties(){
        return properties;
    }

    /**
     * 设置 properties.
     *
     * @param properties
     *            the properties to set
     */
    public void setProperties(String properties){
        this.properties = properties;
    }

    /**
     * 获得 property names.
     *
     * @return the propertyNames
     */
    public String getPropertyNames(){
        return propertyNames;
    }

    /**
     * 设置 property names.
     *
     * @param propertyNames
     *            the propertyNames to set
     */
    public void setPropertyNames(String propertyNames){
        this.propertyNames = propertyNames;
    }

    /**
     * 获得 property alias.
     *
     * @return the propertyAlias
     */
    public String getPropertyAlias(){
        return propertyAlias;
    }

    /**
     * 设置 property alias.
     *
     * @param propertyAlias
     *            the propertyAlias to set
     */
    public void setPropertyAlias(String propertyAlias){
        this.propertyAlias = propertyAlias;
    }

    /**
     * 获得 sell point.
     *
     * @return the sellPoint
     */
    public String getSellPoint(){
        return sellPoint;
    }

    /**
     * 设置 sell point.
     *
     * @param sellPoint
     *            the sellPoint to set
     */
    public void setSellPoint(String sellPoint){
        this.sellPoint = sellPoint;
    }

    /**
     * 获得 nick name.
     *
     * @return the nickName
     */
    public String getNickName(){
        return nickName;
    }

    /**
     * 设置 nick name.
     *
     * @param nickName
     *            the nickName to set
     */
    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    /**
     * 获得 postage id.
     *
     * @return the postageId
     */
    public String getPostageId(){
        return postageId;
    }

    /**
     * 设置 postage id.
     *
     * @param postageId
     *            the postageId to set
     */
    public void setPostageId(String postageId){
        this.postageId = postageId;
    }

    /**
     * 获得 timing.
     *
     * @return the timing
     */
    public boolean getTiming(){
        return timing;
    }

    /**
     * 设置 timing.
     *
     * @param timing
     *            the timing to set
     */
    public void setTiming(boolean timing){
        this.timing = timing;
    }

    /**
     * 获得 list time.
     *
     * @return the listTime
     */
    public Date getListTime(){
        return listTime;
    }

    /**
     * 设置 list time.
     *
     * @param listTime
     *            the listTime to set
     */
    public void setListTime(Date listTime){
        this.listTime = listTime;
    }

    /**
     * 获得 delist time.
     *
     * @return the delistTime
     */
    public Date getDelistTime(){
        return delistTime;
    }

    /**
     * 设置 delist time.
     *
     * @param delistTime
     *            the delistTime to set
     */
    public void setDelistTime(Date delistTime){
        this.delistTime = delistTime;
    }

    /**
     * 获得 template id.
     *
     * @return the templateId
     */
    public Long getTemplateId(){
        return templateId;
    }

    /**
     * 设置 template id.
     *
     * @param templateId
     *            the templateId to set
     */
    public void setTemplateId(Long templateId){
        this.templateId = templateId;
    }

    /**
     * 获得 image url.
     *
     * @return the imageUrl
     */
    public String getImageUrl(){
        return imageUrl;
    }

    /**
     * 设置 image url.
     *
     * @param imageUrl
     *            the imageUrl to set
     */
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    /**
     * 获得 support pod.
     *
     * @return the supportPOD
     */
    public boolean getSupportPOD(){
        return supportPOD;
    }

    /**
     * 设置 support pod.
     *
     * @param supportPOD
     *            the supportPOD to set
     */
    public void setSupportPOD(boolean supportPOD){
        this.supportPOD = supportPOD;
    }

    /**
     * 获得 pod费用 *.
     *
     * @return the podFee
     */
    public BigDecimal getPodFee(){
        return podFee;
    }

    /**
     * 设置 pod费用 *.
     *
     * @param podFee
     *            the podFee to set
     */
    public void setPodFee(BigDecimal podFee){
        this.podFee = podFee;
    }

    /**
     * 获得 created.
     *
     * @return the created
     */
    public Date getCreated(){
        return created;
    }

    /**
     * 设置 created.
     *
     * @param created
     *            the created to set
     */
    public void setCreated(Date created){
        this.created = created;
    }

    /**
     * Getc time.
     *
     * @return the cTime
     */
    public String getcTime(){
        return cTime;
    }

    /**
     * Setc time.
     *
     * @param cTime
     *            the cTime to set
     */
    public void setcTime(String cTime){
        this.cTime = cTime;
    }

    /**
     * 获得 modified.
     *
     * @return the modified
     */
    public Date getModified(){
        return modified;
    }

    /**
     * 设置 modified.
     *
     * @param modified
     *            the modified to set
     */
    public void setModified(Date modified){
        this.modified = modified;
    }

    /**
     * 获得 status.
     *
     * @return the status
     */
    public String getStatus(){
        return status;
    }

    /**
     * 设置 status.
     *
     * @param status
     *            the status to set
     */
    public void setStatus(String status){
        this.status = status;
    }

    /**
     * 获得 shop id.
     *
     * @return the shopId
     */
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 shop id.
     *
     * @param shopId
     *            the shopId to set
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 seller id.
     *
     * @return the sellerId
     */
    public Long getSellerId(){
        return sellerId;
    }

    /**
     * 设置 seller id.
     *
     * @param sellerId
     *            the sellerId to set
     */
    public void setSellerId(Long sellerId){
        this.sellerId = sellerId;
    }

    /**
     * 获得 shop category id.
     *
     * @return the shopCategoryId
     */
    public List<Long> getShopCategoryId(){
        return shopCategoryId;
    }

    /**
     * 设置 shop category id.
     *
     * @param shopCategoryId
     *            the shopCategoryId to set
     */
    public void setShopCategoryId(List<Long> shopCategoryId){
        this.shopCategoryId = shopCategoryId;
    }

    /**
     * 获得 list price.
     *
     * @return the listPrice
     */
    public BigDecimal getListPrice(){
        return listPrice;
    }

    /**
     * 设置 list price.
     *
     * @param listPrice
     *            the listPrice to set
     */
    public void setListPrice(BigDecimal listPrice){
        this.listPrice = listPrice;
    }

    /**
     * 获得 state.
     *
     * @return the state
     */
    public String getState(){
        return state;
    }

    /**
     * 设置 state.
     *
     * @param state
     *            the state to set
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * 获得 city.
     *
     * @return the city
     */
    public String getCity(){
        return city;
    }

    /**
     * 设置 city.
     *
     * @param city
     *            the city to set
     */
    public void setCity(String city){
        this.city = city;
    }

    /**
     * 获得 district.
     *
     * @return the district
     */
    public String getDistrict(){
        return district;
    }

    /**
     * 设置 district.
     *
     * @param district
     *            the district to set
     */
    public void setDistrict(String district){
        this.district = district;
    }

    /**
     * 获得 weight.
     *
     * @return the weight
     */
    public Double getWeight(){
        return weight;
    }

    /**
     * 设置 weight.
     *
     * @param weight
     *            the weight to set
     */
    public void setWeight(Double weight){
        this.weight = weight;
    }

    /**
     * 获得 startime.
     *
     * @return the startime
     */
    public String getStartime(){
        return startime;
    }

    /**
     * 设置 startime.
     *
     * @param startime
     *            the startime to set
     */
    public void setStartime(String startime){
        this.startime = startime;
    }

    /**
     * 获得 endtime.
     *
     * @return the endtime
     */
    public String getEndtime(){
        return endtime;
    }

    /**
     * 设置 endtime.
     *
     * @param endtime
     *            the endtime to set
     */
    public void setEndtime(String endtime){
        this.endtime = endtime;
    }

    /**
     * 获得 shopname.
     *
     * @return the shopname
     */
    public String getShopname(){
        return shopname;
    }

    /**
     * 设置 shopname.
     *
     * @param shopname
     *            the shopname to set
     */
    public void setShopname(String shopname){
        this.shopname = shopname;
    }

    /**
     * 获得 attach tags.
     *
     * @return the attachTags
     */
    public String getAttachTags(){
        return attachTags;
    }

    /**
     * 设置 attach tags.
     *
     * @param attachTags
     *            the attachTags to set
     */
    public void setAttachTags(String attachTags){
        this.attachTags = attachTags;
    }

    /**
     * 获得 is have tag.
     *
     * @return the isHaveTag
     */
    public String getIsHaveTag(){
        return isHaveTag;
    }

    /**
     * 设置 is have tag.
     *
     * @param isHaveTag
     *            the isHaveTag to set
     */
    public void setIsHaveTag(String isHaveTag){
        this.isHaveTag = isHaveTag;
    }

    /**
     * 获得 不在表中出现的数据 start *.
     *
     * @return the inStock
     */
    public int getInStock(){
        return inStock;
    }

    /**
     * 设置 不在表中出现的数据 start *.
     *
     * @param inStock
     *            the inStock to set
     */
    public void setInStock(int inStock){
        this.inStock = inStock;
    }

    /**
     * 获得 seller(卖家承担),buyer(买家承担).
     *
     * @return the freightPayer
     */
    public String getFreightPayer(){
        return freightPayer;
    }

    /**
     * 设置 seller(卖家承担),buyer(买家承担).
     *
     * @param freightPayer
     *            the freightPayer to set
     */
    public void setFreightPayer(String freightPayer){
        this.freightPayer = freightPayer;
    }

    /**
     * 获得 快递费用 *.
     *
     * @return the expressFee
     */
    public Integer getExpressFee(){
        return expressFee;
    }

    /**
     * 设置 快递费用 *.
     *
     * @param expressFee
     *            the expressFee to set
     */
    public void setExpressFee(Integer expressFee){
        this.expressFee = expressFee;
    }

    /**
     * 获得 长.
     *
     * @return the length
     */
    public Integer getLength(){
        return length;
    }

    /**
     * 设置 长.
     *
     * @param length
     *            the length to set
     */
    public void setLength(Integer length){
        this.length = length;
    }

    /**
     * 获得 宽.
     *
     * @return the width
     */
    public Integer getWidth(){
        return width;
    }

    /**
     * 设置 宽.
     *
     * @param width
     *            the width to set
     */
    public void setWidth(Integer width){
        this.width = width;
    }

    /**
     * 获得 高.
     *
     * @return the height
     */
    public Integer getHeight(){
        return height;
    }

    /**
     * 设置 高.
     *
     * @param height
     *            the height to set
     */
    public void setHeight(Integer height){
        this.height = height;
    }

    /**
     * 获得 用户返点积分.
     *
     * @return the buyerObtainPoint
     */
    public Integer getBuyerObtainPoint(){
        return buyerObtainPoint;
    }

    /**
     * 设置 用户返点积分.
     *
     * @param buyerObtainPoint
     *            the buyerObtainPoint to set
     */
    public void setBuyerObtainPoint(Integer buyerObtainPoint){
        this.buyerObtainPoint = buyerObtainPoint;
    }

    /**
     * 获得 old sku.
     *
     * @return the oldSku
     */
    public String getOldSku(){
        return oldSku;
    }

    /**
     * 设置 old sku.
     *
     * @param oldSku
     *            the oldSku to set
     */
    public void setOldSku(String oldSku){
        this.oldSku = oldSku;
    }

    /**
     * 获得 是否修改敏感信息.
     *
     * @return the mustCheck
     */
    public boolean getMustCheck(){
        return mustCheck;
    }

    /**
     * 设置 是否修改敏感信息.
     *
     * @param mustCheck
     *            the mustCheck to set
     */
    public void setMustCheck(boolean mustCheck){
        this.mustCheck = mustCheck;
    }

    /**
     * 获得 excel path.
     *
     * @return the excelPath
     */
    public String getExcelPath(){
        return excelPath;
    }

    /**
     * 设置 excel path.
     *
     * @param excelPath
     *            the excelPath to set
     */
    public void setExcelPath(String excelPath){
        this.excelPath = excelPath;
    }

    /**
     * 获得 excel index.
     *
     * @return the excelIndex
     */
    public int getExcelIndex(){
        return excelIndex;
    }

    /**
     * 设置 excel index.
     *
     * @param excelIndex
     *            the excelIndex to set
     */
    public void setExcelIndex(int excelIndex){
        this.excelIndex = excelIndex;
    }

}
