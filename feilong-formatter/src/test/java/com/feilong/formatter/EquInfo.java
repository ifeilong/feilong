/**
 * Copyright (c) 2017 TianYan All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TianYan.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with TianYan.
 *
 * TianYan MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. TianYan SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.feilong.formatter;

import java.io.Serializable;
import java.util.Date;

/**
 * 股票信息.
 * 
 * @author xianze.zxz
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 */
public class EquInfo implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long  serialVersionUID = 288232184048495608L;

    /** 上证. */
    public static final String SH               = "XSHG";

    /** 深证. */
    public static final String HE               = "XSHE";

    //---------------------------------------------------------------------

    /** id主键. */
    private Long               id;

    /** 股票代码. */
    @FormatterColumn(name = "ticker",order = 1)
    private String             ticker;

    /** 证券简称. */
    @FormatterColumn(name = "secShortName",order = 2)
    private String             secShortName;

    /** 上市状态，L-上市，S-暂停，DE-终止上市，UN-未上市. */
    @FormatterColumn(name = "listStatusCd",order = 3)
    private String             listStatusCd;

    /** 交易市场. */
    private String             exchangeCd;

    /** 上市板块编码. */
    private Integer            listSectorCd;

    /** 上市板块. */
    private String             listSector;

    /** 上市日期. */
    private Date               listDate;

    /** 摘牌日期. */
    private String             delistDate;

    /** 股票分类编码，A或B. */
    private String             equTypeCd;

    /** 交易市场所属地区. */
    private String             exOuntryCd;

    //---------------------------------------------------------------------

    /** 总股本(最新). */
    private Double             totalShares;

    /** 公司无限售流通股份合计(最新). */
    private Double             nonrestFloatShares;

    /** 无限售流通股本(最新)。如果为A股，该列为最新无限售流通A股股本数量；如果为B股，该列为最新流通B股股本数量. */
    private Double             nonrestfloatA;

    /** 机构内部ID. */
    private String             partyId;

    /** 财务报告日期. */
    private Date               endDate;

    /** 所有者权益合计. */
    private Double             tshEquity;

    //---------------------------------------------------------------

    /** 创建时间. */
    private Date               createTime;

    /** 修改时间. */
    private Date               updateTime;

    //---------------------------------------------------------------------

    /**
     * 获得 摘牌日期.
     *
     * @return the 摘牌日期
     */
    public String getDelistDate(){
        return delistDate;
    }

    /**
     * 获得 财务报告日期.
     *
     * @return the 财务报告日期
     */
    public Date getEndDate(){
        return endDate;
    }

    /**
     * 获得 股票分类编码，A或B.
     *
     * @return the 股票分类编码，A或B
     */
    public String getEquTypeCd(){
        return equTypeCd;
    }

    /**
     * 获得 交易市场.
     *
     * @return the 交易市场
     */
    public String getExchangeCd(){
        return exchangeCd;
    }

    /**
     * 获得 交易市场所属地区.
     *
     * @return the 交易市场所属地区
     */
    public String getExOuntryCd(){
        return exOuntryCd;
    }

    /**
     * 获得 id主键.
     *
     * @return the id主键
     */
    public Long getId(){
        return id;
    }

    /**
     * 获得 上市日期.
     *
     * @return the 上市日期
     */
    public Date getListDate(){
        return listDate;
    }

    /**
     * 获得 上市板块.
     *
     * @return the 上市板块
     */
    public String getListSector(){
        return listSector;
    }

    /**
     * 获得 上市板块编码.
     *
     * @return the 上市板块编码
     */
    public Integer getListSectorCd(){
        return listSectorCd;
    }

    /**
     * 获得 上市状态，L-上市，S-暂停，DE-终止上市，UN-未上市.
     *
     * @return the 上市状态，L-上市，S-暂停，DE-终止上市，UN-未上市
     */
    public String getListStatusCd(){
        return listStatusCd;
    }

    /**
     * 获得 无限售流通股本(最新)。如果为A股，该列为最新无限售流通A股股本数量；如果为B股，该列为最新流通B股股本数量.
     *
     * @return the 无限售流通股本(最新)。如果为A股，该列为最新无限售流通A股股本数量；如果为B股，该列为最新流通B股股本数量
     */
    public Double getNonrestfloatA(){
        return nonrestfloatA;
    }

    /**
     * 获得 公司无限售流通股份合计(最新).
     *
     * @return the 公司无限售流通股份合计(最新)
     */
    public Double getNonrestFloatShares(){
        return nonrestFloatShares;
    }

    /**
     * 获得 机构内部ID.
     *
     * @return the 机构内部ID
     */
    public String getPartyId(){
        return partyId;
    }

    /**
     * 获得 证券简称.
     *
     * @return the 证券简称
     */
    public String getSecShortName(){
        return secShortName;
    }

    /**
     * 获得 股票代码.
     *
     * @return the 股票代码
     */
    public String getTicker(){
        return ticker;
    }

    /**
     * 获得 总股本(最新).
     *
     * @return the 总股本(最新)
     */
    public Double getTotalShares(){
        return totalShares;
    }

    /**
     * 获得 所有者权益合计.
     *
     * @return the 所有者权益合计
     */
    public Double getTshEquity(){
        return tshEquity;
    }

    /**
     * 设置 摘牌日期.
     *
     * @param delistDate
     *            the new 摘牌日期
     */
    public void setDelistDate(String delistDate){
        this.delistDate = delistDate == null ? null : delistDate.trim();
    }

    /**
     * 设置 财务报告日期.
     *
     * @param endDate
     *            the new 财务报告日期
     */
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    /**
     * 设置 股票分类编码，A或B.
     *
     * @param equTypeCd
     *            the new 股票分类编码，A或B
     */
    public void setEquTypeCd(String equTypeCd){
        this.equTypeCd = equTypeCd == null ? null : equTypeCd.trim();
    }

    /**
     * 设置 交易市场.
     *
     * @param exchangeCd
     *            the new 交易市场
     */
    public void setExchangeCd(String exchangeCd){
        this.exchangeCd = exchangeCd;
    }

    /**
     * 设置 交易市场所属地区.
     *
     * @param exOuntryCd
     *            the new 交易市场所属地区
     */
    public void setExOuntryCd(String exOuntryCd){
        this.exOuntryCd = exOuntryCd == null ? null : exOuntryCd.trim();
    }

    /**
     * 设置 id主键.
     *
     * @param id
     *            the new id主键
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 设置 上市日期.
     *
     * @param listDate
     *            the new 上市日期
     */
    public void setListDate(Date listDate){
        this.listDate = listDate;
    }

    /**
     * 设置 上市板块.
     *
     * @param listSector
     *            the new 上市板块
     */
    public void setListSector(String listSector){
        this.listSector = listSector == null ? null : listSector.trim();
    }

    /**
     * 设置 上市板块编码.
     *
     * @param listSectorCd
     *            the new 上市板块编码
     */
    public void setListSectorCd(Integer listSectorCd){
        this.listSectorCd = listSectorCd;
    }

    /**
     * 设置 上市状态，L-上市，S-暂停，DE-终止上市，UN-未上市.
     *
     * @param listStatusCd
     *            the new 上市状态，L-上市，S-暂停，DE-终止上市，UN-未上市
     */
    public void setListStatusCd(String listStatusCd){
        this.listStatusCd = listStatusCd == null ? null : listStatusCd.trim();
    }

    /**
     * 设置 无限售流通股本(最新)。如果为A股，该列为最新无限售流通A股股本数量；如果为B股，该列为最新流通B股股本数量.
     *
     * @param nonrestfloatA
     *            the new 无限售流通股本(最新)。如果为A股，该列为最新无限售流通A股股本数量；如果为B股，该列为最新流通B股股本数量
     */
    public void setNonrestfloatA(Double nonrestfloatA){
        this.nonrestfloatA = nonrestfloatA;
    }

    /**
     * 设置 公司无限售流通股份合计(最新).
     *
     * @param nonrestFloatShares
     *            the new 公司无限售流通股份合计(最新)
     */
    public void setNonrestFloatShares(Double nonrestFloatShares){
        this.nonrestFloatShares = nonrestFloatShares;
    }

    /**
     * 设置 机构内部ID.
     *
     * @param partyId
     *            the new 机构内部ID
     */
    public void setPartyId(String partyId){
        this.partyId = partyId == null ? null : partyId.trim();
    }

    /**
     * 设置 证券简称.
     *
     * @param secShortName
     *            the new 证券简称
     */
    public void setSecShortName(String secShortName){
        this.secShortName = secShortName == null ? null : secShortName.trim();
    }

    /**
     * 设置 股票代码.
     *
     * @param ticker
     *            the new 股票代码
     */
    public void setTicker(String ticker){
        this.ticker = ticker == null ? null : ticker.trim();
    }

    /**
     * 设置 总股本(最新).
     *
     * @param totalShares
     *            the new 总股本(最新)
     */
    public void setTotalShares(Double totalShares){
        this.totalShares = totalShares;
    }

    /**
     * 设置 所有者权益合计.
     *
     * @param tshEquity
     *            the new 所有者权益合计
     */
    public void setTshEquity(Double tshEquity){
        this.tshEquity = tshEquity;
    }

    /**
     * 获得 创建时间.
     *
     * @return the 创建时间
     */
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 创建时间.
     *
     * @param createTime
     *            the new 创建时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 获得 修改时间.
     *
     * @return the 修改时间
     */
    public Date getUpdateTime(){
        return updateTime;
    }

    /**
     * 设置 修改时间.
     *
     * @param updateTime
     *            the new 修改时间
     */
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }
}