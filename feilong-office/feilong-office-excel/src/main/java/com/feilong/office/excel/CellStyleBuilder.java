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
package com.feilong.office.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * 生成 excel cell style.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Feb 10, 2014 2:51:03 AM
 */
public final class CellStyleBuilder{

    //---------------------------------------------------------------

    /**
     * 高亮样式.
     * 
     * @param workbook
     *            the workbook
     * @return 高亮样式
     */
    public static HSSFCellStyle buildHightLightHSSFCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(getStressHSSFFont(workbook));
        return cellStyle;
    }

    //---------------------------------------------------------------

    /**
     * Gets the HSSF cell style for title row cell.
     * 
     * @param workbook
     *            the workbook
     * @return the HSSF cell style for title row cell
     */
    public static HSSFCellStyle buildHSSFCellStyleForTitleRowCell(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 指定的填充模式
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        // cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 隔行变色 样式.
     * 
     * @param workbook
     *            the workbook
     * @return 隔行变色
     */
    public static HSSFCellStyle buildChangeRowColorHSSFCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
        // 指定的填充模式
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }

    /**
     * 隔行变色 样式,并且高亮.
     * 
     * @param workbook
     *            the workbook
     * @return 隔行变色 样式,并且高亮
     */
    public static HSSFCellStyle buildChangeRowColorAndHightLightHSSFCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = buildChangeRowColorHSSFCellStyle(workbook);
        cellStyle.setFont(getStressHSSFFont(workbook));
        return cellStyle;
    }

    /**
     * 获得隔行变色的样式.
     * 
     * @param workbook
     *            workbook
     * @return 获得隔行变色的样式
     */
    public HSSFCellStyle buildRowChangeHSSFCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
        // 指定的填充模式
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }

    /**
     * 获得强调的字体.
     * 
     * @param workbook
     *            the workbook
     * @return 获得强调的字体
     */
    private static HSSFFont getStressHSSFFont(HSSFWorkbook workbook){
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.RED.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体
        return font;
    }
}
