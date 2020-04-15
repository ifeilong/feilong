package com.feilong.office.excel.itemupdateprice;

import java.math.BigDecimal;

public class ItemUpdatePriceCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3134310251651746158L;

    /** 商品id. */
    private Long              itemId;

    /** 商品编号. */
    private String            code;

    /** 商品名称. */
    private String            title;

    /** 款式. */
    private String            style;

    /** 商品副标题. */
    private String            subTitle;

    /** item销售价格. */
    private BigDecimal        itemSalePrice;

    /** item吊牌价. */
    private BigDecimal        itemListPrice;

    /**
     * 获得 商品id.
     *
     * @return the 商品id
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 商品id.
     *
     * @param itemId
     *            the new 商品id
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * 获得 商品编号.
     *
     * @return the 商品编号
     */
    public String getCode(){
        return code;
    }

    /**
     * 设置 商品编号.
     *
     * @param code
     *            the new 商品编号
     */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * 获得 商品名称.
     *
     * @return the 商品名称
     */
    public String getTitle(){
        return title;
    }

    /**
     * 设置 商品名称.
     *
     * @param title
     *            the new 商品名称
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * 获得 商品副标题.
     *
     * @return the 商品副标题
     */
    public String getSubTitle(){
        return subTitle;
    }

    /**
     * 设置 商品副标题.
     *
     * @param subTitle
     *            the new 商品副标题
     */
    public void setSubTitle(String subTitle){
        this.subTitle = subTitle;
    }

    /**
     * 获得 item销售价格.
     *
     * @return the item销售价格
     */
    public BigDecimal getItemSalePrice(){
        return itemSalePrice;
    }

    /**
     * 设置 item销售价格.
     *
     * @param itemSalePrice
     *            the new item销售价格
     */
    public void setItemSalePrice(BigDecimal itemSalePrice){
        this.itemSalePrice = itemSalePrice;
    }

    /**
     * 获得 item吊牌价.
     *
     * @return the item吊牌价
     */
    public BigDecimal getItemListPrice(){
        return itemListPrice;
    }

    /**
     * 设置 item吊牌价.
     *
     * @param itemListPrice
     *            the new item吊牌价
     */
    public void setItemListPrice(BigDecimal itemListPrice){
        this.itemListPrice = itemListPrice;
    }

    /**
     * 获得 款式.
     *
     * @return the 款式
     */
    public String getStyle(){
        return style;
    }

    /**
     * 设置 款式.
     *
     * @param style
     *            the new 款式
     */
    public void setStyle(String style){
        this.style = style;
    }

    /**
     * Gets the serialversionuid.
     *
     * @return the serialversionuid
     */
    public static long getSerialversionuid(){
        return serialVersionUID;
    }

}
