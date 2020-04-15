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
package com.feilong.tools.barcode;

import java.io.Serializable;

import com.feilong.io.entity.ImageType;
import com.google.zxing.BarcodeFormat;

/**
 * The Class QRConfig.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.google.zxing.BarcodeFormat
 * @since 1.2.1
 */
public class BarcodeConfig implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID      = -3007168344271201418L;

    /** barcode格式,Enumerates barcode formats known to this package. */
    private BarcodeFormat     barcodeFormat         = BarcodeFormat.QR_CODE;

    //---------------------------------------------------------------
    /** Barcode 宽度. */
    private Integer           width                 = 300;

    /** Barcode 高度. */
    private Integer           height                = 300;

    /**
     * 输出的image 格式,默认 {@link com.feilong.io.entity.ImageType#PNG}.
     * 
     * @see com.feilong.io.entity.ImageType
     */
    private String            outputImageFormatName = ImageType.PNG;

    //---------------------------------------------------------------

    /**
     * logo图片地址,如果是null或者empty,生成的二维码将不会有logo.
     */
    private String            logoImagePath         = "";

    /** LOGO宽度 . */
    private Integer           logoImageWidth        = 60;

    /** LOGO高度. */
    private Integer           logoImageHeight       = 60;

    //---------------------------------------------------------------
    /**
     * 指定边距,单位像素 .
     * 
     * @see com.google.zxing.EncodeHintType#MARGIN
     * @since 1.5.4
     */
    private Integer           encodeHintTypeMargin  = 1;

    /**
     * 获得 barcode 宽度.
     *
     * @return the width
     */
    public Integer getWidth(){
        return width;
    }

    /**
     * 设置 barcode 宽度.
     *
     * @param width
     *            the width to set
     */
    public void setWidth(Integer width){
        this.width = width;
    }

    /**
     * 获得 barcode 高度.
     *
     * @return the height
     */
    public Integer getHeight(){
        return height;
    }

    /**
     * 设置 barcode 高度.
     *
     * @param height
     *            the height to set
     */
    public void setHeight(Integer height){
        this.height = height;
    }

    /**
     * 获得 barcode格式,Enumerates barcode formats known to this package.
     *
     * @return the barcodeFormat
     */
    public BarcodeFormat getBarcodeFormat(){
        return barcodeFormat;
    }

    /**
     * 设置 barcode格式,Enumerates barcode formats known to this package.
     *
     * @param barcodeFormat
     *            the barcodeFormat to set
     */
    public void setBarcodeFormat(BarcodeFormat barcodeFormat){
        this.barcodeFormat = barcodeFormat;
    }

    /**
     * 获得 输出的image 格式,默认 {@link com.feilong.io.entity.ImageType#PNG}.
     *
     * @return the outputImageFormatName
     */
    public String getOutputImageFormatName(){
        return outputImageFormatName;
    }

    /**
     * 设置 输出的image 格式,默认 {@link com.feilong.io.entity.ImageType#PNG}.
     *
     * @param outputImageFormatName
     *            the outputImageFormatName to set
     */
    public void setOutputImageFormatName(String outputImageFormatName){
        this.outputImageFormatName = outputImageFormatName;
    }

    /**
     * 获得 logo图片地址,如果是null或者empty,生成的二维码将不会有logo.
     *
     * @return the logoImagePath
     */
    public String getLogoImagePath(){
        return logoImagePath;
    }

    /**
     * 设置 logo图片地址,如果是null或者empty,生成的二维码将不会有logo.
     *
     * @param logoImagePath
     *            the logoImagePath to set
     */
    public void setLogoImagePath(String logoImagePath){
        this.logoImagePath = logoImagePath;
    }

    /**
     * 获得 lOGO宽度 .
     *
     * @return the logoImageWidth
     */
    public Integer getLogoImageWidth(){
        return logoImageWidth;
    }

    /**
     * 设置 lOGO宽度 .
     *
     * @param logoImageWidth
     *            the logoImageWidth to set
     */
    public void setLogoImageWidth(Integer logoImageWidth){
        this.logoImageWidth = logoImageWidth;
    }

    /**
     * 获得 lOGO高度.
     *
     * @return the logoImageHeight
     */
    public Integer getLogoImageHeight(){
        return logoImageHeight;
    }

    /**
     * 设置 lOGO高度.
     *
     * @param logoImageHeight
     *            the logoImageHeight to set
     */
    public void setLogoImageHeight(Integer logoImageHeight){
        this.logoImageHeight = logoImageHeight;
    }

    /**
     * 获得 指定边距,单位像素 .
     *
     * @return the encodeHintTypeMargin
     * @see com.google.zxing.EncodeHintType#MARGIN
     * @since 1.5.4
     */
    public Integer getEncodeHintTypeMargin(){
        return encodeHintTypeMargin;
    }

    /**
     * 设置 指定边距,单位像素 .
     *
     * @param encodeHintTypeMargin
     *            the encodeHintTypeMargin to set
     * @see com.google.zxing.EncodeHintType#MARGIN
     * @since 1.5.4
     */
    public void setEncodeHintTypeMargin(Integer encodeHintTypeMargin){
        this.encodeHintTypeMargin = encodeHintTypeMargin;
    }
}
